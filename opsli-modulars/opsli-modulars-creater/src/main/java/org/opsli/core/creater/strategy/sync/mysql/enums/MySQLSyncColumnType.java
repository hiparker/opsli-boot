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
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.creater.strategy.sync.mysql.enums
 * @Author: Parker
 * @CreateTime: 2020-11-18 13:21
 * @Description: MySQL 字段类型 判断
 */
public enum MySQLSyncColumnType {

    INSTANCE;

    private static final Map<String, FieldTypeAttribute> fieldTypeMap = new HashMap<>();

    static {
        fieldTypeMap.put("tinyint", new FieldTypeAttribute(true, false));
        fieldTypeMap.put("smallint", new FieldTypeAttribute(true, false));
        fieldTypeMap.put("mediumint", new FieldTypeAttribute(true, false));
        fieldTypeMap.put("int", new FieldTypeAttribute(true, false));
        fieldTypeMap.put("integer", new FieldTypeAttribute(true, false));
        fieldTypeMap.put("bigint", new FieldTypeAttribute(true, false));
        fieldTypeMap.put("real", new FieldTypeAttribute(true, true));
        fieldTypeMap.put("float", new FieldTypeAttribute(true, true));
        fieldTypeMap.put("double", new FieldTypeAttribute(true, true));
        fieldTypeMap.put("decimal", new FieldTypeAttribute(true, true));
        fieldTypeMap.put("numeric", new FieldTypeAttribute(true, true));
        fieldTypeMap.put("char", new FieldTypeAttribute(true, false));
        fieldTypeMap.put("varchar", new FieldTypeAttribute(true, false));
        fieldTypeMap.put("date", new FieldTypeAttribute(false, false));
        fieldTypeMap.put("time", new FieldTypeAttribute(false, false));
        fieldTypeMap.put("timestamp", new FieldTypeAttribute(false, false));
        fieldTypeMap.put("datetime", new FieldTypeAttribute(false, false));
        fieldTypeMap.put("blob", new FieldTypeAttribute(false, false));
        fieldTypeMap.put("mediumblob", new FieldTypeAttribute(false, false));
        fieldTypeMap.put("longblob", new FieldTypeAttribute(false, false));
        fieldTypeMap.put("tinytext", new FieldTypeAttribute(false, false));
        fieldTypeMap.put("text", new FieldTypeAttribute(false, false));
        fieldTypeMap.put("mediumtext", new FieldTypeAttribute(false, false));
        fieldTypeMap.put("longtext", new FieldTypeAttribute(false, false));
    }

    public FieldTypeAttribute getAttr(String fieldType){
        return fieldTypeMap.get(fieldType);
    }

}
