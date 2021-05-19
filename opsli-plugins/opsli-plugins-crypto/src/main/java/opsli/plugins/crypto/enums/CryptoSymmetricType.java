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
package opsli.plugins.crypto.enums;

/**
 * 对称算法类型
 *
 * @author Parker
 * @date 2021年5月17日16:48:14
 */
public enum CryptoSymmetricType {

    /** 对称算法类型 */
    AES("AES", "AES 算法"),
    DES("DES", "DES 算法"),
    DE_SEDE("DESede", "DESede 算法"),
    SM4("SM4", "SM4 算法"),
    ;

    private final String code;
    private final String desc;

    public static CryptoSymmetricType getCryptoType(String code) {
        CryptoSymmetricType[] types = values();
        for (CryptoSymmetricType type : types) {
            if (type.code.equalsIgnoreCase(code)) {
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

    CryptoSymmetricType(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }
}