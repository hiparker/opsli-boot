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
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserRoleRefModel;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.ListDistinctUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.utils.TenantUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.menu.service.IMenuService;
import org.opsli.modulars.system.role.service.IRoleService;
import org.opsli.modulars.system.user.entity.SysUserRoleRef;
import org.opsli.modulars.system.user.mapper.UserRoleRefMapper;
import org.opsli.modulars.system.user.service.IUserRoleRefService;
import org.opsli.modulars.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * 用户-角色 Service Impl
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Service
public class UserRoleRefServiceImpl extends ServiceImpl<UserRoleRefMapper, SysUserRoleRef> implements IUserRoleRefService {

    @Autowired(required = false)
    private UserRoleRefMapper mapper;

    @Lazy
    @Autowired
    private IRoleService iRoleService;

    @Lazy
    @Autowired
    private IUserService iUserService;

    @Lazy
    @Autowired
    private IMenuService iMenuService;

    @Override
    public List<String> getRoleCodeList(String userId) {
        List<String> roles = mapper.getRoleCodeList(userId);
        // 去重
        return ListDistinctUtil.distinct(roles);
    }

    @Override
    public List<String> getRoleIdList(String userId) {
        List<String> roles = mapper.getRoleIdList(userId);
        // 去重
        return ListDistinctUtil.distinct(roles);
    }

    @Override
    public List<String> getAllPerms(String userId) {

        UserModel userModel = iUserService.get(userId);
        if(userModel == null){
            return ListUtil.empty();
        }

        List<String> perms;

        // 判断是否是超级管理员 如果是超级管理员 则默认享有全部权限
        if(StringUtils.equals(UserUtil.SUPER_ADMIN, userModel.getUsername())){
            perms = Lists.newArrayList();
            QueryBuilder<SysMenu> queryBuilder = new GenQueryBuilder<>();
            QueryWrapper<SysMenu> queryWrapper = queryBuilder.build();
            queryWrapper.notIn("parent_id", -1);
            queryWrapper.eq("type", DictType.MENU_BUTTON.getValue());
            queryWrapper.eq("hidden", DictType.NO_YES_NO.getValue());
            queryWrapper.like("label",DictType.MENU_LABEL_SYSTEM.getValue());
            List<SysMenu> menuList = iMenuService.findList(queryWrapper);
            for (SysMenu sysMenu : menuList) {
                perms.add(sysMenu.getPermissions());
            }
        }else{
            if(TenantUtil.SUPER_ADMIN_TENANT_ID.equals(userModel.getTenantId())){
                perms = mapper.queryAllPerms(
                        userId, DictType.MENU_LABEL_SYSTEM.getValue());
            }else {
                perms = mapper.queryAllPerms(
                        userId, DictType.MENU_LABEL_FUNCTION.getValue());
            }
        }

        // 去重
        return ListDistinctUtil.distinct(perms);
    }

    @Override
    public List<MenuModel> getMenuListByUserId(String userId) {

        UserModel userModel = iUserService.get(userId);
        if(userModel == null){
            return ListUtil.empty();
        }

        List<SysMenu> menuList;
        // 判断是否是超级管理员 如果是超级管理员 则默认享有全部权限
        if(StringUtils.equals(UserUtil.SUPER_ADMIN, userModel.getUsername())){
            QueryBuilder<SysMenu> queryBuilder = new GenQueryBuilder<>();
            QueryWrapper<SysMenu> queryWrapper = queryBuilder.build();
            queryWrapper.notIn("parent_id", -1);
            queryWrapper.in("type", DictType.MENU_MENU.getValue(), DictType.MENU_EXTERNAL.getValue());
            queryWrapper.eq("hidden", DictType.NO_YES_NO.getValue());
            queryWrapper.like("label",DictType.MENU_LABEL_SYSTEM.getValue());
            menuList = iMenuService.findList(queryWrapper);
        }else{
            if(TenantUtil.SUPER_ADMIN_TENANT_ID.equals(userModel.getTenantId())){
                menuList = mapper.findMenuListByUserId(
                        userId, DictType.MENU_LABEL_SYSTEM.getValue());
            }else {
                menuList = mapper.findMenuListByUserId(
                        userId, DictType.MENU_LABEL_FUNCTION.getValue());
            }
        }

        // 去重处理 这里不放在SQL 是为了保证数据库兼容性
        List<SysMenu> distinctList = ListDistinctUtil.distinct(
                menuList, Comparator.comparing(ApiWrapper::getId));

        return WrapperUtil.transformInstance(distinctList, MenuModel.class);
    }

    @Override
    public List<MenuModel> getMenuAllListByUserId(String userId, String label) {

        UserModel userModel = iUserService.get(userId);
        if(userModel == null){
            return ListUtil.empty();
        }

        List<SysMenu> menuList;
        // 判断是否是超级管理员 如果是超级管理员 则默认享有全部权限
        if(StringUtils.equals(UserUtil.SUPER_ADMIN, userModel.getUsername())){
            QueryBuilder<SysMenu> queryBuilder = new GenQueryBuilder<>();
            QueryWrapper<SysMenu> queryWrapper = queryBuilder.build();
            queryWrapper.notIn("parent_id", -1);
            queryWrapper.eq("hidden", DictType.NO_YES_NO.getValue());
            queryWrapper.like("label", label);
            menuList = iMenuService.findList(queryWrapper);
        }else{
            menuList = mapper.findMenuAllListByUserId(userId, label);
        }

        if(CollUtil.isEmpty(menuList)){
            return ListUtil.empty();
        }

        // 去重处理 这里不放在SQL 是为了保证数据库兼容性
        List<SysMenu> distinctList = ListDistinctUtil.distinct(
                menuList, Comparator.comparing(ApiWrapper::getId));

        return WrapperUtil.transformInstance(distinctList, MenuModel.class);
    }

    @Override
    public List<String> getUserIdListByRoleId(String roleId) {
        QueryWrapper<SysUserRoleRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("b.deleted", DictType.NO_YES_NO.getValue());
        queryWrapper.eq("role_id", roleId);

        List<String> users = mapper.getUserIdList(queryWrapper);
        if(CollUtil.isEmpty(users)){
            return ListUtil.empty();
        }

        // 去重
        return ListDistinctUtil.distinct(users);
    }

    @Override
    public List<String> getUserIdListByRoleIds(String[] roleIds) {
        QueryWrapper<SysUserRoleRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("b.deleted", DictType.NO_YES_NO.getValue());
        queryWrapper.in("role_id", Convert.toList(roleIds));

        List<String> users = mapper.getUserIdList(queryWrapper);
        if(CollUtil.isEmpty(users)){
            return ListUtil.empty();
        }

        // 去重
        return ListDistinctUtil.distinct(users);
    }

    @Override
    public List<String> getUserIdListByTenantIdAndAllData(String tenantId) {
        QueryWrapper<SysUserRoleRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("b.deleted", DictType.NO_YES_NO.getValue());
        queryWrapper.eq("b.tenant_id", tenantId);
        queryWrapper.eq("c.data_scope", DictType.DATA_SCOPE_ALL.getValue());

        List<String> users = mapper.getUserIdList(queryWrapper);
        if(CollUtil.isEmpty(users)){
            return ListUtil.empty();
        }

        // 去重
        return ListDistinctUtil.distinct(users);
    }

    @Override
    public List<String> getUserIdListByMenuId(String menuId) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("menu.deleted", DictType.NO_YES_NO.getValue())
                .notIn("menu.parent_id", -1)
                .eq("a.menu_id", menuId);

        List<String> users = mapper.getUserIdListByMenu(queryWrapper);
        if(CollUtil.isEmpty(users)){
            return ListUtil.empty();
        }

        // 去重
        return ListDistinctUtil.distinct(users);
    }

    @Override
    public List<String> getUserIdListByMenuIdList(List<String> menuIdList) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("menu.deleted", DictType.NO_YES_NO.getValue())
                .notIn("menu.parent_id", -1)
                .in("a.menu_id", menuIdList);

        List<String> users = mapper.getUserIdListByMenu(queryWrapper);
        if(CollUtil.isEmpty(users)){
            return ListUtil.empty();
        }

        // 去重
        return ListDistinctUtil.distinct(users);
    }

    @Override
    public String getDefRoleId(String userId) {
        String roleId = null;
        QueryWrapper<SysUserRoleRef> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("iz_def", DictType.NO_YES_YES.getValue());
        SysUserRoleRef sysUserRoleRef = this.getOne(wrapper);
        if(sysUserRoleRef != null){
            roleId = sysUserRoleRef.getRoleId();
        }
        return roleId;
    }

    @Override
    public RoleModel getDefRoleByUserId(String userId) {
        String defRoleId = this.getDefRoleId(userId);
        return iRoleService.get(defRoleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setRoles(UserRoleRefModel model) {
        if(StringUtils.isEmpty(model.getUserId())){
            throw new ServiceException(SystemMsg.EXCEPTION_USER_ID_NOT_NULL);
        }

        // 删除已有权限
        String userIdField = "user_id";
        QueryWrapper<SysUserRoleRef> wrapper = new QueryWrapper<>();
        wrapper.eq(userIdField, model.getUserId());
        super.remove(wrapper);

        if(model.getRoleIds() != null && model.getRoleIds().length > 0){
            List<SysUserRoleRef> list = Lists.newArrayListWithCapacity(model.getRoleIds().length);
            for (String roleId : model.getRoleIds()) {
                SysUserRoleRef entity = new SysUserRoleRef();
                entity.setUserId(model.getUserId());
                entity.setRoleId(roleId);

                // 是否默认角色
                if(roleId.equals(model.getDefRoleId())){
                    entity.setIzDef(DictType.NO_YES_YES.getValue());
                }else{
                    entity.setIzDef(DictType.NO_YES_NO.getValue());
                }

                list.add(entity);
            }
            boolean ret = super.saveBatch(list);
            if(ret){
                // 清除缓存
                this.clearCache(model.getUserId());
            }
        }

        return true;
    }

    @Override
    public boolean isRoleUsed(String roleId) {
        if(StringUtils.isBlank(roleId)){
            return false;
        }

        QueryWrapper<SysUserRoleRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);

        return this.count(queryWrapper) != 0;
    }

    @Override
    public boolean isRoleUsed(String[] roleIds) {
        if(ArrayUtil.isEmpty(roleIds)){
            return false;
        }

        QueryWrapper<SysUserRoleRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", Convert.toList(roleIds));

        return this.count(queryWrapper) != 0;
    }


    // ===========

    /**
     * 清除缓存
     * @param userId 用户ID
     */
    private void clearCache(String userId) {
        int cacheCount = 6;
        boolean tmp;
        // 清空当期用户缓存角色、权限、菜单
        tmp = UserUtil.refreshUserRoles(userId);
        if(tmp){
            cacheCount--;
        }
        tmp = UserUtil.refreshUserAllPerms(userId);
        if(tmp){
            cacheCount--;
        }
        tmp = UserUtil.refreshUserMenus(userId);
        if(tmp){
            cacheCount--;
        }
        tmp = UserUtil.refreshUserOrgs(userId);
        if(tmp){
            cacheCount--;
        }
        tmp = UserUtil.refreshUserDefRole(userId);
        if(tmp){
            cacheCount--;
        }
        tmp = UserUtil.refreshUserDefOrg(userId);
        if(tmp){
            cacheCount--;
        }

        // 判断删除状态
        if(cacheCount != 0){
            // 删除缓存失败
            throw new ServiceException(CoreMsg.CACHE_DEL_EXCEPTION);
        }
    }
}


