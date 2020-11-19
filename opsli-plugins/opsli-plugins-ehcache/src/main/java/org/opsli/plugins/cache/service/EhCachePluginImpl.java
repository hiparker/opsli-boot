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
package org.opsli.plugins.cache.service;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.plugins.cache.msg.EhCacheMsg;
import org.springframework.cache.Cache;
import org.opsli.plugins.cache.EhCachePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.cache.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 21:08
 * @Description: EhCachePlugin 实现类
 */
@Slf4j
@Service
public class EhCachePluginImpl implements EhCachePlugin {

    @Autowired(required = false)
    CacheManager cacheManager;

    @Override
    public boolean put(String cacheName, String key, Object value) {
        if(cacheManager == null){
            return false;
        }
        boolean ret = false;
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if(cache != null){
                cache.put(key,value);
                ret = true;
            }
        } catch (Exception e) {
            log.error(EhCacheMsg.EXCEPTION_PUT.getMessage()+"：{}",e.getMessage());
        }
        return ret;
    }

    @Override
    public Object get(String cacheName, String key) {
        if(cacheManager == null){
            return null;
        }
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if(cache != null){
                // 深克隆数据 防止 ehcache在jvm数据串行
                return ObjectUtil.cloneByStream(cache.get(key));
            }
        } catch (Exception e) {
            log.error(EhCacheMsg.EXCEPTION_GET.getMessage()+"：{}",e.getMessage());
        }
        return null;
    }

    @Override
    public <V> V get(String cacheName, String key, Class<V> vClass) {
        if(cacheManager == null){
            return null;
        }
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if(cache != null){
                // 深克隆数据 防止 ehcache在jvm数据串行
                Object obj = ObjectUtil.cloneByStream(cache.get(key,vClass));
                return WrapperUtil.transformInstance(obj, vClass);
            }
        } catch (Exception e) {
            log.error(EhCacheMsg.EXCEPTION_GET.getMessage()+"：{}", e.getMessage());
        }
        return null;
    }

    @Override
    public boolean delete(String cacheName, String key) {
        if(cacheManager == null){
            return false;
        }
        boolean ret = false;
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if(cache != null){
                cache.evict(key);
                ret = true;
            }
        } catch (Exception e) {
            log.error(EhCacheMsg.EXCEPTION_DEL.getMessage()+"：{}", e.getMessage());
        }
        return ret;
    }

}
