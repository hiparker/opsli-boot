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
package org.opsli.modulars.system.logs.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.logs.LogsApi;
import org.opsli.api.wrapper.system.logs.LogsModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.system.logs.entity.SysLogs;
import org.opsli.modulars.system.logs.service.ILogsService;

import javax.servlet.http.HttpServletRequest;


/**
 * 日志 Controller
 *
 * @author Parker
 * @date 2020-11-28 18:59:59
 */
@Api(tags = LogsApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/system/logs")
public class LogsRestController extends BaseRestController<SysLogs, LogsModel, ILogsService>
        implements LogsApi {


    /**
     * 日志 查一条
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "获得单条日志", notes = "获得单条日志 - ID")
    @RequiresPermissions("devops_logs_select")
    @Override
    public ResultVo<LogsModel> get(LogsModel model) {
        model = IService.get(model);
        return ResultVo.success(model);
    }

    /**
     * 日志 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultVo
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @RequiresPermissions("devops_logs_select")
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysLogs> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        Page<SysLogs, LogsModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultVo.success(page.getPageData());
    }


    @Override
    public ResultVo<?> insert(LogsModel model) {
        IService.insert(model);
        return ResultVo.success("新增日志成功");
    }
}
