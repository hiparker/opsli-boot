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

import org.apache.poi.ss.formula.functions.T;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserAndOrgModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserPassword;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.core.persistence.Page;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.entity.SysUserAndOrg;

import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-17 13:07
 * @Description: 用户 接口
 */
public interface IUserService extends CrudServiceInterface<SysUser, UserModel> {

    /**
     * 根据 用户名 获得当前用户
     * @param username
     * @return
     */
    UserModel queryByUserName(String username);

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
    List<String> getAllPerms(String userId);

    /**
     * 根据用户ID 获得菜单集合
     * @param userId
     * @return
     */
    List<MenuModel> getMenuListByUserId(String userId);


    /**
     * 修改密码
     * @param userPassword
     * @return
     */
    boolean updatePassword(UserPassword userPassword);


    /**
     * 更新用户最后登录IP
     * @param model
     * @return
     */
    boolean updateLoginIp(UserModel model);


    /**
     * 更新用户头像
     * @param model
     * @return
     */
    boolean updateAvatar(UserModel model);


    /**
     * 查询分页数据 自定义
     *
     * @return  Page<T>
     */
    Page<SysUserAndOrg, UserAndOrgModel> findPageByCus(Page<SysUserAndOrg,UserAndOrgModel> page);
}
