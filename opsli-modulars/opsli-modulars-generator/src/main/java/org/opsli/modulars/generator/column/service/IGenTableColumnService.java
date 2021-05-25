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
package org.opsli.modulars.generator.column.service;

import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.modulars.generator.column.entity.GenTableColumn;
import org.opsli.modulars.generator.column.wrapper.GenTableColumnModel;

import java.util.List;


/**
 * 代码生成器 - 表结构 接口
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
public interface IGenTableColumnService extends CrudServiceInterface<GenTableColumn, GenTableColumnModel> {

    /**
     * 根据表ID 获得数据
     * @param tableId 表ID
     * @return List
     */
    List<GenTableColumnModel> getByTableId(String tableId);


    /**
     * 根据表ID 删除数据
     * @param tableId 表ID
     */
    void delByTableId(String tableId);

    /**
     * 根据表ID 删除数据
     * @param tableIds 表ID数组
     */
    void delByTableIds(String[] tableIds);
}
