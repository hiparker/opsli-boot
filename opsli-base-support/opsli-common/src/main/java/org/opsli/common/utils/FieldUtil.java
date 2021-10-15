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
 * @author Parker
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
        // 从function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 从序列化方法取出序列化的lambda信息
        boolean isAccessible = writeReplaceMethod.isAccessible();
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);

        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
//        Field field;
//        try {
//            field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
//        } catch (ClassNotFoundException | NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        }

        return fieldName;
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
