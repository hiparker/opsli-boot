package org.opsli.plugins.excel.annotation;



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
public @interface ExcelInfo {

    /** 字典类型 code */
    String dictType() default "";
    /** 字段反射类型 */
    //Class<?> reflectFieldType() default Class.class;
    /** 字段反射名称 */
    //String reflectFieldName() default "";
    /** 字段样式 */
    CellStyleFormat cellStyleFormat() default @CellStyleFormat();

}
