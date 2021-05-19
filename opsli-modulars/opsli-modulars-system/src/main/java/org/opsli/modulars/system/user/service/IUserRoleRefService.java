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

import java.util.List;

/**
 * 用户-角色 Service
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
public interface IUserRoleRefService {

    /**
     * 根据角色ID 获得当前用户Id集合
     * @param roleId 角色ID
     * @return List
     */
    List<String> getUserIdListByRoleId(String roleId);

    /**
     * 根据菜单ID 获得当前用户Id集合
     * @param roleId 角色ID
     * @return List
     */
    List<String> getUserIdListByMenuId(String roleId);

    /**
     * 保存角色
     * @param userId 用户ID
     * @param roleIds 角色ID 集合
     * @return boolean
     */
    boolean setRoles(String userId,String[] roleIds);

}
