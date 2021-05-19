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
package org.opsli.core.creater.strategy.sync.mysql.enums;



import org.opsli.core.creater.strategy.sync.mysql.entity.FieldTypeAttribute;

import java.util.HashMap;
import java.util.Map;

/**
 * MySQL 字段类型 判断
 *
 * @author parker
 * @date 2020-11-18 13:21
 */
public enum MySQLSyncColumnType {

    /** 实例对象 */
    INSTANCE;

    private static final Map<String, FieldTypeAttribute> FIELD_TYPE_MAP = new HashMap<>();

    static {
        FIELD_TYPE_MAP.put("tinyint", new FieldTypeAttribute(true, false));
        FIELD_TYPE_MAP.put("smallint", new FieldTypeAttribute(true, false));
        FIELD_TYPE_MAP.put("mediumint", new FieldTypeAttribute(true, false));
        FIELD_TYPE_MAP.put("int", new FieldTypeAttribute(true, false));
        FIELD_TYPE_MAP.put("integer", new FieldTypeAttribute(true, false));
        FIELD_TYPE_MAP.put("bigint", new FieldTypeAttribute(true, false));
        FIELD_TYPE_MAP.put("real", new FieldTypeAttribute(true, true));
        FIELD_TYPE_MAP.put("float", new FieldTypeAttribute(true, true));
        FIELD_TYPE_MAP.put("double", new FieldTypeAttribute(true, true));
        FIELD_TYPE_MAP.put("decimal", new FieldTypeAttribute(true, true));
        FIELD_TYPE_MAP.put("numeric", new FieldTypeAttribute(true, true));
        FIELD_TYPE_MAP.put("char", new FieldTypeAttribute(true, false));
        FIELD_TYPE_MAP.put("varchar", new FieldTypeAttribute(true, false));
        FIELD_TYPE_MAP.put("date", new FieldTypeAttribute(false, false));
        FIELD_TYPE_MAP.put("time", new FieldTypeAttribute(false, false));
        FIELD_TYPE_MAP.put("timestamp", new FieldTypeAttribute(false, false));
        FIELD_TYPE_MAP.put("datetime", new FieldTypeAttribute(false, false));
        FIELD_TYPE_MAP.put("blob", new FieldTypeAttribute(false, false));
        FIELD_TYPE_MAP.put("mediumblob", new FieldTypeAttribute(false, false));
        FIELD_TYPE_MAP.put("longblob", new FieldTypeAttribute(false, false));
        FIELD_TYPE_MAP.put("tinytext", new FieldTypeAttribute(false, false));
        FIELD_TYPE_MAP.put("text", new FieldTypeAttribute(false, false));
        FIELD_TYPE_MAP.put("mediumtext", new FieldTypeAttribute(false, false));
        FIELD_TYPE_MAP.put("longtext", new FieldTypeAttribute(false, false));
    }

    public FieldTypeAttribute getAttr(String fieldType){
        return FIELD_TYPE_MAP.get(fieldType);
    }

}
