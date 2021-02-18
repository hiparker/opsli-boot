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

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validation.ValidationArgs;
import org.opsli.common.annotation.validation.ValidationArgsLenMax;
import org.opsli.common.enums.ValiArgsType;
import org.opsli.plugins.excel.annotation.ExcelInfo;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 数据字典
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DictModel extends ApiWrapper {



    /** 字典类型编号 */
    @ApiModelProperty(value = "字典类型编号")
    @ExcelProperty(value = "字典类型编号", order = 1)
    @ExcelInfo
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_GENERAL})
    @ValidationArgsLenMax(120)
    private String typeCode;

    /** 字典类型名称 */
    @ApiModelProperty(value = "字典类型名称")
    @ExcelProperty(value = "字典类型名称", order = 2)
    @ExcelInfo
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_GENERAL_WITH_CHINESE})
    @ValidationArgsLenMax(120)
    private String typeName;

    /** 是否内置数据 0是  1否*/
    @ApiModelProperty(value = "是否内置数据 0是  1否")
    @ExcelProperty(value = "是否内置数据", order = 3)
    @ExcelInfo(dictType = "no_yes")
    @ValidationArgs(ValiArgsType.IS_NOT_NULL)
    @ValidationArgsLenMax(1)
    private String izLock;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注", order = 4)
    @ExcelInfo
    @ValidationArgsLenMax(255)
    private String remark;


}
