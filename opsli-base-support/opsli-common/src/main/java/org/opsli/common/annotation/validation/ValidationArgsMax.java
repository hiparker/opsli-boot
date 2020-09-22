package org.opsli.common.annotation.validation;


import java.lang.annotation.*;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.annotation
 * @Author: Parker
 * @CreateTime: 2020-09-22 17:07
 * @Description: 字段验证器 - 字段最大长度
 *
 * 对应 数据库 真实长度数
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ValidationArgsMax {

    int value();

}
