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
package org.opsli.api.wrapper.system.menu;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.enums.ValidatorType;

/**
 * 菜单表
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuModel extends ApiWrapper {

    /** 父级主键 */
    @ApiModelProperty(value = "父级主键")
    @ExcelIgnore
    @ValidatorLenMax(20)
    private String parentId;

    /** 父级主键 ID集合 */
    @ApiModelProperty(value = "父级主键")
    @ExcelIgnore
    private String parentIds;

    /** 权限编号 */
    @ApiModelProperty(value = "权限编号")
    @ExcelIgnore
    @Validator({ValidatorType.IS_GENERAL})
    @ValidatorLenMax(50)
    private String permissions;

    /** 菜单名称 */
    @ApiModelProperty(value = "名称")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL, ValidatorType.IS_GENERAL_WITH_CHINESE})
    @ValidatorLenMax(50)
    private String menuName;

    /** 图标 */
    @ApiModelProperty(value = "图标")
    @ExcelIgnore
    @ValidatorLenMax(50)
    private String icon;

    /** 项目类型: 1-菜单 2-按钮 3-链接 */
    @ApiModelProperty(value = "项目类型")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(20)
    private String type;

    /** url地址 */
    @ApiModelProperty(value = "url地址")
    @ExcelIgnore
    @ValidatorLenMax(200)
    private String url;

    /** 组件 - vue 对应组件 */
    @ApiModelProperty(value = "组件")
    @ExcelIgnore
    @ValidatorLenMax(200)
    private String component;

    /** 重定向 */
    @ApiModelProperty(value = "重定向")
    @ExcelIgnore
    @ValidatorLenMax(200)
    private String redirect;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    private Integer sortNo;

    /** 是否隐藏 0为否 1为是 */
    @ApiModelProperty(value = "是否隐藏")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    private String hidden;

    /** 是否总是显示 0为否 1为是 */
    @ApiModelProperty(value = "是否隐藏")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    private String alwaysShow;

    /** 标签 */
    @ApiModelProperty(value = "标签")
    @ExcelIgnore
    @Validator({ValidatorType.IS_NOT_NULL})
    private String label;

}
