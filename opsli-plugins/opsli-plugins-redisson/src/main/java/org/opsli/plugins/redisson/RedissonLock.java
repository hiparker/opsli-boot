package org.opsli.plugins.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 针对源码Redisson进行一层封装
 *
 * 参考地址：https://github.com/yudiandemingzi/spring-boot-distributed-redisson
 * @author xub
 * @date 2019/6/19 下午10:26
 */
@Slf4j
public class RedissonLock {

    private static final String PREFIX = "lock:";

    private RedissonManager redissonManager;
    private RedissonClient redisson;


    public RedissonLock(RedissonManager redissonManager) {
        this.redissonManager = redissonManager;
        this.redisson = redissonManager.getRedisson();
    }

    public RedissonLock() {}

    /**
     * 加锁操作 （设置锁的有效时间）
     * @param lockName 锁名称
     * @param leaseTime  锁有效时间
     */
    public void lock(String lockName, long leaseTime) {
        RLock rLock = redisson.getLock(PREFIX + lockName);
        rLock.lock(leaseTime,TimeUnit.SECONDS);
    }

    /**
     * 加锁操作 (锁有效时间采用默认时间30秒）
     * @param lockName 锁名称
     */
    public void lock(String lockName) {
        RLock rLock = redisson.getLock(PREFIX + lockName);
        rLock.lock();
    }

    /**
     * 加锁操作(tryLock锁，没有等待时间）
     * @param lockName  锁名称
     * @param leaseTime 锁有效时间
     */
    public boolean tryLock(String lockName, long leaseTime) {

        RLock rLock = redisson.getLock(PREFIX + lockName);
        boolean getLock;
        try {
            getLock = rLock.tryLock( leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("获取Redisson分布式锁[异常]，lockName=" + lockName, e);
            e.printStackTrace();
            return false;
        }
        return getLock;
    }

    /**
     * 加锁操作(tryLock锁，有等待时间）
     * @param lockName   锁名称
     * @param leaseTime  锁有效时间
     * @param waitTime   等待时间
     */
    public boolean tryLock(String lockName, long leaseTime, long waitTime) {
        RLock rLock = redisson.getLock(PREFIX + lockName);
        boolean getLock;
        try {
            getLock = rLock.tryLock( waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("获取Redisson分布式锁[异常]，lockName=" + lockName, e);
            e.printStackTrace();
            return false;
        }
        return getLock;
    }

    /**
     * 解锁
     * @param lockName  锁名称
     */
    public void unlock(String lockName) {
        redisson.getLock(PREFIX + lockName).unlock();
    }

    /**
     * 解锁 当前线程
     * @param lockName  锁名称
     */
    public void unlockByThread(String lockName) {
        // 是否是当前线程
        if(this.isHeldByCurrentThread(lockName)){
            // 释放锁
            this.unlock(lockName);
        }
    }

    /**
     * 判断该锁是否已经被线程持有
     * @param lockName  锁名称
     */
    public boolean isLock(String lockName) {
        RLock rLock = redisson.getLock(PREFIX + lockName);
        if(null == rLock){
            return false;
        }
        return rLock.isLocked();
    }


    /**
     * 判断该线程是否持有当前锁
     * @param lockName  锁名称
     */
    public boolean isHeldByCurrentThread(String lockName) {
        RLock rLock = redisson.getLock(PREFIX + lockName);
        if(null == rLock){
            return false;
        }
        return rLock.isHeldByCurrentThread();
    }

    // ======================

    public RedissonManager getRedissonManager() {
        return redissonManager;
    }

    public void setRedissonManager(RedissonManager redissonManager) {
        this.redissonManager = redissonManager;
    }
}
