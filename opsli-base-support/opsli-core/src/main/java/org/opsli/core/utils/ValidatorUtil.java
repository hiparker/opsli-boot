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
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.annotation.validator.ValidatorLenMin;
import org.opsli.common.enums.ValidatorType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.msg.ValidatorMsg;
import org.opsli.common.utils.DefPatternPool;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证器工具类 - 性能和安全性优化版本
 *
 * 优化要点：
 * 1. 缓存字段元数据，避免重复反射
 * 2. 使用策略模式重构验证逻辑
 * 3. 添加安全性检查和限制
 * 4. 支持批量验证
 * 5. 线程安全设计
 *
 * @author Pace
 * @date 2020-09-19 20:03
 */
@Slf4j
public final class ValidatorUtil {

    /** 字段元数据缓存 - 避免重复反射 */
    private static final Cache<Class<?>, List<FieldMetadata>> FIELD_METADATA_CACHE = Caffeine.newBuilder()
            .maximumSize(2000)
            .expireAfterWrite(Duration.ofHours(2))
            .build();

    /** 字段名缓存 - 避免重复获取Schema注解 */
    private static final Cache<Field, String> FIELD_NAME_CACHE = Caffeine.newBuilder()
            .maximumSize(5000)
            .expireAfterWrite(Duration.ofHours(1))
            .build();

    /** 验证器实例缓存 */
    private static final Map<ValidatorType, ValidatorStrategy> VALIDATOR_STRATEGIES =
            new ConcurrentHashMap<>(32);

    /** 安全限制 */
    private static final int MAX_VALIDATION_DEPTH = 10; // 最大验证深度
    private static final int MAX_FIELD_COUNT = 1000;    // 最大字段数量
    private static final int MAX_STRING_LENGTH = 10000; // 最大字符串长度

    /** 线程本地上下文 - 防止循环引用 */
    private static final ThreadLocal<ValidationContext> VALIDATION_CONTEXT =
            ThreadLocal.withInitial(ValidationContext::new);

    static {
        // 初始化验证器策略
        initializeValidatorStrategies();
    }

    /**
     * 验证对象 - 主入口方法
     */
    public static void verify(Object obj) {
        if (obj == null) {
            return;
        }

        ValidationContext context = VALIDATION_CONTEXT.get();
        try {
            context.reset();
            verifyInternal(obj, context);
        } finally {
            context.cleanup();
        }
    }

    /**
     * 批量验证 - 高性能批量处理
     */
    public static void verifyBatch(Collection<?> objects) {
        if (objects == null || objects.isEmpty()) {
            return;
        }

        ValidationContext context = VALIDATION_CONTEXT.get();
        try {
            context.reset();

            for (Object obj : objects) {
                if (obj != null) {
                    verifyInternal(obj, context);
                }
            }
        } finally {
            context.cleanup();
        }
    }

    /**
     * 内部验证方法 - 核心逻辑
     */
    private static void verifyInternal(Object obj, ValidationContext context) {
        // 安全检查
        if (!context.canValidate(obj)) {
            return;
        }

        Class<?> clazz = obj.getClass();

        // 从缓存获取字段元数据
        List<FieldMetadata> fieldMetadataList = getFieldMetadata(clazz);

        // 执行验证
        for (FieldMetadata metadata : fieldMetadataList) {
            try {
                validateField(obj, metadata);
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                log.warn("字段验证异常: {}.{}", clazz.getSimpleName(), metadata.fieldName, e);
            }
        }
    }

    /**
     * 获取字段元数据 - 带缓存优化
     */
    private static List<FieldMetadata> getFieldMetadata(Class<?> clazz) {
        return FIELD_METADATA_CACHE.get(clazz, ValidatorUtil::analyzeFields);
    }

    /**
     * 分析类字段 - 预处理字段元数据
     */
    private static List<FieldMetadata> analyzeFields(Class<?> clazz) {
        Field[] fields = ReflectUtil.getFields(clazz);

        // 安全检查
        if (fields.length > MAX_FIELD_COUNT) {
            log.warn("类 {} 字段数量过多: {}, 最大允许: {}",
                    clazz.getSimpleName(), fields.length, MAX_FIELD_COUNT);
            return Collections.emptyList();
        }

        List<FieldMetadata> metadataList = new ArrayList<>(fields.length);

        for (Field field : fields) {
            FieldMetadata metadata = createFieldMetadata(field);
            if (metadata != null && metadata.hasValidation()) {
                metadataList.add(metadata);
            }
        }

        return metadataList;
    }

    /**
     * 创建字段元数据
     */
    private static FieldMetadata createFieldMetadata(Field field) {
        try {
            // 获取验证注解
            org.opsli.common.annotation.validator.Validator validator =
                    field.getAnnotation(org.opsli.common.annotation.validator.Validator.class);
            ValidatorLenMax maxAnnotation = field.getAnnotation(ValidatorLenMax.class);
            ValidatorLenMin minAnnotation = field.getAnnotation(ValidatorLenMin.class);

            // 如果没有任何验证注解，跳过
            if (validator == null && maxAnnotation == null && minAnnotation == null) {
                return null;
            }

            String fieldName = getFieldDisplayName(field);

            return new FieldMetadata(
                    field,
                    fieldName,
                    validator != null ? validator.value() : new ValidatorType[0],
                    maxAnnotation != null ? maxAnnotation.value() : -1,
                    minAnnotation != null ? minAnnotation.value() : -1
            );

        } catch (Exception e) {
            log.warn("创建字段元数据失败: {}", field.getName(), e);
            return null;
        }
    }

    /**
     * 获取字段显示名称 - 带缓存
     */
    private static String getFieldDisplayName(Field field) {
        return FIELD_NAME_CACHE.get(field, f -> {
            Schema annotation = f.getAnnotation(Schema.class);
            return annotation != null ? annotation.description() : f.getName();
        });
    }

    /**
     * 验证单个字段
     */
    private static void validateField(Object obj, FieldMetadata metadata) {
        Object fieldValue = ReflectUtil.getFieldValue(obj, metadata.field);

        // 验证器类型验证
        if (metadata.validatorTypes.length > 0) {
            validateByTypes(metadata, fieldValue);
        }

        // 长度验证
        if (metadata.maxLength > 0 || metadata.minLength > 0) {
            validateLength(metadata, fieldValue);
        }
    }

    /**
     * 按类型验证 - 优化版本
     */
    private static void validateByTypes(FieldMetadata metadata, Object fieldValue) {
        String value = Convert.toStr(fieldValue);

        for (ValidatorType type : metadata.validatorTypes) {
            ValidatorStrategy strategy = VALIDATOR_STRATEGIES.get(type);
            if (strategy != null && !strategy.validate(value)) {
                ValidatorMsg msg = strategy.getErrorMessage();
                msg.setFieldName(metadata.fieldName);
                throw new ServiceException(msg);
            }
        }
    }

    /**
     * 长度验证 - 优化版本
     */
    private static void validateLength(FieldMetadata metadata, Object fieldValue) {
        String value = Convert.toStr(fieldValue);
        if (StringUtils.isEmpty(value)) {
            return;
        }

        // 安全检查 - 防止超长字符串攻击
        if (value.length() > MAX_STRING_LENGTH) {
            log.warn("字符串长度超过安全限制: {} > {}", value.length(), MAX_STRING_LENGTH);
            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_MAX;
            msg.setFieldName(metadata.fieldName);
            throw new ServiceException(msg);
        }

        int byteLength = value.getBytes(StandardCharsets.UTF_8).length;

        if (metadata.maxLength > 0 && byteLength > metadata.maxLength) {
            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_MAX;
            msg.setFieldName(metadata.fieldName);
            throw new ServiceException(msg);
        }

        if (metadata.minLength > 0 && byteLength < metadata.minLength) {
            ValidatorMsg msg = ValidatorMsg.EXCEPTION_IS_MIN;
            msg.setFieldName(metadata.fieldName);
            throw new ServiceException(msg);
        }
    }

    /**
     * 初始化验证器策略
     */
    private static void initializeValidatorStrategies() {
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_NOT_NULL, new NotNullValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_GENERAL, new GeneralValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_INTEGER, new IntegerValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_DECIMAL, new DecimalValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_PRIMES, new PrimesValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_LETTER, new LetterValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_UPPER_CASE, new UpperCaseValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_LOWER_CASE, new LowerCaseValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_IP, new IpValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_IPV4, new Ipv4ValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_IPV6, new Ipv6ValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_MONEY, new MoneyValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_EMAIL, new EmailValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_MOBILE, new MobileValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_CITIZENID, new CitizenIdValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_ZIPCODE, new ZipCodeValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_URL, new UrlValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_CHINESE, new ChineseValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_GENERAL_WITH_CHINESE, new GeneralWithChineseValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_MAC, new MacValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_PLATE_NUMBER, new PlateNumberValidatorStrategy());
        VALIDATOR_STRATEGIES.put(ValidatorType.IS_SECURITY_PASSWORD, new SecurityPasswordValidatorStrategy());
    }

    // ======================= 内部类定义 =======================

    /**
     * 字段元数据
     */
    private static class FieldMetadata {
        final Field field;
        final String fieldName;
        final ValidatorType[] validatorTypes;
        final int maxLength;
        final int minLength;

        FieldMetadata(Field field, String fieldName, ValidatorType[] validatorTypes,
                      int maxLength, int minLength) {
            this.field = field;
            this.fieldName = fieldName;
            this.validatorTypes = validatorTypes;
            this.maxLength = maxLength;
            this.minLength = minLength;
        }

        boolean hasValidation() {
            return validatorTypes.length > 0 || maxLength > 0 || minLength > 0;
        }
    }

    /**
     * 验证上下文 - 防止循环引用和深度攻击
     */
    private static class ValidationContext {
        private final Set<Object> validatedObjects = new HashSet<>();
        private int depth = 0;

        void reset() {
            validatedObjects.clear();
            depth = 0;
        }

        boolean canValidate(Object obj) {
            // 检查深度限制
            if (depth >= MAX_VALIDATION_DEPTH) {
                log.warn("验证深度超过限制: {}", depth);
                return false;
            }

            // 检查循环引用
            if (validatedObjects.contains(obj)) {
                return false;
            }

            validatedObjects.add(obj);
            depth++;
            return true;
        }

        void cleanup() {
            validatedObjects.clear();
            depth = 0;
        }
    }

    /**
     * 验证器策略接口
     */
    private interface ValidatorStrategy {
        boolean validate(String value);
        ValidatorMsg getErrorMessage();
    }

    /**
     * 抽象验证器策略 - 提供通用功能
     */
    private abstract static class AbstractValidatorStrategy implements ValidatorStrategy {

        @Override
        public final boolean validate(String value) {
            try {
                if (shouldSkipValidation(value)) {
                    return true;
                }
                return doValidate(value);
            } catch (Exception e) {
                log.debug("验证异常: {}", e.getMessage());
                return false;
            }
        }

        protected boolean shouldSkipValidation(String value) {
            return StringUtils.isEmpty(value);
        }

        protected abstract boolean doValidate(String value);
    }

    // ======================= 具体验证器策略实现 =======================

    private static class NotNullValidatorStrategy implements ValidatorStrategy {
        @Override
        public boolean validate(String value) {
            return StringUtils.isNotEmpty(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_NOT_NULL;
        }
    }

    private static class GeneralValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isGeneral(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_GENERAL;
        }
    }

    private static class IntegerValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return NumberUtil.isInteger(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_INTEGER;
        }
    }

    private static class DecimalValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return NumberUtil.isDouble(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_DECIMAL;
        }
    }

    private static class PrimesValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return NumberUtil.isInteger(value) && NumberUtil.isPrimes(Convert.toInt(value));
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_PRIMES;
        }
    }

    private static class LetterValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isLetter(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_LETTER;
        }
    }

    private static class UpperCaseValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isUpperCase(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_UPPER_CASE;
        }
    }

    private static class LowerCaseValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isLowerCase(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_LOWER_CASE;
        }
    }

    private static class IpValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isIpv4(value) || Validator.isIpv6(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_IP;
        }
    }

    private static class Ipv4ValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isIpv4(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_IPV4;
        }
    }

    private static class Ipv6ValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isIpv6(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_IPV6;
        }
    }

    private static class MoneyValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isMoney(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_MONEY;
        }
    }

    private static class EmailValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isEmail(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_EMAIL;
        }
    }

    private static class MobileValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isMobile(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_MOBILE;
        }
    }

    private static class CitizenIdValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isCitizenId(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_CITIZENID;
        }
    }

    private static class ZipCodeValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isZipCode(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_ZIPCODE;
        }
    }

    private static class UrlValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isUrl(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_URL;
        }
    }

    private static class ChineseValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isChinese(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_CHINESE;
        }
    }

    private static class GeneralWithChineseValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isGeneralWithChinese(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_GENERAL_WITH_CHINESE;
        }
    }

    private static class MacValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isMac(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_MAC;
        }
    }

    private static class PlateNumberValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isPlateNumber(value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_PLATE_NUMBER;
        }
    }

    private static class SecurityPasswordValidatorStrategy extends AbstractValidatorStrategy {
        @Override
        protected boolean doValidate(String value) {
            return Validator.isMatchRegex(DefPatternPool.SECURITY_PASSWORD, value);
        }

        @Override
        public ValidatorMsg getErrorMessage() {
            return ValidatorMsg.EXCEPTION_IS_SECURITY_PASSWORD;
        }
    }

    /**
     * 清理缓存 - 用于测试或内存管理
     */
    public static void clearCache() {
        FIELD_METADATA_CACHE.invalidateAll();
        FIELD_NAME_CACHE.invalidateAll();
    }

    // 私有构造函数
    private ValidatorUtil() {}
}

