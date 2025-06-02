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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.log.annotation.OperateLogger;
import org.opsli.core.log.enums.ModuleEnum;
import org.opsli.core.log.enums.OperationTypeEnum;
import org.opsli.modulars.generator.logs.api.GenLogsApi;
import org.opsli.modulars.generator.logs.entity.GenLogs;
import org.opsli.modulars.generator.logs.service.IGenLogsService;
import org.opsli.modulars.generator.logs.wrapper.GenLogsModel;
import org.opsli.plugins.generator.utils.GeneratorHandleUtil;
import org.springframework.security.access.prepost.PreAuthorize;


/**
 * 代码生成器日志
 *
 * @author Pace
 * @date 2020-09-16 17:34
 */
@Tag(name = GenLogsApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/generator/logs")
public class GenLogsRestController extends BaseRestController<GenLogs, GenLogsModel, IGenLogsService>
        implements GenLogsApi {

    @Operation(summary = "获得当前表生成记录")
    @PreAuthorize("hasAuthority('dev_generator_select')")
    @Override
    public ResultWrapper<GenLogsModel> getByTableId(String tableId) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        GenLogsModel byTableId = IService.getByTableId(tableId);
        return ResultWrapper.getSuccessResultWrapper(byTableId);
    }

    /**
     * 代码生成 修改
     */
    @Operation(summary = "代码生成")
    @PreAuthorize("hasAuthority('dev_generator_create')")
    @OperateLogger(description = "代码生成",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public void create(HttpServletRequest request, HttpServletResponse response) {
        // request 对象属性
        GenLogsModel model = new GenLogsModel();
        try {
            BeanUtils.populate(model, request.getParameterMap());
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

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
     * @return ResultWrapper
     */
    @Operation(summary = "生成菜单")
    @PreAuthorize("hasAuthority('dev_generator_createMenu')")
    @OperateLogger(description = "代码生成 - 生成菜单",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> createMenu(String menuParentId, String tableId) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        super.demoError();

        // 调用生成菜单方法
        boolean menuFlag = IService.createMenu(menuParentId, tableId);
        if(!menuFlag){
            return ResultWrapper.getErrorResultWrapper();
        }
        return ResultWrapper.getSuccessResultWrapper();
    }

}
