package org.opsli.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

/**
 * 验证码
 *
 * @author parker
 * @since 2.0.0 2018-02-10
 */
@Component
public class CaptchaUtil{

    /** 缓存前缀 */
    private static final String PREFIX = "opsli:temp:captcha:";
    /** 默认验证码保存 5 分钟 */
    private static final int TIME_OUT = 300;
    /** Redis插件 */
    private static RedisPlugin redisPlugin;
    /** 谷歌验证码 */
    private static  Producer producer;

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

        boolean ret = redisPlugin.put(PREFIX + uuid, code, TIME_OUT);

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
    public static boolean validate(String uuid, String code) {
        if(StringUtils.isEmpty(uuid)) return false;

        // 验证码
        String codeTemp = (String) redisPlugin.get(PREFIX + uuid);

        if(StringUtils.isEmpty(codeTemp)){
            return false;
        }

        //删除验证码
        redisPlugin.del(PREFIX + uuid);

        return codeTemp.equalsIgnoreCase(code);
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
