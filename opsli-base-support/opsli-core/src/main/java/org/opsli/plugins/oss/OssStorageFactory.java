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
package org.opsli.plugins.oss;

import cn.hutool.core.util.ClassUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.wrapper.system.options.OptionsModel;
import org.opsli.common.enums.OptionsType;
import org.opsli.core.utils.OptionsUtil;
import org.opsli.plugins.oss.enums.OssStorageType;
import org.opsli.plugins.oss.service.OssStorageService;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

/**
 * OSS服务 工厂
 *
 * @author Parker
 * @date 2021年3月16日10:16:29
 */
@Slf4j
public enum OssStorageFactory {

    /** 实例 */
    INSTANCE;

    /** OSS 服务容器 */
    private final Map<OssStorageType, OssStorageService> ossStorageServiceMap;

    /**
     * 获得OSS 执行类
     * @return OssStorageService
     */
    public OssStorageService getHandle(){
        OssStorageType storageType = null;

        OptionsModel storageTypeOption = OptionsUtil.getOptionByCode(OptionsType.STORAGE_TYPE.getCode());
        if(storageTypeOption != null){
            storageType = OssStorageType.getType(storageTypeOption.getOptionValue());
        }

        return this.getHandle(storageType);
    }

    /**
     * 获得OSS 执行类
     * @return OssStorageService
     */
    public OssStorageService getHandle(OssStorageType storageType){
        OssStorageService ossStorageService = ossStorageServiceMap.get(storageType);
        if(ossStorageService == null){
            return ossStorageServiceMap.get(OssStorageType.LOCAL);
        }
        return ossStorageServiceMap.get(storageType);
    }


    // ============================


    /**
     * 初始化
     */
    OssStorageFactory(){
        // 初始化容器
        ossStorageServiceMap = Maps.newHashMap();

        // 拿到实现了 OssStorageService 接口的,所有子类
        Set<Class<?>> clazzSet = ClassUtil.scanPackageBySuper(
                OssStorageService.class.getPackage().getName(),
                OssStorageService.class
        );


        // 入参处理类
        this.handleInit(clazzSet);
    }

    /**
     * 处理类
     * @param clazzSet 扫描处理类
     */
    private void handleInit(Set<Class<?>> clazzSet){
        for (Class<?> aClass : clazzSet) {
            // 位运算 去除抽象类
            if((aClass.getModifiers() & Modifier.ABSTRACT) != 0){
                continue;
            }

            try {
                Object obj = aClass.newInstance();
                OssStorageService handler = (OssStorageService) obj;

                // 加入集合
                ossStorageServiceMap.put(handler.getType(), handler);
            } catch (Exception e){
                log.error("Oss 服务注入失败");
            }
        }
    }

}
