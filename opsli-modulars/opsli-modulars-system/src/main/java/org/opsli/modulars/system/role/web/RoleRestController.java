package org.opsli.modulars.system.role.web;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.role.RoleApi;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.base.concroller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.system.role.entity.SysRole;
import org.opsli.modulars.system.role.service.IRoleService;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 角色
 */
@Slf4j
@ApiRestController("/sys/role")
public class RoleRestController extends BaseRestController<SysRole, RoleModel, IRoleService>
        implements RoleApi {


    /**
     * 角色 查一条
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "获得单条字典明细数据", notes = "获得单条字典明细数据 - ID")
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
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysRole> queryBuilder = new WebQueryBuilder<>(SysRole.class, request.getParameterMap());
        Page<SysRole, RoleModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultVo.success(page.getBootstrapData());
    }

    /**
     * 角色 新增
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "新增字典明细数据", notes = "新增字典明细数据")
    @Override
    public ResultVo<?> insert(RoleModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultVo.success("新增字典明细数据成功");
    }

    /**
     * 角色 修改
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "修改字典明细数据", notes = "修改字典明细数据")
    @Override
    public ResultVo<?> update(RoleModel model) {
        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改字典明细数据成功");
    }


    /**
     * 角色 删除
     * @param id ID
     * @return ResultVo
     */
    @ApiOperation(value = "删除字典明细数据数据", notes = "删除字典明细数据数据")
    @Override
    public ResultVo<?> del(String id){
        IService.delete(id);
        return ResultVo.success("删除字典明细数据成功");
    }


    /**
     * 角色 批量删除
     * @param ids ID 数组
     * @return ResultVo
     */
    @ApiOperation(value = "批量删除字典明细数据数据", notes = "批量删除字典明细数据数据")
    @Override
    public ResultVo<?> delAll(String[] ids){
        IService.deleteAll(ids);
        return ResultVo.success("批量删除字典明细数据成功");
    }


    /**
     * 角色 Excel 导出
     * @param request request
     * @param response response
     * @return ResultVo
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @Override
    public ResultVo<?> exportExcel(HttpServletRequest request, HttpServletResponse response) {
        QueryBuilder<SysRole> queryBuilder = new WebQueryBuilder<>(SysRole.class, request.getParameterMap());
        return super.excelExport(RoleApi.TITLE, queryBuilder.build(), response);
    }

    /**
     * 角色 Excel 导入
     * @param request 文件流 request
     * @return ResultVo
     */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @Override
    public ResultVo<?> excelImport(MultipartHttpServletRequest request) {
        return super.excelImport(request);
    }

    /**
     * 角色 Excel 下载导入模版
     * @param response response
     * @return ResultVo
     */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @Override
    public ResultVo<?> importTemplate(HttpServletResponse response) {
        return super.importTemplate(RoleApi.TITLE, response);
    }

}
