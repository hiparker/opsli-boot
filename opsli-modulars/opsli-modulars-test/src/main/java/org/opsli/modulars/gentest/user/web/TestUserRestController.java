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

import cn.hutool.core.util.ReflectUtil;
import org.opsli.common.annotation.RequiresPermissionsCus;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.core.base.concroller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opsli.modulars.gentest.user.entity.TestUser;
import org.opsli.api.wrapper.gentest.user.TestUserModel;
import org.opsli.modulars.gentest.user.service.ITestUserService;
import org.opsli.api.web.gentest.user.TestUserRestApi;

import java.lang.reflect.Method;

/**
* @BelongsProject: opsli-boot
* @BelongsPackage: org.opsli.modulars.gentest.user.web
* @Author: 周鹏程
* @CreateTime: 2020-11-22 12:12:05
* @Description: 某系统用户 Controller
*/
@Slf4j
@ApiRestController("/gentest/user")
public class TestUserRestController extends BaseRestController<TestUser, TestUserModel, ITestUserService>
    implements TestUserRestApi {


    /**
    * 用户 查一条
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "获得单条用户", notes = "获得单条用户 - ID")
    @RequiresPermissions("gentest_user_select")
    @Override
    public ResultVo<TestUserModel> get(TestUserModel model) {
        // 如果系统内部调用 则直接查数据库
        if(model != null && model.getIzApi() != null && model.getIzApi()){
            model = IService.get(model);
        }
        return ResultVo.success(model);
    }

    /**
    * 用户 查询分页
    * @param pageNo 当前页
    * @param pageSize 每页条数
    * @param request request
    * @return ResultVo
    */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @RequiresPermissions("gentest_user_select")
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<TestUser> queryBuilder = new WebQueryBuilder<>(TestUser.class, request.getParameterMap());
        Page<TestUser, TestUserModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultVo.success(page.getBootstrapData());
    }

    /**
    * 用户 新增
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "新增用户数据", notes = "新增用户数据")
    @RequiresPermissions("gentest_user_insert")
    @EnableLog
    @Override
    public ResultVo<?> insert(TestUserModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultVo.success("新增用户成功");
    }

    /**
    * 用户 修改
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "修改用户数据", notes = "修改用户数据")
    @RequiresPermissions("gentest_user_update")
    @EnableLog
    @Override
    public ResultVo<?> update(TestUserModel model) {
        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改用户成功");
    }


    /**
    * 用户 删除
    * @param id ID
    * @return ResultVo
    */
    @ApiOperation(value = "删除用户数据", notes = "删除用户数据")
    @RequiresPermissions("gentest_user_update")
    @EnableLog
    @Override
    public ResultVo<?> del(String id){
        IService.delete(id);
        return ResultVo.success("删除用户成功");
    }

    /**
    * 用户 批量删除
    * @param ids ID 数组
    * @return ResultVo
    */
    @ApiOperation(value = "批量删除用户数据", notes = "批量删除用户数据")
    @RequiresPermissions("gentest_user_update")
    @EnableLog
    @Override
    public ResultVo<?> delAll(String[] ids){
        IService.deleteAll(ids);
        return ResultVo.success("批量删除用户成功");
    }


    /**
    * 用户 Excel 导出
    * @param request request
    * @param response response
    * @return ResultVo
    */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissionsCus("gentest_user_export")
    @EnableLog
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "exportExcel");
        QueryBuilder<TestUser> queryBuilder = new WebQueryBuilder<>(TestUser.class, request.getParameterMap());
        super.excelExport(TestUserRestApi.TITLE, queryBuilder.build(), response, method);
    }

    /**
    * 用户 Excel 导入
    * @param request 文件流 request
    * @return ResultVo
    */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @RequiresPermissions("gentest_user_import")
    @EnableLog
    @Override
    public ResultVo<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }

    /**
    * 用户 Excel 下载导入模版
    * @param response response
    * @return ResultVo
    */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissionsCus("gentest_user_import")
    @Override
    public void importTemplate(HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "importTemplate");
        super.importTemplate(TestUserRestApi.TITLE, response, method);
    }

}
