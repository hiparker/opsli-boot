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
package org.opsli.modulars.system.menu.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.HumpUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.utils.MenuUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.menu.mapper.MenuMapper;
import org.opsli.modulars.system.menu.service.IMenuService;
import org.opsli.modulars.system.role.service.IRoleMenuRefService;
import org.opsli.modulars.system.user.service.IUserRoleRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:34
 * @Description: 角色 接口实现类
 */
@Service
public class MenuServiceImpl extends CrudServiceImpl<MenuMapper, SysMenu, MenuModel> implements IMenuService {

    /** 按钮类型 */
    private static final String BUTTON_TYPE = "2";

    @Autowired(required = false)
    private MenuMapper mapper;
    @Autowired
    private IUserRoleRefService iUserRoleRefService;
    @Autowired
    private IRoleMenuRefService iRoleMenuRefService;

    @Override
    public MenuModel getByPermissions(String permissions) {
        QueryBuilder<SysMenu> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysMenu> queryWrapper = queryBuilder.build();
        queryWrapper.eq("permissions", permissions);
        List<SysMenu> sysMenus = super.findList(queryWrapper);
        if(sysMenus != null && !sysMenus.isEmpty()){
            return transformT2M(sysMenus.get(0));
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MenuModel insert(MenuModel model) {
        if(model == null){
            return null;
        }

        // 按钮 权限唯一验证
        Integer count = this.uniqueVerificationByPermissions(model);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_MENU_PERMISSIONS_UNIQUE);
        }

        // 如果上级ID 为空 则默认为 0
        if(StringUtils.isEmpty(model.getParentId())){
            model.setParentId("0");
        }

        // 菜单有变动 直接刷新超级管理员 菜单缓存
        UserModel adminUser = UserUtil.getUserByUserName(UserUtil.SUPER_ADMIN);
        if(adminUser != null){
            // 计数器
            int cacheCount = 2;
            boolean cacheRet;
            cacheRet = UserUtil.refreshUserAllPerms(adminUser.getId());
            if(cacheRet){
                cacheCount--;
            }
            cacheRet = UserUtil.refreshUserMenus(adminUser.getId());
            if(cacheRet){
                cacheCount--;
            }

            // 判断删除状态
            if(cacheCount != 0){
                // 删除缓存失败
                throw new ServiceException(CoreMsg.CACHE_DEL_EXCEPTION);
            }
        }

        return super.insert(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuModel update(MenuModel model) {
        if(model == null){
            return null;
        }

        // 按钮 权限唯一验证
        Integer count = this.uniqueVerificationByPermissions(model);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_MENU_PERMISSIONS_UNIQUE);
        }

        MenuModel menuModel = super.update(model);
        if(menuModel != null){
            // 清除缓存
            this.clearCache(model);
        }

        return menuModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        MenuModel menuModel = super.get(id);
        if(menuModel == null){
            return false;
        }

        // 清除缓存
        this.clearCache(menuModel);

        // 删除子数据
        this.deleteByParentId(id);

        // 移除权限数据
        iRoleMenuRefService.delPermsByMenuIds(Convert.toList(String.class, id));

        return super.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAll(String[] ids) {
        QueryBuilder<SysMenu> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysMenu> queryWrapper = queryBuilder.build();
        queryWrapper.in(MyBatisConstants.FIELD_ID, Convert.toList(String.class, ids));
        List<MenuModel> menuList = super.transformTs2Ms(
                super.findList(queryWrapper)
        );

        // 清除缓存
        for (MenuModel menuModel : menuList) {
            this.clearCache(menuModel);
        }

        // 先删除子数据
        for (String id : ids) {
            this.deleteByParentId(id);
        }

        // 移除权限数据
        iRoleMenuRefService.delPermsByMenuIds(Convert.toList(String.class, ids));

        return super.deleteAll(ids);
    }


    /**
     * 逐级删除子数据
     * @param parentId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByParentId(String parentId) {
        boolean ret = false;
        QueryBuilder<SysMenu> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysMenu> queryWrapper = queryBuilder.build();
        queryWrapper.eq(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);
        List<SysMenu> menuList = super.findList(queryWrapper);
        for (SysMenu child : menuList) {
            // 删除菜单数据
            super.delete(child.getId());
            // 移除权限数据
            iRoleMenuRefService.delPermsByMenuIds(Convert.toList(String.class, child.getId()));
            // 逐级删除子数据
            ret = this.deleteByParentId(child.getId());
        }
        return ret;
    }

    // ============

    /**
     * 唯一验证 指比较菜单为 按钮
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public Integer uniqueVerificationByPermissions(MenuModel model){
        if(model == null){
            return null;
        }
        if(!BUTTON_TYPE.equals(model.getType())){
            return null;
        }

        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();

        // code 唯一
        wrapper.eq(MyBatisConstants.FIELD_DELETE_LOGIC,  DictType.NO_YES_NO.getValue())
                .eq("permissions", model.getPermissions())
                .eq("type", BUTTON_TYPE);

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper);
    }

    /**
     * 清除缓存
     * @param menuModel
     */
    private void clearCache(MenuModel menuModel){
        boolean cacheRet;
        // 计数器
        int cacheCount = 1;
        // 先清除缓存
        // 清空编号缓存
        cacheRet = MenuUtil.refreshMenu(menuModel);
        if(cacheRet){
            cacheCount--;
        }
        // 清空该菜单下 用户缓存
        List<String> userIdList = iUserRoleRefService.getUserIdListByMenuId(menuModel.getId());
        if(CollUtil.isNotEmpty(userIdList)){
            for (String userId : userIdList) {
                cacheCount += 3;
                // 清空当期用户缓存角色、权限、菜单
                cacheRet = UserUtil.refreshUserRoles(userId);
                if(cacheRet){
                    cacheCount--;
                }
                cacheRet = UserUtil.refreshUserAllPerms(userId);
                if(cacheRet){
                    cacheCount--;
                }
                cacheRet = UserUtil.refreshUserMenus(userId);
                if(cacheRet){
                    cacheCount--;
                }
            }
        }

        // 菜单有变动 直接刷新超级管理员 菜单缓存
        UserModel adminUser = UserUtil.getUserByUserName(UserUtil.SUPER_ADMIN);
        if(adminUser != null){
            cacheCount += 2;
            cacheRet = UserUtil.refreshUserAllPerms(adminUser.getId());
            if(cacheRet){
                cacheCount--;
            }
            cacheRet = UserUtil.refreshUserMenus(adminUser.getId());
            if(cacheRet){
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


