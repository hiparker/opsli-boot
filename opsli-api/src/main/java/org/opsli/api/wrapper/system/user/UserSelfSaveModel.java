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
package org.opsli.api.wrapper.system.user;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.enums.ValidatorType;
import org.opsli.plugins.excel.annotation.ExcelInfo;

/**
 * 用户信息表 - 用于修改自身信息
 *
 * @author Pace
 * @date 2020-09-16 17:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserSelfSaveModel extends ApiWrapper {


    /** 真实姓名 */
    @Schema(description = "真实姓名")
    @ExcelProperty(value = "真实姓名", order = 1)
    @ExcelInfo
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_GENERAL_WITH_CHINESE})
    @ValidatorLenMax(50)
    private String realName;

    /** 签名 */
    @Schema(description = "签名")
    @ExcelProperty(value = "签名", order = 5)
    @ExcelInfo
    @ValidatorLenMax(255)
    private String sign;

}
