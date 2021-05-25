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
package org.opsli.modulars.generator.table.service;

import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.modulars.generator.table.entity.GenTable;
import org.opsli.modulars.generator.table.wrapper.GenTableAndColumnModel;
import org.opsli.modulars.generator.table.wrapper.GenTableModel;

import java.util.List;


/**
 * 代码生成器 - 表 接口
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
public interface IGenTableService extends CrudServiceInterface<GenTable, GenTableModel> {

    /**
     * 新增表数据
     * @param model 模型
     */
    void insertAny(GenTableAndColumnModel model);

    /**
     * 修改表数据
     * @param model 模型
     */
    void updateAny(GenTableAndColumnModel model);

    /**
     * 根据表ID 删除数据
     * @param id ID
     * @throws Exception 异常
     */
    void removeByIdAny(String id) throws Exception;

    /**
     * 根据表ID 删除数据
     * @param ids id数组
     * @throws Exception 异常
     */
    void removeByIdsAny(String[] ids) throws Exception;

    /**
     * 更新同步状态 为 已同步
     * @param id ID
     */
    void renewSyncState(String id);

    /**
     * 获得当前 生成器中所有表名
     * @return List
     */
    List<String> findAllByTableName();

    /**
     * 导入数据库表
     * @param tableNames 表名数组
     */
    void importTables(String[] tableNames);

}
