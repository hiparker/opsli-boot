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
package org.opsli.core.options;

import lombok.Data;
import opsli.plugins.crypto.enums.CryptoAsymmetricType;
import opsli.plugins.crypto.enums.CryptoSymmetricType;
import opsli.plugins.crypto.model.CryptoAsymmetric;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.common.annotation.OptionDict;
import org.opsli.core.utils.OptionsUtil;
import org.opsli.core.utils.ValidatorUtil;

/**
 * 加密参数
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
public enum CryptoConfigFactory {

    /** 实例对象 */
    INSTANCE;

    /**
     * 获得配置信息
     * @return LocalConfig
     */
    public CryptoAsymmetric getCryptoSymmetric() {
        CryptoSymmetricOption option = new CryptoSymmetricOption();
        // 获得缓存参数配置
        OptionsUtil.getOptionByBean(option);
        // 验证配置
        ValidatorUtil.verify(option);

        // 转化对象
        return WrapperUtil.transformInstance(option, CryptoAsymmetric.class);
    }

    /**
     * 获得配置信息
     * @return LocalConfig
     */
    public CryptoAsymmetric getCryptoAsymmetric() {
        CryptoAsymmetricOption option = new CryptoAsymmetricOption();
        // 获得缓存参数配置
        OptionsUtil.getOptionByBean(option);
        // 验证配置
        ValidatorUtil.verify(option);

        // 转化对象
        return WrapperUtil.transformInstance(option, CryptoAsymmetric.class);
    }


    // =======================

    /**
     * 对称加密
     *
     * @author Parker
     * @date 2021年5月17日15:59:52
     */
    @Data
    public static class CryptoSymmetricOption {

        /** 加解密类别 */
        @OptionDict("crypto_symmetric")
        private CryptoSymmetricType cryptoType;

        /** 私钥 */
        @OptionDict("crypto_symmetric_private_key")
        private String privateKey;

    }

    /**
     * 非对称加密
     *
     * @author Parker
     * @date 2021年5月17日15:59:52
     */
    @Data
    public static class CryptoAsymmetricOption {

        /** 加解密类别 */
        @OptionDict("crypto_asymmetric")
        private CryptoAsymmetricType cryptoType;

        /** 公钥 */
        @OptionDict("crypto_asymmetric_public_key")
        private String publicKey;

        /** 私钥 */
        @OptionDict("crypto_asymmetric_private_key")
        private String privateKey;

    }

}
