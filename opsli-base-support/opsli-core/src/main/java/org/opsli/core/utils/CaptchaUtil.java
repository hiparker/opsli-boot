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

import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.constants.CacheConstants;
import org.opsli.common.exception.TokenException;
import org.opsli.common.utils.Props;
import org.opsli.core.msg.TokenMsg;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * 验证码
 *
 * @author parker
 * @since 2.0.0 2018-02-10
 */
@Component
@Order(UTIL_ORDER)
@Lazy(false)
public class CaptchaUtil{

    /** 缓存前缀 */
    private static final String PREFIX = "temp:captcha:";
    /** 默认验证码保存 5 分钟 */
    private static final int TIME_OUT = 300;
    /** Redis插件 */
    private static RedisPlugin redisPlugin;
    /** 谷歌验证码 */
    private static  Producer producer;
    /** 热点数据前缀 */
    public static final String PREFIX_NAME;
    static{
        // 缓存前缀
        Props props = new Props("application.yaml");
        PREFIX_NAME = props.getStr("spring.cache-conf.prefix", CacheConstants.PREFIX_NAME) + ":";
    }

    /**
     * 获得验证码
     * @param uuid
     * @return
     */
    public static BufferedImage getCaptcha(String uuid) {
        if(StringUtils.isBlank(uuid)){
            throw new RuntimeException("uuid不能为空");
        }

        //生成文字验证码
        String code = producer.createText();

        boolean ret = redisPlugin.put(PREFIX_NAME + PREFIX + uuid, code, TIME_OUT);

        if(ret){
            return producer.createImage(code);
        }
        return null;
    }

    /**
     * 校验验证码
     * @param uuid
     * @param code
     * @return
     */
    public static void validate(String uuid, String code) {
        // 判断UUID 是否为空
        if(StringUtils.isEmpty(uuid)){
            throw new TokenException(TokenMsg.EXCEPTION_CAPTCHA_UUID_NULL);
        }

        // 判断 验证码是否为空
        if(StringUtils.isEmpty(code)){
            throw new TokenException(TokenMsg.EXCEPTION_CAPTCHA_CODE_NULL);
        }

        // 验证码
        String codeTemp = (String) redisPlugin.get(PREFIX_NAME + PREFIX + uuid);
        if(StringUtils.isEmpty(codeTemp)){
            throw new TokenException(TokenMsg.EXCEPTION_CAPTCHA_NULL);
        }

        // 验证 验证码是否正确
        boolean captchaFlag = codeTemp.equalsIgnoreCase(code);
        if(!captchaFlag){
            throw new TokenException(TokenMsg.EXCEPTION_CAPTCHA_ERROR);
        }
    }


    /**
     * 删除验证码
     * @param uuid
     * @return
     */
    public static boolean delCaptcha(String uuid) {
        if(StringUtils.isEmpty(uuid)){
            return false;
        }

        //删除验证码
        return redisPlugin.del(PREFIX_NAME + PREFIX + uuid);
    }



    // ==========================

    @Autowired
    public  void setRedisPlugin(RedisPlugin redisPlugin) {
        CaptchaUtil.redisPlugin = redisPlugin;
    }

    @Autowired
    public  void setProducer(Producer producer) {
        CaptchaUtil.producer = producer;
    }
}
