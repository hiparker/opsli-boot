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
package opsli.plugins.crypto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import opsli.plugins.crypto.enums.CryptoSymmetricType;

/**
 * 对称加密
 *
 * @author Parker
 * @date 2021年5月17日15:59:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CryptoSymmetric {

    /** 加解密类别 */
    private CryptoSymmetricType cryptoType;

    /** 私钥 */
    private String privateKey;

}
