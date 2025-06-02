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
package org.opsli.common.utils;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * 字段处理工具类
 *
 * @author Pace
 * @date 2020-09-19 23:21
 */
public final class FieldUtil {

    public final static String UNDERLINE = "_";


    /***
     * 下划线命名转为驼峰命名
     *
     * @param para 下划线命名的字符串
     * @return String
     */
    public static String underlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String[] a = para.split(UNDERLINE);
        for (String s : a) {
            if (!para.contains(UNDERLINE)) {
                result.append(s);
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para 驼峰命名的字符串
     * @return String
     */
    public static String humpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        //定位
        int temp = 0;
        if (!para.contains(UNDERLINE)) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, UNDERLINE);
                    temp += 1;
                }
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 将第一个字母转换成大写
     *
     * @param str 字符串
     * @return 返回新字段
     */
    public static String upperFirstLetter(String str) {
        if (StringUtils.hasText(str)) {
            String firstUpper = str.substring(0, 1).toUpperCase();
            str = firstUpper + str.substring(1);
        }
        return str;
    }

    /**
     * 将第一个字母转换成小写
     *
     * @param str 字符串
     * @return 返回新内容
     */
    public static String lowerFirstLetter(String str) {
        if (StringUtils.hasText(str)) {
            String firstLower = str.substring(0, 1).toLowerCase();
            str = firstLower + str.substring(1);
        }
        return str;
    }


    /**
     * 获得字段名称
     * @param fn 方法
     * @param <T> 泛型
     * @return String
     */
    public static <T> String getFileName(SFunction<T, ?> fn) {
        if (fn == null) {
            throw new IllegalArgumentException("Function cannot be null");
        }

        try {
            // 获取writeReplace方法
            Method writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");

            // 使用新的canAccess API替代isAccessible
            boolean wasAccessible = writeReplaceMethod.canAccess(fn);
            if (!wasAccessible) {
                writeReplaceMethod.setAccessible(true);
            }

            try {
                // 获取序列化Lambda信息
                SerializedLambda serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);

                // 提取并转换字段名
                return extractFieldName(serializedLambda.getImplMethodName());

            } finally {
                // 恢复原始访问性
                if (!wasAccessible) {
                    writeReplaceMethod.setAccessible(false);
                }
            }

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Invalid lambda function: missing writeReplace method", e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to extract lambda information", e);
        }
    }

    /**
     * 从方法名提取字段名
     * 支持 get/is/has 等前缀
     */
    private static String extractFieldName(String methodName) {
        if (methodName == null || methodName.isEmpty()) {
            throw new IllegalArgumentException("Method name cannot be null or empty");
        }

        String fieldName;

        // 处理不同的getter前缀
        if (methodName.startsWith("get") && methodName.length() > 3) {
            fieldName = methodName.substring(3);
        } else if (methodName.startsWith("is") && methodName.length() > 2) {
            fieldName = methodName.substring(2);
        } else if (methodName.startsWith("has") && methodName.length() > 3) {
            fieldName = methodName.substring(3);
        } else {
            // 如果不是标准getter，直接返回方法名
            return methodName;
        }

        // 首字母小写
        return Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);

    }


    // ====================

    /**
     * 使Function获取序列化能力
     */
    @FunctionalInterface
    public interface SFunction<T, R> extends Function<T, R>, Serializable {

    }

    private FieldUtil(){}
}
