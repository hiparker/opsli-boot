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
import org.springframework.security.access.prepost.PreAuthorize;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.logs.LogsApi;
import org.opsli.api.wrapper.system.logs.LogsModel;
import org.opsli.common.annotation.ApiRestController;

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
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得单条日志", notes = "获得单条日志 - ID")
    @PreAuthorize("hasAuthority('devops_logs_select')")
    @Override
    public ResultWrapper<LogsModel> get(LogsModel model) {
        model = IService.get(model);
        return ResultWrapper.getSuccessResultWrapper(model);
    }

    /**
     * 日志 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @PreAuthorize("hasAuthority('devops_logs_select')")
    @Override
    public ResultWrapper<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysLogs> queryBuilder = new WebQueryBuilder<>(IService.getEntityClass(), request.getParameterMap());
        Page<SysLogs, LogsModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultWrapper.getSuccessResultWrapper(page.getPageData());
    }


    @Override
    public ResultWrapper<?> insert(LogsModel model) {
        IService.insert(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("新增日志成功");
    }
}
