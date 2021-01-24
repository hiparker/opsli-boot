package org.opsli.common.annotation;


import java.lang.annotation.*;

/**
 * 接口数据 加解密
 * @author Parker
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InterfaceEncryptAndDecrypt {

    /** 加密启用状态 */
    boolean enable() default true;

    /** 请求解密 */
    boolean requestDecrypt() default true;

    /** 返回加密 */
    boolean responseEncrypt() default true;

}
