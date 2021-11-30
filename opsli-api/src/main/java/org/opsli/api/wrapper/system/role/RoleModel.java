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
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.enums.ValidatorType;
import org.opsli.plugins.excel.annotation.ExcelInfo;

/**
 * 角色表
 *
 * @author 2020-09-16 17:33
 * @date Parker
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleModel extends ApiWrapper {

    /** 角色编码 */
    @ApiModelProperty(value = "角色编码")
    @ExcelProperty(value = "角色编码", order = 1)
    @ExcelInfo
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_GENERAL})
    @ValidatorLenMax(50)
    private String roleCode;

    /** 角色名称 */
    @ApiModelProperty(value = "角色编码")
    @ExcelProperty(value = "角色编码", order = 2)
    @ExcelInfo
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_GENERAL_WITH_CHINESE})
    @ValidatorLenMax(50)
    private String roleName;

    /** 标签 */
    @ApiModelProperty(value = "标签")
    @ExcelProperty(value = "标签", order = 4)
    @ExcelInfo(dictType = "menu_role_type")
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(1)
    private String label;


    /** 授权数据范围 */
    @ApiModelProperty(value = "授权数据范围")
    @ExcelProperty(value = "授权数据范围", order = 5)
    @ExcelInfo(dictType = "role_data_scope")
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(5)
    private String dataScope;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注", order = 6)
    @ExcelInfo
    @ValidatorLenMax(255)
    private String remark;


    /** 多租户字段 */
    @ApiModelProperty(value = "多租户ID")
    @ExcelIgnore
    @ValidatorLenMax(20)
    private String tenantId;

}
