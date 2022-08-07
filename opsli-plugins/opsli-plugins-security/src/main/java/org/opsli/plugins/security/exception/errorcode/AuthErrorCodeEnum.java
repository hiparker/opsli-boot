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
package org.opsli.plugins.security.exception.errorcode;

/**
 * 业务状态码
 * 组成描述: 错误级别(1位) + 模块标识(2位) + 具体错误码(3位)
 * 0011000
 *
 * 0 为通用正确 一切OK
 * -1 为通用错误
 *
 * 错误级别
 *
 * 1 错误来源于用户
 * 2 错误来源于当前系统
 * 3 示错误来源于第三方服务
 *
 *  模块标识
 * 00 - 公共模块
 *
 * @author Parker
 * @date 2020-09-13 19:36
 */
public enum AuthErrorCodeEnum implements BaseAuthMsg {

    /** 认证服务 */

    AUTH_NOT_FIND_USER_SERVICE(100201, "未加载到获取用户Service", "系统故障请稍微再试，故障码：10201"),
    AUTH_NOT_FIND_USER(100202, "未获取到用户", "认证失败"),
    AUTH_WRONG_PASSWORD(100203, "凭证错误", "密码不正确！"),
    AUTH_ACCOUNT_LOCKED(100204, "用户账号已锁定", "用户账号已锁定，请联系管理员"),
    AUTH_ACCOUNT_DISABLED(100205, "用户账号未启用", "用户账号未启用，请联系管理员"),
    AUTH_ACCOUNT_EXPIRED(100206, "用户账号已过期", "用户账号已过期，请联系管理员"),
    AUTH_ACCOUNT_CREDENTIALS_EXPIRED(100207, "用户密码凭证已过期", "密码凭证已过期，请修改密码"),

    AUTH_CREDENTIALS_INVALID(401, "凭证无效", "凭证已过期，请重新登陆"),
    AUTH_AUTH_INVALID(401, "认证无效", "认证失败，请重新登陆"),

    AUTH_NO_ACCESS(100210, "无权访问", "权限不足，请联系管理员"),

    AUTH_SERVICE_NOT_FIND_HANDLE(100211, "找不到执行器", "认证服务异常，请联系管理员"),

    ;

    private final int code;
    private final String description;
    private final String message;

    AuthErrorCodeEnum(int code, String description, String message){
        this.code = code;
        this.description = description;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

