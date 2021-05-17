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
 * 非对称算法类型
 *
 * @author Parker
 * @date 2021年5月17日16:48:14
 */
public enum CryptoAsymmetricType {

    /** 非对称算法类型 */

    RSA("RSA", "RSA 算法"),
    SM2("SM2", "SM2 算法"),
    ECIES("ECIES", "ECIES 算法"),

    ;

    private final String code;
    private final String desc;

    public static CryptoAsymmetricType getCryptoType(String code) {
        CryptoAsymmetricType[] types = values();
        for (CryptoAsymmetricType type : types) {
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

    CryptoAsymmetricType(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }
}