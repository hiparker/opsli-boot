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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.logs.LoginLogsApi;
import org.opsli.api.wrapper.system.logs.LoginLogsModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.system.logs.entity.SysLoginLogs;
import org.opsli.modulars.system.logs.service.ILoginLogsService;
import org.springframework.security.access.prepost.PreAuthorize;


/**
 * 登录日志 Controller
 *
 * @author Pace
 * @date 2020-11-28 18:59:59
 */
@Tag(name = LoginLogsApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/system/login-logs")
public class LoginLogsRestController extends BaseRestController<SysLoginLogs, LoginLogsModel, ILoginLogsService>
        implements LoginLogsApi {

    /**
     * 登录日志 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultWrapper
     */
    @Operation(summary = "获得分页数据 - 查询构造器")
    @PreAuthorize("hasAuthority('devops_login_logs_select')")
    @Override
    public ResultWrapper<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysLoginLogs> queryBuilder = new WebQueryBuilder<>(IService.getEntityClass(), request.getParameterMap());
        Page<SysLoginLogs, LoginLogsModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultWrapper.getSuccessResultWrapper(page.getPageData());
    }

}
