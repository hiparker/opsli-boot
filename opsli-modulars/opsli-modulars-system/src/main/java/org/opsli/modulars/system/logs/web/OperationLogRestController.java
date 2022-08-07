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
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.logs.OperationLogRestApi;
import org.opsli.api.wrapper.system.logs.OperationLogModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.log.annotation.OperateLogger;
import org.opsli.core.log.enums.ModuleEnum;
import org.opsli.core.log.enums.OperationTypeEnum;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.system.logs.entity.OperationLog;
import org.opsli.modulars.system.logs.service.IOperationLogService;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 行为日志 Controller
 *
 * @author Parker
 * @date 2022-07-26 19:21:57
 */
@Api(tags = OperationLogRestApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/system/op-logs")
public class OperationLogRestController extends BaseRestController<OperationLog, OperationLogModel, IOperationLogService>
    implements OperationLogRestApi {


    /**
    * 行为日志 查一条
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "获得单条行为日志", notes = "获得单条行为日志 - ID")
    @PreAuthorize("hasAuthority('system_op_logs_select')")
    @Override
    public ResultWrapper<OperationLogModel> get(OperationLogModel model) {
        // 如果系统内部调用 则直接查数据库
        if(model != null && model.getIzApi() != null && model.getIzApi()){
            model = IService.get(model);
        }
        return ResultWrapper.getSuccessResultWrapper(model);
    }

    /**
    * 行为日志 查询分页
    * @param pageNo 当前页
    * @param pageSize 每页条数
    * @param request request
    * @return ResultVo
    */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @PreAuthorize("hasAuthority('system_op_logs_select')")
    @Override
    public ResultWrapper<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<OperationLog> queryBuilder = new WebQueryBuilder<>(IService.getEntityClass(), request.getParameterMap());
        Page<OperationLog, OperationLogModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultWrapper.getSuccessResultWrapper(page.getPageData());
    }

    /**
     * 行为日志 Excel 导出认证
     *
     * @param type 类型
     * @param request request
     */
    @ApiOperation(value = "Excel 导出认证", notes = "Excel 导出认证")
    @PreAuthorize("hasAuthority('system_op_logs_export')")
    @Override
    public ResultWrapper<String> exportExcelAuth(String type, HttpServletRequest request) {
        Optional<String> certificateOptional =
                super.excelExportAuth(type, OperationLogRestApi.SUB_TITLE, request);
        if(!certificateOptional.isPresent()){
            return ResultWrapper.getErrorResultWrapper();
        }
        return ResultWrapper.getSuccessResultWrapper(certificateOptional.get());
    }


    /**
     * 行为日志 Excel 导出
     * @param response response
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @PreAuthorize("hasAuthority('system_op_logs_export')")
    @OperateLogger(description = "导出Excel",
            module = ModuleEnum.MODULE_OPERATION, operationType = OperationTypeEnum.SELECT, db = true)
    @Override
    public void exportExcel(String certificate, HttpServletResponse response) {
        // 导出Excel
        super.excelExport(certificate, response);
    }

}
