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
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserAndOrgModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserPassword;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.HumpUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.chain.TenantHandler;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.menu.service.IMenuService;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.entity.SysUserAndOrg;
import org.opsli.modulars.system.user.mapper.UserMapper;
import org.opsli.modulars.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:34
 * @Description: 角色 接口实现类
 */
@Service
public class UserServiceImpl extends CrudServiceImpl<UserMapper, SysUser, UserModel> implements IUserService {

    @Autowired(required = false)
    private UserMapper mapper;
    @Autowired
    private IMenuService iMenuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserModel insert(UserModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        Integer count = this.uniqueVerificationByName(model);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_USER_UNIQUE);
        }
        // 唯一验证 - 工号
        count = this.uniqueVerificationByNo(model);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_USER_NO_UNIQUE);
        }

        // 防止非法操作 - 不允许直接操控到 关键数据
        // 需要注意的是 不要轻易改修改策略
        model.setLoginIp(null);

        // 新增可以直接设置密码
        if(StringUtils.isNotEmpty(model.getPassword())){
            // 设置随机新盐值
            model.setSecretKey(
                    RandomUtil.randomString(20)
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

        return super.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserModel update(UserModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证 - 用户名
        Integer count = this.uniqueVerificationByName(model);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_USER_UNIQUE);
        }
        // 唯一验证 - 工号
        count = this.uniqueVerificationByNo(model);
        if(count != null && count > 0){
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

        UserModel update = super.update(model);
        if(update != null){
            // 如果手机号有变化 则强制覆盖手机号
            if(!StringUtils.equals(userModel.getMobile(), update.getMobile())){
                UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("mobile", null);
                updateWrapper.eq("mobile", update.getMobile());
                this.update(updateWrapper);
            }

            // 刷新用户缓存
            this.clearCache(Collections.singletonList(userModel));
        }

        return update;
    }

    @Override
    public boolean delete(String id) {
        // 杜绝我删我自己行为
        UserModel currUser = UserUtil.getUser();
        if(StringUtils.equals(currUser.getId(), id)){
            return false;
        }

        UserModel userModel = super.get(id);
        boolean ret = super.delete(id);
        if(ret){
            // 刷新用户缓存
            this.clearCache(Collections.singletonList(userModel));
        }
        return ret;
    }

    @Override
    public boolean delete(UserModel model) {
        UserModel userModel = null;
        if(model != null){
            userModel = this.get(model.getId());

        }
        // 非法判断
        if(userModel == null){
            return false;
        }

        // 杜绝我删我自己行为
        UserModel currUser = UserUtil.getUser();
        if(StringUtils.equals(currUser.getId(), userModel.getId())){
            return false;
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

        // 杜绝我删我自己行为
        UserModel currUser = UserUtil.getUser();
        for (String id : ids) {
            if(StringUtils.equals(currUser.getId(), id)){
                return false;
            }
        }

        QueryBuilder<SysUser> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysUser> queryWrapper = queryBuilder.build();
        List<String> idList = Convert.toList(String.class, ids);
        queryWrapper.in(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_ID),idList);
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

        // 杜绝我删我自己行为
        UserModel currUser = UserUtil.getUser();

        List<String> idList = Lists.newArrayListWithCapacity(models.size());
        for (UserModel model : models) {
            if(StringUtils.equals(currUser.getId(), model.getId())){
                return false;
            }
            idList.add(model.getId());
        }

        QueryBuilder<SysUser> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysUser> queryWrapper = queryBuilder.build();
        queryWrapper.in(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_ID),idList);
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
        String key = HumpUtil.humpToUnderline("username");
        QueryBuilder<SysUser> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysUser> queryWrapper = queryBuilder.build();
        queryWrapper.eq(key, username);
        queryWrapper.eq(
                HumpUtil.humpToUnderline(MyBatisConstants.FIELD_DELETE_LOGIC)
                , "0");
        SysUser user = this.getOne(queryWrapper);
        return super.transformT2M(user);
    }

    @Override
    public List<String> getRoleCodeList(String userId) {
        List<String> roles = mapper.getRoleCodeList(userId);
        // 去重
        return new ArrayList<>(new LinkedHashSet<>(roles));
    }

    @Override
    public List<String> getRoleIdList(String userId) {
        List<String> roles = mapper.getRoleIdList(userId);
        // 去重
        return new ArrayList<>(new LinkedHashSet<>(roles));
    }

    @Override
    public List<String> getAllPerms(String userId) {

        UserModel userModel = this.get(userId);
        if(userModel == null){
            return new ArrayList<>();
        }

        List<String> perms;

        // 判断是否是超级管理员 如果是超级管理员 则默认享有全部权限
        if(StringUtils.equals(UserUtil.SUPER_ADMIN, userModel.getUsername())){
            perms = Lists.newArrayList();
            QueryBuilder<SysMenu> queryBuilder = new GenQueryBuilder<>();
            QueryWrapper<SysMenu> queryWrapper = queryBuilder.build();
            queryWrapper.notIn("parent_id", -1);
            queryWrapper.eq("type", '2');
            queryWrapper.eq("hidden", '0');
            List<SysMenu> menuList = iMenuService.findList(queryWrapper);
            for (SysMenu sysMenu : menuList) {
                perms.add(sysMenu.getMenuCode());
            }
        }else{
            perms = mapper.queryAllPerms(userId);
        }

        // 去重
        return new ArrayList<>(new LinkedHashSet<>(perms));
    }

    @Override
    public List<MenuModel> getMenuListByUserId(String userId) {

        UserModel userModel = this.get(userId);
        if(userModel == null){
            return new ArrayList<>();
        }

        List<SysMenu> menuList;

        // 判断是否是超级管理员 如果是超级管理员 则默认享有全部权限
        if(StringUtils.equals(UserUtil.SUPER_ADMIN, userModel.getUsername())){
            QueryBuilder<SysMenu> queryBuilder = new GenQueryBuilder<>();
            QueryWrapper<SysMenu> queryWrapper = queryBuilder.build();
            queryWrapper.notIn("parent_id", -1);
            queryWrapper.in("type", '1', '3');
            queryWrapper.eq("hidden", '0');
            menuList = iMenuService.findList(queryWrapper);
        }else{
            menuList = mapper.findMenuListByUserId(userId);
        }

        return WrapperUtil.transformInstance(menuList, MenuModel.class);
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
     * @param model
     * @return
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
     * @param model
     * @return
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


    public List<SysUserAndOrg> findListByCus(QueryWrapper<SysUserAndOrg> queryWrapper) {
        // 多租户处理
        QueryWrapper<SysUserAndOrg> qWrapper = new TenantHandler().handler(SysUserAndOrg.class, queryWrapper);
        // 逻辑删除 查询未删除数据
        qWrapper.eq(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_DELETE_LOGIC),
            "0"
        );
        return mapper.findList(qWrapper);
    }

    @Override
    public Page<SysUserAndOrg,UserAndOrgModel> findPageByCus(Page<SysUserAndOrg,UserAndOrgModel> page) {
        page.pageHelperBegin();
        try{
            List<SysUserAndOrg> list = this.findListByCus(page.getQueryWrapper());
            PageInfo<SysUserAndOrg> pageInfo = new PageInfo<>(list);
            List<UserAndOrgModel> es = WrapperUtil.transformInstance(pageInfo.getList(), UserAndOrgModel.class);
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
    public Integer uniqueVerificationByNo(UserModel model){
        if(model == null){
            return null;
        }
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();

        // no 唯一
        wrapper.eq(MyBatisConstants.FIELD_DELETE_LOGIC,  DictType.NO_YES_NO.getCode())
                .eq("no", model.getNo());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper);
    }

    /**
     * 唯一验证 名称
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public Integer uniqueVerificationByName(UserModel model){
        if(model == null){
            return null;
        }
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();

        // name 唯一
        wrapper.eq(MyBatisConstants.FIELD_DELETE_LOGIC,  DictType.NO_YES_NO.getCode())
                .eq("username", model.getUsername());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper);
    }

    // ==================

    /**
     * 清除缓存
     * @param list
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


