package org.opsli.plugins.redisson.strategy.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.plugins.redisson.constant.GlobalConstant;
import org.opsli.plugins.redisson.properties.RedissonProperties;
import org.opsli.plugins.redisson.enums.RedissonType;
import org.opsli.plugins.redisson.strategy.RedissonConfigService;
import org.redisson.config.Config;

/**
 * 单机部署Redisson配置
 *
 * @author xub
 * @date 2019/6/19 下午10:04
 */
@Slf4j
public class StandaloneConfigImpl implements RedissonConfigService {

    @Override
    public RedissonType getType() {
        return RedissonType.STANDALONE;
    }


    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String redisAddr = GlobalConstant.REDIS_CONNECTION_PREFIX.getConstantValue() + address;
            config.useSingleServer().setAddress(redisAddr);
            config.useSingleServer().setDatabase(database);
            //密码可以为空
            if (StringUtils.isNotBlank(password)) {
                config.useSingleServer().setPassword(password);
            }
            log.info("初始化[单机部署]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("单机部署 Redisson init error", e);
        }
        return config;
    }
}
