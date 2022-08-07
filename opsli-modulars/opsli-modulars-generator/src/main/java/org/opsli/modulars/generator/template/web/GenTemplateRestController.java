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

package org.opsli.modulars.generator.template.web;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.user.UserApi;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.log.annotation.OperateLogger;
import org.opsli.core.log.enums.ModuleEnum;
import org.opsli.core.log.enums.OperationTypeEnum;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.generator.template.api.GenTemplateRestApi;
import org.opsli.modulars.generator.template.entity.GenTemplate;
import org.opsli.modulars.generator.template.service.IGenTemplateService;
import org.opsli.modulars.generator.template.wrapper.GenTemplateAndDetailModel;
import org.opsli.modulars.generator.template.wrapper.GenTemplateCopyModel;
import org.opsli.modulars.generator.template.wrapper.GenTemplateModel;
import org.opsli.plugins.generator.utils.GeneratorHandleUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


/**
 * 代码模板 Controller
 *
 * @author Parker
 * @date 2021-05-27 14:33:49
 */
@Api(tags = GenTemplateRestApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/generator/template/")
public class GenTemplateRestController extends BaseRestController<GenTemplate, GenTemplateModel, IGenTemplateService>
    implements GenTemplateRestApi {


    /**
    * 代码模板 查一条
    * @param model 模型
    * @return ResultWrapper
    */
    @ApiOperation(value = "获得单条代码模板", notes = "获得单条代码模板 - ID")
    @PreAuthorize("hasAuthority('generator_template_select')")
    @Override
    public ResultWrapper<GenTemplateModel> get(GenTemplateModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        model = IService.get(model);
        return ResultWrapper.getSuccessResultWrapper(model);
    }

    /**
    * 代码模板 查询分页
    * @param pageNo 当前页
    * @param pageSize 每页条数
    * @param request request
    * @return ResultWrapper
    */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @PreAuthorize("hasAuthority('generator_template_select')")
    @Override
    public ResultWrapper<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        QueryBuilder<GenTemplate> queryBuilder = new WebQueryBuilder<>(IService.getEntityClass(), request.getParameterMap());
        Page<GenTemplate, GenTemplateModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultWrapper.getSuccessResultWrapper(page.getPageData());
    }

    /**
    * 代码模板 新增
    * @param model 模型
    * @return ResultWrapper
    */
    @ApiOperation(value = "新增代码模板数据", notes = "新增代码模板数据")
    @PreAuthorize("hasAuthority('generator_template_insert')")
    @OperateLogger(description = "新增代码模板数据",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> insert(GenTemplateModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        super.demoError();

        // 调用新增方法
        IService.insert(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("新增代码模板成功");
    }

    /**
     * 代码模板 新增 和 明细
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "新增代码模板数据", notes = "新增代码模板数据")
    @PreAuthorize("hasAuthority('generator_template_insert')")
    @OperateLogger(description = "新增代码模板数据",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> insertAndDetail(GenTemplateAndDetailModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        super.demoError();

        // 调用新增方法
        IService.insertAndDetail(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("新增代码模板成功");
    }

    /**
    * 代码模板 修改
    * @param model 模型
    * @return ResultWrapper
    */
    @ApiOperation(value = "修改代码模板数据", notes = "修改代码模板数据")
    @PreAuthorize("hasAuthority('generator_template_update')")
    @OperateLogger(description = "修改代码模板数据",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> update(GenTemplateModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.update(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("修改代码模板成功");
    }

    /**
     * 代码模板 修改 和 明细
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "修改代码模板数据", notes = "修改代码模板数据")
    @PreAuthorize("hasAuthority('generator_template_update')")
    @OperateLogger(description = "修改代码模板数据",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> updateAndDetail(GenTemplateAndDetailModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.updateAndDetail(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("修改代码模板成功");
    }

    /**
     * 复制 代码模板
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "新增代码模板数据", notes = "新增代码模板数据")
    @PreAuthorize("hasAuthority('generator_template_copy')")
    @OperateLogger(description = "复制 代码模板",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> copy(GenTemplateCopyModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 调用复制方法
        IService.copy(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("复制代码模板成功");
    }

    /**
    * 代码模板 删除
    * @param id ID
    * @return ResultWrapper
    */
    @ApiOperation(value = "删除代码模板数据", notes = "删除代码模板数据")
    @PreAuthorize("hasAuthority('generator_template_update')")
    @OperateLogger(description = "删除代码模板数据",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> del(String id){
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        super.demoError();

        IService.delete(id);
        return ResultWrapper.getSuccessResultWrapperByMsg("删除代码模板成功");
    }

    /**
     * 代码模板 Excel 导出认证
     *
     * @param type 类型
     * @param request request
     */
    @ApiOperation(value = "Excel 导出认证", notes = "Excel 导出认证")
    @PreAuthorize("hasAnyAuthority('generator_template_export', 'generator_template_import')")
    @Override
    public ResultWrapper<String> exportExcelAuth(String type, HttpServletRequest request) {
        Optional<String> certificateOptional =
                super.excelExportAuth(type, UserApi.SUB_TITLE, request);
        if(!certificateOptional.isPresent()){
            return ResultWrapper.getErrorResultWrapper();
        }
        return ResultWrapper.getSuccessResultWrapper(certificateOptional.get());
    }


    /**
     * 代码模板 Excel 导出
     * @param response response
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @PreAuthorize("hasAuthority('generator_template_export')")
    @OperateLogger(description = "导出Excel",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.SELECT, db = true)
    @Override
    public void exportExcel(String certificate, HttpServletResponse response) {
        // 导出Excel
        super.excelExport(certificate, response);
    }


    /**
    * 代码模板 Excel 导入
    * 注：这里 RequiresPermissions 引入的是 Shiro原生鉴权注解
    * @param request 文件流 request
    * @return ResultWrapper
    */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @PreAuthorize("hasAuthority('generator_template_import')")
    @OperateLogger(description = "代码模板 Excel 导入",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> importExcel(MultipartHttpServletRequest request) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        return super.importExcel(request);
    }

}
