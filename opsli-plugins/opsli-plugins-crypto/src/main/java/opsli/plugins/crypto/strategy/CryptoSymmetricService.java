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
package opsli.plugins.crypto.strategy;

import opsli.plugins.crypto.enums.CryptoAsymmetricType;
import opsli.plugins.crypto.enums.CryptoSymmetricType;
import opsli.plugins.crypto.model.CryptoAsymmetric;
import opsli.plugins.crypto.model.CryptoSymmetric;

/**
 * 对称 加解密策略接口
 *
 * @author Parker
 * @date 2021年5月17日15:49:15
 */
public interface CryptoSymmetricService {

    /**
     * 创建空模型
     * @return Model
     */
    CryptoSymmetric createNilModel();

    /**
     * 创建公私钥
     * @param cryptoSymmetricType 枚举
     * @return Model
     */
    CryptoSymmetric createKeyModel(final CryptoSymmetricType cryptoSymmetricType);

    /**
     * 加密数据
     * @param model 加解密模型
     * @param data 数据
     * @return String
     */
    String encrypt(final CryptoSymmetric model, final Object data);

    /**
     * 解密数据
     * @param model 加解密模型
     * @param data 数据
     * @return Object
     */
    Object decryptToObj(final CryptoSymmetric model, final String data);

    /**
     * 解密数据
     * @param model 加解密模型
     * @param data 数据
     * @return String
     */
    String decrypt(final CryptoSymmetric model, final String data);


}
