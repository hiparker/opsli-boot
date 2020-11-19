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
package org.opsli.modulars.creater.table.service;

import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.modulars.creater.table.entity.CreaterTable;
import org.opsli.modulars.creater.table.wrapper.CreaterTableAndColumnModel;
import org.opsli.modulars.creater.table.wrapper.CreaterTableModel;


/**
 * @BelongsProject: opsli-boot
 * @Author: Parker
 * @CreateTime: 2020-09-17 13:07
 * @Description: 代码生成器 - 表 接口
 */
public interface ITableService extends CrudServiceInterface<CreaterTable, CreaterTableModel> {

    /**
     * 新增表数据
     * @param model
     * @return
     */
    void insertAny(CreaterTableAndColumnModel model);

    /**
     * 修改表数据
     * @param model
     * @return
     */
    void updateAny(CreaterTableAndColumnModel model);

    /**
     * 根据表ID 删除数据
     * @param id
     * @return
     */
    void removeByIdAny(String id) throws Exception;

    /**
     * 根据表ID 删除数据
     * @param ids
     * @return
     */
    void removeByIdsAny(String[] ids) throws Exception;

    /**
     * 更新同步状态 为 已同步
     * @param id
     */
    void renewSyncState(String id);

}
