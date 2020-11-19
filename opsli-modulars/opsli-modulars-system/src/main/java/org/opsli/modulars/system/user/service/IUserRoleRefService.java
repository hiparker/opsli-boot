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
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-17 13:07
 * @Description: 用户-角色 接口
 */
public interface IUserRoleRefService {

    /**
     * 根据角色ID 获得当前用户Id集合
     * @param roleId
     * @return
     */
    List<String> getUserIdListByRoleId(String roleId);

    /**
     * 根据菜单ID 获得当前用户Id集合
     * @param roleId
     * @return
     */
    List<String> getUserIdListByMenuId(String roleId);

    /**
     * 保存角色
     * @param userId
     * @param roleIds
     * @return
     */
    boolean setRoles(String userId,String[] roleIds);

}
