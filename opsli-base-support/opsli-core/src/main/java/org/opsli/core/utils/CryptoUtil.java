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
package org.opsli.core.utils;

import opsli.plugins.crypto.CryptoPlugin;
import opsli.plugins.crypto.enums.CryptoSymmetricType;
import opsli.plugins.crypto.model.CryptoAsymmetric;
import opsli.plugins.crypto.model.CryptoSymmetric;
import opsli.plugins.crypto.strategy.CryptoAsymmetricService;
import opsli.plugins.crypto.strategy.CryptoSymmetricService;
import org.opsli.core.options.CryptoConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * 登陆加解密 工具类
 *
 * @author Parker
 * @date 2022-07-25 6:06 PM
 **/
@Component
@Order(UTIL_ORDER)
@Lazy(false)
public class CryptoUtil {

    /** 获得非对称加解密 执行器 */
    private static CryptoAsymmetricService ASYMMETRIC;
    /** 非对称加解密模型 */
    private static CryptoAsymmetric ASYMMETRIC_CRYPTO_MODEL;
    /** 获得对称加解密 执行器 */
    private static CryptoSymmetricService SYMMETRIC;
    /** 对称加解密模型 */
    private static CryptoSymmetric SYMMETRIC_CRYPTO_MODEL;


    /**
     * 非对称 解密数据
     * @param encryptData 秘文
     * @return 解密厚后Obj
     */
    public static Object asymmetricDecryptToObj(String encryptData){
        return ASYMMETRIC.decryptToObj(
                ASYMMETRIC_CRYPTO_MODEL, encryptData);
    }

    /**
     * 对称 加密数据
     * @param data 数据
     * @return 秘文
     */
    public static String symmetricEncryptToStr(Object data){
        return SYMMETRIC.encrypt(
                SYMMETRIC_CRYPTO_MODEL, data);
    }


    @Autowired
    public void init(OptionsUtil optionsUtil){
        // 非对称
        ASYMMETRIC = CryptoPlugin.getAsymmetric();
        ASYMMETRIC_CRYPTO_MODEL = CryptoConfigFactory.INSTANCE.getCryptoAsymmetric();
        // 对称
        SYMMETRIC = CryptoPlugin.getSymmetric();
        SYMMETRIC_CRYPTO_MODEL = SYMMETRIC.createNilModel();
        SYMMETRIC_CRYPTO_MODEL.setCryptoType(CryptoSymmetricType.DES);
        SYMMETRIC_CRYPTO_MODEL.setPrivateKey(ASYMMETRIC_CRYPTO_MODEL.getPublicKey());
    }
}
