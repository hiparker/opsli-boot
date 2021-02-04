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
package org.opsli.modulars.creater.createrlogs.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.modulars.creater.createrlogs.api.CreaterLogsApi;
import org.opsli.modulars.creater.createrlogs.entity.CreaterLogs;
import org.opsli.modulars.creater.createrlogs.service.ICreateLogsService;
import org.opsli.modulars.creater.createrlogs.wrapper.CreaterLogsModel;

import javax.servlet.http.HttpServletResponse;


/**
 * @BelongsProject: opsli-boot
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 代码生成器日志
 */
@Api(tags = "代码生成器-日志")
@Slf4j
@ApiRestController("/creater/logs")
public class CreaterLogsRestController extends BaseRestController<CreaterLogs, CreaterLogsModel, ICreateLogsService>
        implements CreaterLogsApi {

    @ApiOperation(value = "获得当前表生成记录", notes = "获得当前表生成记录")
    @RequiresPermissions("deve_creater_select")
    @Override
    public ResultVo<CreaterLogsModel> getByTableId(String tableId) {
        CreaterLogsModel byTableId = IService.getByTableId(tableId);
        return ResultVo.success(byTableId);
    }

    /**
     * 代码生成 修改
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "代码生成", notes = "代码生成")
    @RequiresPermissions("deve_creater_create")
    @EnableLog
    @Override
    public void create(CreaterLogsModel model, HttpServletResponse response) {
        // 演示模式 不允许操作
        // super.demoError();

        // 调用生成方法
        IService.create(model, response);

    }

}
