package org.opsli.plugins.redis.conf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.utils.PackageUtil;
import org.opsli.plugins.redis.msg.RedisMsg;
import org.opsli.plugins.redis.scripts.RedisPluginScript;
import org.opsli.plugins.redis.scripts.RedisScriptCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * @Author parker
 *
 * Redis 配置类
 *
 */
@Slf4j
@Configuration
public class RedisPluginConfig {

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
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        // 开启事务
        template.setEnableTransactionSupport(true);
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

        // 拿到state包下 实现了 SystemEventState 接口的,所有子类
        Set<Class<?>> clazzSet = PackageUtil.listSubClazz(RedisPluginScript.class.getPackage().getName(),
                true,
                RedisPluginScript.class
        );

        for (Class<?> aClass : clazzSet) {
            // 位运算 去除抽象类
            if((aClass.getModifiers() & Modifier.ABSTRACT) != 0){
                continue;
            }

            // 通过反射 加载所有的 脚本
            try {
                RedisPluginScript redisPluginScript = (RedisPluginScript) aClass.newInstance();
                redisScriptCache.putScript(redisPluginScript);
            } catch (Exception e) {
                log.error(RedisMsg.EXCEPTION_REFLEX.getMessage());
            }

        }

        return redisScriptCache;
    }

}
