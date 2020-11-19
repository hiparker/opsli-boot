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
package org.opsli.modulars.creater.importable;

import cn.hutool.core.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.utils.Props;
import org.opsli.core.creater.strategy.sync.SyncStrategy;
import org.opsli.core.utils.SpringContextHolder;
import org.opsli.modulars.creater.importable.entity.DatabaseColumn;
import org.opsli.modulars.creater.importable.entity.DatabaseTable;
import org.opsli.modulars.creater.importable.service.DatabaseTableService;
import org.opsli.modulars.creater.table.wrapper.CreaterTableAndColumnModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @BelongsProject: opsli-boot
 * @Author: Parker
 * @CreateTime: 2020-09-15 14:50
 * @Description: 数据库同步策略 工具类
 *
 */
@Slf4j
@Configuration
public class ImportTableUtil{

    /** 数据库名 */
    public static final String DB_NAME;
    /** 数据库类型 */
    public static final String DB_TYPE;
    /** 处理方法集合 */
    private static final ConcurrentMap<String, DatabaseTableService> HANDLER_MAP = new ConcurrentHashMap<>();

    static {
        Props props = new Props("creater.yaml");
        DB_NAME = props.getStr("opsli.db-name","opsli-boot");
        DB_TYPE = props.getStr("opsli.db-type");
    }

    /**
     * 获得当前数据库中表
     * @return
     */
    public static List<DatabaseTable> findTables() {
        DatabaseTableService databaseTableService = HANDLER_MAP.get(DB_TYPE);
        if(databaseTableService == null){
            return null;
        }
        return databaseTableService.findTables(DB_NAME);
    }

    /**
     * 获得当前数据库中表
     * @param tableName
     * @return
     */
    public static List<DatabaseTable> findTables(String tableName) {
        DatabaseTableService databaseTableService = HANDLER_MAP.get(DB_TYPE);
        if(databaseTableService == null){
            return null;
        }
        return databaseTableService.findTables(DB_NAME, tableName);
    }

    /**
     * 获得表字段
     * @param tableName
     * @return
     */
    public static List<DatabaseColumn> findColumns(String tableName) {
        DatabaseTableService databaseTableService = HANDLER_MAP.get(DB_TYPE);
        if(databaseTableService == null){
            return null;
        }
        return databaseTableService.findColumns(DB_NAME, tableName);
    }


    // ====================================


    @Bean
    public void initImportTable(){

        // 拿到state包下 实现了 SystemEventState 接口的,所有子类
        Set<Class<?>> clazzSet = ClassUtil.scanPackageBySuper(DatabaseTableService.class.getPackage().getName()
                , DatabaseTableService.class
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

}
