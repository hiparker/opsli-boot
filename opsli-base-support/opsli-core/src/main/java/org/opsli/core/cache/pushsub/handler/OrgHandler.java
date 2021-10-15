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

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.constants.CacheConstants;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.enums.MsgArgsType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.core.utils.OrgUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.plugins.cache.EhCachePlugin;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户组织消息处理
 *
 * @author Parker
 * @date 2020-09-16
 */
@Slf4j
public class OrgHandler implements RedisPushSubHandler{

    @Autowired
    private EhCachePlugin ehCachePlugin;

    @Override
    public PushSubType getType() {
        return PushSubType.ORG;
    }

    @Override
    public void handler(JSONObject msgJson) {
        // 用户刷新
        this.orgHandler(msgJson);
    }

    /**
     * 用户组织数据处理
     * @param msgJson 信息Json
     */
    private void orgHandler(JSONObject msgJson){
        JSONObject data = msgJson.getJSONObject(MsgArgsType.ORG_USER_DATA.toString());
        // 数据为空则不执行
        if(data == null){
            return;
        }

        // 获得用户ID
        String userId = (String) msgJson.get(MsgArgsType.ORG_USER_ID.toString());
        if(StringUtils.isEmpty(userId)){
            return;
        }

        String cacheKey = CacheUtil.handleKey(UserUtil.PREFIX_ID_ORGS + userId);

        // 先删除
        ehCachePlugin.delete(CacheConstants.EHCACHE_SPACE, cacheKey);
    }


}
