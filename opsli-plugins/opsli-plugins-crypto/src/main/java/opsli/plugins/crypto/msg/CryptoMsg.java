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
package opsli.plugins.crypto.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * 加解密 - 消息
 *
 * @author Parker
 * @date 2021年5月17日16:46:42
 */
public enum CryptoMsg implements BaseMsg {

    /** 加解密 */
    CRYPTO_EXCEPTION_EN(10702,"加密失败"),
    CRYPTO_EXCEPTION_DE(10703,"解密失败"),
    CRYPTO_EXCEPTION_REFLEX(10704,"解密反射失败"),
    CRYPTO_EXCEPTION_MODEL_NULL(10705,"配置信息未初始化"),
    CRYPTO_EXCEPTION_TO_JSON(10706,"加密数据转换Json失败"),
    CRYPTO_EXCEPTION_HANDLER_NULL(10707,"无法获得加解密执行器"),

    ;

    private final int code;
    private final String message;

    CryptoMsg(int code, String message){
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
