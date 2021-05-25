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
package org.opsli.modulars.generator.importable.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代码生成器 - 数据库表字段
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DatabaseColumn {


    /** 数据库 */
    private String dbName;

    /** 表名称 */
    private String tableName;

    /** 字段名称 */
    private String columnName;

    /** 字段类型 */
    private String columnType;

    /** 字段长度 */
    private Integer columnLength;

    /** 字段精度 = 位数 */
    private Integer columnPrecision;

    /** 字段位数 = 精度 */
    private Integer columnScale;

    /** 字段描述 */
    private String columnComment;

    /** 是否主键 */
    private String izPk;

    /** 是否可为空 */
    private String izNotNull;

    // ========================================

}
