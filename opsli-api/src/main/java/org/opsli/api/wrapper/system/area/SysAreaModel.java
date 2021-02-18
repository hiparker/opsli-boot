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
package org.opsli.api.wrapper.system.area;

import com.alibaba.excel.annotation.ExcelIgnore;
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
 * @BelongsPackage: org.opsli.api.wrapper.system.area
 * @Author: Parker
 * @CreateTime: 2020-11-28 18:59:59
 * @Description: 地域表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysAreaModel extends ApiWrapper {

    /** 父级主键 */
    @ApiModelProperty(value = "父级主键")
    @ExcelProperty(value = "父级主键", order = 1)
    @ExcelInfo
    @ValidationArgsLenMax(19)
    private String parentId;

    /** 地域编号 */
    @ApiModelProperty(value = "地域编号")
    @ExcelProperty(value = "地域编号", order = 2)
    @ExcelInfo
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_NUMBER})
    @ValidationArgsLenMax(40)
    private String areaCode;

    /** 地域名称 */
    @ApiModelProperty(value = "地域名称")
    @ExcelProperty(value = "地域名称", order = 3)
    @ExcelInfo
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_GENERAL_WITH_CHINESE})
    @ValidationArgsLenMax(40)
    private String areaName;

}
