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
package org.opsli.plugins.generator.converter;


import com.google.common.collect.Lists;
import org.opsli.common.utils.ListDistinctUtil;
import org.opsli.plugins.generator.enums.JavaType;
import org.opsli.plugins.generator.enums.TypeEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Java 字段类型
 *
 * @author Parker
 * @date 2021年5月26日14:25:47
 */
public class JavaColumnTypeConverter implements ColumnTypeConverter {

    private static final Map<TypeEnum, JavaType> TYPE_MAP = new HashMap<>(64);
    static {
        TYPE_MAP.put(TypeEnum.BIT, JavaType.BOOLEAN);
        TYPE_MAP.put(TypeEnum.TINYINT, JavaType.BYTE);
        TYPE_MAP.put(TypeEnum.SMALLINT, JavaType.SHORT);
        TYPE_MAP.put(TypeEnum.INT, JavaType.INTEGER);
        TYPE_MAP.put(TypeEnum.INTEGER, JavaType.INTEGER);
        TYPE_MAP.put(TypeEnum.BIGINT, JavaType.LONG);
        TYPE_MAP.put(TypeEnum.FLOAT, JavaType.FLOAT);
        TYPE_MAP.put(TypeEnum.DOUBLE, JavaType.DOUBLE);
        TYPE_MAP.put(TypeEnum.DECIMAL, JavaType.BIG_DECIMAL);
        TYPE_MAP.put(TypeEnum.CHAR, JavaType.STRING);
        TYPE_MAP.put(TypeEnum.VARCHAR, JavaType.STRING);
        TYPE_MAP.put(TypeEnum.TEXT, JavaType.STRING);
        TYPE_MAP.put(TypeEnum.DATE, JavaType.DATE);
        TYPE_MAP.put(TypeEnum.DATETIME, JavaType.DATE);
        TYPE_MAP.put(TypeEnum.TIMESTAMP, JavaType.DATE);
        TYPE_MAP.put(TypeEnum.BLOB, JavaType.BYTE_ARRAY);
        TYPE_MAP.put(TypeEnum.JSONB, JavaType.MAP_OBJECT) ;
        TYPE_MAP.put(TypeEnum.BOOLEAN, JavaType.BOOLEAN);
    }

    @Override
    public String convertType(TypeEnum type) {
        return TYPE_MAP.getOrDefault(type, TYPE_MAP.get(TypeEnum.VARCHAR)).getType();
    }

    @Override
    public List<String> convertTypeBySafety(TypeEnum type) {
        String currType = TYPE_MAP.getOrDefault(type, TYPE_MAP.get(TypeEnum.VARCHAR)).getType();
        String defType = TYPE_MAP.get(TypeEnum.VARCHAR).getType();
        List<String> typeList = Lists.newArrayListWithCapacity(2);
        typeList.add(currType);
        typeList.add(defType);
        return ListDistinctUtil.distinct(typeList);
    }
}
