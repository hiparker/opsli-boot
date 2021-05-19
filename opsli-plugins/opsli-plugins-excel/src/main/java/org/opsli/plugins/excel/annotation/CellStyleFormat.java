/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.plugins.excel.annotation;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.lang.annotation.*;

/**
 * Excel 样式注解
 *
 * @author Parker
 * @date 2020-09-16 11:47
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CellStyleFormat {

    /**
     * 水平居中方式 默认左居中
     * @see HorizontalAlignment
     */
    HorizontalAlignment horizontalAlignment() default HorizontalAlignment.LEFT;

    /**
     * 字体设置
     * @see org.apache.poi.xssf.usermodel.XSSFFont
     * @see org.apache.poi.hssf.usermodel.HSSFFont
     */
    CellFontFormat cellFont() default @CellFontFormat();


    /**
     * 背景颜色
     * @see IndexedColors
     */
    IndexedColors fillBackgroundColor() default IndexedColors.WHITE;

}
