package org.opsli.common.annotation;

import java.lang.annotation.*;

/**
 * 搜索历史注解
 *
 * 默认按照当前用户 key， 搜索记录排行最高
 *
 * @author Parker
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SearchHis {

    /**
     * 搜索 key，即 url 参数key
     *
     * http://127.0.0.1/opsli-boot?username=123
     *
     * username 就是 key
     *
     * 如果使用条件构造器， 比如 username_EQ key需要一致
     *
     */
    String[] keys();

}
