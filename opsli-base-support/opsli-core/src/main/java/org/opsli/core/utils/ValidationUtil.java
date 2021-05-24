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
import org.opsli.common.utils.DefPatternPool;
import org.opsli.core.msg.ValidationMsg;
import org.opsli.api.wrapper.system.dict.DictModel;
import org.opsli.common.annotation.validation.ValidationArgs;
import org.opsli.common.annotation.validation.ValidationArgsLenMax;
import org.opsli.common.annotation.validation.ValidationArgsLenMin;
import org.opsli.common.enums.ValiArgsType;
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
public final class ValidationUtil {

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
            // 获得 统一验证 注解
            ValidationArgs validationArgs = field.getAnnotation(ValidationArgs.class);
            if (validationArgs != null) {
                ValiArgsType[] types = validationArgs.value();
                Object fieldValue = ReflectUtil.getFieldValue(obj, field);
                ValidationUtil.check(field, types, fieldValue);
            }

            // 获得 最大长度 注解
            ValidationArgsLenMax validationArgsMax = field.getAnnotation(ValidationArgsLenMax.class);
            if (validationArgsMax != null) {
                int maxLength = validationArgsMax.value();
                Object fieldValue = ReflectUtil.getFieldValue(obj, field);
                ValidationUtil.checkMax(field, maxLength, fieldValue);
            }

            // 获得 最小长度 注解
            ValidationArgsLenMin validationArgsMin = field.getAnnotation(ValidationArgsLenMin.class);
            if (validationArgsMin != null) {
                int minLength = validationArgsMin.value();
                Object fieldValue = ReflectUtil.getFieldValue(obj, field);
                ValidationUtil.checkMin(field, minLength, fieldValue);
            }
        }
    }

    /**
     * 通用校验
     * @param field 字段
     * @param types 类型数组
     * @param fieldValue 字段值
     */
    private static void check(Field field, ValiArgsType[] types, Object fieldValue){
        // 获得字段名
        String fieldName = field.getName();
        ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
        if(annotation != null){
            fieldName = annotation.value();
        }

        String value = Convert.toStr(fieldValue);

        // 循环验证
        for (ValiArgsType type : types) {
            try {
                boolean validateRet;
                switch (type) {
                    // 不能为空
                    case IS_NOT_NULL: {
                        validateRet = Validator.isNotEmpty(fieldValue);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_NOT_NULL;
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
                        validateRet = Validator.isGeneral(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_GENERAL;
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
                        validateRet =  NumberUtil.isInteger(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_INTEGER;
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
                        validateRet =  NumberUtil.isDouble(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_DECIMAL;
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
                        validateRet =  NumberUtil.isInteger(value) &&
                                NumberUtil.isPrimes(Convert.toInt(value));
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_PRIMES;
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
                        validateRet =  Validator.isLetter(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_LETTER;
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
                        validateRet =  Validator.isUpperCase(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_UPPER_CASE;
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
                        validateRet =  Validator.isLowerCase(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_LOWER_CASE;
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
                        validateRet =  Validator.isIpv4(value) || Validator.isIpv6(value) ;
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_IP;
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
                        validateRet =  Validator.isIpv4(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_IPV4;
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
                        validateRet = Validator.isIpv6(value);
                        if(!validateRet){
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_IPV6;
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
                        validateRet = Validator.isMoney(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_MONEY;
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
                        validateRet = Validator.isEmail(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_EMAIL;
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
                        validateRet = Validator.isMobile(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_MOBILE;
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
                        validateRet = Validator.isCitizenId(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_CITIZENID;
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
                        validateRet = Validator.isZipCode(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_ZIPCODE;
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
                        validateRet = Validator.isUrl(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_URL;
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
                        validateRet = Validator.isChinese(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_CHINESE;
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
                        validateRet = Validator.isGeneralWithChinese(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_GENERAL_WITH_CHINESE;
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
                        validateRet = Validator.isMac(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_MAC;
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
                        validateRet = Validator.isPlateNumber(value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_PLATE_NUMBER;
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
                        validateRet = Validator.isMatchRegex(
                                DefPatternPool.SECURITY_PASSWORD, value);
                        if (!validateRet) {
                            ValidationMsg msg = ValidationMsg.EXCEPTION_IS_SECURITY_PASSWORD;
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
                    ValidationMsg msg = ValidationMsg.EXCEPTION_IS_MAX;
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
                    ValidationMsg msg = ValidationMsg.EXCEPTION_IS_MIN;
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

        ValidationUtil.verify(dictModel);
    }


    // ================

    private ValidationUtil(){}

}
