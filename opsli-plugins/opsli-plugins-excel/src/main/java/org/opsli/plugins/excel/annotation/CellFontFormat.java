package org.opsli.plugins.excel.annotation;

import org.apache.poi.ss.usermodel.IndexedColors;

import java.lang.annotation.*;
/**
 * Created Date by 2020/5/9 0009.
 *
 * @author Parker
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CellFontFormat {

    String fontName() default "Arial";

    short fontHeightInPoints() default 10;

    IndexedColors fontColor() default IndexedColors.BLACK;

    boolean bold() default false;

}
