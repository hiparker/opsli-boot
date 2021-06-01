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
package org.opsli.plugins.redis.conf;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.opsli.plugins.redis.jsonserializer.FastJson2JsonRedisSerializer;
import org.opsli.plugins.redis.scripts.RedisScriptCache;
import org.opsli.plugins.redis.scripts.enums.RedisScriptsEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Redis 配置类
 *
 * @author Parker
 * @date 2020-09-16 11:47
 */
@Slf4j
@Configuration
public class RedisPluginConfig {

    private static final FastJson2JsonRedisSerializer<?> FAST_JSON_REDIS_SERIALIZER = new FastJson2JsonRedisSerializer<>(Object.class);

    @Resource
    private LettuceConnectionFactory factory;

    /**
     * RedisTemplate配置
     * 序列化设置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // key采用String的序列化方式
        template.setKeySerializer(RedisSerializer.string());
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        // value序列化方式采用 json
        template.setValueSerializer(FAST_JSON_REDIS_SERIALIZER);
        // hash的value序列化方式采用 json
        template.setHashValueSerializer(FAST_JSON_REDIS_SERIALIZER);

        template.afterPropertiesSet();

        // 开启事务
        //template.setEnableTransactionSupport(true);

        return template;
    }


    /**
     * 加载脚本到缓存内
     *
     * 默认开启 全局乐观锁 一劳永逸
     *
     * @return RedisScriptCache
     */
    @Bean
    public RedisScriptCache loadScripts() {
        RedisScriptCache redisScriptCache = new RedisScriptCache();
        RedisScriptsEnum[] scriptEnums = RedisScriptsEnum.values();
        for (RedisScriptsEnum scriptEnum : scriptEnums) {
            String path = scriptEnum.getPath();

            try {
                ClassPathResource resource = new ClassPathResource(path);
                InputStream inputStream = resource.getInputStream();
                String read = IoUtil.read(inputStream, StandardCharsets.UTF_8);
                // 保存脚本到缓存中
                redisScriptCache.putScript(scriptEnum,read);
            }catch (Exception ignored){}
        }
        return redisScriptCache;
    }

}
