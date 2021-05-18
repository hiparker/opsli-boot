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
package org.opsli.common.enums;

/**
 * 参数类型
 *
 * @author Parker
 * @date 2020-09-17 23:40
 */
public enum OptionsType {

    /** 参数类型 */

    /** 非对称加密 目前支持 RSA SM2 ECIES 3种模式 */
    CRYPTO_ASYMMETRIC("crypto_asymmetric", "加解密-非对称"),
    /** 非对称加密 公钥 */
    CRYPTO_ASYMMETRIC_PUBLIC_KEY("crypto_asymmetric_public_key", "加解密-非对称-公钥"),
    /** 非对称加密 私钥 */
    CRYPTO_ASYMMETRIC_PRIVATE_KEY("crypto_asymmetric_private_key", "加解密-非对称-私钥"),

    /** 存储服务类型 */
    STORAGE_TYPE("storage_type", "存储服务类型"),

    ;

    private final String code;
    private final String desc;

    public static OptionsType getType(String cacheType) {
        OptionsType[] types = values();
        for (OptionsType type : types) {
            if (type.code.equalsIgnoreCase(cacheType)) {
                return type;
            }
        }
        return null;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    // =================

    OptionsType(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }
}
