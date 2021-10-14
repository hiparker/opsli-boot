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
package org.opsli.modulars.generator.table.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.plugins.generator.exception.GeneratorException;
import org.opsli.plugins.generator.msg.GeneratorMsg;
import org.opsli.modulars.generator.column.service.IGenTableColumnService;
import org.opsli.modulars.generator.column.wrapper.GenTableColumnModel;
import org.opsli.modulars.generator.logs.service.IGenLogsService;
import org.opsli.modulars.generator.importable.ImportTableUtil;
import org.opsli.modulars.generator.importable.entity.DatabaseColumn;
import org.opsli.modulars.generator.importable.entity.DatabaseTable;
import org.opsli.modulars.generator.table.entity.GenTable;
import org.opsli.modulars.generator.table.mapper.GenTableMapper;
import org.opsli.modulars.generator.table.service.IGenTableService;
import org.opsli.modulars.generator.table.wrapper.GenTableAndColumnModel;
import org.opsli.modulars.generator.table.wrapper.GenTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * 代码生成器 - 表 接口实现类
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
@Service
public class GenTableServiceImpl extends CrudServiceImpl<GenTableMapper, GenTable, GenTableModel>
        implements IGenTableService {

    @Autowired(required = false)
    private GenTableMapper mapper;

    @Autowired
    private IGenTableColumnService iGenTableColumnService;

    @Autowired
    private IGenLogsService iGenLogsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenTableModel insert(GenTableModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        boolean verificationByTableName = this.uniqueVerificationByTableName(model);
        if(!verificationByTableName){
            // 重复
            throw new GeneratorException(GeneratorMsg.EXCEPTION_TABLE_NAME_REPEAT);
        }

        if(!model.getIzApi()){
            // 新增后 默认未同步
            model.setIzSync(
                    DictType.NO_YES_NO.getValue());
        }

        // 默认旧表名称为当前新增名称（用于删除表操作）
        model.setOldTableName(model.getTableName());

        return super.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenTableModel update(GenTableModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        boolean verificationByTableName = this.uniqueVerificationByTableName(model);
        if(!verificationByTableName){
            // 重复
            throw new GeneratorException(GeneratorMsg.EXCEPTION_TABLE_NAME_REPEAT);
        }

        GenTableModel oldModel = this.get(model.getId());


        // 修改后 默认未同步
        model.setIzSync(
                DictType.NO_YES_NO.getValue());

        // 默认旧表名称为 修改前表名 便于改表后删除操作
        model.setOldTableName(oldModel.getTableName());

        return super.update(model);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertAny(GenTableAndColumnModel model){
        GenTableModel tableModel = WrapperUtil.transformInstance(model, GenTableModel.class);
        // 保存表头数据
        tableModel = this.insert(tableModel);
        if(tableModel != null){
            // 删除旧字段 全部新增
            iGenTableColumnService.delByTableId(tableModel.getId());

            // 保存 表结构数据
            List<GenTableColumnModel> columnList = model.getColumnList();
            for (GenTableColumnModel tableColumnModel : columnList) {
                tableColumnModel.setTableId(tableModel.getId());
            }
            iGenTableColumnService.insertBatch(columnList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAny(GenTableAndColumnModel model){
        GenTableModel tableModel = WrapperUtil.transformInstance(model, GenTableModel.class);
        // 保存表头数据
        tableModel = this.update(tableModel);
        if(tableModel != null){
            // 删除旧字段 全部新增
            iGenTableColumnService.delByTableId(tableModel.getId());

            // 保存 表结构数据
            List<GenTableColumnModel> columnList = model.getColumnList();
            for (GenTableColumnModel tableColumnModel : columnList) {
                tableColumnModel.setTableId(tableModel.getId());
            }
            iGenTableColumnService.insertBatch(columnList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIdAny(String id) throws Exception{
        // 删除表头
        boolean delFlag = super.delete(id);
        if(!delFlag){
            throw new Exception();
        }
        // 删除字段
        iGenTableColumnService.delByTableId(id);

        // 删除生成记录
        iGenLogsService.delByTableId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIdsAny(String[] ids) throws Exception{
        // 删除表头
        boolean delFlag = super.deleteAll(ids);
        if(!delFlag){
            throw new Exception();
        }

        // 删除字段
        iGenTableColumnService.delByTableIds(ids);

        // 删除生成记录
        iGenLogsService.delByTableIds(ids);
    }

    @Override
    public void renewSyncState(String id) {
        mapper.renewSyncState(id);
    }


    @Override
    public List<String> findAllByTableName() {
        return mapper.findAllByTableName();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importTables(String[] tableNames) {
        for (String tableName : tableNames) {
            // 获得当前表
            DatabaseTable table = null;
            List<DatabaseTable> tables = ImportTableUtil.findTables(tableName);
            if(tables != null && !tables.isEmpty()){
                table = tables.get(0);
            }

            if(table == null){
                String msg = StrUtil.format(GeneratorMsg.EXCEPTION_IMPORT_TABLE_NULL.getMessage(), tableName);
                // 暂无该表
                throw new GeneratorException(GeneratorMsg.EXCEPTION_IMPORT_TABLE_NULL.getCode(), msg);
            }

            // 获得表字段
            List<DatabaseColumn> columns = ImportTableUtil.findColumns(tableName);
            if(CollUtil.isEmpty(columns)){
                // 暂未获得表字段
                throw new GeneratorException(GeneratorMsg.EXCEPTION_IMPORT_FIELD_NULL);
            }

            List<GenTableColumnModel> columnModels = new ArrayList<>();
            for (int i = 0; i < columns.size(); i++) {
                DatabaseColumn column = columns.get(i);
                GenTableColumnModel columnModel = new GenTableColumnModel();
                columnModel.setFieldName(column.getColumnName());
                columnModel.setFieldType(column.getColumnType());
                columnModel.setFieldLength(column.getColumnLength());
                columnModel.setFieldPrecision(column.getColumnScale());
                columnModel.setFieldComments(column.getColumnComment());
                columnModel.setIzPk(column.getIzPk());
                columnModel.setIzNotNull(column.getIzNotNull());
                columnModel.setSort(i);
                // 赋默认值
                columnModel.setJavaType("String");
                columnModels.add(columnModel);
            }

            // 生成本地数据
            GenTableAndColumnModel genTableModel = new GenTableAndColumnModel();
            genTableModel.setComments(table.getTableComments());
            genTableModel.setTableName(table.getTableName());
            genTableModel.setOldTableName(table.getTableName());
            genTableModel.setIzSync("1");
            genTableModel.setJdbcType(ImportTableUtil.getDbType()!=null?ImportTableUtil.getDbType().getDb():null);
            genTableModel.setTableType("0");
            genTableModel.setColumnList(columnModels);
            genTableModel.setIzApi(true);
            this.insertAny(genTableModel);
        }
    }


    // =======================

    /**
     * 唯一验证
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public boolean uniqueVerificationByTableName(GenTableModel model){
        if(model == null){
            return false;
        }
        QueryWrapper<GenTable> wrapper = new QueryWrapper<>();

        // code 唯一
        wrapper.eq("table_name", model.getTableName());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper) == 0;
    }
}


