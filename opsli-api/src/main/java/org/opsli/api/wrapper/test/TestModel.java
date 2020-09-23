package org.opsli.api.wrapper.test;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.DictType;
import org.opsli.plugins.excel.annotation.CellStyleFormat;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 测试类
 *
 * 测试导入导出
 *
 */
@ApiModel(value="测试接口返回Model",description="测试接口返回Model")
@Data
public class TestModel extends ApiWrapper {

    @ApiModelProperty(value = "名称")
    @ExcelProperty(value = "名称", order = 1)
    @CellStyleFormat
    private String name;

    @ApiModelProperty(value = "分类")
    @DictType("testType")
    @ExcelProperty(value = "分类", order = 2)
    @CellStyleFormat
    private String type;

    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注", order = 3)
    @CellStyleFormat
    private String remark;

}
