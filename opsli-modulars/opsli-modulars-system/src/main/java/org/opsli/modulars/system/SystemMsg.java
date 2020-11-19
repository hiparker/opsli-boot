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
package org.opsli.modulars.system;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.msg
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:36
 * @Description: 核心类 - 消息
 */
public enum SystemMsg implements BaseMsg {

    /**
     * 系统
     */
    EXCEPTION_LOCK_DATA(20000,"内置数据只有超级管理员可以更改"),


    /**
     * 数据字典
     */
    EXCEPTION_DICT_UNIQUE(20100,"字典编号重复，该字典已存在"),
    EXCEPTION_DICT_DETAIL_UNIQUE(20101,"字典名称或值重复，该字典已存在"),

    /**
     * 角色
     */
    EXCEPTION_ROLE_UNIQUE(20200,"角色编号重复，该角色已存在！"),
    EXCEPTION_ROLE_ID_NOT_NULL(20201,"角色Id不可为空"),
    EXCEPTION_ROLE_PERMS_ERROR(20202,"角色权限设置失败"),


    /**
     * 用户
     */
    EXCEPTION_USER_UNIQUE(20300,"该用户已存在"),
    EXCEPTION_USER_PASSWORD_ERROR(20301,"旧密码不正确"),
    EXCEPTION_USER_ID_NOT_NULL(20302,"用户Id不可为空"),
    EXCEPTION_USER_ROLES_ERROR(20303,"用户角色设置失败"),
    EXCEPTION_USER_MENU_NOT_NULL(20304,"用户暂无角色菜单，请设置后登录"),
    EXCEPTION_USER_NO_UNIQUE(20305,"该工号已存在"),


    /**
     * 租户
     */
    EXCEPTION_TENANT_UNIQUE(20200,"租户名称重复，该租户已存在！"),

    ;

    private int code;
    private String message;

    SystemMsg(int code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
