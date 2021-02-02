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
package org.opsli.core.utils;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.constants.CacheConstants;
import org.opsli.common.utils.Props;
import org.opsli.plugins.redis.RedisLockPlugins;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * @Author: 周鹏程
 * @CreateTime: 2020-09-22 11:17
 * @Description: 搜索历史工具类
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class SearchHisUtil {


    /** 搜索历史缓存数据KEY */
    private static final int DEFAULT_COUNT = 10;

    /**
     * 热点数据前缀
     */
    public static final String PREFIX_NAME;
    private static final String CACHE_PREFIX = "his:username:";

    /** Redis插件 */
    private static RedisPlugin redisPlugin;

    static {
        Props props = new Props("application.yaml");
        PREFIX_NAME = props.getStr("spring.cache-conf.prefix", CacheConstants.PREFIX_NAME) + ":";
    }


    /**
     * 获得搜索历史记录
     * @param key 类型
     * @param count 获取数量
     */
    public static Set<Object> getSearchHis(HttpServletRequest request, String key, Integer count) {
        if(request == null || StringUtils.isEmpty(key)){
            return null;
        }

        if(count == null){
            count = DEFAULT_COUNT;
        }

        // 获得当前用户
        UserModel user = UserUtil.getUser();

        String cacheKey = PREFIX_NAME + CACHE_PREFIX + user.getUsername()  + ":" + key;

        return redisPlugin.zReverseRange(cacheKey, 0, count - 1);
    }

    /**
     * 存放搜索历史记录
     * @param request
     * @param keys 搜索key
     */
    public static void putSearchHis(HttpServletRequest request, List<String> keys) {
        if(request == null || CollUtil.isEmpty(keys)){
            return;
        }

        // 获得当前用户
        UserModel user = UserUtil.getUser();

        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String key : keys) {
            String[] values = parameterMap.get(key);
            if(values == null || values.length == 0){
                continue;
            }


            String cacheKey = PREFIX_NAME + CACHE_PREFIX + user.getUsername()  + ":" + key;
            String val = values[0];

            // 记录
            redisPlugin.zIncrementScore(cacheKey, val, 1);
        }
    }


    // ===================================

    @Autowired
    public  void setRedisPlugin(RedisPlugin redisPlugin) {
        SearchHisUtil.redisPlugin = redisPlugin;
    }

}
