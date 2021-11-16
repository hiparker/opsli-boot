/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.modulars.system.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.options.OptionsModel;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserPassword;
import org.opsli.api.wrapper.system.user.UserRoleRefModel;
import org.opsli.api.wrapper.system.user.UserWebModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.CheckStrength;
import org.opsli.common.utils.FieldUtil;
import org.opsli.common.utils.ListDistinctUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.chain.QueryDataPermsHandler;
import org.opsli.core.persistence.querybuilder.chain.QueryTenantHandler;
import org.opsli.core.utils.OptionsUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.menu.service.IMenuService;
import org.opsli.modulars.system.role.entity.SysRole;
import org.opsli.modulars.system.role.service.IRoleService;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.entity.SysUserWeb;
import org.opsli.modulars.system.user.mapper.UserMapper;
import org.opsli.modulars.system.user.service.IUserRoleRefService;
import org.opsli.modulars.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 用户信息 Service Impl
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Service
public class UserServiceImpl extends CrudServiceImpl<UserMapper, SysUser, UserModel> implements IUserService {

    @Autowired(required = false)
    private UserMapper mapper;
    @Autowired
    private IMenuService iMenuService;
    @Autowired
    private IRoleService iRoleService;
    @Autowired
    private IUserRoleRefService iUserRoleRefService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserModel insert(UserModel model) {
        if(model == null){
            return null;
        }

        // 判断用户是否有 修改租户的能力 (超级管理员除外)
        if(StringUtils.isNotEmpty(model.getTenantId())){
            // 如果没有租户修改能力 则清空对应字段
            if(!UserUtil.isHasUpdateTenantPerms(UserUtil.getUser())){
                model.setTenantId(null);
                model.setIzTenantAdmin(null);
            }
        }

        // 唯一验证 - 用户名
        boolean verificationByName = this.uniqueVerificationByName(model);
        if(!verificationByName){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_USER_UNIQUE);
        }
        // 唯一验证 - 工号
        boolean verificationByNo  = this.uniqueVerificationByNo(model);
        if(!verificationByNo){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_USER_NO_UNIQUE);
        }

        // 防止非法操作 - 不允许直接操控到 关键数据
        // 需要注意的是 不要轻易改修改策略
        model.setLoginIp(null);
        // 默认用户状态为启用
        model.setEnable(DictType.NO_YES_YES.getValue());
        // 默认未分配组织
        model.setIzExistOrg(DictType.NO_YES_NO.getValue());

        // 新增可以直接设置密码
        if(StringUtils.isNotEmpty(model.getPassword())){
            // 设置随机新盐值
            model.setSecretKey(
                    RandomUtil.randomString(20)
            );
            // 获得密码强度
            model.setPasswordLevel(
                    CheckStrength.getPasswordLevel(model.getPassword()).getCode()
            );
            // 处理密码
            model.setPassword(
                    UserUtil.handlePassword(model.getPassword(),
                            model.getSecretKey())
            );
        }

        // 如果手机号有变化 则强制覆盖手机号
        if(StringUtils.isNotEmpty(model.getMobile())){
            UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("mobile", null);
            updateWrapper.eq("mobile", model.getMobile());
            this.update(updateWrapper);
        }

        UserModel insertModel = super.insert(model);

        // 新增用户 设置默认角色
        if(insertModel != null){
            String defRole = null;
            // 获得option 缓存中 角色编号
            OptionsModel optionsModel = OptionsUtil.getOptionByCode("def_role");
            if(optionsModel != null){
                defRole = optionsModel.getOptionValue();
            }

            if(StringUtils.isNotBlank(defRole)){
                QueryWrapper<SysRole> roleQueryWrapper = new QueryWrapper<>();
                roleQueryWrapper.eq("role_code", defRole);
                roleQueryWrapper.eq(
                        FieldUtil.humpToUnderline(MyBatisConstants.FIELD_DELETE_LOGIC),
                        DictType.NO_YES_NO.getValue());
                SysRole sysRole = iRoleService.getOne(roleQueryWrapper);
                if(sysRole != null){
                    UserRoleRefModel userRoleRefModel = UserRoleRefModel.builder()
                            .userId(insertModel.getId())
                            .roleIds(Convert.toStrArray(sysRole.getId()))
                            .defRoleId(sysRole.getId())
                            .build();

                    // 设置用户默认角色
                    iUserRoleRefService.setRoles(userRoleRefModel);
                }
            }
        }

        return insertModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserModel update(UserModel model) {
        if(model == null){
            return null;
        }

        // 判断用户是否有 修改租户的能力 (超级管理员除外)
        if(StringUtils.isNotEmpty(model.getTenantId())){
            // 如果没有租户修改能力 则清空对应字段
            if(!UserUtil.isHasUpdateTenantPerms(UserUtil.getUser())){
                model.setTenantId(null);
                model.setIzTenantAdmin(null);
            }
        }

        // 唯一验证 - 用户名
        boolean verificationByName = this.uniqueVerificationByName(model);
        if(!verificationByName){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_USER_UNIQUE);
        }
        // 唯一验证 - 工号
        boolean verificationByNo  = this.uniqueVerificationByNo(model);
        if(!verificationByNo){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_USER_NO_UNIQUE);
        }

        UserModel userModel = super.get(model);

        // 防止非法操作 - 不允许直接操控到 关键数据
        // 需要注意的是 不要轻易改修改策略
        model.setUsername(null);
        model.setPassword(null);
        model.setSecretKey(null);
        model.setLoginIp(null);
        model.setEnable(null);
        model.setIzExistOrg(null);

        UserModel update = super.update(model);
        if(update != null){
            // 如果手机号有变化 则强制覆盖手机号
            if(!StringUtils.equals(userModel.getMobile(), update.getMobile())){
                UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("mobile", null);
                updateWrapper.eq("mobile", model.getMobile());
                updateWrapper.notIn(MyBatisConstants.FIELD_ID, update.getId());
                this.update(updateWrapper);
            }

            // 刷新用户缓存
            this.clearCache(Collections.singletonList(userModel));
        }

        return update;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableAccount(String userId, String enable) {
        if(!DictType.hasDict(DictType.NO_YES_YES.getType(), enable)){
            // 非法参数
            throw new ServiceException(SystemMsg.EXCEPTION_USER_ILLEGAL_PARAMETER);
        }

        UserModel model = this.get(userId);
        if(model == null){
            return false;
        }

        UserModel currUser = UserUtil.getUser();
        if(StringUtils.equals(currUser.getId(), userId)){
            // 不可操作自身
            throw new ServiceException(SystemMsg.EXCEPTION_USER_HANDLE_SELF);
        }

        if(StringUtils.equals(UserUtil.SUPER_ADMIN, model.getUsername())){
            // 不可操作超管账号
            throw new ServiceException(SystemMsg.EXCEPTION_USER_HANDLE_SUPER_ADMIN);
        }


        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("enable", enable)
                .eq(
                FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID), userId
        );
        if(this.update(updateWrapper)){
            // 刷新用户缓存
            this.clearCache(Collections.singletonList(model));
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        UserModel userModel = super.get(id);
        // 非法判断
        if(userModel == null){
            return false;
        }

        // 杜绝我删我自己行为
        UserModel currUser = UserUtil.getUser();
        if(StringUtils.equals(currUser.getId(), id)){
            // 不可操作自身
            throw new ServiceException(SystemMsg.EXCEPTION_USER_HANDLE_SELF);
        }
        if(StringUtils.equals(UserUtil.SUPER_ADMIN, userModel.getUsername())){
            // 不可操作超管账号
            throw new ServiceException(SystemMsg.EXCEPTION_USER_HANDLE_SUPER_ADMIN);
        }

        boolean ret = super.delete(id);
        if(ret){
            // 刷新用户缓存
            this.clearCache(Collections.singletonList(userModel));
        }
        return ret;
    }

    @Override
    public boolean delete(UserModel model) {
        if(model == null){
            return false;
        }
        UserModel userModel = super.get(model.getId());
        // 非法判断
        if(userModel == null){
            return false;
        }

        // 杜绝我删我自己行为
        UserModel currUser = UserUtil.getUser();
        if(StringUtils.equals(currUser.getId(), userModel.getId())){
            // 不可操作自身
            throw new ServiceException(SystemMsg.EXCEPTION_USER_HANDLE_SELF);
        }
        if(StringUtils.equals(UserUtil.SUPER_ADMIN, userModel.getUsername())){
            // 不可操作超管账号
            throw new ServiceException(SystemMsg.EXCEPTION_USER_HANDLE_SUPER_ADMIN);
        }

        boolean ret = super.delete(model);
        if(ret){
            // 刷新用户缓存
            this.clearCache(Collections.singletonList(userModel));
        }
        return ret;
    }

    @Override
    public boolean deleteAll(String[] ids) {
        // 非法判断
        if(ids == null){
            return false;
        }

        List<String> idList = Convert.toList(String.class, ids);

        // 杜绝我删我自己行为
        UserModel currUser = UserUtil.getUser();
        if(CollUtil.isNotEmpty(idList)){
            if(idList.contains(currUser.getId())){
                // 不可操作自身
                throw new ServiceException(SystemMsg.EXCEPTION_USER_HANDLE_SELF);
            }

            // 超级管理员
            UserModel superAdmin = UserUtil.getUserByUserName(UserUtil.SUPER_ADMIN);
            if(superAdmin != null){
                String superAdminId = superAdmin.getId();
                if(idList.contains(superAdminId)){
                    // 不可操作超管账号
                    throw new ServiceException(SystemMsg.EXCEPTION_USER_HANDLE_SUPER_ADMIN);
                }
            }
        }

        QueryBuilder<SysUser> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysUser> queryWrapper = queryBuilder.build();
        queryWrapper.in(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID),idList);
        List<UserModel> modelList = super.transformTs2Ms(
                this.findList(queryWrapper)
        );

        boolean ret = super.deleteAll(ids);
        if(ret){
            // 刷新用户缓存
            this.clearCache(modelList);
        }

        return ret;
    }

    @Override
    public boolean deleteAll(Collection<UserModel> models) {
        // 非法判断
        if(CollUtil.isEmpty(models)){
            return false;
        }

        List<String> idList = Lists.newArrayListWithCapacity(models.size());
        for (UserModel model : models) {
            idList.add(model.getId());
        }

        // 杜绝我删我自己行为
        UserModel currUser = UserUtil.getUser();
        if(CollUtil.isNotEmpty(idList)){
            if(idList.contains(currUser.getId())){
                // 不可操作自身
                throw new ServiceException(SystemMsg.EXCEPTION_USER_HANDLE_SELF);
            }

            // 超级管理员
            UserModel superAdmin = UserUtil.getUserByUserName(UserUtil.SUPER_ADMIN);
            if(superAdmin != null){
                String superAdminId = superAdmin.getId();
                if(idList.contains(superAdminId)){
                    // 不可操作超管账号
                    throw new ServiceException(SystemMsg.EXCEPTION_USER_HANDLE_SUPER_ADMIN);
                }
            }
        }

        QueryBuilder<SysUser> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysUser> queryWrapper = queryBuilder.build();
        queryWrapper.in(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID),idList);
        List<UserModel> modelList = super.transformTs2Ms(
                this.findList(queryWrapper)
        );

        boolean ret = super.deleteAll(models);
        if(ret){
            // 刷新用户缓存
            this.clearCache(modelList);
        }

        return ret;
    }

    @Override
    public UserModel queryByUserName(String username) {
        String key = FieldUtil.humpToUnderline("username");
        QueryBuilder<SysUser> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysUser> queryWrapper = queryBuilder.build();
        queryWrapper.eq(key, username);
        queryWrapper.eq(
                FieldUtil.humpToUnderline(MyBatisConstants.FIELD_DELETE_LOGIC)
                , "0");
        SysUser user = this.getOne(queryWrapper);
        return super.transformT2M(user);
    }

    @Override
    public Page<SysUser, UserModel> findPage(Page<SysUser, UserModel> page) {
        UserModel currUser = UserUtil.getUser();
        // 如果不是超级管理员则 无法看到超级管理员账户
        if(!UserUtil.SUPER_ADMIN.equals(currUser.getUsername())){
            QueryWrapper<SysUser> queryWrapper = page.getQueryWrapper();
            queryWrapper.notIn("username", UserUtil.SUPER_ADMIN);
            page.setQueryWrapper(queryWrapper);
        }
        return super.findPage(page);
    }

    @Override
    public List<SysUser> findList(QueryWrapper<SysUser> queryWrapper) {
        // 如果没有租户修改能力 则默认增加租户限制
        if(!UserUtil.isHasUpdateTenantPerms(UserUtil.getUser())){
            // 数据处理责任链
            queryWrapper = new QueryTenantHandler(
                    new QueryDataPermsHandler()
            ).handler(entityClazz, queryWrapper);
        }

        return super.list(queryWrapper);
    }

    @Override
    public List<SysUser> findAllList() {
        QueryBuilder<SysUser> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysUser> queryWrapper = queryBuilder.build();
        // 如果没有租户修改能力 则默认增加租户限制
        if(!UserUtil.isHasUpdateTenantPerms(UserUtil.getUser())){
            // 数据处理责任链
            queryWrapper = new QueryTenantHandler(
                    new QueryDataPermsHandler()
            ).handler(entityClazz, queryWrapper);
        }
        return super.list(queryWrapper);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePassword(UserPassword userPassword) {
        UserModel userModel = super.get(userPassword.getUserId());
        // 如果为空则 不修改密码
        if(userModel == null){
            return false;
        }

        // 判断老密码是否等于新密码
        if(userPassword.getOldPassword().equals(userPassword.getNewPassword())){
            throw new ServiceException(SystemMsg.EXCEPTION_USER_PASSWORD_EQ_ERROR);
        }

        // 获得 处理后 老密码
        String orlPassword = UserUtil.handlePassword(userPassword.getOldPassword(),
                userModel.getSecretKey());

        // 判断老密码是否正确
        if(!userModel.getPassword().equals(orlPassword)){
            throw new ServiceException(SystemMsg.EXCEPTION_USER_PASSWORD_ERROR);
        }

        // 设置随机新盐值
        userPassword.setSalt(
                RandomUtil.randomString(20)
        );
        // 获得密码强度
        userPassword.setPasswordLevel(
                CheckStrength.getPasswordLevel(userPassword.getNewPassword()).getCode()
        );
        // 处理密码
        userPassword.setNewPassword(
                UserUtil.handlePassword(userPassword.getNewPassword(),
                        userPassword.getSalt())
        );

        // 修改密码
        boolean ret = mapper.updatePassword(userPassword);

        if(ret){
            // 刷新用户缓存
            this.clearCache(Collections.singletonList(userModel));
        }

        return ret;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(UserPassword userPassword) {
        UserModel userModel = super.get(userPassword.getUserId());
        // 如果为空则 不修改密码
        if(userModel == null){
            return false;
        }

        // 设置随机新盐值
        userPassword.setSalt(
                RandomUtil.randomString(20)
        );
        // 获得密码强度
        userPassword.setPasswordLevel(
                CheckStrength.getPasswordLevel(userPassword.getNewPassword()).getCode()
        );
        // 处理密码
        userPassword.setNewPassword(
                UserUtil.handlePassword(userPassword.getNewPassword(),
                        userPassword.getSalt())
        );


        // 修改密码
        boolean ret = mapper.updatePassword(userPassword);

        if(ret){
            // 刷新用户缓存
            this.clearCache(Collections.singletonList(userModel));
        }

        return ret;
    }

    /**
     * 更新用户最后登录IP
     * @param model 模型
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLoginIp(UserModel model) {
        if(model == null){
            return false;
        }
        // 激活一下 当前登录用户 User
        //UserUtil.getUser(model.getId());
        SysUser sysUser = new SysUser();
        sysUser.setId(model.getId());
        sysUser.setLoginIp(model.getLoginIp());
        sysUser.setUpdateBy(model.getUpdateBy());
        return mapper.updateLoginIp(sysUser);
    }

    /**
     * 更新用户头像
     * @param model 模型
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAvatar(UserModel model) {
        if(model == null){
            return false;
        }

        SysUser sysUser = new SysUser();
        sysUser.setId(model.getId());
        sysUser.setAvatar(model.getAvatar());
        return mapper.updateAvatar(sysUser);
    }


    private List<SysUserWeb> findListByCus(QueryWrapper<SysUserWeb> queryWrapper) {
        // 如果没有租户修改能力 则默认增加租户限制
        if(!UserUtil.isHasUpdateTenantPerms(UserUtil.getUser())){
            // 数据处理责任链
            queryWrapper = new QueryTenantHandler(
                    new QueryDataPermsHandler()
            ).handler(SysUserWeb.class, queryWrapper);
        }


        // 逻辑删除 查询未删除数据
        queryWrapper.eq(
                FieldUtil.humpToUnderline(MyBatisConstants.FIELD_DELETE_LOGIC), DictType.NO_YES_NO.getValue());
        // 按照ID 分组
        queryWrapper.groupBy("a.id","b.user_id");
        return mapper.findList(queryWrapper);
    }

    @Override
    public Page<SysUserWeb, UserWebModel> findPageByCus(Page<SysUserWeb, UserWebModel> page) {
        UserModel currUser = UserUtil.getUser();

        QueryWrapper<SysUserWeb> queryWrapper = page.getQueryWrapper();

        // 如果不是超级管理员则 无法看到超级管理员账户
        if(!UserUtil.SUPER_ADMIN.equals(currUser.getUsername())){
            queryWrapper.notIn("username", UserUtil.SUPER_ADMIN);
            page.setQueryWrapper(queryWrapper);
        }

        // 不能查看自身
        queryWrapper.notIn("username", currUser.getUsername());

        page.pageHelperBegin();
        try{
            List<SysUserWeb> list = this.findListByCus(page.getQueryWrapper());
            PageInfo<SysUserWeb> pageInfo = new PageInfo<>(list);
            List<UserWebModel> es = WrapperUtil.transformInstance(pageInfo.getList(), UserWebModel.class);
            page.instance(pageInfo, es);
        } finally {
            page.pageHelperEnd();
        }
        return page;
    }



    /**
     * 唯一验证 工号
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public boolean uniqueVerificationByNo(UserModel model){
        if(model == null){
            return false;
        }
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();

        // no 唯一
        wrapper.eq("no", model.getNo());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        // 租户检测
        // 数据处理责任链
        wrapper = new QueryTenantHandler(
                new QueryDataPermsHandler()
        ).handler(entityClazz, wrapper);

        return super.count(wrapper) == 0;
    }

    /**
     * 唯一验证 名称
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public boolean uniqueVerificationByName(UserModel model){
        if(model == null){
            return false;
        }
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();

        // name 唯一
        wrapper.eq("username", model.getUsername());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper) == 0;
    }

    // ==================

    /**
     * 清除缓存
     * @param list 用户集合
     */
    private void clearCache(List<UserModel> list){
        if(CollUtil.isNotEmpty(list)){
            int cacheCount = 0;
            for (UserModel userModel : list) {
                cacheCount++;
                // 刷新用户缓存
                boolean tmp = UserUtil.refreshUser(userModel);
                if(tmp){
                    cacheCount--;
                }
            }
            // 判断删除状态
            if(cacheCount != 0){
                // 删除缓存失败
                throw new ServiceException(CoreMsg.CACHE_DEL_EXCEPTION);
            }
        }
    }

}


