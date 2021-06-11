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
package org.opsli.modulars.system.dict.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * 字典表 - 明细
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysDictDetail extends BaseEntity {


    /** 字典类型ID */
    private String typeId;

    /** 字典类型Code 冗余字段 */
    private String typeCode;

    /** 字典文本 */
    private String dictName;

    /** 字典值 */
    private String dictValue;

    /** 是否内置数据 0否  1是*/
    private String izLock;

    /** 排序 */
    private Integer sortNo;

    /** 备注 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remark;


    // ========================================

    /** 逻辑删除字段 */
    @TableLogic
    private String deleted;

}
