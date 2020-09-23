package org.opsli.common.annotation;


import java.lang.annotation.*;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.annotation
 * @Author: Parker
 * @CreateTime: 2020-09-16 16:36
 * @Description: 字典标示

 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DictType {

    /** 字典类型 code */
    String value();

}
