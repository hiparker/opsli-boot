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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validation.ValidationArgs;
import org.opsli.common.annotation.validation.ValidationArgsLenMax;
import org.opsli.common.enums.ValiArgsType;

import java.io.Serializable;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.api.wrapper.system.menu
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 创建完整菜单
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuFullModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 上级菜单ID */
    @ApiModelProperty(value = "上级菜单ID")
    @ExcelIgnore
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsLenMax(19)
    private String parentId;

    /** 菜单名称 */
    @ApiModelProperty(value = "菜单名称")
    @ExcelIgnore
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsLenMax(100)
    private String title;

    /** 模块名 */
    @ApiModelProperty(value = "模块名")
    @ExcelIgnore
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsLenMax(40)
    private String moduleName;

    /** 子模块名 */
    @ApiModelProperty(value = "子模块名")
    @ExcelIgnore
    @ValidationArgsLenMax(40)
    private String subModuleName;

}
