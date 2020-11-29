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

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.HumpUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.utils.MenuUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.menu.mapper.MenuMapper;
import org.opsli.modulars.system.menu.service.IMenuService;
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

    @Autowired(required = false)
    private MenuMapper mapper;
    @Autowired
    private IUserRoleRefService iUserRoleRefService;

    @Override
    public MenuModel getByCode(String menuCode) {
        QueryBuilder<SysMenu> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysMenu> queryWrapper = queryBuilder.build();
        queryWrapper.eq("menu_code", menuCode);
        List<SysMenu> sysMenus = super.findList(queryWrapper);
        if(sysMenus != null && !sysMenus.isEmpty()){
            return transformT2M(sysMenus.get(0));
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MenuModel insert(MenuModel model) {
        if(model == null) return null;

        SysMenu entity = super.transformM2T(model);
        // 唯一验证
        Integer count = mapper.uniqueVerificationByCode(entity);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_UNIQUE);
        }

        // 如果上级ID 为空 则默认为 0
        if(StringUtils.isEmpty(model.getParentId())){
            model.setParentId("0");
        }

        return super.insert(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuModel update(MenuModel model) {
        if(model == null) return null;

        SysMenu entity = super.transformM2T(model);
        // 唯一验证
        Integer count = mapper.uniqueVerificationByCode(entity);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_UNIQUE);
        }

        MenuModel menuModel = super.update(model);
        if(menuModel != null){
            // 清空编号缓存
            MenuUtil.refreshMenu(menuModel);
            // 清空该菜单下 用户缓存
            List<String> userIdList = iUserRoleRefService.getUserIdListByMenuId(menuModel.getId());
            if(userIdList != null && !userIdList.isEmpty()){
                for (String userId : userIdList) {
                    // 清空当期用户缓存角色、权限、菜单
                    UserUtil.refreshUserRoles(userId);
                    UserUtil.refreshUserAllPerms(userId);
                    UserUtil.refreshUserMenus(userId);
                }
            }
        }

        return menuModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        MenuModel menuModel = super.get(id);
        boolean ret = super.delete(id);
        // 删除子数据
        this.deleteByParentId(id);


        if(ret){
            // 清空编号缓存
            MenuUtil.refreshMenu(menuModel);
            // 清空该菜单下 用户缓存
            List<String> userIdList = iUserRoleRefService.getUserIdListByMenuId(id);
            if(userIdList != null && !userIdList.isEmpty()){
                for (String userId : userIdList) {
                    // 清空当期用户缓存角色、权限、菜单
                    UserUtil.refreshUserRoles(userId);
                    UserUtil.refreshUserAllPerms(userId);
                    UserUtil.refreshUserMenus(userId);
                }
            }
        }
        return ret;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAll(String[] ids) {
        QueryBuilder<SysMenu> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysMenu> queryWrapper = queryBuilder.build();
        queryWrapper.in(MyBatisConstants.FIELD_ID, Convert.toList(String.class, ids));
        List<SysMenu> menuList = super.findList(queryWrapper);

        boolean ret = super.deleteAll(ids);
        // 删除子数据
        for (String id : ids) {
            this.deleteByParentId(id);
        }

        if(ret){
            // 清空编号缓存
            for (SysMenu sysMenu : menuList) {
                MenuUtil.refreshMenu(
                        WrapperUtil.transformInstance(sysMenu, MenuModel.class)
                );
            }

            for (String id : ids) {
                // 清空该菜单下 用户缓存
                List<String> userIdList = iUserRoleRefService.getUserIdListByMenuId(id);
                if(userIdList != null && !userIdList.isEmpty()){
                    for (String userId : userIdList) {
                        // 清空当期用户缓存角色、权限、菜单
                        UserUtil.refreshUserRoles(userId);
                        UserUtil.refreshUserAllPerms(userId);
                        UserUtil.refreshUserMenus(userId);
                    }
                }
            }
        }
        return ret;
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
        for (SysMenu sysMenu : menuList) {
            super.delete(sysMenu.getId());
            // 逐级删除子数据
            ret = this.deleteByParentId(sysMenu.getId());
        }
        return ret;
    }
}


