package opsli.plugins.crypto.spring.annotation;



import opsli.plugins.crypto.spring.crypto.ICrypto;
import opsli.plugins.crypto.spring.crypto.impl.AESCrypto;

import java.lang.annotation.*;

/**
 * 字段加密
 * 注意注解只能放在可以被 mybatis Interceptor拦截器拦截的实体上 才能生效
 *
 * @author Parker
 * @date 2022-08-07 17:33
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface CryptoMapperField {

    /**
     * 秘钥 (加密口令，盐)
     * 优先 走全局配置 encrypt.property.key
     * @return String
     */
    String key() default "0546c7b2790448658a1816a7803d7ba1";

    /**
     * 加密解密器
     * @return Class<? extends ICrypto>
     */
    Class<? extends ICrypto> iCrypto() default AESCrypto.class;

}
