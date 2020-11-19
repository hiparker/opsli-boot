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
 * @Author parker
 *
 * Redis 配置类
 *
 */
@Slf4j
@Configuration
public class RedisPluginConfig {

    private static final FastJsonRedisSerializer<Object> FAST_JSON_REDIS_SERIALIZER = new FastJsonRedisSerializer<>(Object.class);

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
     * @return
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
//                List<String> readList = Lists.newArrayList();
//                IoUtil.readLines(inputStream, StandardCharsets.UTF_8, readList);
//                StringBuilder stb = new StringBuilder();
//                for (String readLine : readList) {
//                    stb.append(readLine);
//                    stb.append("\n");
//                }
                String read = IoUtil.read(inputStream, StandardCharsets.UTF_8);
                // 保存脚本到缓存中
                redisScriptCache.putScript(scriptEnum,read);
            }catch (Exception ignored){}
        }


        /**
         * 暂时放弃这种写法 ， 如果把Lua脚本 直接写在Java中，可读性太低了
         * Lua 就放在 lua 文件夹下 IDEA装一个Lua插件 直接写Lua
         */

//        // 拿到state包下 实现了 SystemEventState 接口的,所有子类
//        Set<Class<?>> clazzSet = PackageUtil.listSubClazz(RedisPluginScript.class.getPackage().getName(),
//                true,
//                RedisPluginScript.class
//        );
//
//        for (Class<?> aClass : clazzSet) {
//            // 位运算 去除抽象类
//            if((aClass.getModifiers() & Modifier.ABSTRACT) != 0){
//                continue;
//            }
//
//            // 通过反射 加载所有的 脚本
//            try {
//                RedisPluginScript redisPluginScript = (RedisPluginScript) aClass.newInstance();
//                redisScriptCache.putScript(redisPluginScript);
//            } catch (Exception e) {
//                log.error(RedisMsg.EXCEPTION_REFLEX.getMessage());
//            }
//
//        }

        return redisScriptCache;
    }

}
