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


import cn.hutool.core.util.ReflectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.annotation.RequiresPermissionsCus;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.generator.template.api.GenTemplateRestApi;
import org.opsli.modulars.generator.template.wrapper.GenTemplateAndDetailModel;
import org.opsli.modulars.generator.template.wrapper.GenTemplateCopyModel;
import org.opsli.modulars.generator.template.wrapper.GenTemplateModel;
import org.opsli.plugins.generator.utils.GeneratorHandleUtil;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


import org.opsli.modulars.generator.template.entity.GenTemplate;
import org.opsli.modulars.generator.template.service.IGenTemplateService;


/**
 * 代码模板 Controller
 *
 * @author 周鹏程
 * @date 2021-05-27 14:33:49
 */
@Api(tags = GenTemplateRestApi.TITLE)
@Slf4j
@ApiRestController("/generator/template/{ver}")
public class GenTemplateRestController extends BaseRestController<GenTemplate, GenTemplateModel, IGenTemplateService>
    implements GenTemplateRestApi {


    /**
    * 代码模板 查一条
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "获得单条代码模板", notes = "获得单条代码模板 - ID")
    @RequiresPermissions("generator_template_select")
    @Override
    public ResultVo<GenTemplateModel> get(GenTemplateModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 如果系统内部调用 则直接查数据库
        if(model != null && model.getIzApi() != null && model.getIzApi()){
            model = IService.get(model);
        }
        return ResultVo.success(model);
    }

    /**
    * 代码模板 查询分页
    * @param pageNo 当前页
    * @param pageSize 每页条数
    * @param request request
    * @return ResultVo
    */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @RequiresPermissions("generator_template_select")
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        QueryBuilder<GenTemplate> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        Page<GenTemplate, GenTemplateModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultVo.success(page.getPageData());
    }

    /**
    * 代码模板 新增
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "新增代码模板数据", notes = "新增代码模板数据")
    @RequiresPermissions("generator_template_insert")
    @EnableLog
    @Override
    public ResultVo<?> insert(GenTemplateModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        super.demoError();

        // 调用新增方法
        IService.insert(model);
        return ResultVo.success("新增代码模板成功");
    }

    /**
     * 代码模板 新增 和 明细
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "新增代码模板数据", notes = "新增代码模板数据")
    @RequiresPermissions("generator_template_insert")
    @EnableLog
    @Override
    public ResultVo<?> insertAndDetail(GenTemplateAndDetailModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        super.demoError();

        // 调用新增方法
        IService.insertAndDetail(model);
        return ResultVo.success("新增代码模板成功");
    }

    /**
    * 代码模板 修改
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "修改代码模板数据", notes = "修改代码模板数据")
    @RequiresPermissions("generator_template_update")
    @EnableLog
    @Override
    public ResultVo<?> update(GenTemplateModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改代码模板成功");
    }

    /**
     * 代码模板 修改 和 明细
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "修改代码模板数据", notes = "修改代码模板数据")
    @RequiresPermissions("generator_template_update")
    @EnableLog
    @Override
    public ResultVo<?> updateAndDetail(GenTemplateAndDetailModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.updateAndDetail(model);
        return ResultVo.success("修改代码模板成功");
    }

    /**
     * 复制 代码模板
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "新增代码模板数据", notes = "新增代码模板数据")
    @RequiresPermissions("generator_template_copy")
    @EnableLog
    @Override
    public ResultVo<?> copy(GenTemplateCopyModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 调用复制方法
        IService.copy(model);
        return ResultVo.success("复制代码模板成功");
    }

    /**
    * 代码模板 删除
    * @param id ID
    * @return ResultVo
    */
    @ApiOperation(value = "删除代码模板数据", notes = "删除代码模板数据")
    @RequiresPermissions("generator_template_update")
    @EnableLog
    @Override
    public ResultVo<?> del(String id){
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        super.demoError();

        IService.delete(id);
        return ResultVo.success("删除代码模板成功");
    }

    /**
    * 代码模板 Excel 导出
    * 注：这里 RequiresPermissionsCus 引入的是 自定义鉴权注解
    *
    * 导出时，Token认证和方法权限认证 全部都由自定义完成
    * 因为在 导出不成功时，需要推送错误信息，
    * 前端直接走下载流，当失败时无法获得失败信息，即使前后端换一种方式后端推送二进制文件前端再次解析也是最少2倍的耗时
    * ，且如果数据量过大，前端进行渲染时直接会把浏览器卡死
    * 而直接开启socket接口推送显然是太过浪费资源了，所以目前采用Java最原始的手段
    * response 推送 javascript代码 alert 提示报错信息
    *
    * @param request request
    * @param response response
    */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissionsCus("generator_template_export")
    @EnableLog
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "exportExcel");
        QueryBuilder<GenTemplate> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        super.excelExport(GenTemplateRestApi.SUB_TITLE, queryBuilder.build(), response, method);
    }

    /**
    * 代码模板 Excel 导入
    * 注：这里 RequiresPermissions 引入的是 Shiro原生鉴权注解
    * @param request 文件流 request
    * @return ResultVo
    */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @RequiresPermissions("generator_template_import")
    @EnableLog
    @Override
    public ResultVo<?> importExcel(MultipartHttpServletRequest request) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        return super.importExcel(request);
    }

    /**
    * 代码模板 Excel 下载导入模版
    * 注：这里 RequiresPermissionsCus 引入的是 自定义鉴权注解
    * @param response response
    */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissionsCus("generator_template_import")
    @Override
    public void importTemplate(HttpServletResponse response) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "importTemplate");
        super.importTemplate(GenTemplateRestApi.SUB_TITLE, response, method);
    }

}
