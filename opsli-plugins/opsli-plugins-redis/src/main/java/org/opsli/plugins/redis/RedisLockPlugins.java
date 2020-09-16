package org.opsli.plugins.redis;

import org.opsli.plugins.redis.lock.RedisLock;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.redis
 * @Author: Parker
 * @CreateTime: 2020-09-16 11:47
 * @Description: Redis 锁 接口
 */
public interface RedisLockPlugins {

    /**
     * 尝试锁
     * @param redisLock redis锁参数
     * @return
     */
    RedisLock tryLock(RedisLock redisLock);


    /**
     * 释放锁
     * @param redisLock redis锁参数
     * @return
     */
    boolean unLock(RedisLock redisLock);
}
