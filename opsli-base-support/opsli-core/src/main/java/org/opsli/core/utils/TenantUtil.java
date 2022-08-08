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

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.tenant.TenantApi;
import org.opsli.api.wrapper.system.tenant.TenantModel;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.cache.SecurityCache;
import org.opsli.core.msg.CoreMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * 租户工具类
 *
 * @author Parker
 * @date 2020-09-19 23:21
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class TenantUtil {

    /** 前缀 */
    public static final String PREFIX_CODE = "tenant:id:";

    /** 超级管理员 租户ID */
    public static final String SUPER_ADMIN_TENANT_ID = "0";

    /** 租户 Api */
    private static TenantApi tenantApi;

    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;

    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据 tenantId 获得租户
     * @param tenantId 租户ID
     * @return model
     */
    public static TenantModel getTenant(String tenantId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(PREFIX_CODE + tenantId);


        Object cache = SecurityCache.get(redisTemplate, cacheKey, (k) -> {
            // 查询数据库
            ResultWrapper<TenantModel> resultVo = tenantApi.getTenantByUsable(tenantId);
            if(!ResultWrapper.isSuccess(resultVo)){
                return null;
            }

            return resultVo.getData();
        }, true);

        return Convert.convert(TenantModel.class, cache);
    }


    // ============== 刷新缓存 ==============

    /**
     * 刷新租户 - 删就完了
     * @param tenantId 租户ID
     * @return boolean
     */
    public static boolean refreshTenant(String tenantId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(StringUtils.isEmpty(tenantId)){
            return true;
        }

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(PREFIX_CODE + tenantId);

        return SecurityCache.remove(redisTemplate, cacheKey);
    }


    // =====================================

    /**
     * 初始化
     */
    @Autowired
    public void init(TenantApi tenantApi,
                     RedisTemplate<String, Object> redisTemplate) {
        TenantUtil.tenantApi = tenantApi;
        TenantUtil.redisTemplate = redisTemplate;
        IS_INIT = true;
    }

}
