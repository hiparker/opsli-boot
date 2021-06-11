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
package org.opsli.modulars.system.area.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * 地域表
 *
 * @author Parker
 * @date 2020-11-28 18:59:59
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysArea extends BaseEntity {


    /** 父级主键 */
    private String parentId;

    /** 地域编号 */
    private String areaCode;

    /** 地域名称 */
    private String areaName;


    // ========================================


    /** 逻辑删除字段 */
    @TableLogic
    private String deleted;

}
