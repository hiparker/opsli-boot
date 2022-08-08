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
package org.opsli.modulars.system.user.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * 用户信息
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUser extends BaseEntity {


    /** 登录账户 */
    private String username;

    /** 登录密码 */
    private String password;

    /** 登录密码强度 */
    private String passwordLevel;

    /** 是否启用 */
    private String enable;

    /** 真实姓名 */
    private String realName;

    /** 手机 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String mobile;

    /** 邮箱 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String email;

    /** 工号 */
    private String no;

    /** 头像 */
    private String avatar;

    /** 最后登陆IP */
    private String loginIp;

    /** 备注 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remark;

    /** 签名 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String sign;

    /** 是否存在组织 */
    private String izExistOrg;

    /** 是否租户管理员 */
    private String izTenantAdmin;

    /** 允许切换租户（0 不允许 1 允许） */
    private String enableSwitchTenant;

    // ========================================

    /** 逻辑删除字段 */
    @TableLogic
    private String deleted;

    /** 多租户字段 */
    private String tenantId;

}
