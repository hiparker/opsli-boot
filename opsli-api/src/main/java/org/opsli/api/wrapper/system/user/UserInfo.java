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
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.annotation.validator.ValidatorLenMin;
import org.opsli.common.enums.ValidatorType;

import java.util.List;

/**
 * 用户信息表
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ExcelIgnoreUnannotated
public class UserInfo extends ApiWrapper {


    /** 登录账户 */
    @ApiModelProperty(value = "登录账户")
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_GENERAL})
    @ValidatorLenMax(32)
    @ValidatorLenMin(5)
    private String username;

    /** 真实姓名 */
    @ApiModelProperty(value = "真实姓名")
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_GENERAL_WITH_CHINESE})
    @ValidatorLenMax(50)
    private String realName;

    /** 手机 */
    @ApiModelProperty(value = "手机")
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_MOBILE})
    private String mobile;

    /** 邮箱 */
    @ApiModelProperty(value = "邮箱")
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_EMAIL})
    private String email;

    /** 工号 */
    @ApiModelProperty(value = "工号")
    @Validator({ValidatorType.IS_GENERAL})
    @ValidatorLenMax(32)
    private String no;

    /** 头像 */
    @ApiModelProperty(value = "头像")
    @ValidatorLenMax(255)
    private String avatar;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @ValidatorLenMax(255)
    private String remark;

    /** 签名 */
    @ApiModelProperty(value = "签名")
    @ValidatorLenMax(255)
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

    /** 密码强度 字典 password_level */
    @ApiModelProperty(value = "密码强度")
    private String passwordLevel;

    /** 多租户字段 */
    @ApiModelProperty(value = "多租户ID")
    private String tenantId;

    /** 允许切换租户（0 不允许 1 允许） */
    @ApiModelProperty(value = "是否允许切换运营商")
    private String enableSwitchTenant;

    /** 切换后的租户id*/
    @ApiModelProperty(value = "切换后的租户id")
    private String switchTenantId;

    /** 切换后的租户管理员*/
    @ApiModelProperty(value = "切换后的租户管理员")
    private String switchTenantUserId;

    /** 数据范围*/
    @ApiModelProperty(value = "数据范围")
    private String dataScope;

}
