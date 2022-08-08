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

import org.opsli.api.wrapper.system.user.*;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.core.persistence.Page;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.entity.SysUserWeb;


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
     * 根据 手机号 获得当前用户
     * @param mobile 手机
     * @return UserModel
     */
    UserModel queryByMobile(String mobile);

    /**
     * 根据 邮箱 获得当前用户
     * @param email 邮箱
     * @return UserModel
     */
    UserModel queryByEmail(String email);


    /**
     * 修改密码 验证旧密码
     * @param userPassword 账户密码
     * @return boolean
     */
    boolean updatePasswordByCheckOld(UserPassword userPassword);

    /**
     * 修改密码 不验证旧密码
     * @param userPassword 账户密码
     * @return boolean
     */
    boolean updatePasswordByNotCheckOld(ToUserPassword userPassword);

    /**
     * 修改邮箱
     * @param updateUserEmailModel model
     * @return boolean
     */
    boolean updateUserEmail(UpdateUserEmailModel updateUserEmailModel);

    /**
     * 修改手机
     * @param updateUserMobileModel model
     * @return boolean
     */
    boolean updateUserMobile(UpdateUserMobileModel updateUserMobileModel);

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

    /**
     * 查询分页数据 租户
     * @param page 分页
     * @return  Page<T>
     */
    Page<SysUserWeb, UserWebModel> findPageByTenant(Page<SysUserWeb, UserWebModel> page);
}
