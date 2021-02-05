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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.tenant.TenantApi;
import org.opsli.api.wrapper.system.tenant.TenantModel;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.msgs.TenantMsgFactory;
import org.opsli.plugins.redis.RedisLockPlugins;
import org.opsli.plugins.redis.RedisPlugin;
import org.opsli.plugins.redis.lock.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.utils
 * @Author: Parker
 * @CreateTime: 2020-09-19 20:03
 * @Description: 租户工具类
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class TenantUtil {

    /** 前缀 */
    public static final String PREFIX_CODE = "tenant::id::";


    /** Redis插件 */
    private static RedisPlugin redisPlugin;

    /** Redis分布式锁 */
    private static RedisLockPlugins redisLockPlugins;

    /** 租户 Api */
    private static TenantApi tenantApi;


    /**
     * 根据 tenantId 获得租户
     * @param tenantId
     * @return
     */
    public static TenantModel getTenant(String tenantId){
        // 先从缓存里拿
        TenantModel tenantModel = CacheUtil.getTimed(TenantModel.class, PREFIX_CODE + tenantId);
        if (tenantModel != null){
            return tenantModel;
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_CODE + tenantId);
        if(hasNilFlag){
            return null;
        }

        // 锁凭证 redisLock 贯穿全程
        RedisLock redisLock = new RedisLock();
        redisLock.setLockName(PREFIX_CODE + tenantId)
                .setAcquireTimeOut(3000L)
                .setLockTimeOut(5000L);

        try {
            // 这里增加分布式锁 防止缓存击穿
            // ============ 尝试加锁
            redisLock = redisLockPlugins.tryLock(redisLock);
            if(redisLock == null){
                return null;
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            tenantModel = CacheUtil.getTimed(TenantModel.class, PREFIX_CODE + tenantId);
            if (tenantModel != null){
                return tenantModel;
            }

            // 查询数据库
            ResultVo<TenantModel> resultVo = tenantApi.getTenantByUsable(tenantId);
            if(resultVo.isSuccess()){
                tenantModel = resultVo.getData();
                // 存入缓存
                CacheUtil.put(PREFIX_CODE + tenantId, tenantModel);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            // ============ 释放锁
            redisLockPlugins.unLock(redisLock);
        }

        if(tenantModel == null){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(PREFIX_CODE + tenantId);
            return null;
        }

        return tenantModel;
    }


    // ============== 刷新缓存 ==============

    /**
     * 刷新租户 - 删就完了
     * @param tenantId
     * @return
     */
    public static boolean refreshTenant(String tenantId){
        if(StringUtils.isEmpty(tenantId)){
            return true;
        }

        // 计数器
        int count = 0;

        TenantModel tenantModel = CacheUtil.getTimed(TenantModel.class, PREFIX_CODE + tenantId);
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_CODE + tenantId);

        // 只要不为空 则执行刷新
        if (hasNilFlag){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(PREFIX_CODE + tenantId);
            if(tmp){
                count--;
            }
        }

        if(tenantModel != null){
            count++;
            // 先删除
            boolean tmp = CacheUtil.del(PREFIX_CODE + tenantId);
            if(tmp){
                count--;
            }

            // 发送通知消息
            redisPlugin.sendMessage(
                    TenantMsgFactory.createTenantMsg(tenantModel)
            );
        }

        return count == 0;
    }




    // =====================================

    @Autowired
    public  void setRedisPlugin(RedisPlugin redisPlugin) {
        TenantUtil.redisPlugin = redisPlugin;
    }

    @Autowired
    public  void setRedisLockPlugins(RedisLockPlugins redisLockPlugins) {
        TenantUtil.redisLockPlugins = redisLockPlugins;
    }

    @Autowired
    public void setTenantApi(TenantApi tenantApi) {
        TenantUtil.tenantApi = tenantApi;
    }
}
