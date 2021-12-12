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
package org.opsli.modulars.generator.logs.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.modulars.generator.logs.api.GenLogsApi;
import org.opsli.modulars.generator.logs.entity.GenLogs;
import org.opsli.modulars.generator.logs.service.IGenLogsService;
import org.opsli.modulars.generator.logs.wrapper.GenLogsModel;
import org.opsli.plugins.generator.utils.GeneratorHandleUtil;

import javax.servlet.http.HttpServletResponse;


/**
 * 代码生成器日志
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
@Api(tags = GenLogsApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/generator/logs")
public class GenLogsRestController extends BaseRestController<GenLogs, GenLogsModel, IGenLogsService>
        implements GenLogsApi {

    @ApiOperation(value = "获得当前表生成记录", notes = "获得当前表生成记录")
    @RequiresPermissions("dev_generator_select")
    @Override
    public ResultVo<GenLogsModel> getByTableId(String tableId) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        GenLogsModel byTableId = IService.getByTableId(tableId);
        return ResultVo.success(byTableId);
    }

    /**
     * 代码生成 修改
     * @param model 模型
     */
    @ApiOperation(value = "代码生成", notes = "代码生成")
    @RequiresPermissions("dev_generator_create")
    @EnableLog
    @Override
    public void create(GenLogsModel model, HttpServletResponse response) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 调用生成方法
        IService.create(model, response);

    }

    /**
     * 生成菜单 修改
     *
     * @param menuParentId 上级菜单ID
     * @param tableId 表ID
     * @return ResultVo
     */
    @ApiOperation(value = "生成菜单", notes = "生成菜单")
    @RequiresPermissions("dev_generator_createMenu")
    @EnableLog
    @Override
    public ResultVo<?> createMenu(String menuParentId, String tableId) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        super.demoError();

        // 调用生成菜单方法
        boolean menuFlag = IService.createMenu(menuParentId, tableId);
        if(!menuFlag){
            return ResultVo.error();
        }
        return ResultVo.success();
    }

}
