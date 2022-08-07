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
package org.opsli.modulars.gentest.carinfo.web;

import cn.hutool.core.convert.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.opsli.core.log.enums.*;
import org.opsli.core.log.annotation.OperateLogger;

import org.opsli.modulars.gentest.carinfo.entity.TestCar;
import org.opsli.api.wrapper.gentest.carinfo.TestCarModel;
import org.opsli.modulars.gentest.carinfo.service.ITestCarService;
import org.opsli.api.web.gentest.carinfo.TestCarRestApi;

import java.util.Optional;

/**
 * 测试汽车 Controller
 *
 * @author Parker
 * @date 2022-08-06 23:58:27
 */
@Api(tags = TestCarRestApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/gentest/carinfo")
public class TestCarRestController extends BaseRestController<TestCar, TestCarModel, ITestCarService>
    implements TestCarRestApi {


    /**
     * 测试汽车 查一条
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得单条测试汽车", notes = "获得单条测试汽车 - ID")
    @PreAuthorize("hasAuthority('gentest_carinfo_select')")
    @Override
    public ResultWrapper<TestCarModel> get(TestCarModel model) {
        // 如果系统内部调用 则直接查数据库
        if(model != null && model.getIzApi() != null && model.getIzApi()){
            model = IService.get(model);
        }
        return ResultWrapper.getSuccessResultWrapper(model);
    }

    /**
     * 测试汽车 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @PreAuthorize("hasAuthority('gentest_carinfo_select')")
    @Override
    public ResultWrapper<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<TestCar> queryBuilder = new WebQueryBuilder<>(IService.getEntityClass(), request.getParameterMap());
        Page<TestCar, TestCarModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultWrapper.getSuccessResultWrapper(page.getPageData());
    }

    /**
     * 测试汽车 新增
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "新增测试汽车数据", notes = "新增测试汽车数据")
    @PreAuthorize("hasAuthority('gentest_carinfo_insert')")
    @OperateLogger(description = "新增测试汽车数据",
            module = ModuleEnum.MODULE_UNKNOWN, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> insert(TestCarModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("新增测试汽车成功");
    }

    /**
     * 测试汽车 修改
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "修改测试汽车数据", notes = "修改测试汽车数据")
    @PreAuthorize("hasAuthority('gentest_carinfo_update')")
    @OperateLogger(description = "修改测试汽车数据",
            module = ModuleEnum.MODULE_UNKNOWN, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> update(TestCarModel model) {
        // 调用修改方法
        IService.update(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("修改测试汽车成功");
    }


    /**
     * 测试汽车 删除
     * @param id ID
     * @return ResultVo
     */
    @ApiOperation(value = "删除测试汽车数据", notes = "删除测试汽车数据")
    @PreAuthorize("hasAuthority('gentest_carinfo_delete')")
    @OperateLogger(description = "删除测试汽车数据",
            module = ModuleEnum.MODULE_UNKNOWN, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> del(String id){
        IService.delete(id);
        return ResultWrapper.getSuccessResultWrapperByMsg("删除测试汽车成功");
    }

    /**
     * 测试汽车 批量删除
     * @param ids ID 数组
     * @return ResultVo
     */
    @ApiOperation(value = "批量删除测试汽车数据", notes = "批量删除测试汽车数据")
    @PreAuthorize("hasAuthority('gentest_carinfo_delete')")
    @OperateLogger(description = "批量删除测试汽车数据",
            module = ModuleEnum.MODULE_UNKNOWN, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> delAll(String ids){
        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);
        return ResultWrapper.getSuccessResultWrapperByMsg("批量删除测试汽车成功");
    }

    /**
     * 测试汽车 Excel 导出认证
     *
     * @param type 类型
     * @param request request
     */
    @ApiOperation(value = "Excel 导出认证", notes = "Excel 导出认证")
    @PreAuthorize("hasAnyAuthority('gentest_carinfo_export', 'gentest_carinfo_import')")
    @Override
    public ResultWrapper<String> exportExcelAuth(String type, HttpServletRequest request) {
        Optional<String> certificateOptional =
                super.excelExportAuth(type, TestCarRestApi.SUB_TITLE, request);
        if(!certificateOptional.isPresent()){
            return ResultWrapper.getErrorResultWrapper();
        }
        return ResultWrapper.getSuccessResultWrapper(certificateOptional.get());
    }


    /**
     * 测试汽车 Excel 导出
     * @param response response
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @PreAuthorize("hasAuthority('gentest_carinfo_export')")
    @OperateLogger(description = "测试汽车 导出Excel",
            module = ModuleEnum.MODULE_UNKNOWN, operationType = OperationTypeEnum.SELECT, db = true)
    @Override
    public void exportExcel(String certificate, HttpServletResponse response) {
        // 导出Excel
        super.excelExport(certificate, response);
    }

    /**
     * 测试汽车 Excel 导入
     * 注：这里 RequiresPermissions 引入的是 Shiro原生鉴权注解
     * @param request 文件流 request
     * @return ResultVo
     */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @PreAuthorize("hasAuthority('gentest_carinfo_import')")
    @OperateLogger(description = "测试汽车 Excel 导入",
            module = ModuleEnum.MODULE_UNKNOWN, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }

}
