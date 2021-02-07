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
import org.opsli.api.wrapper.system.area.SysAreaModel;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.HumpUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.utils.MenuUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.area.entity.SysArea;
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
        if(model == null){
            return null;
        }

        // 唯一验证
        Integer count = this.uniqueVerificationByCode(model);
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
        if(model == null){
            return null;
        }

        // 唯一验证
        Integer count = this.uniqueVerificationByCode(model);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_UNIQUE);
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
        boolean ret = super.delete(id);
        // 删除子数据
        this.deleteByParentId(id);

        if(ret){
            // 清除缓存
            this.clearCache(menuModel);
        }
        return ret;
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

        boolean ret = super.deleteAll(ids);
        // 删除子数据
        for (String id : ids) {
            this.deleteByParentId(id);
        }

        return ret;
    }

    /**
     * 唯一验证
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public Integer uniqueVerificationByCode(MenuModel model){
        if(model == null){
            return null;
        }
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();

        // code 唯一
        wrapper.eq(MyBatisConstants.FIELD_DELETE_LOGIC, "0")
                .eq("menu_code", model.getMenuCode());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper);
    }


    /**
     * 逐级删除子数据
     * @param parentId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByParentId(String parentId) {
        QueryBuilder<SysMenu> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysMenu> queryWrapper = queryBuilder.build();
        queryWrapper.eq(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);
        List<SysMenu> menuList = super.findList(queryWrapper);
        for (SysMenu sysMenu : menuList) {
            super.delete(sysMenu.getId());
            // 逐级删除子数据
            this.deleteByParentId(sysMenu.getId());
        }
    }

    // ============

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
        // 判断删除状态
        if(cacheCount != 0){
            // 删除缓存失败
            throw new ServiceException(CoreMsg.CACHE_DEL_EXCEPTION);
        }
    }
}


