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
package org.opsli.modulars.system.user.service;

import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.api.wrapper.system.user.UserRoleRefModel;
import org.opsli.modulars.system.menu.entity.SysMenu;

import java.util.List;

/**
 * 用户-角色 Service
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
public interface IUserRoleRefService {

    /**
     * 根据用户ID 获得当前角色编码集合
     * @param userId 用户ID
     * @return List
     */
    List<String> getRoleCodeList(String userId);

    /**
     * 根据用户ID 获得当前角色Id集合
     * @param userId 用户ID
     * @return List
     */
    List<String> getRoleIdList(String userId);

    /**
     * 根据用户ID 获得权限
     * @param userId 用户ID
     * @return List
     */
    List<String> getAllPerms(String userId);

    /**
     * 根据用户ID 获得菜单集合
     * @param userId 用户ID
     * @return List
     */
    List<MenuModel> getMenuListByUserId(String userId);

    /**
     * 根据用户ID 获得全部菜单集合
     * @param userId 用户ID
     * @param label 标签
     * @return List
     */
    List<MenuModel> getMenuAllListByUserId(String userId, String label);

    /**
     * 根据角色ID 获得当前用户Id集合
     * @param roleId 角色ID
     * @return List
     */
    List<String> getUserIdListByRoleId(String roleId);

    /**
     * 根据角色ID 获得当前用户Id集合
     * @param roleIds 角色ID 数组
     * @return List
     */
    List<String> getUserIdListByRoleIds(String[] roleIds);

    /**
     * 根据租户ID 获得当前租户 下 数据权限为全部数据的 所有用户ID
     * @param tenantId 角色ID
     * @return List
     */
    List<String> getUserIdListByTenantIdAndAllData(String tenantId);

    /**
     * 根据菜单ID 获得当前用户Id集合
     * @param menuId 菜单ID
     * @return List
     */
    List<String> getUserIdListByMenuId(String menuId);

    /**
     * 根据菜单ID集合 获得当前用户Id集合
     * @param menuIdList 菜单ID 集合
     * @return List
     */
    List<String> getUserIdListByMenuIdList(List<String> menuIdList);

    /**
     * 根据用户ID 获得当前默认角色ID
     * @param userId 用户ID
     * @return List
     */
    String getDefRoleId(String userId);

    /**
     * 根据用户ID 获得当前角色对象
     * @param userId 用户ID
     * @return List
     */
    RoleModel getDefRoleByUserId(String userId);

    /**
     * 保存角色
     * @param model 模型
     * @return boolean
     */
    boolean setRoles(UserRoleRefModel model);


    /**
     * 判断角色是否 有用户使用
     * @param roleId 角色ID
     * @return boolean
     */
    boolean isRoleUsed(String roleId);

    /**
     * 判断角色是否 有用户使用
     * @param roleIds 角色ID 数组
     * @return boolean
     */
    boolean isRoleUsed(String[] roleIds);


}
