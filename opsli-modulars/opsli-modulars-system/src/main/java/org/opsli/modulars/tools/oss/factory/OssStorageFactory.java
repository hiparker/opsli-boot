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
package org.opsli.modulars.tools.oss.factory;

import cn.hutool.core.util.ClassUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.wrapper.system.options.OptionsModel;
import org.opsli.core.utils.OptionsUtil;
import org.opsli.modulars.tools.oss.enums.OssConfType;
import org.opsli.modulars.tools.oss.enums.OssStorageType;
import org.opsli.modulars.tools.oss.service.OssStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

/**
 * OSS服务 工厂
 *
 * @author Parker
 * @date 2021年3月16日10:16:29
 *
 */
@Slf4j
@Component
public class OssStorageFactory {

    /** Spring Bean 前缀 */
    public static final String SPRING_PREFIX = "ossStorage_";

    /** OSS 服务容器 */
    private static final Map<OssStorageType, OssStorageService> OSS_HANDLE_MAP = Maps.newHashMap();

    /**
     * 获得OSS 执行类
     * @return OssStorageService
     */
    public static OssStorageService getHandle(){
        OssStorageType storageType = null;

        OptionsModel storageTypeOption = OptionsUtil.getOptionByCode(OssConfType.STORAGE_TYPE.getCode());
        if(storageTypeOption != null){
            storageType = OssStorageType.getType(storageTypeOption.getOptionValue());
        }

        return OssStorageFactory.getHandle(storageType);
    }

    /**
     * 获得OSS 执行类
     * @return OssStorageService
     */
    public static OssStorageService getHandle(OssStorageType storageType){
        OssStorageService ossStorageService = OSS_HANDLE_MAP.get(storageType);
        if(ossStorageService == null){
            return OSS_HANDLE_MAP.get(OssStorageType.LOCAL);
        }
        return OSS_HANDLE_MAP.get(storageType);
    }


    // ============================


    /**
     * 初始化
     */
    @Autowired
    public void init(AutowireCapableBeanFactory beanFactory,
                     DefaultListableBeanFactory defaultListableBeanFactory){

        // 清空执行器集合
        OSS_HANDLE_MAP.clear();

        // 拿到实现了 OssStorageService 接口的,所有子类
        Set<Class<?>> clazzSet = ClassUtil.scanPackageBySuper(OssStorageService.class.getPackage().getName()+".impl"
                , OssStorageService.class
        );


        // 入参处理类
        this.handleInit(beanFactory, defaultListableBeanFactory, clazzSet);
    }

    /**
     * 处理类
     * @param beanFactory Bean工程
     * @param defaultListableBeanFactory 默认BeanList工厂
     * @param clazzSet 扫描处理类
     */
    private void handleInit(AutowireCapableBeanFactory beanFactory,
                            DefaultListableBeanFactory defaultListableBeanFactory,
                            Set<Class<?>> clazzSet){
        for (Class<?> aClass : clazzSet) {
            // 位运算 去除抽象类
            if((aClass.getModifiers() & Modifier.ABSTRACT) != 0){
                continue;
            }

            try {
                Object obj = aClass.newInstance();

                OssStorageService handler = (OssStorageService) obj;

                // 加入集合
                OssStorageFactory.OSS_HANDLE_MAP.put(handler.getType(), handler);

                // 将new出的对象放入Spring容器中
                defaultListableBeanFactory.registerSingleton(SPRING_PREFIX + aClass.getSimpleName(), obj);

                // 自动注入依赖
                beanFactory.autowireBean(obj);

            } catch (Exception e){
                log.error("Oss 服务注入失败");
            }
        }
    }

}
