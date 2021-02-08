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

package org.opsli.modulars.system.options.entity;


import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.system.options.entity
 * @Author: Parker
 * @CreateTime: 2021-02-07 18:24:38
 * @Description: 系统参数
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysOptions extends BaseEntity {

    /** 参数编号 */
    private String optionCode;

    /** 参数名称 */
    private String optionName;

    /** 参数值 */
    private String optionValue;

    // ========================================


}
