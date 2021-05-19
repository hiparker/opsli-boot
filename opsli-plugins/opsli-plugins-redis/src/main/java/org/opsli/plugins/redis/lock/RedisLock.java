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
package org.opsli.plugins.redis.lock;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Redis 锁
 *
 * @author Parker
 * @date 2020-09-16 11:47
 */
public class RedisLock {

    private static final String LOCK_PREFIX = "lock:";

    /** 锁名称 */
    private String lockName;

    /** 尝试获取锁等待时间 */
    private Long acquireTimeOut;

    /** 锁有效时间 */
    private Long lockTimeOut;

    /** 锁凭证 */
    private String identifier;

    /** 线程锁 */
    private final AtomicInteger atomicInteger;

    /**
     * 构造函数
     */
    public RedisLock() {
        // 初始化锁
        atomicInteger = new AtomicInteger(1);
    }

    /**
     * 构造函数
     */
    public RedisLock(String lockName, Long acquireTimeOut, Long lockTimeOut, String identifier) {
        this.lockName = LOCK_PREFIX + lockName;
        this.acquireTimeOut = acquireTimeOut;
        this.lockTimeOut = lockTimeOut;
        this.identifier = identifier;
        // 初始化锁
        atomicInteger = new AtomicInteger(1);
    }

    /** 获得线程锁 */
    public int threadGetLock(){
        return atomicInteger.get();
    }

    /** 解除线程锁 */
    public int threadUnLock(){
        return atomicInteger.decrementAndGet();
    }



    // ==========================================================


    public String getLockName() {
        return lockName;
    }

    public RedisLock setLockName(String lockName) {
        this.lockName = LOCK_PREFIX + lockName;
        return this;
    }

    public Long getAcquireTimeOut() {
        return acquireTimeOut;
    }

    public RedisLock setAcquireTimeOut(Long acquireTimeOut) {
        this.acquireTimeOut = acquireTimeOut;
        return this;
    }

    public Long getLockTimeOut() {
        return lockTimeOut;
    }

    public RedisLock setLockTimeOut(Long lockTimeOut) {
        this.lockTimeOut = lockTimeOut;
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public RedisLock setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }


}
