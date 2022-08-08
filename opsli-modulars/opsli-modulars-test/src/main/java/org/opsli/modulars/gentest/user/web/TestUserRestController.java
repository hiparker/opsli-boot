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
package org.opsli.modulars.gentest.user.web;

import cn.hutool.core.convert.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.gentest.user.TestUserRestApi;
import org.opsli.api.wrapper.gentest.user.TestUserModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.log.annotation.OperateLogger;
import org.opsli.core.log.enums.ModuleEnum;
import org.opsli.core.log.enums.OperationTypeEnum;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.gentest.user.entity.TestUser;
import org.opsli.modulars.gentest.user.service.ITestUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 某系统用户 Controller
 *
 * @author Parker
 * @date 2020-11-22 12:12:05
 */
@Api(tags = TestUserRestApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/gentest/user")
public class TestUserRestController extends BaseRestController<TestUser, TestUserModel, ITestUserService>
        implements TestUserRestApi {


    /**
     * 用户 查一条
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得单条用户", notes = "获得单条用户 - ID")
    @PreAuthorize("hasAuthority('gentest_user_select')")
    @Override
    public ResultWrapper<TestUserModel> get(TestUserModel model) {
        model = IService.get(model);
        return ResultWrapper.getSuccessResultWrapper(model);
    }

    /**
     * 用户 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @PreAuthorize("hasAuthority('gentest_user_select')")
    @Override
    public ResultWrapper<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<TestUser> queryBuilder = new WebQueryBuilder<>(TestUser.class, request.getParameterMap());
        Page<TestUser, TestUserModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultWrapper.getSuccessResultWrapper(page.getPageData());
    }

    /**
     * 用户 新增
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "新增用户数据", notes = "新增用户数据")
    @PreAuthorize("hasAuthority('gentest_user_insert')")
    @OperateLogger(description = "新增用户数据",
            module = ModuleEnum.MODULE_TEST_USER, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> insert(TestUserModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("新增用户成功");
    }

    /**
     * 用户 修改
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "修改用户数据", notes = "修改用户数据")
    @PreAuthorize("hasAuthority('gentest_user_update')")
    @OperateLogger(description = "修改用户数据",
            module = ModuleEnum.MODULE_TEST_USER, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> update(TestUserModel model) {
        // 调用修改方法
        IService.update(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("修改用户成功");
    }


    /**
     * 用户 删除
     * @param id ID
     * @return ResultWrapper
     */
    @ApiOperation(value = "删除用户数据", notes = "删除用户数据")
    @PreAuthorize("hasAuthority('gentest_user_update')")
    @OperateLogger(description = "删除用户数据",
            module = ModuleEnum.MODULE_TEST_USER, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> del(String id){
        IService.delete(id);
        return ResultWrapper.getSuccessResultWrapperByMsg("删除用户成功");
    }

    /**
     * 用户 批量删除
     * @param ids ID 数组
     * @return ResultWrapper
     */
    @ApiOperation(value = "批量删除用户数据", notes = "批量删除用户数据")
    @PreAuthorize("hasAuthority('gentest_user_update')")
    @OperateLogger(description = "批量删除用户数据",
            module = ModuleEnum.MODULE_TEST_USER, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> delAll(String ids){
        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);
        return ResultWrapper.getSuccessResultWrapperByMsg("批量删除用户成功");
    }



    /**
     * 用户 Excel 导出认证
     *
     * @param type 类型
     * @param request request
     */
    @ApiOperation(value = "Excel 导出认证", notes = "Excel 导出认证")
    @PreAuthorize("hasAnyAuthority('gentest_user_export', 'gentest_user_import')")
    @Override
    public ResultWrapper<String> exportExcelAuth(String type, HttpServletRequest request) {
        Optional<String> certificateOptional =
                super.excelExportAuth(type, TestUserRestApi.SUB_TITLE, request);
        if(!certificateOptional.isPresent()){
            return ResultWrapper.getErrorResultWrapper();
        }
        return ResultWrapper.getSuccessResultWrapper(certificateOptional.get());
    }


    /**
     * 用户 Excel 导出
     * @param response response
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @PreAuthorize("hasAuthority('gentest_user_export')")
    @OperateLogger(description = "导出Excel",
            module = ModuleEnum.MODULE_TEST_USER, operationType = OperationTypeEnum.SELECT, db = true)
    @Override
    public void exportExcel(String certificate, HttpServletResponse response) {
        // 导出Excel
        super.excelExport(certificate, response);
    }

    /**
     * 用户 Excel 导入
     * 注：这里 RequiresPermissions 引入的是 Shiro原生鉴权注解
     * @param request 文件流 request
     * @return ResultWrapper
     */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @PreAuthorize("hasAuthority('gentest_user_import')")
    @OperateLogger(description = "用户 Excel 导入",
            module = ModuleEnum.MODULE_TEST_USER, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }


}
