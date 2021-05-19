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
package org.opsli.modulars.system.role.service;

import org.opsli.modulars.system.menu.entity.SysMenu;

import java.util.List;

/**
 * 角色-菜单 Service
 *
 * @author Parker
 * @date 2020-09-17 13:07
 */
public interface IRoleMenuRefService {

    /**
     * 根据角色ID 获得权限（包含菜单）
     * @param roleId 角色ID
     * @return List
     */
    List<SysMenu> getPerms(String roleId);

    /**
     * 保存权限
     * @param roleId 角色ID
     * @param permsIds 权限集合
     * @return boolean
     */
    boolean setPerms(String roleId,String[] permsIds);

    /**
     * 根据角色ID 移除对应 权限数据
     * @param roleIds 角色ID
     * @return boolean
     */
    boolean delPermsByRoleIds(List<String> roleIds);

    /**
     * 根据菜单ID 移除对应 权限数据
     * @param menuIds 菜单ID
     * @return boolean
     */
    boolean delPermsByMenuIds(List<String> menuIds);

}
