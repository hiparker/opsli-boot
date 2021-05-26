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
package org.opsli.plugins.generator.database.sqlserver;


import org.opsli.plugins.generator.TypeFormatter;

import java.util.Arrays;
import java.util.Collections;

/**
 * SqlServer 字段格式化判断
 *
 * @author Parker
 * @date 2021年5月26日14:51:50
 */
public class SqlServerTypeFormatter implements TypeFormatter {

    @Override
    public boolean isBit(String columnType) {
        return isContains(Collections.singletonList("bit"), columnType);
    }

    @Override
    public boolean isBoolean(String columnType) {
        return isContains(Collections.singletonList("boolean"), columnType);
    }

    @Override
    public boolean isTinyint(String columnType) {
        return isContains(Collections.singletonList("tinyint"), columnType);
    }

    @Override
    public boolean isSmallint(String columnType) {
        return isContains(Collections.singletonList("smallint"), columnType);
    }

    @Override
    public boolean isInt(String columnType) {
        return !isLong(columnType) && isContains(Arrays.asList("int", "integer"), columnType);
    }

    @Override
    public boolean isLong(String columnType) {
        return !isVarchar(columnType) && isContains(Collections.singletonList("bigint"), columnType);
    }

    @Override
    public boolean isFloat(String columnType) {
        return isContains(Arrays.asList("float", "real"), columnType);
    }

    @Override
    public boolean isDouble(String columnType) {
        return isContains(Collections.singletonList("double"), columnType);
    }

    @Override
    public boolean isDecimal(String columnType) {
        return isContains(Arrays.asList("decimal", "numeric", "money", "smallmoney"), columnType);
    }

    @Override
    public boolean isVarchar(String columnType) {
        return isContains(Arrays.asList("CHAR", "VARCHAR", "TEXT", "nchar", "nvarchar", "ntext"), columnType);
    }

    @Override
    public boolean isDatetime(String columnType) {
        return isContains(Arrays.asList("DATE", "TIME", "DATETIME", "TIMESTAMP", "datetime2", "smalldatetime", "datetimeoffset"), columnType);
    }

    @Override
    public boolean isBlob(String columnType) {
        return isContains(Arrays.asList("blob", "binary", "varbinary"), columnType);
    }

    @Override
    public boolean isJsonb(String columnType) {
        return false;
    }
}
