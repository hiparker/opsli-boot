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

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.exception.TokenException;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.msg.TokenMsg;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.List;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;


/**
 * 验证码
 *
 * @author parker
 * @date 2021年5月19日12:47:20
 */
@Component
@Order(UTIL_ORDER)
@Lazy(false)
public class CaptchaUtil {

    /** 验证码宽度 */
    private static final int CAPTCHA_WIDTH = 180;
    /** 验证码高度 */
    private static final int CAPTCHA_HEIGHT = 58;
    /** 验证码位数 */
    private static final int CAPTCHA_LEN = 4;
    /** 验证码策略 */
    private static final List<CaptchaStrategy> CAPTCHA_STRATEGY_LIST;

    /** 缓存前缀 */
    private static final String PREFIX = "temp:captcha:";
    /** 默认验证码保存 5 分钟 */
    private static final int TIME_OUT = 300;
    /** Redis插件 */
    private static RedisPlugin redisPlugin;

    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;

    static {
        CAPTCHA_STRATEGY_LIST = Lists.newArrayListWithCapacity(3);
        CAPTCHA_STRATEGY_LIST.add(new CaptchaStrategyBySpec());
        CAPTCHA_STRATEGY_LIST.add(new CaptchaStrategyByGif());
        CAPTCHA_STRATEGY_LIST.add(new CaptchaStrategyByArithmetic());
    }

    /**
     * 获得验证码
     *
     * @param uuid UUID
     */
    public static void createCaptcha(String uuid, OutputStream out) {
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if (StringUtils.isBlank(uuid)) {
            throw new RuntimeException("uuid不能为空");
        }

        // 随机生成验证码
        int randomInt = RandomUtil.randomInt(0, CAPTCHA_STRATEGY_LIST.size());

        // 获得验证码生成策略
        CaptchaStrategy captchaStrategy = CAPTCHA_STRATEGY_LIST.get(randomInt);

        // 生成验证码
        Captcha captcha = captchaStrategy.createCaptcha();

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(PREFIX + uuid);

        // 保存至缓存
        boolean ret = redisPlugin.put(cacheKey, captcha.text(), TIME_OUT);
        if(ret){
            // 输出
            captcha.out(out);
        }
    }

    /**
     * 校验验证码
     *
     * @param uuid UUID
     * @param code 验证码
     */
    public static void validate(String uuid, String code) {
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 判断UUID 是否为空
        if (StringUtils.isEmpty(uuid)) {
            throw new TokenException(TokenMsg.EXCEPTION_CAPTCHA_UUID_NULL);
        }

        // 判断 验证码是否为空
        if (StringUtils.isEmpty(code)) {
            throw new TokenException(TokenMsg.EXCEPTION_CAPTCHA_CODE_NULL);
        }

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(PREFIX + uuid);

        // 验证码
        String codeTemp = (String) redisPlugin.get(cacheKey);
        if (StringUtils.isEmpty(codeTemp)) {
            throw new TokenException(TokenMsg.EXCEPTION_CAPTCHA_NULL);
        }

        // 验证 验证码是否正确
        boolean captchaFlag = codeTemp.equalsIgnoreCase(code);
        if (!captchaFlag) {
            throw new TokenException(TokenMsg.EXCEPTION_CAPTCHA_ERROR);
        }
    }


    /**
     * 删除验证码
     *
     * @param uuid UUID
     * @return boolean
     */
    public static boolean delCaptcha(String uuid) {
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if (StringUtils.isEmpty(uuid)) {
            return false;
        }

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(PREFIX + uuid);

        //删除验证码
        return redisPlugin.del(cacheKey);
    }

    // ======================

    public interface CaptchaStrategy{

        /**
         * 生成验证码对象
         * @return Captcha
         */
        Captcha createCaptcha();

    }

    /**
     * 数字英文混合验证码 静态
     */
    private static class CaptchaStrategyBySpec implements CaptchaStrategy{
        @Override
        public Captcha createCaptcha() {
            // 生成验证码
            SpecCaptcha captcha = new SpecCaptcha(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, CAPTCHA_LEN);
            captcha.setCharType(Captcha.TYPE_DEFAULT);
            return captcha;
        }
    }

    /**
     * 数字英文混合验证码 动态
     */
    private static class CaptchaStrategyByGif implements CaptchaStrategy{
        @Override
        public Captcha createCaptcha() {
            // 生成验证码
            GifCaptcha captcha = new GifCaptcha(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, CAPTCHA_LEN);
            captcha.setCharType(Captcha.TYPE_DEFAULT);
            return captcha;
        }
    }

    /**
     * 算数验证码 动态
     */
    private static class CaptchaStrategyByArithmetic implements CaptchaStrategy{
        @Override
        public Captcha createCaptcha() {
            // 生成验证码
            return new ArithmeticCaptcha(CAPTCHA_WIDTH, CAPTCHA_HEIGHT);
        }
    }

    // ==========================

    /**
     * 初始化
     */
    @Autowired
    public void init(RedisPlugin redisPlugin) {
        CaptchaUtil.redisPlugin = redisPlugin;

        IS_INIT = true;
    }

}
