package org.opsli.plugins.redis.lock;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.redis.lock
 * @Author: Parker
 * @CreateTime: 2020-09-16 00:51
 * @Description: Redis 锁
 */
public class RedisLock {

    /** 锁名称 */
    private String lockName;

    /** 尝试获取锁等待时间 */
    private Long acquireTimeOut;

    /** 锁有效时间 */
    private Long lockTimeOut;

    /** 锁凭证 */
    private String identifier;

    /** 线程锁 */
    private AtomicInteger atomicInteger;



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
        this.lockName = lockName;
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
        this.lockName = lockName;
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
