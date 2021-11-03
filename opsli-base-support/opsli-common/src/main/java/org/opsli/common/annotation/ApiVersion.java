package org.opsli.common.annotation;

import java.lang.annotation.*;

/**
 * 标识版本号，从1开始
 *
 * @author Parker
 * @date 2021年10月27日12:36:55
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {

    int value() default 1;

}