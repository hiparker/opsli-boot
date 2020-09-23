package org.opsli.modulars.system.dict.web;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.dict.DictDetailApi;
import org.opsli.api.wrapper.system.dict.SysDictDetailModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.concroller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.system.dict.entity.SysDictDetail;
import org.opsli.modulars.system.dict.service.IDictDetailService;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 数据字典明细
 */
@Slf4j
@ApiRestController("/dict/detail")
public class DictDetailRestController extends BaseRestController<SysDictDetailModel, SysDictDetail, IDictDetailService>
        implements DictDetailApi {


    /**
     * 数据字典 查一条
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "获得单条数据", notes = "获得单条数据 - ID")
    @Override
    public ResultVo<SysDictDetailModel> get(SysDictDetailModel model) {
        return ResultVo.success(model);
    }

    /**
     * 数据字典 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultVo
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysDictDetail> queryBuilder = new WebQueryBuilder<>(SysDictDetail.class, request.getParameterMap());
        Page<SysDictDetailModel, SysDictDetail> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultVo.success(page.getBootstrapData());
    }

    /**
     * 数据字典 新增
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "新增数据", notes = "新增数据")
    @Override
    public ResultVo<?> insert(SysDictDetailModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultVo.success("新增成功");
    }

    /**
     * 数据字典 修改
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "修改数据", notes = "修改数据")
    @Override
    public ResultVo<?> update(SysDictDetailModel model) {
        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改成功");
    }


    /**
     * 数据字典 删除
     * @param id ID
     * @return ResultVo
     */
    @ApiOperation(value = "删除数据", notes = "删除数据")
    @Override
    public ResultVo<?> del(String id){
        IService.delete(id);
        return ResultVo.success("删除对象成功");
    }


    /**
     * 数据字典 批量删除
     * @param ids ID 数组
     * @return ResultVo
     */
    @ApiOperation(value = "删除全部数据", notes = "删除全部数据")
    @Override
    public ResultVo<?> delAll(String[] ids){
        IService.deleteAll(ids);
        return ResultVo.success("删除对象成功");
    }


    /**
     * 数据字典 Excel 导出
     * @param request request
     * @param response response
     * @return ResultVo
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @Override
    public ResultVo<?> exportExcel(HttpServletRequest request, HttpServletResponse response) {
        QueryBuilder<SysDictDetail> queryBuilder = new WebQueryBuilder<>(SysDictDetail.class, request.getParameterMap());
        return super.excelExport(DictDetailApi.TITLE, queryBuilder.build(), response);
    }

    /**
     * 数据字典 Excel 导入
     * @param request 文件流 request
     * @return ResultVo
     */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @Override
    public ResultVo<?> excelImport(MultipartHttpServletRequest request) {
        return super.excelImport(request);
    }

    /**
     * 数据字典 Excel 下载导入模版
     * @param response response
     * @return ResultVo
     */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @Override
    public ResultVo<?> importTemplate(HttpServletResponse response) {
        return super.importTemplate(DictDetailApi.TITLE, response);
    }

    /**
     * 根据字典类型编号 查询出所有字典
     *
     * @param typeCode 字典类型编号
     * @return
     */
    @ApiOperation(value = "根据字典类型编号 查询出所有字典", notes = "根据字典类型编号 查询出所有字典")
    // 权限
    @Override
    public ResultVo<List<SysDictDetailModel>> findListByTypeCode(String typeCode) {
        return ResultVo.success(IService.findListByTypeCode(typeCode));
    }
}
