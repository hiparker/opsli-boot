package org.opsli.plugins.redis.jsonserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * FastJson2JsonRedisSerializer
 *  Redis使用FastJson序列化
 *
 * @author Parker
 * @date 2021年6月1日13:57:02
 */
@Slf4j
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();
    private final Class<T> type;

    public FastJson2JsonRedisSerializer(Class<T> type) {
        this.type = type;
    }

    public FastJsonConfig getFastJsonConfig() {
        return fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        try {
            return JSON.toJSONBytesWithFastJsonConfig(
                    fastJsonConfig.getCharset(),
                    t,
                    fastJsonConfig.getSerializeConfig(),
                    fastJsonConfig.getSerializeFilters(),
                    fastJsonConfig.getDateFormat(),
                    JSON.DEFAULT_GENERATE_FEATURE,
                    fastJsonConfig.getSerializerFeatures()
            );
        } catch (Exception ex) {
            throw new SerializationException("Could not serialize: " + ex.getMessage(), ex);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return cast(JSON.parseObject(
                    bytes,
                    fastJsonConfig.getCharset(),
                    type,
                    fastJsonConfig.getParserConfig(),
                    fastJsonConfig.getParseProcess(),
                    JSON.DEFAULT_PARSER_FEATURE,
                    fastJsonConfig.getFeatures()
            ));
        } catch (Exception ex) {
            // 容错处理
            log.error(ex.getMessage(), ex);
            String str = new String(bytes, DEFAULT_CHARSET);
            return cast(str);
        }
    }

    /**
     * 欺骗编译器 强制转换
     * @param obj 对象
     * @return T
     */
    private T cast(Object obj){
        return (T) obj;
    }

}