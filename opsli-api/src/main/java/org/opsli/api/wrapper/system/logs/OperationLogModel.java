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
package org.opsli.api.wrapper.system.logs;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.annotation.validator.ValidatorLenMin;
import org.opsli.common.enums.ValidatorType;
import org.opsli.plugins.excel.annotation.ExcelInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
* 行为日志 Model
*
* @author Parker
* @date 2022-07-26 19:21:57
*/
@Data
@EqualsAndHashCode(callSuper = false)
public class OperationLogModel extends ApiWrapper {

    /** 多租户字段 */
    private String tenantId;

    /** 日志等级 */
    @ApiModelProperty(value = "日志等级")
    @ExcelProperty(value = "日志等级", order = 1)
    @ExcelInfo( dictType = "log_level" )
    @ValidatorLenMax(8)
    private String level;

    /** 被操作的系统模块 */
    @ApiModelProperty(value = "被操作的系统模块")
    @ExcelProperty(value = "被操作的系统模块", order = 2)
    @ExcelInfo( dictType = "log_model_type" )
    @ValidatorLenMax(20)
    private String moduleId;

    /** 方法名 */
    @ApiModelProperty(value = "方法名")
    @ExcelProperty(value = "方法名", order = 3)
    @ExcelInfo
    @ValidatorLenMax(100)
    private String method;

    /** 参数 */
    @ApiModelProperty(value = "参数")
    @ExcelProperty(value = "参数", order = 4)
    @ExcelInfo
    @ValidatorLenMax(20000)
    private String args;

    /** 操作人id */
    @ApiModelProperty(value = "操作人id")
    @ExcelProperty(value = "操作人id", order = 5)
    @ExcelInfo
    @ValidatorLenMax(19)
    private String userId;

    /** 操作账号 */
    @ApiModelProperty(value = "操作账号")
    @ExcelProperty(value = "操作账号", order = 6)
    @ExcelInfo
    @ValidatorLenMax(32)
    private String username;

    /** 操作人真实名称 */
    @ApiModelProperty(value = "操作人真实名称")
    @ExcelProperty(value = "操作人真实名称", order = 7)
    @ExcelInfo
    @ValidatorLenMax(50)
    private String realName;

    /** 日志描述 */
    @ApiModelProperty(value = "日志描述")
    @ExcelProperty(value = "日志描述", order = 8)
    @ExcelInfo
    @ValidatorLenMax(255)
    private String description;

    /** 操作类型 */
    @ApiModelProperty(value = "操作类型")
    @ExcelProperty(value = "操作类型", order = 9)
    @ExcelInfo( dictType = "log_operation_type" )
    @ValidatorLenMax(20)
    private String operationType;

    /** 方法运行时间 */
    @ApiModelProperty(value = "方法运行时间")
    @ExcelProperty(value = "方法运行时间", order = 10)
    @ExcelInfo
    @ValidatorLenMax(19)
    private String runTime;

    /** 方法返回值 */
    @ApiModelProperty(value = "方法返回值")
    @ExcelProperty(value = "方法返回值", order = 11)
    @ExcelInfo
    @ValidatorLenMax(20000)
    private String returnValue;

    /** 日志请求类型 */
    @ApiModelProperty(value = "日志请求类型")
    @ExcelProperty(value = "日志请求类型", order = 12)
    @ExcelInfo( dictType = "log_type" )
    @ValidatorLenMax(8)
    private String logType;



}
