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

package org.opsli.modulars.generator.template.entity;



import java.math.BigDecimal;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * 代码模板详情 Entity
 *
 * @author Parker
 * @date 2021-05-28 17:12:38
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GenTemplateDetail extends BaseEntity {


    /** 父级ID */
    private String parentId;

    /** 类型 0 后端 1 前端 */
    private String type;

    /** 路径 */
    private String path;

    /** 文件名 */
    private String fileName;

    /** 文件内容 */
    private String fileContent;

    /** 是否忽略文件名 */
    private String ignoreFileName;

    // ========================================


}
