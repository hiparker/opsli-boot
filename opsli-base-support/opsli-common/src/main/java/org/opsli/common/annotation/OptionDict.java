package org.opsli.common.annotation;


import java.lang.annotation.*;

/**
 * 参数配置
 * @author Parker
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptionDict {

    /** 参数编码 */
    String value();

}
