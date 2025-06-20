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

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.annotation.validator.ValidatorLenMin;
import org.opsli.common.enums.ValidatorType;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户 修改密码
 *
 * @author Pace
 * @date 2020-09-16 17:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ExcelIgnoreUnannotated
public class ToUserPassword implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** User Id */
    @Schema(description = "用户Id")
    private String userId;

    /** 新密码 */
    @Schema(description = "新密码")
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_SECURITY_PASSWORD})
    @ValidatorLenMin(6)
    @ValidatorLenMax(50)
    private String newPassword;

    /** 登录密码强度 前端不可改 */
    @Schema(description = "登录密码强度 前端不可改")
    @ExcelIgnore
    @ValidatorLenMin(1)
    @ValidatorLenMax(1)
    private String passwordLevel;

}
