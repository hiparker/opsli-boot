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
package org.opsli.modulars.system.tenant.web;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.tenant.TenantApi;
import org.opsli.api.wrapper.system.tenant.TenantModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.annotation.RequiresPermissionsCus;
import org.opsli.common.enums.DictType;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.system.tenant.entity.SysTenant;
import org.opsli.modulars.system.tenant.service.ITenantService;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * 租户管理 Controller
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Api(tags = TenantApi.TITLE)
@Slf4j
@ApiRestController("/system/tenant/{ver}")
public class TenantRestController extends BaseRestController<SysTenant, TenantModel, ITenantService>
        implements TenantApi {


    /**
     * 变更租户状态账户
     * @return ResultVo
     */
    @ApiOperation(value = "变更租户状态账户", notes = "变更租户状态账户")
    @RequiresPermissions("system_tenant_enable")
    @EnableLog
    @Override
    public ResultVo<?> enableTenant(String tenantId, String enable) {
        // 演示模式 不允许操作
        super.demoError();

        // 变更租户状态账户
        boolean enableStatus = IService.enableTenant(tenantId, enable);
        if(!enableStatus){
            return ResultVo.error("变更用户状态账户失败");
        }
        return ResultVo.success();
    }

    /**
     * 租户 查一条
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "获得单条租户", notes = "获得单条租户 - ID")
    @RequiresPermissions("system_tenant_select")
    @Override
    public ResultVo<TenantModel> get(TenantModel model) {
        // 如果系统内部调用 则直接查数据库
        if(model != null && model.getIzApi() != null && model.getIzApi()){
            model = IService.get(model);
        }
        return ResultVo.success(model);
    }

    /**
     * 租户 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultVo
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    //@RequiresPermissions("system_tenant_select")
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysTenant> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        Page<SysTenant, TenantModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultVo.success(page.getPageData());
    }

    /**
     * 租户 新增
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "新增租户", notes = "新增租户")
    @RequiresPermissions("system_tenant_insert")
    @EnableLog
    @Override
    public ResultVo<?> insert(TenantModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultVo.success("新增租户成功");
    }

    /**
     * 租户 修改
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "修改租户", notes = "修改租户")
    @RequiresPermissions("system_tenant_update")
    @EnableLog
    @Override
    public ResultVo<?> update(TenantModel model) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改租户成功");
    }


    /**
     * 租户 删除
     * @param id ID
     * @return ResultVo
     */
    @ApiOperation(value = "删除租户数据", notes = "删除租户数据")
    @RequiresPermissions("system_tenant_delete")
    @EnableLog
    @Override
    public ResultVo<?> del(String id){
        // 演示模式 不允许操作
        super.demoError();

        IService.delete(id);
        return ResultVo.success("删除租户成功");
    }


    /**
     * 租户 批量删除
     * @param ids ID 数组
     * @return ResultVo
     */
    @ApiOperation(value = "批量删除租户数据", notes = "批量删除租户数据")
    @RequiresPermissions("system_tenant_delete")
    @EnableLog
    @Override
    public ResultVo<?> delAll(String ids){
        // 演示模式 不允许操作
        super.demoError();

        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);
        return ResultVo.success("批量删除租户成功");
    }


    /**
     * 租户 Excel 导出
     * @param request request
     * @param response response
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissionsCus("system_tenant_export")
    @EnableLog
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "exportExcel");
        QueryBuilder<SysTenant> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        super.excelExport(TenantApi.SUB_TITLE, queryBuilder.build(), response, method);
    }

    /**
     * 租户 Excel 导入
     * @param request 文件流 request
     * @return ResultVo
     */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @RequiresPermissions("system_tenant_import")
    @EnableLog
    @Override
    public ResultVo<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }

    /**
     * 租户 Excel 下载导入模版
     * @param response response
     */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissionsCus("system_tenant_import")
    @Override
    public void importTemplate(HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "importTemplate");
        super.importTemplate(TenantApi.SUB_TITLE, response, method);
    }

    // =========================

    /**
     * 获得已启用租户 查一条
     * @param tenantId 模型
     * @return ResultVo
     */
    @ApiOperation(value = "获得已启用租户", notes = "获得已启用租户 - ID")
    @Override
    public ResultVo<TenantModel> getTenantByUsable(String tenantId) {
        QueryBuilder<SysTenant> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysTenant> queryWrapper = queryBuilder.build();
        queryWrapper.eq("id", tenantId)
                .eq("enable", DictType.NO_YES_YES.getValue());
        SysTenant entity = IService.getOne(queryWrapper);

        return ResultVo.success(
                WrapperUtil.transformInstance(entity, modelClazz)
        );
    }

}
