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
package org.opsli.modulars.generator.table.web;

import cn.hutool.core.convert.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.core.log.annotation.OperateLogger;
import org.opsli.core.log.enums.ModuleEnum;
import org.opsli.core.log.enums.OperationTypeEnum;
import org.springframework.security.access.prepost.PreAuthorize;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.common.annotation.ApiRestController;

import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.plugins.generator.exception.GeneratorException;
import org.opsli.plugins.generator.msg.GeneratorMsg;
import org.opsli.plugins.generator.SqlSyncUtil;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.generator.column.service.IGenTableColumnService;
import org.opsli.modulars.generator.column.wrapper.GenTableColumnModel;
import org.opsli.modulars.generator.importable.ImportTableUtil;
import org.opsli.modulars.generator.table.api.TableApi;
import org.opsli.modulars.generator.table.entity.GenTable;
import org.opsli.modulars.generator.table.service.IGenTableService;
import org.opsli.modulars.generator.table.wrapper.GenTableAndColumnModel;
import org.opsli.modulars.generator.table.wrapper.GenTableModel;
import org.opsli.plugins.generator.utils.GeneratorHandleUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * 代码生成器 - 表
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
@Api(tags = TableApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/generator/table")
public class GenTableRestController extends BaseRestController<GenTable, GenTableModel, IGenTableService>
        implements TableApi {

    @Autowired
    private IGenTableColumnService iGenTableColumnService;


    /**
     * 表 查一条
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得单条表", notes = "获得单条表 - ID")
    @PreAuthorize("hasAuthority('dev_generator_select')")
    @Override
    public ResultWrapper<GenTableAndColumnModel> get(GenTableModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        model = IService.get(model);

        GenTableAndColumnModel currModel = WrapperUtil.transformInstance(
                model, GenTableAndColumnModel.class
        );

        if(currModel == null){
            // 暂无数据
            throw new GeneratorException(GeneratorMsg.EXCEPTION_OTHER_NULL);
        }

        List<GenTableColumnModel> columnModelList = iGenTableColumnService.
                getByTableId(currModel.getId());

        currModel.setColumnList(columnModelList);

        return ResultWrapper.getSuccessResultWrapper(currModel);
    }

    /**
     * 表 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @PreAuthorize("hasAuthority('dev_generator_select')")
    @Override
    public ResultWrapper<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        QueryBuilder<GenTable> queryBuilder = new WebQueryBuilder<>(GenTable.class, request.getParameterMap());
        Page<GenTable, GenTableModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultWrapper.getSuccessResultWrapper(page.getPageData());
    }

    /**
     * 表 新增
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "新增表", notes = "新增表")
    @PreAuthorize("hasAuthority('dev_generator_insert')")
    @OperateLogger(description = "新增表",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> insert(GenTableAndColumnModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        //super.demoError();

        // 调用新增方法
        IService.insertAny(model);

        return ResultWrapper.getSuccessResultWrapperByMsg("新增表成功");
    }

    /**
     * 表 修改
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "修改表", notes = "修改表")
    @PreAuthorize("hasAuthority('dev_generator_update')")
    @OperateLogger(description = "修改表",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> update(GenTableAndColumnModel model) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        //super.demoError();

        // 调用修改方法
        IService.updateAny(model);

        return ResultWrapper.getSuccessResultWrapperByMsg("修改表成功");
    }


    /**
     * 表 删除
     * @param id ID
     * @return ResultWrapper
     */
    @ApiOperation(value = "删除表数据", notes = "删除表数据")
    @PreAuthorize("hasAuthority('dev_generator_delete')")
    @OperateLogger(description = "删除表数据",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> del(String id){
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        //super.demoError();

        try {
            IService.removeByIdAny(id);
        } catch (ServiceException e){
            throw e;
        } catch (Exception e){
            throw new ServiceException(CoreMsg.SQL_EXCEPTION_DELETE);
        }
        return ResultWrapper.getSuccessResultWrapperByMsg("删除表成功");
    }


    /**
     * 表 批量删除
     * @param ids ID 数组
     * @return ResultWrapper
     */
    @ApiOperation(value = "批量删除表数据", notes = "批量删除表数据")
    @PreAuthorize("hasAuthority('dev_generator_delete')")
    @OperateLogger(description = "批量删除表数据",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> delAll(String ids){
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        //super.demoError();
        String[] idArray = Convert.toStrArray(ids);

        try {
            IService.removeByIdsAny(idArray);
        } catch (ServiceException e){
            throw e;
        } catch (Exception e){
            throw new ServiceException(CoreMsg.SQL_EXCEPTION_DELETE);
        }

        return ResultWrapper.getSuccessResultWrapperByMsg("批量删除表成功");
    }

    /**
     * 表 同步
     * @param id ID
     * @return ResultWrapper
     */
    @ApiOperation(value = "同步数据库表", notes = "同步数据库表")
    @PreAuthorize("hasAuthority('dev_generator_sync')")
    @OperateLogger(description = "同步数据库表",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> sync(String id){
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        // 演示模式 不允许操作
        super.demoError();

        GenTableAndColumnModel currModel = WrapperUtil.transformInstance(
                IService.get(id), GenTableAndColumnModel.class
        );

        if(currModel == null){
            return ResultWrapper.getErrorResultWrapper().setMsg("同步失败");
        }

        List<GenTableColumnModel> columnModelList = iGenTableColumnService.
                getByTableId(currModel.getId());

        currModel.setColumnList(columnModelList);

        // 执行同步操作
        SqlSyncUtil.execute(currModel);

        return ResultWrapper.getSuccessResultWrapperByMsg("同步成功");
    }

    @ApiOperation(value = "获得当前数据库表", notes = "获得当前数据库表")
    @PreAuthorize("hasAuthority('dev_generator_select')")
    @Override
    public ResultWrapper<?> getTables() {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        return ResultWrapper.getSuccessResultWrapper(ImportTableUtil.findTables());
    }

    @ApiOperation(value = "导入数据库表", notes = "导入数据库表")
    @PreAuthorize("hasAuthority('dev_generator_import')")
    @OperateLogger(description = "导入数据库表",
            module = ModuleEnum.MODULE_GENERATOR, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> importTables(String tableNames) {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        String[] tableNameArray = Convert.toStrArray(tableNames);
        if(tableNameArray == null){
            // 未选中表，无法导入
            throw new GeneratorException(GeneratorMsg.EXCEPTION_IMPORT_NULL);
        }
        IService.importTables(tableNameArray);
        return ResultWrapper.getSuccessResultWrapperByMsg("导入成功");
    }

    @ApiOperation(value = "获得数据库类型下 字段类型",
            notes = "获得数据库类型下 字段类型")
    @Override
    public ResultWrapper<List<String>> getFieldTypes() {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        return ResultWrapper.getSuccessResultWrapper(ImportTableUtil.getFieldTypes());
    }

    @ApiOperation(value = "获得数据库类型下 全部类型对应Java类型",
            notes = "获得数据库类型下 全部类型对应Java类型")
    @Override
    public ResultWrapper<Map<String, String>> getJavaFieldTypes() {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        return ResultWrapper.getSuccessResultWrapper(ImportTableUtil.getJavaFieldTypes());
    }

    @ApiOperation(value = "获得全部类型对应Java类型集合（兜底String 类型）",
            notes = "获得全部类型对应Java类型集合（兜底String 类型）")
    @Override
    public ResultWrapper<Map<String, List<String>>> getJavaFieldTypesBySafety() {
        // 判断代码生成器 是否启用
        GeneratorHandleUtil.judgeGeneratorEnable(super.globalProperties);

        return ResultWrapper.getSuccessResultWrapper(ImportTableUtil.getJavaFieldTypesBySafety());
    }
}
