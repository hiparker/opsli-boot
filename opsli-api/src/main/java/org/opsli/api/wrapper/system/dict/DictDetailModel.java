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
package org.opsli.api.wrapper.system.dict;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.enums.ValidatorType;
import org.opsli.plugins.excel.annotation.ExcelInfo;

/**
 * 数据字典 - 明细
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DictDetailModel extends ApiWrapper {


    /** 字典ID */
    @ApiModelProperty(value = "字典类型ID")
    @ExcelIgnore
    @Validator(ValidatorType.IS_NOT_NULL)
    private String typeId;

    /** 类型编号 - 冗余 */
    @ApiModelProperty(value = "字典类型Code")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_GENERAL})
    @ValidatorLenMax(120)
    private String typeCode;

    /** 字典名称 */
    @ApiModelProperty(value = "字典名称")
    @ExcelProperty(value = "字典名称", order = 1)
    @ExcelInfo
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_GENERAL_WITH_CHINESE})
    @ValidatorLenMax(120)
    private String dictName;

    /** 字典值 */
    @ApiModelProperty(value = "字典值")
    @ExcelProperty(value = "字典值", order = 2)
    @ExcelInfo
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(120)
    private String dictValue;

    /** 是否内置数据 0否  1是*/
    @ApiModelProperty(value = "是否内置数据 0否  1是")
    @ExcelProperty(value = "是否内置数据", order = 2)
    @ExcelInfo(dictType = "no_yes")
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(1)
    private String izLock;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    @ExcelProperty(value = "排序", order = 2)
    @ExcelInfo
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_INTEGER})
    @ValidatorLenMax(10)
    private Integer sortNo;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注", order = 2)
    @ExcelInfo
    @ValidatorLenMax(255)
    private String remark;


}
