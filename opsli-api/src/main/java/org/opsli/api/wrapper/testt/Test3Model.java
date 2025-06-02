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
package org.opsli.api.wrapper.testt;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.enums.ValidatorType;
import org.opsli.plugins.excel.annotation.ExcelInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 测试3
 *
 * @author Pace
 * @date 2020-12-20 18:27:04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Test3Model extends ApiWrapper {

    /** 金钱 */
    @Schema(description = "金钱")
    @ExcelProperty(value = "金钱", order = 1)
    @ExcelInfo
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_MONEY})
    @ValidatorLenMax(10)
    private Double money;

    /** 年龄 */
    @Schema(description = "年龄")
    @ExcelProperty(value = "年龄", order = 2)
    @ExcelInfo
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_INTEGER})
    @ValidatorLenMax(3)
    private Integer age;

    /** 名称 */
    @Schema(description = "名称")
    @ExcelProperty(value = "名称", order = 3)
    @ExcelInfo
    @Validator({ValidatorType.IS_CHINESE})
    @ValidatorLenMax(50)
    private String name;

    /** 生日 */
    @Schema(description = "生日")
    @ExcelProperty(value = "生日", order = 4)
    @ExcelInfo
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birth;

    /** 是否启用 */
    @Schema(description = "是否启用")
    @ExcelProperty(value = "是否启用", order = 5)
    @ExcelInfo( dictType = "no_yes" )
    @ValidatorLenMax(1)
    private String izUsable;



}
