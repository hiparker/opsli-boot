package org.opsli.plugins.redisson.strategy;


import org.opsli.plugins.redisson.properties.RedissonProperties;
import org.opsli.plugins.redisson.enums.RedissonType;
import org.redisson.config.Config;

/**
 * Redisson配置构建接口
 *
 * @author xub
 * @date 2019/6/20 下午3:35
 */
public interface RedissonConfigService {

    /**
     * 获得类型
     * @return type
     */
    RedissonType getType();

    /**
     * 根据不同的Redis配置策略创建对应的Config
     * @param redissonProperties
     * @return Config
     */
    Config createRedissonConfig(RedissonProperties redissonProperties);
}
