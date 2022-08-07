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
package org.opsli.plugins.security.service;

import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加载用户信息工厂
 *
 * @author Parker
 * @date 2022-07-18 10:16 AM
 **/
@Component
public class LoadUserDetailServiceFactory {

    private boolean isInit;

    private final Map<Class<? extends Authentication>, ILoadUserDetailService>
            serviceMap = new ConcurrentHashMap<>();

    @Autowired
    private ListableBeanFactory listableBeanFactory;

    /**
     * 获得 加载用户服务
     * @param clazz Token class
     * @return Optional<ILoadUserDetailService>
     */
    public Optional<ILoadUserDetailService> getUserDetailService(Class<? extends Authentication> clazz){
        if(!isInit){
            throw new RuntimeException("加载用户工厂未初始化");
        }

        return Optional.ofNullable(serviceMap.get(clazz));
    }


    @PostConstruct
    public void init(){
        Map<String, ILoadUserDetailService> iLoadUserDetailServiceMap =
                listableBeanFactory.getBeansOfType(ILoadUserDetailService.class);
        for (Map.Entry<String, ILoadUserDetailService> serviceEntry : iLoadUserDetailServiceMap.entrySet()) {
            ILoadUserDetailService loadUserDetailService = serviceEntry.getValue();

            // 根据classType 循环加载 Service
            Collection<Class<? extends Authentication>> classTypes = loadUserDetailService.getClassTypes();
            if(CollUtil.isEmpty(classTypes)){
                continue;
            }

            classTypes.forEach((classType)-> serviceMap.put(classType, loadUserDetailService));
        }
        isInit = true;
    }

}
