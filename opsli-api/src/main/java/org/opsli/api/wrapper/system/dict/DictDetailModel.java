package org.opsli.api.wrapper.system.dict;

import com.alibaba.excel.annotation.ExcelIgnore;
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
 * @Description: 数据字典 - 明细
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DictDetailModel extends ApiWrapper {


    /** 字典ID */
    @ApiModelProperty(value = "字典类型ID")
    @ExcelIgnore
    @ValidationArgs(ValiArgsType.IS_NOT_NULL)
    private String typeId;

    /** 类型编号 - 冗余 */
    @ApiModelProperty(value = "字典类型Code")
    @ExcelIgnore
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_GENERAL})
    @ValidationArgsMax(120)
    private String typeCode;

    /** 字典名称 */
    @ApiModelProperty(value = "字典名称")
    @ExcelProperty(value = "字典名称", order = 1)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_GENERAL_WITH_CHINESE})
    @ValidationArgsMax(120)
    private String dictName;

    /** 字典值 */
    @ApiModelProperty(value = "字典值")
    @ExcelProperty(value = "字典值", order = 2)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsMax(120)
    private String dictValue;

    /** 是否内置数据 0是  1否*/
    @ApiModelProperty(value = "是否内置数据 0是  1否")
    @ExcelProperty(value = "是否内置数据", order = 2)
    @ExcelInfo(dictType = "yes_no")
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsMax(1)
    private Character izLock;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    @ExcelProperty(value = "排序", order = 2)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL, ValiArgsType.IS_NUMBER})
    @ValidationArgsMax(10)
    private Integer sortNo;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注", order = 2)
    @ExcelInfo
    // 验证器
    @ValidationArgsMax(255)
    private String remark;


}
