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

package org.opsli.api.wrapper.system.other.crypto;


import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validation.ValidationArgs;
import org.opsli.common.annotation.validation.ValidationArgsLenMax;
import org.opsli.common.enums.ValiArgsType;
import org.opsli.plugins.excel.annotation.ExcelInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @BelongsProject: opsli-boot

 * @BelongsPackage: org.opsli.api.wrapper.other.crypto

 * @Author: Parker
 * @CreateTime: 2021-02-10 17:09:34
 * @Description: 非对称加密
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OtherCryptoAsymmetricModel extends ApiWrapper {


    /** 加解密类别 */
    @ApiModelProperty(value = "加解密类别")
    @ExcelProperty(value = "加解密类别", order = 1)
    @ExcelInfo
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_GENERAL})
    @ValidationArgsLenMax(100)
    private String cryptoType;

    /** 公钥 */
    @ApiModelProperty(value = "公钥")
    @ExcelProperty(value = "公钥", order = 2)
    @ExcelInfo
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsLenMax(2000)
    private String publicKey;

    /** 私钥 */
    @ApiModelProperty(value = "私钥")
    @ExcelProperty(value = "私钥", order = 3)
    @ExcelInfo
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsLenMax(2000)
    private String privateKey;


}
