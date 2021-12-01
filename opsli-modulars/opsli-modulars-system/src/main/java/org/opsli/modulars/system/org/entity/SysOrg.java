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
package org.opsli.modulars.system.org.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * 组织机构表
 *
 * @author Parker
 * @date 2021-02-07 18:24:38
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysOrg extends BaseEntity {

    /** 父级主键 */
    private String parentId;

    /** 父级主键集合   xxx,xxx */
    private String parentIds;

    /** 组织机构编号 */
    private String orgCode;

    /** 组织机构名称 */
    private String orgName;

    /** 排序 */
    private Integer sortNo;

    /** 备注 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remark;

    // ========================================


    /** 逻辑删除字段 */
    @TableLogic
    private String deleted;

    /** 组织机构 */
    private String orgIds;

    /** 多租户字段 */
    private String tenantId;

}
