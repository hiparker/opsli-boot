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
package org.opsli.common.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * 核心类 - 消息
 *
 * @author Parker
 * @date 2020-09-13 19:41
 */
public enum CommonMsg implements BaseMsg {


    /** Controller 参数默认序列化 */
    EXCEPTION_CONTROLLER_MODEL(10100,"序列化对象失败！"),

    /** 创建文件失败 */
    EXCEPTION_CREATE_FILE_ERROR(10101,"创建文件失败！"),


    ;

    private final int code;
    private final String message;

    CommonMsg(int code, String message){
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
