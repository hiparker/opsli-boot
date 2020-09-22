package org.opsli.common.annotation.validation;

import org.opsli.common.enums.ValiArgsType;

import java.lang.annotation.*;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.annotation
 * @Author: Parker
 * @CreateTime: 2020-09-22 17:07
 * @Description: 字段验证器
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ValidationArgs {

    ValiArgsType[] value();

}
