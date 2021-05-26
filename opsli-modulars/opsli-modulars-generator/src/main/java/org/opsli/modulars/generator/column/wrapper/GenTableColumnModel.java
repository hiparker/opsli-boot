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
package org.opsli.modulars.generator.column.wrapper;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.enums.ValidatorType;

import java.util.List;

/**
 * 代码生成器 - 表 模型
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GenTableColumnModel extends ApiWrapper {

    /** 归属表ID */
    @ApiModelProperty(value = "归属表ID")
    @ExcelIgnore
    private String tableId;

    /** 字段名称 */
    @ApiModelProperty(value = "字段名称")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_GENERAL})
    @ValidatorLenMax(100)
    private String fieldName;

    /** 字段类型 */
    @ApiModelProperty(value = "字段类型")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_GENERAL})
    @ValidatorLenMax(100)
    private String fieldType;

    /** 字段长度 */
    @ApiModelProperty(value = "字段长度")
    @ExcelIgnore
    private Integer fieldLength;

    /** 字段精度 */
    @ApiModelProperty(value = "字段精度")
    @ExcelIgnore
    private Integer fieldPrecision;

    /** 字段描述 */
    @ApiModelProperty(value = "字段描述")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(200)
    private String fieldComments;

    /** 是否主键 */
    @ApiModelProperty(value = "是否主键")
    @ExcelIgnore
    @ValidatorLenMax(1)
    private String izPk;

    /** 是否可为空 */
    @ApiModelProperty(value = "是否可为空")
    @ExcelIgnore
    @ValidatorLenMax(1)
    private String izNotNull;

    /** 是否列表显示 */
    @ApiModelProperty(value = "是否列表显示")
    @ExcelIgnore
    @ValidatorLenMax(1)
    private String izShowList;

    /** 是否表单显示 */
    @ApiModelProperty(value = "是否表单显示")
    @ExcelIgnore
    @ValidatorLenMax(1)
    private String izShowForm;

    /** Java字段类型 */
    @ApiModelProperty(value = "Java字段类型")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(50)
    private String javaType;

    /** 字段生成方案（文本框、文本域、字典选择） */
    @ApiModelProperty(value = "字段生成方案")
    @ExcelIgnore
    @ValidatorLenMax(1)
    private String showType;

    /** 字典类型编号 */
    @ApiModelProperty(value = "字典类型编号")
    @ExcelIgnore
    @Validator({ValidatorType.IS_GENERAL})
    @ValidatorLenMax(100)
    private String dictTypeCode;

    /** 排序（升序） */
    @ApiModelProperty(value = "排序")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(6)
    private Integer sort;

    /** 验证类别 */
    @ApiModelProperty(value = "验证类别")
    @ExcelIgnore
    @ValidatorLenMax(500)
    private String validateType;

    /** 检索类别 */
    @ApiModelProperty(value = "检索类别")
    @ExcelIgnore
    @Validator({ValidatorType.IS_GENERAL})
    @ValidatorLenMax(100)
    private String queryType;

    // =======================

    /** 字段名称 - 驼峰 */
    @JsonIgnore
    @ExcelIgnore
    private String fieldHumpName;

    /** 验证集合 */
    @JsonIgnore
    @ExcelIgnore
    private List<String> validateTypeList;

    /** 验证集合(含逗号) */
    @JsonIgnore
    @ExcelIgnore
    private List<String> validateTypeAndCommaList;

}
