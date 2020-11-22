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
package org.opsli.modulars.creater.createrlogs.service;

import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.modulars.creater.createrlogs.entity.CreaterLogs;
import org.opsli.modulars.creater.createrlogs.wrapper.CreaterLogsModel;

import javax.servlet.http.HttpServletResponse;


/**
 * @BelongsProject: opsli-boot
 * @Author: Parker
 * @CreateTime: 2020-09-17 13:07
 * @Description: 代码生成器 - 表 接口
 */
public interface ICreateLogsService extends CrudServiceInterface<CreaterLogs, CreaterLogsModel> {

    /**
     * 根据表Id 删除
     * @param tableId
     */
    void delByTableId(String tableId);

    /**
     * 根据表Id 批量删除
     * @param tableIds
     */
    void delByTableIds(String[] tableIds);

    /**
     * 根据表Id 查询
     * @param tableId
     */
    CreaterLogsModel getByTableId(String tableId);

    /**
     * 代码生成
     * @param model
     */
    void create(CreaterLogsModel model, HttpServletResponse response);

}
