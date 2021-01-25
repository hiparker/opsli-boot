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
package org.opsli.api.wrapper.system.user;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.common.annotation.validation.ValidationArgs;
import org.opsli.common.annotation.validation.ValidationArgsLenMax;
import org.opsli.common.annotation.validation.ValidationArgsLenMin;
import org.opsli.common.enums.ValiArgsType;

import java.io.Serializable;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 用户 修改密码
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ExcelIgnoreUnannotated
public class UserPassword implements Serializable {

    private static final long serialVersionUID = 1L;

    /** User Id */
    @ApiModelProperty(value = "用户Id")
    private String userId;

    /** 旧密码 */
    @ApiModelProperty(value = "旧密码")
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsLenMin(6)
    @ValidationArgsLenMax(50)
    private String oldPassword;

    /** 新密码 */
    @ApiModelProperty(value = "新密码")
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsLenMin(6)
    @ValidationArgsLenMax(50)
    private String newPassword;

    /** 盐值，密码秘钥 */
    @ApiModelProperty(value = "盐值，密码秘钥")
    @ExcelIgnore
    // 验证器
    @ValidationArgsLenMax(50)
    private String salt;

}
