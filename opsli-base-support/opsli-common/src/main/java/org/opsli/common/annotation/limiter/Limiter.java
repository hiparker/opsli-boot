package org.opsli.common.annotation.limiter;


import org.opsli.common.enums.AlertType;
import org.opsli.common.utils.RateLimiterUtil;

import java.lang.annotation.*;

/**
 * Java 限流器
 * @author Parker
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Limiter {

    /** QPS */
    double qps() default RateLimiterUtil.DEFAULT_QPS;

    /** 提醒方式 */
    AlertType alertType() default AlertType.JSON;

}
