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
package org.opsli.plugins.redisson.conf;

import lombok.extern.slf4j.Slf4j;
import org.opsli.plugins.redisson.RedissonLock;
import org.opsli.plugins.redisson.RedissonManager;
import org.opsli.plugins.redisson.properties.RedissonProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Redisson自动化配置
 *
 * @author Parker
 * @date 2019/6/19 下午11:55
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
@ConditionalOnProperty(prefix = RedissonProperties.PROP_PREFIX, name = "enable", havingValue = "true")
public class RedissonConfig {

    @Bean
    @ConditionalOnMissingBean
    @Order(value = 2)
    public RedissonLock redissonLock(RedissonManager redissonManager) {
        RedissonLock redissonLock = new RedissonLock(redissonManager);
        log.info("[RedissonLock]组装完毕");
        return redissonLock;
    }

    @Bean
    @ConditionalOnMissingBean
    @Order(value = 1)
    public RedissonManager redissonManager(RedissonProperties redissonProperties) {
        RedissonManager redissonManager =
                new RedissonManager(redissonProperties);
        log.info("[RedissonManager]组装完毕,当前连接方式:" + redissonProperties.getType().getDesc() +
            ",连接地址:" + redissonProperties.getAddress());
        return redissonManager;
    }
}

