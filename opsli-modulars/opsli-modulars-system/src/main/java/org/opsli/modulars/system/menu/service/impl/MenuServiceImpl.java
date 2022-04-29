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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.menu.MenuFullModel;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.constants.MenuConstants;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.constants.TreeConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.FieldUtil;
import org.opsli.core.base.entity.HasChildren;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.utils.MenuUtil;
import org.opsli.core.utils.TreeBuildUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.menu.factory.MenuFactory;
import org.opsli.modulars.system.menu.mapper.MenuMapper;
import org.opsli.modulars.system.menu.service.IMenuService;
import org.opsli.modulars.system.role.service.IRoleMenuRefService;
import org.opsli.modulars.system.user.service.IUserRoleRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * 菜单表 Service Impl
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Service
public class MenuServiceImpl extends CrudServiceImpl<MenuMapper, SysMenu, MenuModel> implements IMenuService {

    /** 菜单管理ID */
    private static final String MENU_ID = "2";

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

        // 先清空 parentIds 防止数据出现异常问题
        model.setParentIds(null);

        // 如果上级ID 为空 则默认为 0
        if(StringUtils.isEmpty(model.getParentId())){
            model.setParentId(MenuConstants.GEN_ID);
        }



        // 容错处理
        // 如果根结点为0 且 为菜单类型 判断首位是否为 / 如果没有则自动加 /
        if(MenuConstants.GEN_ID.equals(model.getParentId())){
            if(StringUtils.isNotEmpty(model.getUrl())){
                String url = StrUtil.addPrefixIfNot(model.getUrl(), "/");
                model.setUrl(url);
            }
        }

        // 刷新缓存
        clearCache(Collections.singletonList(model));

        // 新增并更新 parent_ids
        MenuModel menuModel = super.insert(model);
        MenuModel newParentModel = getParentMenuModel(menuModel.getParentId());
        String newParentIds = unWrap(
                StrUtil.join(",",
                        newParentModel.getParentIds(), menuModel.getId()), ',');
        menuModel.setParentIds(newParentIds);
        menuModel.setVersion(null);
        return super.update(menuModel);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuModel update(MenuModel model) {
        if(model == null){
            return null;
        }

        // 先清空 parentIds 防止数据出现异常问题
        model.setParentIds(null);

        // 如果开启 隐藏功能
        if(DictType.NO_YES_YES.getValue().equals(model.getHidden())){
            // 需要判断是否直接隐藏掉 菜单管理功能 （防止直接隐藏掉后 无法管理菜单）
            if(MENU_ID.equals(model.getId())){
                // 不可操作自身
                throw new ServiceException(SystemMsg.EXCEPTION_MENU_HANDLE_SELF);
            }
        }

        // 原数据
        MenuModel sourceModel = this.get(model);
        if(null == sourceModel){
            // 未找到菜单
            throw new ServiceException(SystemMsg.EXCEPTION_MENU_NULL);
        }

        MenuModel oldParentModel = getParentMenuModel(sourceModel.getParentId());
        MenuModel newParentModel = getParentMenuModel(model.getParentId());

        // 去除 两边的[,] 然后拼接 parentIds
        String ordParentIds = unWrap(
                StrUtil.join(",",
                        oldParentModel.getParentIds(), sourceModel.getId()), ',');

        String newParentIds = unWrap(
                StrUtil.join(",",
                        newParentModel.getParentIds(), model.getId()), ',');

        // 判断 上级是否发生变更 如果发生变更 则当前菜单 继承上级菜单的 label
        if(!sourceModel.getParentId().equals(model.getParentId())){
            model.setParentIds(newParentIds);
            model.setLabel(newParentModel.getLabel());

            // 查询所有受影响的子集菜单 批量修改子类的parentIds
            QueryWrapper<SysMenu> findChildWrapper = new QueryWrapper<>();
            findChildWrapper.likeRight("parent_ids", ordParentIds);
            List<SysMenu> menuList = this.list(findChildWrapper);
            // 循环变更 parentIds
            for (SysMenu sysMenu : menuList) {
                String parentIds = sysMenu.getParentIds();
                if(StringUtils.isBlank(parentIds)){
                    parentIds = newParentIds;
                }else {
                    parentIds = StrUtil.replace(parentIds, ordParentIds, newParentIds);
                }
                sysMenu.setParentIds(parentIds);
                // 内部修改 防止版本行锁出现问题
                sysMenu.setVersion(null);
            }

            this.saveOrUpdateBatch(menuList);
        }

        // 如果 原始标签 与 当前标签发送变更 则逐级变更子类标签
        if(!sourceModel.getLabel().equals(model.getLabel())){
            // 查询所有受影响的子集菜单
            QueryWrapper<SysMenu> findChildWrapper = new QueryWrapper<>();
            findChildWrapper.likeRight("parent_ids", newParentIds);
            List<MenuModel> menuList = super.transformTs2Ms(this.list(findChildWrapper));

            // 刷新缓存
            this.clearCache(menuList);

            // 修改所有子集 label
            UpdateWrapper<SysMenu> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("label", model.getLabel());
            updateWrapper.likeRight("parent_ids", newParentIds);
            this.update(updateWrapper);
        }

        // 容错处理
        // 如果根结点为0 且 为菜单类型 判断首位是否为 / 如果没有则自动加 /
        if(MenuConstants.GEN_ID.equals(model.getParentId())){
            if(StringUtils.isNotEmpty(model.getUrl())){
                String url = StrUtil.addPrefixIfNot(model.getUrl(), "/");
                model.setUrl(url);
            }
        }

        MenuModel menuModel = super.update(model);
        if(menuModel != null){
            // 刷新缓存
            this.clearCache(Collections.singletonList(model));
        }

        return menuModel;
    }

    /**
     * 获得父级菜单 model
     * @param parentId 父级菜单ID
     * @return MenuModel
     */
    private MenuModel getParentMenuModel(String parentId) {
        // 父类菜单
        MenuModel parentModel;
        if(TreeBuildUtil.DEF_PARENT_ID.equals(parentId)){
            parentModel = new MenuModel();
            parentModel.setId(TreeBuildUtil.DEF_PARENT_ID);
            parentModel.setParentId(TreeBuildUtil.DEF_PARENT_ID);
            parentModel.setParentIds(TreeBuildUtil.DEF_PARENT_ID);
            parentModel.setLabel(DictType.MENU_LABEL_SYSTEM.getValue());
        }else {
            parentModel = this.get(parentId);
            if(null == parentModel){
                // 未找到菜单
                throw new ServiceException(SystemMsg.EXCEPTION_MENU_NULL);
            }
        }
        return parentModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMenuByFull(MenuFullModel menuFullModel) {
        if(menuFullModel == null){
            return;
        }

        // 容错处理
        // 如果根节点没有 Layout 节点 则自动创建一个
        if(MenuConstants.GEN_ID.equals(menuFullModel.getParentId())){
            MenuModel genMenu = new MenuModel();
            genMenu.setParentId(menuFullModel.getParentId());
            genMenu.setMenuName(menuFullModel.getTitle());
            genMenu.setAlwaysShow(DictType.NO_YES_NO.getValue());
            genMenu.setHidden(DictType.NO_YES_NO.getValue());
            genMenu.setUrl(menuFullModel.getSubModuleName()+"Gen");
            genMenu.setType(DictType.MENU_MENU.getValue());
            genMenu.setComponent("Layout");
            genMenu.setLabel(DictType.MENU_LABEL_SYSTEM.getValue());
            genMenu.setRedirect(menuFullModel.getSubModuleName());
            genMenu.setSortNo(1);
            MenuModel genMenuModel = this.insert(genMenu);

            // 重置上级ID
            menuFullModel.setParentId(genMenuModel.getId());
        }

        MenuModel menu = MenuFactory.INSTANCE.createMenu(menuFullModel);
        // 保存菜单
        MenuModel menuModel = this.insert(menu);

        // 保存按钮

        // 重置上级ID
        menuFullModel.setParentId(menuModel.getId());
        List<MenuModel> menuBtnList = MenuFactory.INSTANCE.createMenuBtnList(menuFullModel);
        for (MenuModel btnModel : menuBtnList) {
            this.insert(btnModel);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        MenuModel menuModel = super.get(id);
        if(menuModel == null){
            return false;
        }

        // 需要判断是否直接删 菜单管理功能 （防止直接删除掉后 无法管理菜单）
        if(MENU_ID.equals(menuModel.getId())){
            // 不可操作自身
            throw new ServiceException(SystemMsg.EXCEPTION_MENU_HANDLE_SELF);
        }

        // 清除缓存
        this.clearCache(Collections.singletonList(menuModel));

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

        // 循环处理
        for (MenuModel menuModel : menuList) {
            // 需要判断是否直接删 菜单管理功能 （防止直接删除掉后 无法管理菜单）
            if(MENU_ID.equals(menuModel.getId())){
                // 不可操作自身
                throw new ServiceException(SystemMsg.EXCEPTION_MENU_HANDLE_SELF);
            }

            // 清除缓存
            this.clearCache(Collections.singletonList(menuModel));

            // 先删子数据
            this.deleteByParentId(menuModel.getId());
        }

        // 移除权限数据
        iRoleMenuRefService.delPermsByMenuIds(Convert.toList(String.class, ids));

        return super.deleteAll(ids);
    }


    /**
     * 逐级删除子数据
     * @param parentId 父级ID
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByParentId(String parentId) {
        boolean ret = false;
        QueryBuilder<SysMenu> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysMenu> queryWrapper = queryBuilder.build();
        queryWrapper.eq(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);
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

    /**
     * 是否有下级
     * @param parentIds 父级ID 集合
     * @return List
     */
    @Override
    @Transactional(readOnly = true)
    public List<HasChildren> hasChildren(Set<String> parentIds){
        if(CollUtil.isEmpty(parentIds)){
            return null;
        }
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.in(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentIds)
                .eq(MyBatisConstants.FIELD_DELETE_LOGIC,  DictType.NO_YES_NO.getValue())
                .groupBy(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID));

        return mapper.hasChildren(wrapper);
    }

    /**
     * 是否有下级
     * @param parentIds 父级ID 集合
     * @return List
     */
    @Override
    @Transactional(readOnly = true)
    public List<HasChildren> hasChildrenByChoose(Set<String> parentIds){
        if(CollUtil.isEmpty(parentIds)){
            return null;
        }
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.in(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentIds)
                .eq(MyBatisConstants.FIELD_DELETE_LOGIC,  DictType.NO_YES_NO.getValue())
                .eq("type", "1")
                .groupBy(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID));

        return mapper.hasChildren(wrapper);
    }

    // ============

    /**
     * 唯一验证 只比较菜单为 按钮
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public boolean uniqueVerificationByPermissions(MenuModel model){
        if(model == null){
            return false;
        }
        if(!MenuConstants.BUTTON.equals(model.getType())){
            return true;
        }

        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();

        // code 唯一
        wrapper.eq("permissions", model.getPermissions())
                .eq("type", MenuConstants.BUTTON);

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper) == 0;
    }

    /**
     * 清除缓存
     * @param menuModelList 模型
     */
    private void clearCache(List<MenuModel> menuModelList){
        if(CollUtil.isEmpty(menuModelList)){
            return;
        }

        boolean cacheRet;
        // 计数器
        int cacheCount = menuModelList.size();

        // 菜单ID
        List<String> menuIdList = Lists.newArrayListWithCapacity(menuModelList.size());
        for (MenuModel menuModel : menuModelList) {
            menuIdList.add(menuModel.getId());
            // 先清除缓存
            // 清空编号缓存
            cacheRet = MenuUtil.refreshMenu(menuModel);
            if(cacheRet){
                cacheCount--;
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

        // 刷新用户缓存
        // 清空该菜单下 用户缓存
        List<String> userIdList = iUserRoleRefService.getUserIdListByMenuIdList(menuIdList);
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



    /**
     * 去掉字符包装，如果未被包装则返回原字符串
     *
     * @param str    字符串
     * @param prefix 前置字符
     * @return 去掉包装字符的字符串
     * @since 4.0.1
     */
    public static String unWrap(CharSequence str, char prefix) {
        if (StrUtil.isEmpty(str)) {
            return StrUtil.str(str);
        }

        if (str.charAt(0) == prefix) {
            return StrUtil.subSuf(str, 1);
        }
        return str.toString();
    }
}


