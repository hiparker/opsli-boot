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

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserAndOrgModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserPassword;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.HumpUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.chain.TenantHandler;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.entity.SysUserAndOrg;
import org.opsli.modulars.system.user.mapper.UserMapper;
import org.opsli.modulars.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserModel insert(UserModel model) {
        if(model == null){
            return null;
        }

        SysUser entity = super.transformM2T(model);
        // 唯一验证
        Integer count = mapper.uniqueVerificationByUsername(entity);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_USER_UNIQUE);
        }
        // 唯一验证 - 工号
        count = mapper.uniqueVerificationByNo(entity);
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
            model.setSecretkey(
                    RandomUtil.randomString(20)
            );
            // 处理密码
            model.setPassword(
                    UserUtil.handlePassword(model.getPassword(),
                            model.getSecretkey())
            );
        }

        return super.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserModel update(UserModel model) {
        if(model == null){
            return null;
        }

        SysUser entity = super.transformM2T(model);
        // 唯一验证 - 用户名
        Integer count = mapper.uniqueVerificationByUsername(entity);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_USER_UNIQUE);
        }
        // 唯一验证 - 工号
        count = mapper.uniqueVerificationByNo(entity);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_USER_NO_UNIQUE);
        }

        // 防止非法操作 - 不允许直接操控到 关键数据
        // 需要注意的是 不要轻易改修改策略
        model.setUsername(null);
        model.setPassword(null);
        model.setSecretkey(null);
        model.setLoginIp(null);

        UserModel update = super.update(model);
        if(update != null){
            // 刷新用户缓存
            UserUtil.refreshUser(update);
        }

        return update;
    }

    @Override
    public UserModel queryByUserName(String username) {
        String key = HumpUtil.humpToUnderline("username");
        QueryBuilder<SysUser> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysUser> queryWrapper = queryBuilder.build();
        queryWrapper.eq(key, username);
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
        List<String> perms = mapper.queryAllPerms(userId);
        // 去重
        return new ArrayList<>(new LinkedHashSet<>(perms));
    }

    @Override
    public List<MenuModel> getMenuListByUserId(String userId) {
        List<SysMenu> menuList = mapper.findMenuListByUserId(userId);
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
        // 获得 处理后 老密码
        String orlPassword = UserUtil.handlePassword(userPassword.getOldPassword(),
                userModel.getSecretkey());

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
            UserUtil.refreshUser(userModel);
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
        UserUtil.getUser(model.getId());

        SysUser sysUser = new SysUser();
        sysUser.setId(model.getId());
        sysUser.setLoginIp(model.getLoginIp());
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

}


