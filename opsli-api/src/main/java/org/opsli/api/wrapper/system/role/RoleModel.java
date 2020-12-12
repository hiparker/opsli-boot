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
package org.opsli.api.wrapper.system.role;

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
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 角色表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleModel extends ApiWrapper {



    /** 角色编码 */
    @ApiModelProperty(value = "角色编码")
    @ExcelProperty(value = "角色编码", order = 1)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL,ValiArgsType.IS_GENERAL})
    @ValidationArgsLenMax(50)
    private String roleCode;

    /** 角色名称 */
    @ApiModelProperty(value = "角色编码")
    @ExcelProperty(value = "角色编码", order = 2)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL,ValiArgsType.IS_GENERAL_WITH_CHINESE})
    @ValidationArgsLenMax(50)
    private String roleName;

    /** 是否内置数据 0是  1否*/
    @ApiModelProperty(value = "是否内置数据 0是  1否")
    @ExcelProperty(value = "是否内置数据", order = 3)
    @ExcelInfo(dictType = "yes_no")
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsLenMax(1)
    private String izLock;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注", order = 4)
    @ExcelInfo
    // 验证器
    @ValidationArgsLenMax(255)
    private String remark;


    /** 多租户字段 */
    @ApiModelProperty(value = "多租户ID")
    @ExcelIgnore
    // 验证器
    @ValidationArgsLenMax(20)
    private String tenantId;

}
