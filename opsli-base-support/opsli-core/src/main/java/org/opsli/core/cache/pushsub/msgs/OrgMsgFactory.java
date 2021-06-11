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
package org.opsli.core.cache.pushsub.msgs;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;
import org.opsli.api.wrapper.system.user.UserOrgRefWebModel;
import org.opsli.core.cache.pushsub.enums.MsgArgsType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.core.cache.pushsub.receiver.RedisPushSubReceiver;
import org.opsli.plugins.redis.pushsub.entity.BaseSubMessage;

/**
 * 用户组织消息
 *
 * @author Parker
 * @date 2020-09-15
 */
@Data
@Accessors(chain = true)
public final class OrgMsgFactory extends BaseSubMessage{

    /** 通道 */
    private static final String CHANNEL = RedisPushSubReceiver.BASE_CHANNEL+RedisPushSubReceiver.CHANNEL;

    private OrgMsgFactory(){}

    /**
     * 构建消息 - 用户组织
     * @param orgRefModel 用户组织模型
     * @return 消息
     */
    public static BaseSubMessage createOrgMsg(UserOrgRefWebModel orgRefModel){
        BaseSubMessage baseSubMessage = new BaseSubMessage();
        // 数据
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(MsgArgsType.ORG_USER_ID.toString(), orgRefModel.getUserId());
        jsonObj.put(MsgArgsType.ORG_USER_DATA.toString(), orgRefModel);

        // 组织
        baseSubMessage.build(CHANNEL,PushSubType.ORG.toString(),jsonObj);
        return baseSubMessage;
    }


}
