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
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.enums.ValidatorType;
import org.opsli.plugins.excel.annotation.ExcelInfo;

/**
 * 组织机构表
 *
 * @author Parker
 * @date 2020-11-28 18:59:59
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysOrgModel extends ApiWrapper {


    /** 父级主键 */
    @ApiModelProperty(value = "父级主键")
    @ExcelProperty(value = "父级主键", order = 1)
    @ExcelInfo
    @ValidatorLenMax(19)
    private String parentId;

    /** 父级主键集合 xxx,xxx */
    @ApiModelProperty(value = "父级主键集合")
    @ExcelProperty(value = "父级主键集合", order = 2)
    @ExcelInfo
    private String parentIds;

    /** 组织机构ID组 xxx,xxx */
    @ApiModelProperty(value = "组织机构ID组")
    @JsonIgnore
    private String orgIds;

    /** 组织机构编号 */
    @ApiModelProperty(value = "组织机构编号")
    @ExcelProperty(value = "组织机构编号", order = 4)
    @ExcelInfo
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_GENERAL})
    @ValidatorLenMax(120)
    private String orgCode;

    /** 组织机构名称 */
    @ApiModelProperty(value = "组织机构名称")
    @ExcelProperty(value = "组织机构名称", order = 5)
    @ExcelInfo
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_GENERAL_WITH_CHINESE})
    @ValidatorLenMax(120)
    private String orgName;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    @ExcelProperty(value = "排序", order = 6)
    @ExcelInfo
    @ValidatorLenMax(10)
    private Integer sortNo;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注", order = 7)
    @ExcelInfo
    @ValidatorLenMax(255)
    private String remark;

    /** 多租户字段 */
    @ApiModelProperty(value = "多租户ID")
    @ExcelIgnore
    @ValidatorLenMax(20)
    private String tenantId;

}
