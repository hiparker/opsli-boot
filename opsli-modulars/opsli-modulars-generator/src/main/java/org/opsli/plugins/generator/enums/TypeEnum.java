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
package org.opsli.plugins.generator.enums;

/**
 * 统一类型枚举
 *
 * @author tanghc
 */
public enum TypeEnum {

    /** 统一类型 */
    BIT("bit"),

    BOOLEAN("boolean"),

    TINYINT("tinyint"),

    SMALLINT("smallint"),

    INT("int"),

    INTEGER("integer"),

    BIGINT("bigint"),

    FLOAT("float"),

    DOUBLE("double"),

    DECIMAL("decimal"),

    CHAR("char"),

    VARCHAR("varchar"),

    TEXT("text"),

    DATE("date"),

    DATETIME("datetime"),

    TIMESTAMP("timestamp"),

    BLOB("blob"),

    JSONB("jsonb")

    ;

    private final String type;

    TypeEnum(String type) {
        this.type = type;
    }

    public static TypeEnum getType(String type) {
        TypeEnum[] types = values();
        for (TypeEnum typeEnum : types) {
            if (typeEnum.type.equalsIgnoreCase(type)) {
                return typeEnum;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }
}
