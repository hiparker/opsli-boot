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
package org.opsli.modulars.generator.logs.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * 代码生成器 - 生成日志 （便于二次生成时查看）
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GenLogs extends BaseEntity {

    /** 归属表ID */
    private String tableId;

    /** 表类型 */
    private String tableType;

    /** 包名 */
    private String packageName;

    /** 模块名 */
    private String moduleName;

    /** 子模块名 */
    private String subModuleName;

    /** 代码标题 */
    private String codeTitle;

    /** 代码标题简介 */
    private String codeTitleBrief;

    /** 作者名 */
    private String authorName;

    /** 模板ID */
    private String templateId;

    // ========================================

}
