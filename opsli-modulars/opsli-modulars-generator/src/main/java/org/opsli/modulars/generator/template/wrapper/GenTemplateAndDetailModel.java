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

package org.opsli.modulars.generator.template.wrapper;


import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.enums.ValidatorType;
import org.opsli.plugins.excel.annotation.ExcelInfo;

import java.util.List;

/**
* 代码模板 Model
*
* @author Parker
* @date 2021-05-27 14:33:49
*/
@Data
@EqualsAndHashCode(callSuper = false)
public class GenTemplateAndDetailModel extends ApiWrapper {

    
    /** 模板名称 */
    @ApiModelProperty(value = "模板名称")
    @ExcelProperty(value = "模板名称", order = 1)
    @ExcelInfo
    @Validator({
        ValidatorType.IS_GENERAL_WITH_CHINESE,
        ValidatorType.IS_NOT_NULL
    })
    @ValidatorLenMax(100)
    private String tempName;

    /** 表类型 */
    @ApiModelProperty(value = "表类型")
    @ExcelProperty(value = "表类型", order = 2)
    @ExcelInfo( dictType = "table_type" )
    @Validator({
        ValidatorType.IS_NOT_NULL
    })
    @ValidatorLenMax(30)
    private String tableType;

    
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息")
    @ExcelProperty(value = "备注信息", order = 3)
    @ExcelInfo
    @ValidatorLenMax(255)
    private String remark;

    /** 模板信息 */
    @ApiModelProperty(value = "模板信息")
    @ExcelProperty(value = "模板信息", order = 4)
    @ExcelInfo
    private List<GenTemplateDetailModel> detailList;

}
