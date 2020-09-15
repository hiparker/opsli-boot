package org.opsli.plugins.redis.lock;

import lombok.Data;
import lombok.Value;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.redis.lock
 * @Author: Parker
 * @CreateTime: 2020-09-16 00:51
 * @Description: Redis 锁
 */
@Data
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
    private AtomicInteger atomicInteger = new AtomicInteger(1);

    /** 获得线程锁 */
    public int getThreadLock(){
        return atomicInteger.get();
    }

    /** 解除线程锁 */
    public int unThreadLock(){
        return atomicInteger.decrementAndGet();
    }

}
