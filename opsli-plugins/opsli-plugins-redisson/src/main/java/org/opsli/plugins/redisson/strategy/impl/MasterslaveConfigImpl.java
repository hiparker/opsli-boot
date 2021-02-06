package org.opsli.plugins.redisson.strategy.impl;


import cn.hutool.core.convert.Convert;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.plugins.redisson.constant.GlobalConstant;
import org.opsli.plugins.redisson.properties.RedissonProperties;
import org.opsli.plugins.redisson.enums.RedissonType;
import org.opsli.plugins.redisson.strategy.RedissonConfigService;
import org.redisson.config.Config;

import java.util.List;

/**
 * 主从部署Redisson配置
 *       连接方式:  主节点,子节点,子节点
 *         格式为:  127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381
 * @author xub
 * @date 2019/6/19 下午9:21
 */

@Slf4j
public class MasterslaveConfigImpl implements RedissonConfigService {

    @Override
    public RedissonType getType() {
        return RedissonType.MASTER_SLAVE;
    }

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String[] addrTokens = address.split(",");
            String masterNodeAddr = addrTokens[0];
            //设置主节点ip
            config.useMasterSlaveServers().setMasterAddress(masterNodeAddr);
            if (StringUtils.isNotBlank(password)) {
                config.useMasterSlaveServers().setPassword(password);
            }
            config.useMasterSlaveServers().setDatabase(database);
            //设置从节点，移除第一个节点，默认第一个为主节点
            List<String> slaveList = Lists.newArrayList();
            for (String addrToken : addrTokens) {
                slaveList.add(GlobalConstant.REDIS_CONNECTION_PREFIX.getConstantValue() + addrToken);
            }
            slaveList.remove(0);

            config.useMasterSlaveServers().addSlaveAddress(
                    Convert.toStrArray(slaveList));
            log.info("初始化[主从部署]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("主从部署 Redisson init error", e);
            e.printStackTrace();
        }
        return config;
    }

}
