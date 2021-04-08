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
package org.opsli.api.wrapper.system.org;


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
 * @BelongsPackage: org.opsli.api.wrapper.system.org
 * @Author: Parker
 * @CreateTime: 2020-11-28 18:59:59
 * @Description: 组织机构表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysOrgModel extends ApiWrapper {

    /** 父级主键 */
    @ApiModelProperty(value = "父级主键")
    @ExcelProperty(value = "父级主键", order = 1)
    @ExcelInfo
    @ValidationArgsLenMax(19)
    private String parentId;

    /** 组织机构编号 */
    @ApiModelProperty(value = "组织机构编号")
    @ExcelProperty(value = "组织机构编号", order = 2)
    @ExcelInfo
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_GENERAL})
    @ValidationArgsLenMax(120)
    private String orgCode;

    /** 组织机构名称 */
    @ApiModelProperty(value = "组织机构名称")
    @ExcelProperty(value = "组织机构名称", order = 3)
    @ExcelInfo
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_GENERAL_WITH_CHINESE})
    @ValidationArgsLenMax(120)
    private String orgName;

    /** 组织机构类型 1-公司 2-部门 3-岗位*/
    @ApiModelProperty(value = "组织机构类型")
    @ExcelProperty(value = "组织机构类型", order = 4)
    @ExcelInfo( dictType = "org_type")
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsLenMax(3)
    private String orgType;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    @ExcelProperty(value = "排序", order = 5)
    @ExcelInfo
    @ValidationArgsLenMax(10)
    private Integer sortNo;

    /** 多租户字段 */
    @ApiModelProperty(value = "多租户ID")
    @ExcelIgnore
    @ValidationArgsLenMax(20)
    private String tenantId;

}
