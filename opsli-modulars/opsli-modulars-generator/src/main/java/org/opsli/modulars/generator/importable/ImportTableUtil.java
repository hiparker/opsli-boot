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
package org.opsli.modulars.generator.importable;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ClassUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.core.autoconfigure.properties.DbSourceProperties;
import org.opsli.plugins.generator.database.mysql.MySqlSyncColumnType;
import org.opsli.plugins.generator.enums.DataBaseType;
import org.opsli.core.utils.SpringContextHolder;
import org.opsli.modulars.generator.importable.entity.DatabaseColumn;
import org.opsli.modulars.generator.importable.entity.DatabaseTable;
import org.opsli.modulars.generator.importable.service.DatabaseTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 数据库同步策略 工具类
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
@Slf4j
@Configuration
public class ImportTableUtil{

    /** 处理方法集合 */
    private static final ConcurrentMap<DataBaseType, DatabaseTableService> HANDLER_MAP = new ConcurrentHashMap<>();
    /** 数据库类型 */
    private static final Map<String, DataBaseType> DB_TYPE_MAP;
    /** 指定 master 数据库 */
    private static final String ASSIGN_DB = "master";
    static {
        DB_TYPE_MAP = Maps.newHashMap();
        DB_TYPE_MAP.put("com.mysql.jdbc.Driver", DataBaseType.MYSQL);
        DB_TYPE_MAP.put("com.mysql.cj.jdbc.Driver", DataBaseType.MYSQL);
        DB_TYPE_MAP.put("com.microsoft.jdbc.sqlserver.SQLServerDriver", DataBaseType.SQL_SERVER);
        DB_TYPE_MAP.put("com.microsoft.sqlserver.jdbc.SQLServerDriver", DataBaseType.SQL_SERVER);
        DB_TYPE_MAP.put("com.sybase.jdbc.SybDriver", DataBaseType.SYBASE);
        DB_TYPE_MAP.put("oracle.jdbc.driver.OracleDriver", DataBaseType.ORACLE);
        DB_TYPE_MAP.put("org.postgresql.Driver", DataBaseType.POSTGRE_SQL);
        DB_TYPE_MAP.put("com.ibm.db2.jdbc.app.DB2.Driver", DataBaseType.DB2);
    }

    /** 数据库信息 */
    private static DbSourceProperties dbSourceProperties;

    /**
     * 获得 数据库类型
     * @return 数据库类型
     */
    public static DataBaseType getDbType(){
        if(dbSourceProperties != null){
            Map<String, DbSourceProperties.DataSourceInfo> dataSourceInfoMap = dbSourceProperties.getDataSourceInfoMap();
            DbSourceProperties.DataSourceInfo dataSourceInfo = dataSourceInfoMap.get(ASSIGN_DB);
            if(dataSourceInfo != null){
                return DB_TYPE_MAP.get(dataSourceInfo.getDriverClassName());
            }
        }
        return null;
    }

    /**
     * 获得当前数据库中表
     * @return List
     */
    public static List<DatabaseTable> findTables() {
        return ImportTableUtil.findTables(null);
    }

    /**
     * 获得当前数据库中表
     * @param tableName 表名
     * @return List
     */
    public static List<DatabaseTable> findTables(String tableName) {
        Map<String, DbSourceProperties.DataSourceInfo> dataSourceInfoMap =
                dbSourceProperties.getDataSourceInfoMap();
        // 非法判断
        if(CollUtil.isEmpty(dataSourceInfoMap)){
            return null;
        }


        List<DatabaseTable> databaseTables = Lists.newArrayList();
        for (Map.Entry<String, DbSourceProperties.DataSourceInfo> dataSourceInfoEntry
                : dataSourceInfoMap.entrySet()) {
            // 如果master不为空 且 多数据源不为主数据源 则不进行处理
            if(!StringUtils.equals(ASSIGN_DB, dataSourceInfoEntry.getKey())){
                continue;
            }

            // 数据源
            String key = dataSourceInfoEntry.getKey();
            DbSourceProperties.DataSourceInfo dataSource = dataSourceInfoEntry.getValue();

            // 根据类型获得查询器
            DataBaseType dataBaseType = DB_TYPE_MAP.get(dataSource.getDriverClassName());
            DatabaseTableService databaseTableService = HANDLER_MAP.get(dataBaseType);
            if(databaseTableService == null){
                continue;
            }

            // 获得当前库下表集合
            List<DatabaseTable> tables;
            if(StringUtils.isNotEmpty(tableName)){
                tables = databaseTableService.findTables(dataSource.getDbName(), tableName);
            }else{
                tables = databaseTableService.findTables(dataSource.getDbName());
            }

            // 如果不为空则存入集合
            if(CollUtil.isNotEmpty(tables)){
                for (DatabaseTable table : tables) {
                    table.setDbSource(key);
                }
                databaseTables.addAll(tables);
            }
        }

        return databaseTables;
    }

    /**
     * 获得表字段
     * @param tableName 表名
     * @return List
     */
    public static List<DatabaseColumn> findColumns(String tableName) {

        Map<String, DbSourceProperties.DataSourceInfo> dataSourceInfoMap =
                dbSourceProperties.getDataSourceInfoMap();
        // 非法判断
        if(CollUtil.isEmpty(dataSourceInfoMap)){
            return null;
        }

        DbSourceProperties.DataSourceInfo dataSource = dataSourceInfoMap.get(ASSIGN_DB);
        if(dataSource == null){
            return null;
        }

        // 根据类型获得查询器
        DataBaseType dataBaseType = DB_TYPE_MAP.get(dataSource.getDriverClassName());
        DatabaseTableService databaseTableService = HANDLER_MAP.get(dataBaseType);
        if(databaseTableService == null){
            return null;
        }

        return databaseTableService.findColumns(dataSource.getDbName(), tableName);
    }

    /**
     * 获得数据库类型下 字段类型
     * @return List
     */
    public static List<String> getFieldTypes() {
        DatabaseTableService databaseTableService = getDatabaseTableService();
        if(databaseTableService == null){
            return ListUtil.empty();
        }
        return databaseTableService.getFieldTypes();
    }

    /**
     * 获得数据库类型下 全部类型对应Java类型
     * @return List
     */
    public static Map<String, String> getJavaFieldTypes() {
        DatabaseTableService databaseTableService = getDatabaseTableService();
        if(databaseTableService == null){
            return Maps.newHashMap();
        }
        return databaseTableService.getJavaFieldTypes();
    }

    /**
     * 获得全部类型对应Java类型集合（兜底String 类型）
     * @return List
     */
    public static Map<String, List<String>> getJavaFieldTypesBySafety() {
        DatabaseTableService databaseTableService = getDatabaseTableService();
        if(databaseTableService == null){
            return Maps.newHashMap();
        }
        return databaseTableService.getJavaFieldTypesBySafety();
    }

    /**
     * 获得当前数据处理Service
     * @return DatabaseTableService
     */
    private static DatabaseTableService getDatabaseTableService(){
        Map<String, DbSourceProperties.DataSourceInfo> dataSourceInfoMap =
                dbSourceProperties.getDataSourceInfoMap();
        // 非法判断
        if(CollUtil.isEmpty(dataSourceInfoMap)){
            return null;
        }

        DbSourceProperties.DataSourceInfo dataSource = dataSourceInfoMap.get(ASSIGN_DB);
        if(dataSource == null){
            return null;
        }

        // 根据类型获得查询器
        DataBaseType dataBaseType = DB_TYPE_MAP.get(dataSource.getDriverClassName());

        return HANDLER_MAP.get(dataBaseType);
    }

    // ====================================


    @Bean
    public void initImportTable(){

        // 拿到state包下 实现了 SystemEventState 接口的,所有子类
        Set<Class<?>> clazzSet = ClassUtil.scanPackageBySuper(
                DatabaseTableService.class.getPackage().getName(),
                DatabaseTableService.class
        );

        for (Class<?> aClass : clazzSet) {
            // 位运算 去除抽象类
            if((aClass.getModifiers() & Modifier.ABSTRACT) != 0){
                continue;
            }

            DatabaseTableService handler = (DatabaseTableService) SpringContextHolder.getBean(aClass);
            // 加入集合
            HANDLER_MAP.put(handler.getType(),handler);
        }
    }

    @Autowired
    public void init(DbSourceProperties dbSourceProperties) {
        ImportTableUtil.dbSourceProperties = dbSourceProperties;
    }
}
