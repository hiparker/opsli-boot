package org.opsli.plugins.redisson.annotation;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;


import org.opsli.plugins.redisson.RedissonLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Redisson分布式锁注解解析器
 *
 * @author xub
 * @date 2019/6/20 下午9:34
 */
@Aspect
@Component
@Slf4j
public class DistributedLockHandler {

    @Autowired(required = false)
    private RedissonLock redissonLock;

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        Object returnValue = null;

        if(redissonLock != null){
            log.info("[开始]执行RedisLock环绕通知,获取Redis分布式锁开始");
            //获取锁名称
            String lockName = distributedLock.value();
            //获取超时时间，默认10秒
            int leaseTime = distributedLock.leaseTime();
            redissonLock.lock(lockName, leaseTime);
            try {
                log.info("获取Redis分布式锁[成功]，加锁完成，开始执行业务逻辑...");
                returnValue = joinPoint.proceed();
            } catch (Throwable throwable) {
                log.error("获取Redis分布式锁[异常]，加锁失败", throwable);
                throwable.printStackTrace();
            } finally {
                //如果该线程还持有该锁，那么释放该锁。如果该线程不持有该锁，说明该线程的锁已到过期时间，自动释放锁
                if (redissonLock.isHeldByCurrentThread(lockName)) {
                    redissonLock.unlock(lockName);
                }
            }
            log.info("释放Redis分布式锁[成功]，解锁完成，结束业务逻辑...");
        }else {
            returnValue = joinPoint.proceed();
        }

        return returnValue;
    }
}
