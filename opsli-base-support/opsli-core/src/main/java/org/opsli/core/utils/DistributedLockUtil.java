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
package org.opsli.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.msg.CoreMsg;
import org.opsli.plugins.redisson.RedissonLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * 分布式锁工具类
 *
 * @author parker
 * @date 2020-09-22 11:17
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class DistributedLockUtil {

    /** 锁有效时长为 默认10秒 */
    private static final int LEASE_TIME = 10;

    /** Redisson 分布式锁 */
    private static RedissonLock REDISSON_LOCK;

    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;

    /**
     * 分布式 加锁
     * @param lockName 锁名称
     * @return boolean
     */
    public static boolean lock(String lockName){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        boolean isLock = true;
        // 分布式上锁
        if(REDISSON_LOCK != null){
            // 缓存Key
            String cacheKey = CacheUtil.formatKey(lockName);
            isLock = REDISSON_LOCK.tryLock(cacheKey, LEASE_TIME);
        }
        return isLock;
    }

    /**
     * 分布式 释放锁
     * @param lockName 锁名称
     */
    public static void unlock(String lockName){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 释放锁
        if(REDISSON_LOCK != null){
            // 缓存Key
            String cacheKey = CacheUtil.formatKey(lockName);
            REDISSON_LOCK.unlockByThread(cacheKey);
        }
    }

    // =============

    /**
     * 初始化
     */
    @Autowired(required = false)
    public void init(RedissonLock redissonLock){
        DistributedLockUtil.REDISSON_LOCK = redissonLock;

        IS_INIT = true;
    }


}
