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
package org.opsli.modulars.system.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.user.entity.SysUserRoleRef;

import java.util.List;

/**
 * 用户角色 Mapper
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Mapper
public interface UserRoleRefMapper extends BaseMapper<SysUserRoleRef> {

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
     * @param label 标签
     * @return List
     */
    List<String> queryAllPerms(@Param("userId") String userId, @Param("label") String label);

    /**
     * 根据用户ID 获得菜单集合
     * @param userId 用户ID
     * @param label 标签
     * @return List
     */
    List<SysMenu> findMenuListByUserId(@Param("userId") String userId, @Param("label") String label);

    /**
     * 根据用户ID 获得全部菜单集合
     * @param userId 用户ID
     * @param label 标签
     * @return List
     */
    List<SysMenu> findMenuAllListByUserId(@Param("userId") String userId, @Param("label") String label);

    /**
     * 根据条件 获得当前用户Id集合
     *
     * @param wrapper wrapper
     * @return List
     */
    List<String> getUserIdList(@Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * 根据条件 获得当前用户Id集合
     *
     * @param wrapper wrapper
     * @return List
     */
    List<String> getUserIdListByMenu(@Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * 根据菜单ID 获得当前用户Id集合
     * @param menuId 菜单ID
     * @return List
     */
    List<String> getUserIdListByMenuId(String menuId);
}
