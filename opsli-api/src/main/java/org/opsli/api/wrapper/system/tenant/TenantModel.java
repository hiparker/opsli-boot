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
package org.opsli.api.wrapper.system.tenant;

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
 * @Description: 租户表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TenantModel extends ApiWrapper {


    /** 租户名称 */
    @ApiModelProperty(value = "租户名称")
    @ExcelProperty(value = "租户名称", order = 1)
    @ExcelInfo
    @ValidationArgs({ValiArgsType.IS_NOT_NULL,ValiArgsType.IS_GENERAL_WITH_CHINESE})
    @ValidationArgsLenMax(50)
    private String tenantName;

    /** 是否启用 0是  1否*/
    @ApiModelProperty(value = "是否启用")
    @ExcelProperty(value = "是否启用", order = 2)
    @ExcelInfo(dictType = "no_yes")
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsLenMax(1)
    private String enable;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注", order = 3)
    @ExcelInfo
    @ValidationArgsLenMax(255)
    private String remark;


}
