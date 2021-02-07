package org.opsli.plugins.redisson.annotation;

import java.lang.annotation.*;

/**
 * 基于注解的分布式式锁
 *
 * @author xub
 * @date 2019/6/19 下午9:22
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributedLock {

    /**
     * 锁的名称
     */
    String value() default "opsli:redisson";

    /**
     * 锁的有效时间
     */
    int leaseTime() default 10;
}


