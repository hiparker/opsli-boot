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
package org.opsli.modulars.generator.logs.wrapper;

import com.alibaba.excel.annotation.ExcelIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.enums.ValidatorType;

/**
 * 代码生成器 - 生成日志 模型
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GenLogsModel extends ApiWrapper {


    /** 归属表ID */
    @ApiModelProperty(value = "归属表ID")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(19)
    private String tableId;

    /** 表类型 */
    @ApiModelProperty(value = "表类型")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(1)
    private String tableType;


    /** 包名 */
    @ApiModelProperty(value = "包名")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(255)
    private String packageName;

    /** 模块名 */
    @ApiModelProperty(value = "模块名")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(40)
    private String moduleName;

    /** 子模块名 */
    @ApiModelProperty(value = "子模块名")
    @ExcelIgnore
    @ValidatorLenMax(40)
    private String subModuleName;


    /** 代码标题 */
    @ApiModelProperty(value = "代码标题")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(100)
    private String codeTitle;

    /** 代码标题简介 */
    @ApiModelProperty(value = "代码标题简介")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(100)
    private String codeTitleBrief;

    /** 作者名 */
    @ApiModelProperty(value = "作者名")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(64)
    private String authorName;

    /** 模板ID */
    @ApiModelProperty(value = "模板ID")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(19)
    private String templateId;

}
