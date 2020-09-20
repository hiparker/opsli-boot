package org.opsli.modulars.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import lombok.Data;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.opsli.plugins.excel.annotation.CellStyleFormat;

import java.io.Serializable;

/**
 * Created Date by 2020/5/9 0009.
 *
 * Excel 导入 导出测试类
 * @author Parker
 */
@Data
@ContentRowHeight(16)
@HeadRowHeight(21)
@HeadFontStyle(fontName = "Arial",color = 9,fontHeightInPoints = 10)
@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 23)
@ColumnWidth(22)
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 名称 */
    @ExcelProperty(value = "名称",index = 0)
    @CellStyleFormat
    private String name;

    /** 编号 */
    @ExcelProperty(value = "编号",index = 1)
    @CellStyleFormat
    private String code;

    /** 年龄 */
    @ExcelProperty(value = "年龄",index = 2)
    @CellStyleFormat
    private Integer age;

    /** 性别 */
    @ExcelProperty(value = "性别",index = 3)
    @CellStyleFormat
    private Integer sex;

    /** 金额 */
//    @ExcelProperty(value = "金额",index = 4)
//    @CellStyleFormat
    private Double amt;

    /**
     * 构造函数
     */
    public Test() {

    }
    public Test(String name, String code, Integer age, Integer sex, Double amt) {
        this.name = name;
        this.code = code;
        this.age = age;
        this.sex = sex;
        this.amt = amt;
    }

}
