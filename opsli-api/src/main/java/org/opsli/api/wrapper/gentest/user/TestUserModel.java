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
package org.opsli.api.wrapper.gentest.user;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validation.ValidationArgs;
import org.opsli.common.annotation.validation.ValidationArgsLenMax;
import org.opsli.common.enums.ValiArgsType;
import org.opsli.plugins.excel.annotation.ExcelInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.api.wrapper.gentest.user
 * @Author: 周鹏程
 * @CreateTime: 2020-11-22 12:12:05
 * @Description: 某系统用户
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TestUserModel extends ApiWrapper {

    /** 名称 */
    @ApiModelProperty(value = "名称")
    @ExcelProperty(value = "名称", order = 1)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_GENERAL_WITH_CHINESE})
    @ValidationArgsLenMax(50)
    private String name;

    /** 金钱 */
    @ApiModelProperty(value = "金钱")
    @ExcelProperty(value = "金钱", order = 2)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_MONEY})
    @ValidationArgsLenMax(8)
    private Double money;

    /** 年龄 */
    @ApiModelProperty(value = "年龄")
    @ExcelProperty(value = "年龄", order = 3)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_NUMBER})
    @ValidationArgsLenMax(5)
    private Integer age;

    /** 生日 */
    @ApiModelProperty(value = "生日")
    @ExcelProperty(value = "生日", order = 4)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, })
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;

    /** 是否启用 */
    @ApiModelProperty(value = "是否启用")
    @ExcelProperty(value = "是否启用", order = 5)
    @ExcelInfo( dictType = "no_yes" )
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, })
    @ValidationArgsLenMax(1)
    private String izUsable;



}
