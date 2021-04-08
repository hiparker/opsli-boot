package org.opsli.modulars.test.web;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.test.TestRestApi;
import org.opsli.api.wrapper.test.TestModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.annotation.RequiresPermissionsCus;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.test.entity.TestEntity;
import org.opsli.modulars.test.service.ITestService;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 测试类
 */
@Api(tags = "测试类")
@Slf4j
@ApiRestController("/test")
public class TestRestController extends BaseRestController<TestEntity, TestModel, ITestService>
        implements TestRestApi {

    /**
     * 测试 查一条
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "获得单条测试", notes = "获得单条测试 - ID")
    @RequiresPermissions("gentest_test_select")
    @Override
    public ResultVo<TestModel> get(TestModel model) {
        // 如果系统内部调用 则直接查数据库
        if(model != null && model.getIzApi() != null && model.getIzApi()){
            model = IService.get(model);
        }
        return ResultVo.success(model);
    }

    /**
     * 测试 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultVo
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @RequiresPermissions("gentest_test_select")
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<TestEntity> queryBuilder = new WebQueryBuilder<>(TestEntity.class, request.getParameterMap());
        Page<TestEntity, TestModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultVo.success(page.getPageData());
    }

    /**
     * 测试 新增
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "新增测试", notes = "新增测试")
    @RequiresPermissions("gentest_test_insert")
    @EnableLog
    @Override
    public ResultVo<?> insert(TestModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultVo.success("新增测试成功");
    }

    /**
     * 测试 修改
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "修改测试", notes = "修改测试")
    @RequiresPermissions("gentest_test_update")
    @EnableLog
    @Override
    public ResultVo<?> update(TestModel model) {
        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改测试成功");
    }


    /**
     * 测试 删除
     * @param id ID
     * @return ResultVo
     */
    @ApiOperation(value = "删除测试数据", notes = "删除测试数据")
    @RequiresPermissions("gentest_test_delete")
    @EnableLog
    @Override
    public ResultVo<?> del(String id){
        IService.delete(id);
        return ResultVo.success("删除测试成功");
    }


    /**
     * 测试 批量删除
     * @param ids ID 数组
     * @return ResultVo
     */
    @ApiOperation(value = "批量删除测试数据", notes = "批量删除测试数据")
    @RequiresPermissions("gentest_test_delete")
    @EnableLog
    @Override
    public ResultVo<?> delAll(String ids){
        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);
        return ResultVo.success("批量删除测试成功");
    }


    /**
     * 测试 Excel 导出
     * @param request request
     * @param response response
     * @return ResultVo
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissionsCus("gentest_test_export")
    @EnableLog
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "exportExcel");
        QueryBuilder<TestEntity> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        super.excelExport(TestRestApi.SUB_TITLE, queryBuilder.build(), response, method);
    }

    /**
     * 测试 Excel 导入
     * @param request 文件流 request
     * @return ResultVo
     */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @RequiresPermissions("gentest_test_import")
    @EnableLog
    @Override
    public ResultVo<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }

    /**
     * 测试 Excel 下载导入模版
     * @param response response
     * @return ResultVo
     */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissionsCus("gentest_test_import")
    @Override
    public void importTemplate(HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "importTemplate");
        super.importTemplate(TestRestApi.SUB_TITLE, response, method);
    }


}
