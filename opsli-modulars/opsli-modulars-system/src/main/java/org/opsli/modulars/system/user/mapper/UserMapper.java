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
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.opsli.api.wrapper.system.user.UserPassword;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.org.entity.SysOrg;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.entity.SysUserAndOrg;

import java.util.List;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.mapper
 * @Author: Parker
 * @CreateTime: 2020-09-17 13:01
 * @Description: 用户 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {

    /**
     * 唯一验证 - 用户名
     * @param entity
     * @return
     */
    Integer uniqueVerificationByUsername(SysUser entity);

    /**
     * 唯一验证 - 工号
     * @param entity
     * @return
     */
    Integer uniqueVerificationByNo(SysUser entity);


    /**
     * 根据用户ID 获得当前角色编码集合
     * @param userId
     * @return
     */
    List<String> getRoleCodeList(String userId);

    /**
     * 根据用户ID 获得当前角色Id集合
     * @param userId
     * @return
     */
    List<String> getRoleIdList(String userId);

    /**
     * 根据用户ID 获得权限
     * @param userId
     * @return
     */
    List<String> queryAllPerms(String userId);

    /**
     * 根据用户ID 获得菜单集合
     * @param userId
     * @return
     */
    List<SysMenu> findMenuListByUserId(String userId);

    /**
     * 修改密码
     * @param userPassword
     * @return
     */
    boolean updatePassword(UserPassword userPassword);

    /**
     * 更新用户最后登录IP
     * @param entity
     * @return
     */
    boolean updateLoginIp(SysUser entity);

    /**
     * 更新用户头像
     * @param entity
     * @return
     */
    boolean updateAvatar(SysUser entity);


    /**
     * 查询集合
     * @return
     */
    List<SysUserAndOrg> findList(@Param("ew") Wrapper<SysUserAndOrg> wrapper);
}
