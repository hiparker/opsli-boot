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

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.opsli.plugins.cache.EhCachePlugin;
import org.opsli.plugins.cache.msg.EhCacheMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;


/**
 * EhCachePlugin 实现类
 *
 * @author Parker
 * @date 2020-09-16 11:47
 */
@Slf4j
@Service
public class EhCachePluginImpl implements EhCachePlugin {

    /** Ehcache Json Key */
    private static final String EHCACHE_JSON_KEY = "ehcache_tmp_json";

    @Autowired(required = false)
    CacheManager cacheManager;

    @Override
    public boolean put(String cacheName, String key, Object value) {
        if(cacheManager == null){
            return true;
        }
        boolean ret = false;
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if(cache != null){
                // 强制转化为 String 字符串 ， 用来解决EhCache jvm共用对象问题
                // 则统一转换为 JSONObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(EHCACHE_JSON_KEY, value);
                cache.put(key,jsonObject.toJSONString());
                ret = true;
            }
        } catch (Exception e) {
            log.error(EhCacheMsg.EXCEPTION_PUT.getMessage()+"：{}",e.getMessage());
        }
        return ret;
    }

    @Override
    public <V> V get(String cacheName, String key, Class<V> vClass) {
        if(cacheManager == null){
            return null;
        }
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if(cache != null){
                V v = null;
                String jsonStr = cache.get(key, String.class);
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                if(jsonObject != null){
                    JSONObject dataJson = jsonObject.getJSONObject(EHCACHE_JSON_KEY);
                    if(dataJson != null){
                        try {
                            v = dataJson.toJavaObject(vClass);
                        }catch (Exception e){
                            String message = EhCacheMsg.EXCEPTION_GET_JAVA.getMessage();
                            log.error(StrUtil.format(message, vClass.getName())+"：{}", e.getMessage());
                        }
                    }
                }
                return v;
            }
        } catch (Exception e) {
            log.error(EhCacheMsg.EXCEPTION_GET.getMessage()+"：{}", e.getMessage());
        }
        return null;
    }

    @Override
    public boolean delete(String cacheName, String key) {
        if(cacheManager == null){
            return true;
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
