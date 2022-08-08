package org.opsli.modulars.test.web;

import cn.hutool.core.convert.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.test.TestRestApi;
import org.opsli.api.wrapper.test.TestModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.log.annotation.OperateLogger;
import org.opsli.core.log.enums.ModuleEnum;
import org.opsli.core.log.enums.OperationTypeEnum;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.test.entity.TestEntity;
import org.opsli.modulars.test.service.ITestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 测试类 Controller
 *
 * @author Parker
 * @date 2020-09-17 13:01
 */
@Api(tags = "测试类")
@Slf4j
@ApiRestController("/{ver}/test")
public class TestRestController extends BaseRestController<TestEntity, TestModel, ITestService>
        implements TestRestApi {

    /**
     * 测试 查一条
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得单条测试", notes = "获得单条测试 - ID")
    @PreAuthorize("hasAuthority('gentest_test_select')")
    @Override
    public ResultWrapper<TestModel> get(TestModel model) {
        model = IService.get(model);
        return ResultWrapper.getSuccessResultWrapper(model);
    }

    /**
     * 测试 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @PreAuthorize("hasAuthority('gentest_test_select')")
    @Override
    public ResultWrapper<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<TestEntity> queryBuilder = new WebQueryBuilder<>(TestEntity.class, request.getParameterMap());
        Page<TestEntity, TestModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultWrapper.getSuccessResultWrapper(page.getPageData());
    }

    /**
     * 测试 新增
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "新增测试", notes = "新增测试")
    @PreAuthorize("hasAuthority('gentest_test_insert')")
    @OperateLogger(description = "新增测试",
            module = ModuleEnum.MODULE_TEST, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> insert(TestModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("新增测试成功");
    }

    /**
     * 测试 修改
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "修改测试", notes = "修改测试")
    @PreAuthorize("hasAuthority('gentest_test_update')")
    @OperateLogger(description = "修改测试",
            module = ModuleEnum.MODULE_TEST, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> update(TestModel model) {
        // 调用修改方法
        IService.update(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("修改测试成功");
    }


    /**
     * 测试 删除
     * @param id ID
     * @return ResultWrapper
     */
    @ApiOperation(value = "删除测试数据", notes = "删除测试数据")
    @PreAuthorize("hasAuthority('gentest_test_delete')")
    @OperateLogger(description = "测试 删除",
            module = ModuleEnum.MODULE_TEST, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> del(String id){
        IService.delete(id);
        return ResultWrapper.getSuccessResultWrapperByMsg("删除测试成功");
    }


    /**
     * 测试 批量删除
     * @param ids ID 数组
     * @return ResultWrapper
     */
    @ApiOperation(value = "批量删除测试数据", notes = "批量删除测试数据")
    @PreAuthorize("hasAuthority('gentest_test_delete')")
    @OperateLogger(description = "测试 批量删除",
            module = ModuleEnum.MODULE_TEST, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> delAll(String ids){
        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);
        return ResultWrapper.getSuccessResultWrapperByMsg("批量删除测试成功");
    }


    /**
     * 测试 Excel 导出认证
     *
     * @param type 类型
     * @param request request
     */
    @ApiOperation(value = "Excel 导出认证", notes = "Excel 导出认证")
    @PreAuthorize("hasAnyAuthority('gentest_test_export', 'gentest_test_import')")
    @Override
    public ResultWrapper<String> exportExcelAuth(String type, HttpServletRequest request) {
        Optional<String> certificateOptional =
                super.excelExportAuth(type, TestRestApi.SUB_TITLE, request);
        if(!certificateOptional.isPresent()){
            return ResultWrapper.getErrorResultWrapper();
        }
        return ResultWrapper.getSuccessResultWrapper(certificateOptional.get());
    }


    /**
     * 测试 Excel 导出
     * @param response response
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @PreAuthorize("hasAuthority('gentest_test_export')")
    @OperateLogger(description = "导出Excel",
            module = ModuleEnum.MODULE_TEST, operationType = OperationTypeEnum.SELECT, db = true)
    @Override
    public void exportExcel(String certificate, HttpServletResponse response) {
        // 导出Excel
        super.excelExport(certificate, response);
    }

    /**
     * 测试 Excel 导入
     * @param request 文件流 request
     * @return ResultWrapper
     */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @PreAuthorize("hasAuthority('gentest_test_import')")
    @OperateLogger(description = "测试 Excel 导入",
            module = ModuleEnum.MODULE_TEST, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }


}
