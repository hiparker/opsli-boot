package org.opsli.plugins.redis.conf;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.opsli.plugins.redis.scripts.RedisScriptCache;
import org.opsli.plugins.redis.scripts.enums.RedisScriptsEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;

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
            InputStream resourceAsStream = null;
            try {
                // IO流 读取lua脚本 存入缓存当中 提高访问效率 避免每次使用脚本还需要再开启IO流
                StringBuffer stb = new StringBuffer();
                resourceAsStream= RedisPluginConfig.class.getResourceAsStream(path);
                BufferedReader br = IoUtil.getUtf8Reader(resourceAsStream);
                String line = null;
                while((line = br.readLine())!=null){
                    stb.append(line);
                    stb.append("\n");
                }
                // 保存脚本到缓存中
                redisScriptCache.putScript(scriptEnum,stb.toString());
                br.close();
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }finally {
                IoUtil.close(resourceAsStream);
            }
        }


//
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
