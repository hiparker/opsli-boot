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
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.annotation.validator.ValidatorLenMin;
import org.opsli.common.enums.ValidatorType;
import org.opsli.plugins.excel.annotation.ExcelInfo;

/**
 * 用户信息表
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserModel extends ApiWrapper {

    /** 登录账户 */
    @ApiModelProperty(value = "登录账户")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_GENERAL})
    @ValidatorLenMax(32)
    @ValidatorLenMin(4)
    private String username;

    /** 登录密码 */
    @ApiModelProperty(value = "登录密码")
    @ExcelIgnore
    @ValidatorLenMin(6)
    @ValidatorLenMax(50)
    private String password;

    /** 登录密码强度 */
    @ApiModelProperty(value = "登录密码强度")
    @ExcelIgnore
    @ValidatorLenMin(1)
    @ValidatorLenMax(1)
    private String passwordLevel;

    /** 是否启用 */
    @ApiModelProperty(value = "是否启用")
    @ExcelIgnore
    @ValidatorLenMax(1)
    private String enable;

    /** 真实姓名 */
    @ApiModelProperty(value = "真实姓名")
    @ExcelProperty(value = "真实姓名", order = 1)
    @ExcelInfo
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_GENERAL_WITH_CHINESE})
    @ValidatorLenMax(50)
    private String realName;

    /** 手机 */
    @ApiModelProperty(value = "手机")
    @ExcelProperty(value = "手机", order = 2)
    @ExcelInfo
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_MOBILE})
    private String mobile;

    /** 邮箱 */
    @ApiModelProperty(value = "邮箱")
    @ExcelProperty(value = "邮箱", order = 3)
    @ExcelInfo
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_EMAIL})
    @ValidatorLenMax(100)
    private String email;

    /** 工号 */
    @ApiModelProperty(value = "工号")
    @ExcelProperty(value = "工号", order = 4)
    @ExcelInfo
    @Validator({ValidatorType.IS_GENERAL})
    @ValidatorLenMax(32)
    private String no;

    /** 头像 */
    @ApiModelProperty(value = "头像")
    @ExcelIgnore
    @ValidatorLenMax(255)
    private String avatar;

    /** 最后登陆IP */
    @ApiModelProperty(value = "最后登陆IP")
    @ExcelIgnore
    @Validator(ValidatorType.IS_IPV4)
    private String loginIp;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注", order = 5)
    @ExcelInfo
    @ValidatorLenMax(255)
    private String remark;

    /** 签名 */
    @ApiModelProperty(value = "签名")
    @ExcelProperty(value = "签名", order = 5)
    @ExcelInfo
    @ValidatorLenMax(255)
    private String sign;


    /** 多租户字段 */
    @ApiModelProperty(value = "多租户ID")
    @ExcelIgnore
    @ValidatorLenMax(20)
    private String tenantId;

    /** 是否租户管理员 */
    @ApiModelProperty(value = "是否租户管理员")
    @ExcelIgnore
    @ValidatorLenMax(1)
    private String izTenantAdmin;

    /** 是否存在组织 */
    @JsonIgnore
    @ExcelIgnore
    @ValidatorLenMax(1)
    private String izExistOrg;

    /** 允许切换租户（0 不允许 1 允许） */
    @ApiModelProperty(value = "是否允许切换运营商")
    @ExcelIgnore
    private String enableSwitchTenant;

    /** 切换后的租户id*/
    @JsonIgnore
    @ExcelIgnore
    private String switchTenantId;

    /** 切换后的租户管理员*/
    @JsonIgnore
    @ExcelIgnore
    private String switchTenantUserId;

}
