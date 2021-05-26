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
package org.opsli.plugins.generator;

import cn.hutool.core.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.opsli.plugins.generator.enums.DataBaseType;
import org.opsli.plugins.generator.SyncStrategy;
import org.opsli.core.utils.SpringContextHolder;
import org.opsli.modulars.generator.table.wrapper.GenTableAndColumnModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 数据库同步策略 工具类
 *
 * @author parker
 * @date 2020-09-15 14:50
 */
@Slf4j
@Configuration
public class SqlSyncUtil {


    /** 处理方法集合 */
    private static final ConcurrentMap<DataBaseType, SyncStrategy> HANDLER_MAP = new ConcurrentHashMap<>();


    @Bean
    public void initSyncStrategy(){

        // 拿到state包下 实现了 SystemEventState 接口的,所有子类
        Set<Class<?>> clazzSet = ClassUtil.scanPackageBySuper(
                SyncStrategy.class.getPackage().getName(),
                SyncStrategy.class
        );

        for (Class<?> aClass : clazzSet) {
            // 位运算 去除抽象类
            if((aClass.getModifiers() & Modifier.ABSTRACT) != 0){
                continue;
            }

            SyncStrategy handler = (SyncStrategy) SpringContextHolder.getBean(aClass);
            // 加入集合
            HANDLER_MAP.put(handler.getType(),handler);
        }
    }

    /**
     * 执行
     * @param model 模型
     */
    public static void execute(GenTableAndColumnModel model){
        if(model == null){
            return;
        }

        // 获得数据库类型 枚举
        String jdbcType = model.getJdbcType();
        DataBaseType dbType = DataBaseType.getDbType(jdbcType);

        SyncStrategy syncStrategy = HANDLER_MAP.get(dbType);
        if(syncStrategy != null){
            syncStrategy.execute(model);
        }
    }

}
