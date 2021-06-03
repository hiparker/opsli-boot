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
package org.opsli.plugins.email.msg;


import org.opsli.common.base.msg.BaseMsg;

/**
 * 邮件服务异常 - 消息
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
public enum EmailMsg implements BaseMsg {

    /**
     * 邮件服务异常
     */
    EXCEPTION_CONFIG_INIT_NULL(90400, "邮件服务初始化异常"),

    EXCEPTION_MODEL_TO_NULL(90401, "收件人不可为空"),
    EXCEPTION_MODEL_SUBJECT_NULL(90402, "主题不可为空"),
    EXCEPTION_MODEL_CONTENT_NULL(90403, "内容不可为空"),


    ;

    private final int code;
    private final String message;

    EmailMsg(int code, String message){
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
