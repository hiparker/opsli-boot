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
import org.opsli.api.web.system.user.UserApi;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.msgs.OrgMsgFactory;
import org.opsli.core.msg.CoreMsg;
import org.opsli.plugins.redis.RedisPlugin;
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
 * @Description: 组织机构工具类
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class OrgUtil {

    /** 前缀 */
    public static final String PREFIX_CODE = "org::userId::";


    /** Redis插件 */
    private static RedisPlugin redisPlugin;

    /** 用户 Api */
    private static UserApi userApi;


    /**
     * 根据 userId 获得用户组织
     * @param userId
     * @return
     */
    public static UserOrgRefModel getOrgByUserId(String userId){
        // 缓存Key
        String cacheKey = PREFIX_CODE + userId;

        // 先从缓存里拿
        UserOrgRefModel orgRefModel = CacheUtil.getTimed(UserOrgRefModel.class, cacheKey);
        if (orgRefModel != null){
            return orgRefModel;
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKey);
        if(hasNilFlag){
            return null;
        }

        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKey)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return null;
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            orgRefModel = CacheUtil.getTimed(UserOrgRefModel.class, cacheKey);
            if (orgRefModel != null){
                return orgRefModel;
            }

            // 查询数据库
            ResultVo<UserOrgRefModel> resultVo = userApi.getOrgInfoByUserId(userId);
            if(resultVo.isSuccess()){
                orgRefModel = resultVo.getData();
                // 存入缓存
                CacheUtil.put(cacheKey, orgRefModel);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKey);
        }

        if(orgRefModel == null){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKey);
            return null;
        }

        return orgRefModel;
    }


    // ============== 刷新缓存 ==============

    /**
     * 刷新用户组织 - 删就完了
     * @param userId
     * @return
     */
    public static boolean refreshOrg(String userId){
        if(StringUtils.isEmpty(userId)){
            return true;
        }

        // 计数器
        int count = 0;

        UserOrgRefModel orgRefModel = CacheUtil.getTimed(UserOrgRefModel.class, PREFIX_CODE + userId);
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_CODE + userId);

        // 只要不为空 则执行刷新
        if (hasNilFlag){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(PREFIX_CODE + userId);
            if(tmp){
                count--;
            }
        }

        if(orgRefModel != null){
            count++;
            // 先删除
            boolean tmp = CacheUtil.del(PREFIX_CODE + userId);
            if(tmp){
                count--;
            }

            // 发送通知消息
            redisPlugin.sendMessage(
                    OrgMsgFactory.createOrgMsg(orgRefModel)
            );
        }
        return count == 0;
    }




    // =====================================

    @Autowired
    public  void setRedisPlugin(RedisPlugin redisPlugin) {
        OrgUtil.redisPlugin = redisPlugin;
    }

    @Autowired
    public void setUserApi(UserApi userApi) {
        OrgUtil.userApi = userApi;
    }

}
