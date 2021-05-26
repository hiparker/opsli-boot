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
package org.opsli.modulars.generator.importable.service;

import org.apache.commons.lang3.StringUtils;
import org.opsli.common.utils.Props;
import org.opsli.plugins.generator.database.mysql.MySqlSyncColumnType;
import org.opsli.plugins.generator.enums.DataBaseType;
import org.opsli.modulars.generator.importable.entity.DatabaseColumn;
import org.opsli.modulars.generator.importable.entity.DatabaseTable;
import org.opsli.modulars.generator.importable.mapper.MySQLDatabaseTableMapper;
import org.opsli.modulars.generator.table.service.IGenTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 代码生成器 - 表 接口实现类
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
@Service
public class MySQLDatabaseTableServiceImpl implements DatabaseTableService {

    /** 排除表 */
    private static final List<String> EXCLUDE_TABLES;

    static {
        Props props = new Props("generator.yaml");
        EXCLUDE_TABLES = props.getList("opsli.exclude-tables");
    }

    @Autowired(required = false)
    private MySQLDatabaseTableMapper mapper;

    @Autowired
    private IGenTableService iGenTableService;

    @Override
    public DataBaseType getType() {
        return DataBaseType.MYSQL;
    }

    @Override
    public List<String> getFieldTypes() {
        return MySqlSyncColumnType.INSTANCE.getFieldTypes();
    }

    @Override
    public Map<String, String> getJavaFieldTypes() {
        return MySqlSyncColumnType.INSTANCE.getJavaFieldTypes();
    }

    @Override
    public Map<String, List<String>> getJavaFieldTypesBySafety() {
        return MySqlSyncColumnType.INSTANCE.getJavaFieldTypesBySafety();
    }

    @Override
    public List<DatabaseTable> findTables(String dbName) {
        return this.findTables(dbName, null);
    }

    @Override
    public List<DatabaseTable> findTables(String dbName, String tableName) {
        DatabaseTable table = new DatabaseTable();
        table.setDbName(dbName);

        if(StringUtils.isNotEmpty(tableName)){
            table.setTableName(tableName);
        }

        List<DatabaseTable> tables = mapper.findTables(table);

        // 表去重复
        List<String> currTableNames = iGenTableService.findAllByTableName();
        if(currTableNames != null && !currTableNames.isEmpty()){
            //遍历删除
            tables.removeIf(tmp -> currTableNames.contains(tmp.getTableName()));
            //遍历删除
            tables.removeIf(tmp -> EXCLUDE_TABLES.contains(tmp.getTableName()));
        }

        return tables;
    }


    @Override
    public List<DatabaseColumn> findColumns(String dbName, String tableName) {
        DatabaseColumn entity = new DatabaseColumn();
        entity.setDbName(dbName);
        entity.setTableName(tableName);

        List<DatabaseColumn> columns = mapper.findColumns(entity);

        // 设置字段长度
        for (DatabaseColumn column : columns) {
            // MySQL 中 这两个 有一个会代表为当前字段长度
            Integer len1 = column.getColumnLength();
            Integer len2 = column.getColumnPrecision();
            if(len1 == null && len2 != null){
                column.setColumnLength(len2);
            }
            // 如果小数位不为空 则需要 减掉小数位置
            if(column.getColumnScale() != null){
                column.setColumnLength(
                        column.getColumnLength() - column.getColumnScale()
                );
            }
        }

        return columns;
    }
}


