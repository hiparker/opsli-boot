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
package org.opsli.modulars.system.login.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.annotation.validator.ValidatorLenMin;
import org.opsli.common.enums.ValidatorType;

/**
 * @author Parker
 * @date 2022-07-16 8:14 PM
 **/
@Data
public class LoginModel {

    /** 主键 */
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(50)
    private String principal;

    /** 密码 */
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMin(6)
    private String password;

    /** 验证码 */
    @ValidatorLenMax(20)
    private String verificationCode;

    /** UUID */
    @ApiModelProperty(value = "UUID")
    private String uuid;

    /** 登录来源: 0:PC端；1:APP-安卓 2:APP-IOS 3:小程序 */
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(5)
    private String loginFrom;

}
