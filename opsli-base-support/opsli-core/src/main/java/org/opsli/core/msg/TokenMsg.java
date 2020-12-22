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
package org.opsli.core.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.msg
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:36
 * @Description: Token - 消息
 */
public enum TokenMsg implements BaseMsg {

    /**
     * Token
     */
    EXCEPTION_TOKEN_CREATE_ERROR(12000,"生成Token失败"),
    EXCEPTION_TOKEN_LOSE_EFFICACY(401,"Token失效，请重新登录"),


    /**
     * 登陆
     */
    EXCEPTION_CAPTCHA_ERROR(12100,"验证码不正确！"),
    EXCEPTION_CAPTCHA_NULL(12201, "验证码已失效"),
    EXCEPTION_CAPTCHA_UUID_NULL(12202, "验证码UUID为空"),
    EXCEPTION_CAPTCHA_CODE_NULL(12203, "验证码为空"),
    EXCEPTION_LOGIN_ACCOUNT_NO(12101,"账号或密码不正确！"),
    EXCEPTION_LOGIN_ACCOUNT_LOCKED(12102,"账号已被锁定,请联系管理员！"),
    EXCEPTION_LOGOUT_ERROR(12103,"登出失败，没有授权Token！"),
    EXCEPTION_LOGOUT_SUCCESS(12104,"登出成功！"),
    EXCEPTION_LOGIN_ACCOUNT_LOCK(12104,"账号已锁定，请{}后，再次尝试"),
    EXCEPTION_LOGIN_TENANT_NOT_USABLE(12105,"租户未启用，请联系管理员"),
    EXCEPTION_LOGIN_NULL(12106,"请输入账号密码"),

    /**
     * 其他
     */
    EXCEPTION_USER_NULL(12200, "用户为空"),
    EXCEPTION_NOT_AUTH(12204, "无权访问该方法"),
    ;

    private final int code;
    private final String message;

    TokenMsg(int code, String message){
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
