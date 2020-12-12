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
package org.opsli.modulars.system.role.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 角色表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRole extends BaseEntity {



    /** 角色编码 */
    private String roleCode;

    /** 角色名称 */
    private String roleName;

    /** 是否内置数据 0是  1否*/
    private String izLock;

    /** 备注 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remark;


    // ========================================

    /** 逻辑删除字段 */
    @TableLogic
    private Integer deleted;

    /** 多租户字段 */
    private String tenantId;

}
