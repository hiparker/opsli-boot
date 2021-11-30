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
package org.opsli.modulars.system.role.web;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.role.RoleApi;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.annotation.RequiresPermissionsCus;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.system.role.entity.SysRole;
import org.opsli.modulars.system.role.service.IRoleService;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * 角色管理 Controller
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Api(tags = "角色管理")
@Slf4j
@ApiRestController("/system/role/{ver}")
public class RoleRestController extends BaseRestController<SysRole, RoleModel, IRoleService>
        implements RoleApi {

    /**
     * 角色 查一条
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "获得单条角色", notes = "获得单条角色 - ID")
    @RequiresPermissions("system_role_select")
    @Override
    public ResultVo<RoleModel> get(RoleModel model) {
        // 如果系统内部调用 则直接查数据库
        if(model != null && model.getIzApi() != null && model.getIzApi()){
            model = IService.get(model);
        }
        return ResultVo.success(model);
    }

    /**
     * 角色 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultVo
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    //@RequiresPermissions("system_role_select")
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysRole> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        QueryWrapper<SysRole> wrapper = queryBuilder.build();

        Page<SysRole, RoleModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(wrapper);
        page = IService.findPage(page);

        return ResultVo.success(page.getPageData());
    }

    /**
     * 角色 新增
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "新增角色", notes = "新增角色")
    @RequiresPermissions("system_role_insert")
    @EnableLog
    @Override
    public ResultVo<?> insert(RoleModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultVo.success("新增角色成功");
    }

    /**
     * 角色 修改
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "修改角色", notes = "修改角色")
    @RequiresPermissions("system_role_update")
    @EnableLog
    @Override
    public ResultVo<?> update(RoleModel model) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改角色成功");
    }


    /**
     * 角色 删除
     * @param id ID
     * @return ResultVo
     */
    @ApiOperation(value = "删除角色数据", notes = "删除角色数据")
    @RequiresPermissions("system_role_delete")
    @EnableLog
    @Override
    public ResultVo<?> del(String id){
        // 演示模式 不允许操作
        super.demoError();

        IService.delete(id);
        return ResultVo.success("删除角色成功");
    }


    /**
     * 角色 批量删除
     * @param ids ID 数组
     * @return ResultVo
     */
    @ApiOperation(value = "批量删除角色数据", notes = "批量删除角色数据")
    @RequiresPermissions("system_role_delete")
    @EnableLog
    @Override
    public ResultVo<?> delAll(String ids){
        // 演示模式 不允许操作
        super.demoError();

        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);
        return ResultVo.success("批量删除角色成功");
    }


    /**
     * 角色 Excel 导出
     * @param request request
     * @param response response
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissionsCus("system_role_export")
    @EnableLog
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "exportExcel");
        QueryBuilder<SysRole> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        super.excelExport(RoleApi.SUB_TITLE, queryBuilder.build(), response, method);
    }

    /**
     * 角色 Excel 导入
     * @param request 文件流 request
     * @return ResultVo
     */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @RequiresPermissions("system_role_import")
    @EnableLog
    @Override
    public ResultVo<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }

    /**
     * 角色 Excel 下载导入模版
     * @param response response
     */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissionsCus("system_role_import")
    @EnableLog
    @Override
    public void importTemplate(HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "importTemplate");
        super.importTemplate(RoleApi.SUB_TITLE, response, method);
    }

}
