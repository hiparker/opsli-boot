package org.opsli.api.wrapper.system.dict;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validation.ValidationArgs;
import org.opsli.common.annotation.validation.ValidationArgsMax;
import org.opsli.common.enums.ValiArgsType;
import org.opsli.plugins.excel.annotation.ExcelInfo;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 数据字典
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DictModel extends ApiWrapper {



    /** 字典类型编号 */
    @ApiModelProperty(value = "字典类型编号")
    @ExcelProperty(value = "字典类型编号", order = 1)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_GENERAL})
    @ValidationArgsMax(120)
    private String typeCode;

    /** 字典类型名称 */
    @ApiModelProperty(value = "字典类型名称")
    @ExcelProperty(value = "字典类型名称", order = 2)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_GENERAL_WITH_CHINESE})
    @ValidationArgsMax(120)
    private String typeName;

    /** 是否内置数据 0是  1否*/
    @ApiModelProperty(value = "是否内置数据 0是  1否")
    @ExcelProperty(value = "是否内置数据", order = 3)
    @ExcelInfo(dictType = "yes_no")
    // 验证器
    @ValidationArgs(ValiArgsType.IS_NOT_NULL)
    @ValidationArgsMax(1)
    private Character izLock;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注", order = 4)
    @ExcelInfo
    // 验证器
    @ValidationArgsMax(255)
    private String remark;


}
