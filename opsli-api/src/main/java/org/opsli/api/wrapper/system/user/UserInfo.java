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

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validation.ValidationArgs;
import org.opsli.common.annotation.validation.ValidationArgsLenMax;
import org.opsli.common.enums.ValiArgsType;

import java.util.List;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 用户信息表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ExcelIgnoreUnannotated
public class UserInfo extends ApiWrapper {


    /** 登录账户 */
    @ApiModelProperty(value = "登录账户")
    @ValidationArgs({ValiArgsType.IS_NOT_NULL,ValiArgsType.IS_GENERAL})
    @ValidationArgsLenMax(32)
    private String username;

    /** 真实姓名 */
    @ApiModelProperty(value = "真实姓名")
    @ValidationArgs({ValiArgsType.IS_NOT_NULL,ValiArgsType.IS_GENERAL_WITH_CHINESE})
    @ValidationArgsLenMax(50)
    private String realName;

    /** 手机 */
    @ApiModelProperty(value = "手机")
    @ValidationArgs({ValiArgsType.IS_MOBILE})
    private String mobile;

    /** 邮箱 */
    @ApiModelProperty(value = "邮箱")
    @ExcelProperty(value = "邮箱", order = 3)
    @ValidationArgs({ValiArgsType.IS_EMAIL})
    private String email;

    /** 工号 */
    @ApiModelProperty(value = "工号")
    @ExcelProperty(value = "工号", order = 4)
    @ValidationArgs({ValiArgsType.IS_GENERAL})
    @ValidationArgsLenMax(32)
    private String no;

    /** 头像 */
    @ApiModelProperty(value = "头像")
    @ValidationArgsLenMax(255)
    private String avatar;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @ValidationArgsLenMax(255)
    private String remark;

    /** 签名 */
    @ApiModelProperty(value = "签名")
    @ValidationArgsLenMax(255)
    private String sign;

    /** 角色列表 */
    @ApiModelProperty(value = "角色列表")
    private List<String> roles;

    /** 权限列表 */
    @ApiModelProperty(value = "权限列表")
    private List<String> perms;


    /** 是否是超级管理员 */
    @ApiModelProperty(value = "是否是超级管理员")
    private boolean izSuperAdmin;

}
