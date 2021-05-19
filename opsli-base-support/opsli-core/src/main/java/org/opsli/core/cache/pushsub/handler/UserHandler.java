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
package org.opsli.core.cache.pushsub.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.constants.CacheConstants;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.enums.MsgArgsType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.core.cache.pushsub.enums.UserModelType;
import org.opsli.core.utils.UserUtil;
import org.opsli.plugins.cache.EhCachePlugin;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户消息处理
 *
 * @author Parker
 * @date 2020-09-16
 */
@Slf4j
public class UserHandler implements RedisPushSubHandler{

    @Autowired
    private EhCachePlugin ehCachePlugin;

    @Override
    public PushSubType getType() {
        return PushSubType.USER;
    }

    @Override
    public void handler(JSONObject msgJson) {
        UserModelType userModelType = UserModelType.valueOf((String)
                msgJson.get(MsgArgsType.USER_MODEL_TYPE.toString()));

        // 用户刷新
        if(UserModelType.USER_MODEL == userModelType){
            this.userHandler(msgJson);
        }
        // 用户角色刷新
        else if(UserModelType.USER_ROLES_MODEL == userModelType){
            this.userRolesHandler(msgJson);
        }
        // 用户权限刷新
        else if(UserModelType.USER_PERMS_MODEL == userModelType){
            this.userPermsHandler(msgJson);
        }
        // 用户菜单刷新
        else if(UserModelType.USER_MENU_MODEL == userModelType){
            this.userMenusHandler(msgJson);
        }

    }

    /**
     * 用户数据处理
     * @param msgJson 信息Json
     */
    private void userHandler(JSONObject msgJson){
        JSONObject data = msgJson.getJSONObject(MsgArgsType.USER_MODEL_DATA.toString());
        // 数据为空则不执行
        if(data == null){
            return;
        }

        // 获得用户ID 和 用户名
        String userId = (String) msgJson.get(MsgArgsType.USER_ID.toString());
        String username = (String) msgJson.get(MsgArgsType.USER_USERNAME.toString());
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(username) ){
            return;
        }

        String cacheKeyById = CacheUtil.handleKey(UserUtil.PREFIX_ID + userId);
        String cacheKeyByName = CacheUtil.handleKey(UserUtil.PREFIX_USERNAME + username);

        // 先删除
        ehCachePlugin.delete(CacheConstants.EHCACHE_SPACE, cacheKeyById);
        ehCachePlugin.delete(CacheConstants.EHCACHE_SPACE, cacheKeyByName);
    }

    /**
     * 用户角色数据处理
     * @param msgJson 信息Json
     */
    private void userRolesHandler(JSONObject msgJson){
        JSONArray dataArray = msgJson.getJSONArray(MsgArgsType.USER_MODEL_DATA.toString());
        // 数据为空则不执行
        if(dataArray == null || dataArray.isEmpty()){
            return;
        }

        // 获得用户ID
        String userId = (String) msgJson.get(MsgArgsType.USER_ID.toString());
        if(StringUtils.isEmpty(userId)){
            return;
        }

        String cacheKey = CacheUtil.handleKey(UserUtil.PREFIX_ID_ROLES + userId);

        // 先删除
        ehCachePlugin.delete(CacheConstants.EHCACHE_SPACE, cacheKey);
    }

    /**
     * 用户权限数据处理
     * @param msgJson 信息Json
     */
    private void userPermsHandler(JSONObject msgJson){
        JSONArray dataArray = msgJson.getJSONArray(MsgArgsType.USER_MODEL_DATA.toString());
        // 数据为空则不执行
        if(dataArray == null || dataArray.isEmpty()){
            return;
        }

        // 获得用户ID
        String userId = (String) msgJson.get(MsgArgsType.USER_ID.toString());
        if(StringUtils.isEmpty(userId)){
            return;
        }

        String cacheKey = CacheUtil.handleKey(UserUtil.PREFIX_ID_PERMISSIONS + userId);

        // 先删除
        ehCachePlugin.delete(CacheConstants.EHCACHE_SPACE, cacheKey);
    }

    /**
     * 用户菜单数据处理
     * @param msgJson 信息Json
     */
    private void userMenusHandler(JSONObject msgJson){
        JSONArray dataArray = msgJson.getJSONArray(MsgArgsType.USER_MODEL_DATA.toString());
        // 数据为空则不执行
        if(dataArray == null || dataArray.isEmpty()){
            return;
        }

        // 获得用户ID
        String userId = (String) msgJson.get(MsgArgsType.USER_ID.toString());
        if(StringUtils.isEmpty(userId)){
            return;
        }

        String cacheKey = CacheUtil.handleKey(UserUtil.PREFIX_ID_MENUS + userId);

        // 先删除
        ehCachePlugin.delete(CacheConstants.EHCACHE_SPACE, cacheKey);
    }


}
