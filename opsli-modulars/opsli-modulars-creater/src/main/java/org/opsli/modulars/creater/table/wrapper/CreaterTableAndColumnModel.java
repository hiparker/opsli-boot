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
package org.opsli.modulars.creater.table.wrapper;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validation.ValidationArgs;
import org.opsli.common.annotation.validation.ValidationArgsLenMax;
import org.opsli.common.enums.ValiArgsType;
import org.opsli.modulars.creater.column.wrapper.CreaterTableColumnModel;
import org.opsli.plugins.excel.annotation.ExcelInfo;

import java.util.List;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 代码生成器 - 表和表结构
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CreaterTableAndColumnModel extends ApiWrapper {


    /** 表名称 */
    @ApiModelProperty(value = "表名称")
    @ExcelProperty(value = "表名称", order = 1)
    @ExcelInfo
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_GENERAL})
    @ValidationArgsLenMax(100)
    private String tableName;

    /** 旧表名称 */
    @ApiModelProperty(value = "旧表名称")
    @ExcelIgnore
    private String oldTableName;

    /** 表类型 */
    @ApiModelProperty(value = "表类型")
    @ExcelProperty(value = "表类型", order = 2)
    @ExcelInfo(dictType = "table_type")
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsLenMax(1)
    private String tableType;

    /** 数据库类型 */
    @ApiModelProperty(value = "数据库类型")
    @ExcelProperty(value = "数据库类型", order = 3)
    @ExcelInfo(dictType = "jdbc_type")
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsLenMax(30)
    private String jdbcType;

    /** 描述 */
    @ApiModelProperty(value = "描述")
    @ExcelProperty(value = "描述", order = 4)
    @ExcelInfo
    @ValidationArgsLenMax(200)
    private String comments;

    /** 同步 */
    @ApiModelProperty(value = "同步")
    @ExcelProperty(value = "是否同步", order = 5)
    @ExcelInfo(dictType = "no_yes")
    @ValidationArgsLenMax(1)
    private String izSync;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注", order = 6)
    @ExcelInfo
    @ValidationArgsLenMax(255)
    private String remark;


    /** 表结构 */
    @ApiModelProperty(value = "表结构")
    @ExcelProperty(value = "表结构", order = 7)
    @ExcelInfo
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    private List<CreaterTableColumnModel> columnList;

}
