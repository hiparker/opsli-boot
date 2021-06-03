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
package org.opsli.core.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.annotation.validator.ValidatorLenMin;
import org.opsli.common.enums.ValidatorType;
import org.opsli.common.utils.DefPatternPool;
import org.opsli.common.msg.ValidatorMsg;
import org.opsli.api.wrapper.system.dict.DictModel;
import org.opsli.common.exception.ServiceException;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

/**
 * 验证器工具类
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
@Slf4j
public final class ValidatorUtil {

    /**
     * 验证对象
     * @param obj 验证对象
     */
    public static void verify(Object obj){
        if(obj == null){
            return;
        }

        Field[] fields = ReflectUtil.getFields(obj.getClass());
        for (Field field : fields) {
            // 获得 统一验证 注解 （起码冲突了）
            org.opsli.common.annotation.validator.Validator validator =
                    field.getAnnotation(org.opsli.common.annotation.validator.Validator.class);
            if (validator != null) {
                ValidatorType[] types = validator.value();
                Object fieldValue = ReflectUtil.getFieldValue(obj, field);
                ValidatorUtil.check(field, types, fieldValue);
            }

            // 获得 最大长度 注解
            ValidatorLenMax validationArgsMax = field.getAnnotation(ValidatorLenMax.class);
            if (validationArgsMax != null) {
                int maxLength = validationArgsMax.value();
                Object fieldValue = ReflectUtil.getFieldValue(obj, field);
                ValidatorUtil.checkMax(field, maxLength, fieldValue);
            }

            // 获得 最小长度 注解
            ValidatorLenMin validationArgsMin = field.getAnnotation(ValidatorLenMin.class);
            if (validationArgsMin != null) {
                int minLength = validationArgsMin.value();
                Object fieldValue = ReflectUtil.getFieldValue(obj, field);
                ValidatorUtil.checkMin(field, minLength, fieldValue);
            }
        }
    }

    /**
     * 通用校验
     * @param field 字段
     * @param types 类型数组
     * @param fieldValue 字段值
     */
    private static void check(Field field, ValidatorType[] types, Object fieldValue){
        // 获得字段名
        String fieldName = field.getName();
        ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
        if(annotation != null){
            fieldName = annotation.value();
        }

        String value = Convert.toStr(fieldValue);

        // 循环验证
        for (ValidatorType type : types) {
            try {
                boolean verifyRet;
                switch (type) {
                    // 不能为空
                    case IS_NOT_NULL: {
                        verifyRet = Validator.isNotEmpty(fieldValue);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_NOT_NULL;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 字母，数字和下划线
                    case IS_GENERAL: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet = Validator.isGeneral(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_GENERAL;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 整数
                    case IS_INTEGER: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet =  NumberUtil.isInteger(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_INTEGER;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 浮点数
                    case IS_DECIMAL: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet =  NumberUtil.isDouble(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_DECIMAL;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 质数
                    case IS_PRIMES: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        // 先验证 是否是 整数, 再验证是否是 质数
                        verifyRet =  NumberUtil.isInteger(value) &&
                                NumberUtil.isPrimes(Convert.toInt(value));
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_PRIMES;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 纯字母
                    case IS_LETTER: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet =  Validator.isLetter(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_LETTER;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 大写
                    case IS_UPPER_CASE: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet =  Validator.isUpperCase(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_UPPER_CASE;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 小写
                    case IS_LOWER_CASE: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet =  Validator.isLowerCase(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_LOWER_CASE;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // IP
                    case IS_IP: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet =  Validator.isIpv4(value) || Validator.isIpv6(value) ;
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_IP;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // IPV4
                    case IS_IPV4: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet =  Validator.isIpv4(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_IPV4;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // IPV6
                    case IS_IPV6: {
                        if(StringUtils.isEmpty(value)){
                            break;
                        }
                        verifyRet = Validator.isIpv6(value);
                        if(!verifyRet){
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_IPV6;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 金额
                    case IS_MONEY: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet = Validator.isMoney(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_MONEY;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 邮箱
                    case IS_EMAIL: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet = Validator.isEmail(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_EMAIL;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 手机号
                    case IS_MOBILE: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet = Validator.isMobile(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_MOBILE;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 18位身份证
                    case IS_CITIZENID: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet = Validator.isCitizenId(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_CITIZENID;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 邮编
                    case IS_ZIPCODE: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet = Validator.isZipCode(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_ZIPCODE;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // URL
                    case IS_URL: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet = Validator.isUrl(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_URL;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 汉字
                    case IS_CHINESE: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet = Validator.isChinese(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_CHINESE;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 汉字，字母，数字和下划线
                    case IS_GENERAL_WITH_CHINESE: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet = Validator.isGeneralWithChinese(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_GENERAL_WITH_CHINESE;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // MAC地址
                    case IS_MAC: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet = Validator.isMac(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_MAC;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 中国车牌
                    case IS_PLATE_NUMBER: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet = Validator.isPlateNumber(value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_PLATE_NUMBER;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    // 安全密码
                    case IS_SECURITY_PASSWORD: {
                        if (StringUtils.isEmpty(value)) {
                            break;
                        }
                        verifyRet = Validator.isMatchRegex(
                                DefPatternPool.SECURITY_PASSWORD, value);
                        if (!verifyRet) {
                            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_SECURITY_PASSWORD;
                            msg.setFieldName(fieldName);
                            throw new ServiceException(msg);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }catch (ServiceException e){
                throw e;
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
    }


    /**
     * 最大长度校验
     * @param field 字段
     * @param maxLength 最大长度
     * @param fieldValue 字段值
     */
    private static void checkMax(Field field, int maxLength, Object fieldValue){
        // 获得字段名
        String fieldName = field.getName();
        ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
        if(annotation != null){
            fieldName = annotation.value();
        }

        // 循环验证
        try {
            String value = Convert.toStr(fieldValue);
            if(StringUtils.isNotEmpty(value)){
                // 转换为 数据库真实 长度
                int strLength = value.getBytes(StandardCharsets.UTF_8).length;
                if(strLength > maxLength){
                    ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_MAX;
                    msg.setFieldName(fieldName);
                    throw new ServiceException(msg);
                }
            }
        }catch (ServiceException e){
            throw e;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    /**
     * 最小长度校验
     * @param field 字段
     * @param minLength 最小长度
     * @param fieldValue 字段值
     */
    private static void checkMin(Field field, int minLength, Object fieldValue){
        // 获得字段名
        String fieldName = field.getName();
        ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
        if(annotation != null){
            fieldName = annotation.value();
        }

        // 循环验证
        try {
            String value = Convert.toStr(fieldValue);
            if(StringUtils.isNotEmpty(value)){
                // 转换为 数据库真实 长度
                int strLength = value.getBytes(StandardCharsets.UTF_8).length;
                if(strLength < minLength){
                    ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_MIN;
                    msg.setFieldName(fieldName);
                    throw new ServiceException(msg);
                }
            }
        }catch (ServiceException e){
            throw e;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    public static void main(String[] args) {
        DictModel dictModel = new DictModel();
        dictModel.setTypeCode("asdsa");
        dictModel.setTypeName("阿哈哈哈哈");
        dictModel.setRemark("测试11232131231231223123");
        dictModel.setIzLock("1");

        ValidatorUtil.verify(dictModel);
    }


    // ================

    private ValidatorUtil(){}

}
