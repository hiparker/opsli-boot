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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.core.base.entity.BaseEntity;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 用户信息表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserAndOrg extends BaseEntity {


    /** 登录账户 */
    private String username;

    /** 登录密码 */
    private String password;

    /** 盐值，密码秘钥 */
    private String secretKey;

    /** 是否锁定 */
    private String locked;

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

    /** 组织机构 */
    private UserOrgRefModel org;

    // ========================================

    /** 逻辑删除字段 */
    @TableLogic
    private Integer deleted;

    /** 多租户字段 */
    private String tenantId;

}
