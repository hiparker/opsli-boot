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
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.user.UserApi;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.msgs.OrgMsgFactory;
import org.opsli.core.msg.CoreMsg;
import org.opsli.plugins.redis.RedisPlugin;
import org.opsli.plugins.redisson.RedissonLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.utils
 * @Author: Parker
 * @CreateTime: 2020-09-19 20:03
 * @Description: 分布式锁工具类
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

    /**
     * 分布式 加锁
     * @param lockName 锁名称
     * @return boolean
     */
    public static boolean lock(String lockName){
        boolean isLock = true;
        // 分布式上锁
        if(REDISSON_LOCK != null){
            isLock = REDISSON_LOCK.tryLock(CacheUtil.getPrefixName() + lockName, LEASE_TIME);
        }
        return isLock;
    }

    /**
     * 分布式 释放锁
     * @param lockName 锁名称
     */
    public static void unlock(String lockName){
        // 释放锁
        if(REDISSON_LOCK != null){
            REDISSON_LOCK.unlockByThread(CacheUtil.getPrefixName() + lockName);
        }
    }

    // =============

    /**
     * 初始化
     * @param redissonLock 分布式锁
     */
    @Autowired
    public void init(RedissonLock redissonLock){
        DistributedLockUtil.REDISSON_LOCK = redissonLock;
    }


}
