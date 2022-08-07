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

/**
* 代码模板详情 Model
*
* @author Parker
* @date 2021-05-28 17:12:38
*/
@Data
@EqualsAndHashCode(callSuper = false)
public class GenTemplateDetailModel extends ApiWrapper {

    
    /** 父级ID */
    @ApiModelProperty(value = "父级ID")
    @ExcelProperty(value = "父级ID", order = 1)
    @ExcelInfo
    @Validator({
        ValidatorType.IS_NOT_NULL
    })
    @ValidatorLenMax(19)
    private String parentId;

    /** 类型 0 后端 1 前端 */
    @ApiModelProperty(value = "类型")
    @ExcelProperty(value = "类型", order = 2)
    @ExcelInfo
    @Validator({
            ValidatorType.IS_NOT_NULL
    })
    @ValidatorLenMax(1)
    private String type;

    /** 路径 */
    @ApiModelProperty(value = "路径")
    @ExcelProperty(value = "路径", order = 3)
    @ExcelInfo
    @Validator({
        ValidatorType.IS_NOT_NULL
    })
    @ValidatorLenMax(255)
    private String path;

    /** 文件名 */
    @ApiModelProperty(value = "文件名")
    @ExcelProperty(value = "文件名", order = 4)
    @ExcelInfo
    @Validator({
        ValidatorType.IS_NOT_NULL
    })
    @ValidatorLenMax(100)
    private String fileName;

    /** 文件内容 */
    @ApiModelProperty(value = "文件内容")
    @ExcelProperty(value = "文件内容", order = 5)
    @ExcelInfo
    @Validator({
        ValidatorType.IS_NOT_NULL
    })
    @ValidatorLenMax(20000)
    private String fileContent;

    /** 是否忽略文件名 */
    @ApiModelProperty(value = "文件名")
    @ExcelProperty(value = "文件名", order = 6)
    @ExcelInfo
    @Validator({
            ValidatorType.IS_NOT_NULL
    })
    @ValidatorLenMax(1)
    private String ignoreFileName;

}
