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
import org.opsli.api.wrapper.system.user.UserWebModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserPassword;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.core.persistence.Page;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.entity.SysUserWeb;

import java.util.List;


/**
 * 用户信息 Service
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
public interface IUserService extends CrudServiceInterface<SysUser, UserModel> {

    /**
     * 根据 用户名 获得当前用户
     * @param username 用户名
     * @return UserModel
     */
    UserModel queryByUserName(String username);

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
     * @return List
     */
    List<MenuModel> getMenuAllListByUserId(String userId);


    /**
     * 修改密码
     * @param userPassword 账户密码
     * @return boolean
     */
    boolean updatePassword(UserPassword userPassword);

    /**
     * 重置密码
     * @param userPassword 账户密码
     * @return boolean
     */
    boolean resetPassword(UserPassword userPassword);


    /**
     * 更新用户最后登录IP
     * @param model 模型
     * @return boolean
     */
    boolean updateLoginIp(UserModel model);


    /**
     * 更新用户头像
     * @param model 模型
     * @return boolean
     */
    boolean updateAvatar(UserModel model);


    /**
     * 变更账户状态
     * @param userId 用户ID
     * @param enable 状态
     * @return boolean
     */
    boolean enableAccount(String userId, String enable);

    /**
     * 查询分页数据 自定义
     * @param page 分页
     * @return  Page<T>
     */
    Page<SysUserWeb, UserWebModel> findPageByCus(Page<SysUserWeb, UserWebModel> page);
}
