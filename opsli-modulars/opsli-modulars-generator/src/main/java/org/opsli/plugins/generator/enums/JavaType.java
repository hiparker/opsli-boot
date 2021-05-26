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

import org.apache.commons.lang3.StringUtils;
import org.opsli.common.utils.ListDistinctUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Java 类型
 *
 * @author Parker
 * @date 2021年5月26日16:31:08
 */
public enum JavaType {

    /** 包装类型 */
    BYTE("Byte", null),
    SHORT("Short", null),
    CHARACTER("Character", null),
    INTEGER("Integer", null),
    LONG("Long", null),
    FLOAT("Float", null),
    DOUBLE("Double", null),
    BOOLEAN("Boolean", null),
    STRING("String", null),

    /** Big类 */
    BIG_INTEGER("BigInteger", "java.math.BigInteger"),
    BIG_DECIMAL("BigDecimal", "java.math.BigDecimal"),

    /** 时间类 */
    DATE("Date", "java.util.Date"),
    LOCAL_DATE("LocalDate", "java.time.LocalDate"),
    LOCAL_TIME("LocalTime", "java.time.LocalTime"),
    YEAR("Year", "java.time.Year"),
    YEAR_MONTH("YearMonth", "java.time.YearMonth"),
    LOCAL_DATE_TIME("LocalDateTime", "java.time.LocalDateTime"),
    INSTANT("Instant", "java.time.Instant"),

    /** 其他 */
    BYTE_ARRAY("Byte[]",null),
    MAP_OBJECT("Map<String, Object>", "java.util.Map"),

    ;

    /** 类型 */
    private final String type;
    /** 包路径 */
    private final String pkg;

    JavaType(final String type, final String pkg) {
        this.type = type;
        this.pkg = pkg;
    }

    public String getType() {
        return type;
    }

    public String getPkg() {
        return pkg;
    }

    /**
     * 获得 包地址
     * @param typeList 类型集合
     * @return 包集合
     */
    public static List<String> getPkgList(List<String> typeList) {
        if(typeList == null || typeList.size() == 0){
            return Collections.emptyList();
        }

        List<String> pkgList = new ArrayList<>();
        JavaType[] types = values();
        for (JavaType javaType : types) {
            if(typeList.contains(javaType.type)){
                pkgList.add(javaType.pkg);
            }
        }
        pkgList.removeIf(StringUtils::isBlank);
        // 去重复
        return ListDistinctUtil.distinct(pkgList);
    }

}
