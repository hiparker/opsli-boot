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
package org.opsli.plugins.redis.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * Redis消息
 *
 * @author Parker
 * @date 2020-09-16 11:47
 */
public enum RedisMsg implements BaseMsg {

    /** Redis异常 */
    EXCEPTION_KEY_NULL(90300,"Key不可为空！"),
    EXCEPTION_INCREMENT(90301,"递增值必须大于0！"),
    EXCEPTION_DECREMENT(90302,"递减值必须大于0！"),
    EXCEPTION_REFLEX(90303,"反射Redis脚本失败"),
    EXCEPTION_PUSH_SUB_NULL(90304,"发布消息体不可为空！"),
    ;


    private final int code;
    private final String message;

    RedisMsg(int code, String message){
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
