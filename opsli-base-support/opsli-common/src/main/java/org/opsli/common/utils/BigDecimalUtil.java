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

import java.math.BigDecimal;

/**
 * 小数精度处理工具类
 *
 * @author Parker
 * @date 2020-09-19 23:21
 */
public class BigDecimalUtil {
    /**
     * 提供精确加法计算的add方法
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static double add(double value1,double value2){
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确减法运算的sub方法
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(double value1,double value2){
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确乘法运算的mul方法
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static double mul(double value1,double value2){
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的除法运算方法div
     * @param value1 被除数
     * @param value2 除数
     * @param scale 精确范围
     * @param model 舍入模式
     *              0   ROUND_UP              远离零方向舍入的舍入模式。始终对非零舍弃部分前面的数字加 1。
     *              1   ROUND_DOWN            向零部方向舍入的舍入模式。从不对舍弃分前面的数字加 1（即截尾）。
     *              2   ROUND_CEILING         向正无限大方向舍入的舍入模式。如果结果为正，则舍入行为类似于 舍入模式.UP；如果结果为负，则舍入行为类似于 舍入模式.DOWN。
     *              3   ROUND_FLOOR           向负无限大方向舍入的舍入模式。如果结果为正，则舍入行为类似于 舍入模式.DOWN；如果结果为负，则舍入行为类似于 舍入模式.UP。
     * （四舍五入）   4   ROUND_HALF_UP         向最接近数字方向舍入的舍入模式，如果与两个相邻数字的距离相等，则向上舍入。如果被舍弃部分 >= 0.5，则舍入行为同 舍入模式.UP；否则舍入行为同 舍入模式.DOWN。
     *              5   ROUND_HALF_DOWN       向最接近数字方向舍入的舍入模式，如果与两个相邻数字的距离相等，则向下舍入。如果被舍弃部分 > 0.5，则舍入行为同 舍入模式.UP；否则舍入行为同 舍入模式.DOWN。
     *              6   ROUND_HALF_EVEN       向最接近数字方向舍入的舍入模式，如果与两个相邻数字的距离相等，则向相邻的偶数舍入。如果舍弃部分左边的数字为奇数，则舍入行为同 舍入模式.HALF_UP；如果为偶数，则舍入行为同 舍入模式.HALF_DOWN
     *              7   ROUND_UNNECESSARY     用于断言请求的操作具有精确结果的舍入模式，因此不需要舍入。如果对生成精确结果的操作指定此舍入模式，则抛出 ArithmeticException。
     * @return 两个参数的商
     * @throws IllegalAccessException
     */
    public static double div(double value1,double value2,int scale,int model) throws IllegalAccessException{
        //如果精确范围小于0，抛出异常信息
        if(scale<0){
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.divide(b2, scale, model).doubleValue();
    }
    public static double div(double value1,double value2,int scale) throws IllegalAccessException{
        //如果精确范围小于0，抛出异常信息
        if(scale<0){
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.divide(b2, scale,4).doubleValue();
    }
}
