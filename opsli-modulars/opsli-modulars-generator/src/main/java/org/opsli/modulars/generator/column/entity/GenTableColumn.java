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
package org.opsli.modulars.generator.column.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * 代码生成器 - 表结构
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GenTableColumn extends BaseEntity {

    /** 归属表ID */
    private String tableId;

    /** 字段名称 */
    private String fieldName;

    /** 字段类型 */
    private String fieldType;

    /** 字段长度 */
    private Integer fieldLength;

    /** 字段精度 */
    private Integer fieldPrecision;

    /** 字段描述 */
    private String fieldComments;

    /** 是否主键 */
    private String izPk;

    /** 是否可为空 */
    private String izNotNull;

    /** 是否列表字段 */
    private String izShowList;

    /** 是否表单显示 */
    private String izShowForm;

    /** Java字段类型 */
    private String javaType;

    /** 字段生成方案（文本框、文本域、字典选择） */
    private String showType;

    /** 字典类型编号 */
    private String dictTypeCode;

    /** 排序（升序） */
    private Integer sort;

    /** 验证类别 */
    private String validateType;

    /** 检索类别 */
    private String queryType;


    // ========================================

}
