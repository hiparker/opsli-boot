package org.opsli.api.wrapper.system.menu;

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
 * @Description: 菜单表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuModel extends ApiWrapper {

    /** 父级主键 */
    @ApiModelProperty(value = "父级主键")
    @ExcelProperty(value = "父级主键", order = 1)
    @ExcelInfo
    // 验证器
    @ValidationArgs(ValiArgsType.IS_NOT_NULL)
    @ValidationArgsMax(20)
    private String parentId;

    /** 菜单编号 */
    @ApiModelProperty(value = "菜单编号")
    @ExcelProperty(value = "菜单编号", order = 2)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL,ValiArgsType.IS_GENERAL})
    @ValidationArgsMax(50)
    private String menuCode;

    /** 菜单名称 */
    @ApiModelProperty(value = "菜单名称")
    @ExcelProperty(value = "菜单名称", order = 3)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL,ValiArgsType.IS_GENERAL_WITH_CHINESE})
    @ValidationArgsMax(50)
    private String menuName;

    /** 图标 */
    @ApiModelProperty(value = "图标")
    @ExcelProperty(value = "图标", order = 4)
    @ExcelInfo
    // 验证器
    @ValidationArgsMax(50)
    private String icon;

    /** 项目类型:1-菜单2-按钮3-链接4-表单 */
    @ApiModelProperty(value = "项目类型:1-菜单2-按钮3-链接4-表单")
    @ExcelProperty(value = "项目类型", order = 5)
    @ExcelInfo(dictType = "menuType")
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsMax(20)
    private String type;

    /** url地址 */
    @ApiModelProperty(value = "url地址")
    @ExcelProperty(value = "url地址", order = 6)
    @ExcelInfo
    // 验证器
    @ValidationArgsMax(200)
    private String url;


}
