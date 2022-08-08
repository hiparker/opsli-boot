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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.role.RoleApi;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.log.annotation.OperateLogger;
import org.opsli.core.log.enums.ModuleEnum;
import org.opsli.core.log.enums.OperationTypeEnum;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.system.role.entity.SysRole;
import org.opsli.modulars.system.role.service.IRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


/**
 * 角色管理 Controller
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Api(tags = "角色管理")
@Slf4j
@ApiRestController("/{ver}/system/role")
public class RoleRestController extends BaseRestController<SysRole, RoleModel, IRoleService>
        implements RoleApi {

    /**
     * 角色 查一条
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得单条角色", notes = "获得单条角色 - ID")
    @PreAuthorize("hasAuthority('system_role_select')")
    @Override
    public ResultWrapper<RoleModel> get(RoleModel model) {
        model = IService.get(model);
        return ResultWrapper.getSuccessResultWrapper(model);
    }

    /**
     * 角色 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    //@PreAuthorize("hasAuthority('system_role_select')")
    @Override
    public ResultWrapper<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysRole> queryBuilder = new WebQueryBuilder<>(IService.getEntityClass(), request.getParameterMap());
        QueryWrapper<SysRole> wrapper = queryBuilder.build();

        Page<SysRole, RoleModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(wrapper);
        page = IService.findPage(page);

        return ResultWrapper.getSuccessResultWrapper(page.getPageData());
    }

    /**
     * 角色 新增
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "新增角色", notes = "新增角色")
    @PreAuthorize("hasAuthority('system_role_insert')")
    @OperateLogger(description = "新增角色",
            module = ModuleEnum.MODULE_ROLE, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> insert(RoleModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("新增角色成功");
    }

    /**
     * 角色 修改
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "修改角色", notes = "修改角色")
    @PreAuthorize("hasAuthority('system_role_update')")
    @OperateLogger(description = "修改角色",
            module = ModuleEnum.MODULE_ROLE, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> update(RoleModel model) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.update(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("修改角色成功");
    }


    /**
     * 角色 删除
     * @param id ID
     * @return ResultWrapper
     */
    @ApiOperation(value = "删除角色数据", notes = "删除角色数据")
    @PreAuthorize("hasAuthority('system_role_delete')")
    @OperateLogger(description = "删除角色数据",
            module = ModuleEnum.MODULE_ROLE, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> del(String id){
        // 演示模式 不允许操作
        super.demoError();

        IService.delete(id);
        return ResultWrapper.getSuccessResultWrapperByMsg("删除角色成功");
    }


    /**
     * 角色 批量删除
     * @param ids ID 数组
     * @return ResultWrapper
     */
    @ApiOperation(value = "批量删除角色数据", notes = "批量删除角色数据")
    @PreAuthorize("hasAuthority('system_role_delete')")
    @OperateLogger(description = "批量删除角色数据",
            module = ModuleEnum.MODULE_ROLE, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> delAll(String ids){
        // 演示模式 不允许操作
        super.demoError();

        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);
        return ResultWrapper.getSuccessResultWrapperByMsg("批量删除角色成功");
    }


    /**
     * 角色 Excel 导出认证
     *
     * @param type 类型
     * @param request request
     */
    @ApiOperation(value = "Excel 导出认证", notes = "Excel 导出认证")
    @PreAuthorize("hasAnyAuthority('system_role_export', 'system_role_import')")
    @Override
    public ResultWrapper<String> exportExcelAuth(String type, HttpServletRequest request) {
        Optional<String> certificateOptional =
                super.excelExportAuth(type, RoleApi.SUB_TITLE, request);
        if(!certificateOptional.isPresent()){
            return ResultWrapper.getErrorResultWrapper();
        }
        return ResultWrapper.getSuccessResultWrapper(certificateOptional.get());
    }


    /**
     * 角色 Excel 导出
     * @param response response
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @PreAuthorize("hasAuthority('system_role_export')")
    @OperateLogger(description = "导出Excel",
            module = ModuleEnum.MODULE_ROLE, operationType = OperationTypeEnum.SELECT, db = true)
    @Override
    public void exportExcel(String certificate, HttpServletResponse response) {
        // 导出Excel
        super.excelExport(certificate, response);
    }


    /**
     * 角色 Excel 导入
     * @param request 文件流 request
     * @return ResultWrapper
     */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @PreAuthorize("hasAuthority('system_role_import')")
    @OperateLogger(description = "角色 Excel 导入",
            module = ModuleEnum.MODULE_ROLE, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }


}
