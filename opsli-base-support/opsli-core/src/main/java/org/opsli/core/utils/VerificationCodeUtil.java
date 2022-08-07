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

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opsli.common.constants.RedisConstants;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.UniqueStrGeneratorUtils;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.msg.TokenMsg;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 验证码工具类
 *
 * @author 周鹏程
 * @date 2022-07-28 5:55 PM
 **/
@Component
public class VerificationCodeUtil {

    /** 5分钟失效 */
    private static final int EXPIRED_MINUTE = 5;
    /** 凭证 10分钟失效 */
    private static final int CERTIFICATE_EXPIRED_MINUTE = 10;

    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;
    private static RedisPlugin redisPlugin;


    /**
     * 创建邮箱验证码
     * @param email 邮箱
     * @return VerificationCodeModel
     */
    public static VerificationCodeModel createEmailCode(String email, String type){
        return createCode(RedisConstants.PREFIX_TMP_EMAIL_CODE_NAME, email, type);
    }

    /**
     * 创建手机验证码
     * @param mobile 手机号
     * @return VerificationCodeModel
     */
    public static VerificationCodeModel createMobileCode(String mobile, String type){
        return createCode(RedisConstants.PREFIX_TMP_MOBILE_CODE_NAME, mobile, type);
    }

    /**
     * 创建验证码
     * @param cacheKey 缓存Key
     * @param principal 主键
     * @return VerificationCodeModel
     */
    private static VerificationCodeModel createCode(String cacheKey, String principal, String type){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(!StrUtil.isAllNotEmpty(principal, type)){
            // 参数异常
            throw new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_ARGS_NULL);
        }

        long currentTimeMillis = System.currentTimeMillis();

        // 缓存Key
        String cacheKeyTmp = CacheUtil.formatKey(cacheKey + type + ":" + principal);

        // 判断是否频繁生成
        String valueTmp = (String) redisPlugin.get(cacheKeyTmp);
        if(StrUtil.isNotEmpty(valueTmp)){
            String[] split = valueTmp.split("_");
            if(split.length == 2){
                long expiredDateTs = Long.parseLong(split[0]);
                // 获取开始时间
                Date createTime = DateUtil.offsetMinute(DateUtil.date(expiredDateTs), -EXPIRED_MINUTE);
                // 偏移40秒后时间
                Date offsetTime = DateUtil.offsetSecond(createTime, 30);
                // 如果当前时间 小于 偏移后时间 则不可再次生成验证码
                boolean isNotOk = currentTimeMillis < offsetTime.getTime();
                if(isNotOk){
                    // 验证码 生成过于频繁
                    throw new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_OFTEN);
                }
            }
        }

        // 失效时间
        DateTime expiredDate = DateUtil.offsetMinute(
                DateUtil.date(currentTimeMillis), EXPIRED_MINUTE);

        String randomNumbers = RandomUtil.randomNumbers(6);

        // 缓存值
        String value = expiredDate.getTime() + "_" + randomNumbers;

        // 相同的 验证码存在 一个 hash中
        redisPlugin.put(cacheKeyTmp, value, EXPIRED_MINUTE, TimeUnit.MINUTES);

        return VerificationCodeModel.builder()
                .verificationCode(randomNumbers)
                .expiredDate(expiredDate)
                .expiredMinute(EXPIRED_MINUTE)
                .build();
    }


    /**
     * 验证邮箱验证码（生成凭证）
     * @param email 主键
     * @param code 验证码
     * @return certificate
     */
    public static String checkEmailCodeToCreateCertificate(String email, String code, String type){
        return checkCode(RedisConstants.PREFIX_TMP_EMAIL_CODE_NAME, email, code, type, true);
    }

    /**
     * 验证手机验证码（生成凭证）
     * @param mobile 主键
     * @param code 验证码
     * @return certificate
     */
    public static String checkMobileCodeToCreateCertificate(String mobile, String code, String type){
        return checkCode(RedisConstants.PREFIX_TMP_MOBILE_CODE_NAME, mobile, code, type, true);
    }

    /**
     * 验证邮箱验证码
     * @param email 主键
     * @param code 验证码
     */
    public static void checkEmailCode(String email, String code, String type){
        checkCode(RedisConstants.PREFIX_TMP_EMAIL_CODE_NAME, email, code, type, false);
    }

    /**
     * 验证手机验证码
     * @param mobile 主键
     * @param code 验证码
     */
    public static void checkMobileCode(String mobile, String code, String type){
        checkCode(RedisConstants.PREFIX_TMP_MOBILE_CODE_NAME, mobile, code, type, false);
    }

    /**
     * 验证验证码
     * @param cacheKey 缓存Key
     * @param principal 主键
     * @param code 验证码
     * @param type 类型
     * @param isCreateCertificate 是否生成凭证
     * @return certificate
     */
    private static String checkCode(
            String cacheKey, String principal, String code, String type, boolean isCreateCertificate){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(!StrUtil.isAllNotEmpty(principal, code, type)){
            // 参数异常
            throw new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_ARGS_NULL);
        }

        long currentTimeMillis = System.currentTimeMillis();

        // 缓存Key
        String cacheKeyTmp = CacheUtil.formatKey(cacheKey + type + ":" + principal);

        // 缓存值
        String value = (String) redisPlugin.get(cacheKeyTmp);
        if(StrUtil.isEmpty(value)){
            // 验证码过期
            throw new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_NULL);
        }

        String[] split = value.split("_");
        if(split.length != 2){
            // 验证码过期
            throw new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_NULL);
        }

        if(!code.equals(split[1])){
            // 验证码不正确
            throw new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_ERROR);
        }

        long expiredDateTs = Long.parseLong(split[0]);
        boolean isExpired = currentTimeMillis > expiredDateTs;
        if(isExpired){
            // 验证码过期
            throw new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_NULL);
        }

        redisPlugin.del(cacheKeyTmp);

        // 判断是否生成凭证
        if(!isCreateCertificate){
            return null;
        }

        // 创建唯一数
        // 缓存Key
        Long increment = redisPlugin
                .increment(CacheUtil.formatKey(RedisConstants.PREFIX_TMP_VERIFICATION_CERTIFICATE_NUM_NAME));
        String certificate = UniqueStrGeneratorUtils.generator(increment);

        // 缓存Key
        String certificateCacheKeyTmp = CacheUtil.formatKey(
                RedisConstants.PREFIX_TMP_VERIFICATION_CERTIFICATE_NAME + certificate);
        redisPlugin.put(certificateCacheKeyTmp, "0", CERTIFICATE_EXPIRED_MINUTE, TimeUnit.MINUTES);
        return certificate;
    }


    /**
     * 验证验凭证
     * @param type 类型
     * @param certificate 凭证
     */
    public static void checkCertificate(String type, String certificate){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(!StrUtil.isAllNotEmpty(type, certificate)){
            // 参数异常
            throw new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_ARGS_NULL);
        }

        // 缓存Key
        String certificateCacheKeyTmp = CacheUtil.formatKey(
                RedisConstants.PREFIX_TMP_VERIFICATION_CERTIFICATE_NAME + certificate);

        Object value = redisPlugin.get(certificateCacheKeyTmp);
        if(value == null){
            // 凭证验证失败
            throw new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_CERTIFICATE_ERROR);
        }
        // 验证后不论成功还是失败 直接删除凭证
        redisPlugin.del(certificateCacheKeyTmp);
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VerificationCodeModel {

        /** 验证码 */
        private String verificationCode;

        /** 过期时间 */
        private Date expiredDate;

        /** 过期分钟 */
        private Integer expiredMinute;

    }

    @Autowired
    public void init(RedisPlugin redisPlugin){
        VerificationCodeUtil.redisPlugin = redisPlugin;
        IS_INIT = true;
    }

}
