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
package org.opsli.plugins.sms.msg;


import org.opsli.common.base.msg.BaseMsg;

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
 *
 * 10000 ~ 10099 短信服务
 * 10100 ~ 10199 微信服务
 *
 * @author Parker
 * @date 2020-09-13 19:36
 */
public enum SmsMsgCodeEnum implements BaseMsg {


    /** 短信服务 */
    CODE_ERROR_SMS_HANDLER(310000, "未找到短息执行策略"),
    CODE_ERROR_SMS_SIG_NAME_NULL(310001, "签名不可为空"),
    CODE_ERROR_SMS_TEMPLATE_CODE_NULL(310002, "模版编号不可为空"),
    CODE_ERROR_SMS_TEMPLATE_PARAM_NULL(310003, "模版参数不可为空"),
    CODE_ERROR_SMS_PHONE_NUMBERS_NULL(310004, "手机号不可为空"),
    CODE_ERROR_SMS_ACCESS_KEY_NULL(310001, "ACCESS_KEY 不可为空"),
    CODE_ERROR_SMS_ACCESS_KEY_SECRET_NULL(310001, "ACCESS_KEY_SECRET 不可为空"),

    CODE_ERROR_SMS_INIT(310010, "初始化参数异常"),
    EXCEPTION_IS_PHONE(98010,"不是座机号码+手机号码（CharUtil中国）+ 400 + 800电话 + 手机号号码（香港）! "),


    ;

    private final int code;
    private final String message;

    SmsMsgCodeEnum(int code, String message){
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

