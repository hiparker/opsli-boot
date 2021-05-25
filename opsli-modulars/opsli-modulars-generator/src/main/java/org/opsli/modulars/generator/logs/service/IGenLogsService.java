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
package org.opsli.modulars.generator.logs.service;

import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.modulars.generator.logs.entity.GenLogs;
import org.opsli.modulars.generator.logs.wrapper.GenLogsModel;

import javax.servlet.http.HttpServletResponse;


/**
 * 代码生成器 - 日志 接口
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
public interface IGenLogsService extends CrudServiceInterface<GenLogs, GenLogsModel> {

    /**
     * 根据表Id 删除
     * @param tableId 表ID
     */
    void delByTableId(String tableId);

    /**
     * 根据表Id 批量删除
     * @param tableIds 表ID 数组
     */
    void delByTableIds(String[] tableIds);

    /**
     * 根据表Id 查询
     * @param tableId 表ID
     * @return Model
     */
    GenLogsModel getByTableId(String tableId);

    /**
     * 代码生成
     * @param model 模型
     * @param response response
     */
    void create(GenLogsModel model, HttpServletResponse response);

    /**
     * 生成菜单
     * @param menuParentId 上级菜单ID
     * @param tableId 表ID
     * @return boolean
     */
    boolean createMenu(String menuParentId, String tableId);

}
