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
import org.opsli.common.constants.RedisConstants;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.msg.CoreMsg;
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
 * 搜索历史工具类
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class SearchHisUtil {

    /** 搜索历史缓存数据KEY */
    private static final int DEFAULT_COUNT = 10;

    /** Redis插件 */
    private static RedisPlugin redisPlugin;

    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;


    /**
     * 获得搜索历史记录
     * @param key 类型
     * @param count 获取数量
     */
    public static Set<Object> getSearchHis(HttpServletRequest request, String key, Integer count) {
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(request == null || StringUtils.isEmpty(key)){
            return null;
        }

        if(count == null){
            count = DEFAULT_COUNT;
        }

        // 获得当前用户
        UserModel user = UserUtil.getUser();

        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_HIS_USERNAME + user.getUsername()  + ":" + key);

        return redisPlugin.zReverseRange(cacheKey, 0, count - 1);
    }

    /**
     * 存放搜索历史记录
     * @param request request
     * @param keys 搜索key
     */
    public static void putSearchHis(HttpServletRequest request, List<String> keys) {
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

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


            String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_HIS_USERNAME + user.getUsername()  + ":" + key);
            String val = values[0];

            // 记录
            redisPlugin.zIncrementScore(cacheKey, val, 1);
        }
    }


    // ===================================

    /**
     * 初始化
     */
    @Autowired
    public void init(RedisPlugin redisPlugin) {
        SearchHisUtil.redisPlugin = redisPlugin;

        IS_INIT = true;
    }

}
