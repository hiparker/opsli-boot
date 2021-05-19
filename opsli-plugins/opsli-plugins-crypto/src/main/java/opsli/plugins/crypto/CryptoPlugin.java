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
package opsli.plugins.crypto;

import lombok.extern.slf4j.Slf4j;
import opsli.plugins.crypto.strategy.CryptoAsymmetricService;
import opsli.plugins.crypto.strategy.CryptoSymmetricService;
import opsli.plugins.crypto.strategy.impl.CryptoAsymmetricServiceImpl;
import opsli.plugins.crypto.strategy.impl.CryptoSymmetricServiceImpl;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.utils
 * @Author: Parker
 * @CreateTime: 2020-09-22 17:29
 * @Description: 验证器工具类
 */
@Slf4j
public class CryptoPlugin {

    /** 对称加密 */
    private static final CryptoSymmetricService CRYPTO_SYMMETRIC = new CryptoSymmetricServiceImpl();
    /** 非对称加密 */
    private static final CryptoAsymmetricService CRYPTO_ASYMMETRIC = new CryptoAsymmetricServiceImpl();

    /**
     * 获得对称加密
     * @return CryptoSymmetricService
     */
    public static CryptoSymmetricService getSymmetric(){
        return CRYPTO_SYMMETRIC;
    }

    /**
     * 获得非对称加密
     * @return CryptoAsymmetricService
     */
    public static CryptoAsymmetricService getAsymmetric(){
        return CRYPTO_ASYMMETRIC;
    }

    // ================

    private CryptoPlugin(){}

}
