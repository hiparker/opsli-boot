package org.opsli.plugins.redisson;

import cn.hutool.core.util.ClassUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.opsli.plugins.redisson.properties.RedissonProperties;
import org.opsli.plugins.redisson.enums.RedissonType;
import org.opsli.plugins.redisson.strategy.RedissonConfigService;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;


/**
 *
 * Redisson核心配置，用于提供初始化的redisson实例
 *
 * 参考地址：https://github.com/yudiandemingzi/spring-boot-distributed-redisson
 * @author xub
 * @date 2019/6/19 下午10:16
 */
@Slf4j
public class RedissonManager {

    private final RedissonClient redisson;

    public RedissonManager(RedissonProperties redissonProperties) {
        try {
            //通过不同部署方式获得不同config实体
            Config config = RedissonConfigFactory.getInstance().createConfig(redissonProperties);
            redisson = Redisson.create(config);
        } catch (Exception e) {
            log.error("Redisson 初始化异常", e);
            throw new IllegalArgumentException("请输入正确的配置," +
                    "connectionType必须在 standalone/sentinel/cluster/masterslave");
        }
    }

    public RedissonClient getRedisson() {
        return redisson;
    }

    /**
     * Redisson连接方式配置工厂
     * 双重检查锁
     */
    static class RedissonConfigFactory {

        /** 策略集合 */
        private final Map<RedissonType, RedissonConfigService> strategyMap = Maps.newHashMap();

        private RedissonConfigFactory() {
            Set<Class<?>> clazzSet = ClassUtil.scanPackageBySuper(
                    RedissonConfigService.class.getPackage().getName()+".impl",
                    RedissonConfigService.class
            );
            for (Class<?> aClass : clazzSet) {
                // 位运算 去除抽象类
                if((aClass.getModifiers() & Modifier.ABSTRACT) != 0){
                    continue;
                }

                try {
                    Object obj = aClass.newInstance();
                    RedissonConfigService handler = (RedissonConfigService) obj;
                    // 加入集合
                    strategyMap.put(handler.getType(),handler);
                } catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }
        }

        private static volatile RedissonConfigFactory factory = null;

        public static RedissonConfigFactory getInstance() {
            if (factory == null) {
                synchronized (Object.class) {
                    if (factory == null) {
                        factory = new RedissonConfigFactory();
                    }
                }
            }
            return factory;
        }


        /**
         * 根据连接类型获取对应连接方式的配置,基于策略模式
         *
         * @param redissonProperties redis连接信息
         * @return Config
         */
        Config createConfig(RedissonProperties redissonProperties) {
            Preconditions.checkNotNull(redissonProperties);
            Preconditions.checkNotNull(redissonProperties.getAddress(), "redisson.lock.server.address 不能为空!");
            Preconditions.checkNotNull(redissonProperties.getType().getType(), "redisson.lock.server.password 不能为空！");
            String connectionType = redissonProperties.getType().getType();
            //声明配置上下文
            RedissonConfigService redissonConfigService = strategyMap.get(redissonProperties.getType());
            if (redissonConfigService == null){
                throw new IllegalArgumentException("创建Redisson连接Config失败！当前连接方式:" + connectionType);
            }
            return redissonConfigService.createRedissonConfig(redissonProperties);
        }
    }

}


