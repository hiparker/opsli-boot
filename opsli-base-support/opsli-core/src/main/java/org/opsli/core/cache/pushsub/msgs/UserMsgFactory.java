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
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.core.cache.pushsub.enums.MsgArgsType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.core.cache.pushsub.enums.UserModelType;
import org.opsli.core.cache.pushsub.receiver.RedisPushSubReceiver;
import org.opsli.plugins.redis.pushsub.entity.BaseSubMessage;

import java.util.List;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.msgs
 * @Author: Parker
 * @CreateTime: 2020-09-15 16:50
 * @Description: 用户消息
 */

@Data
@Accessors(chain = true)
public final class UserMsgFactory extends BaseSubMessage{

    /** 通道 */
    private static final String CHANNEL = RedisPushSubReceiver.BASE_CHANNEL+RedisPushSubReceiver.CHANNEL;

    private UserMsgFactory(){}

    /**
     * 构建消息 - 用户
     */
    public static BaseSubMessage createUserMsg(UserModel userModel){
        BaseSubMessage baseSubMessage = new BaseSubMessage();
        // 数据
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(MsgArgsType.USER_ID.toString(), userModel.getId());
        jsonObj.put(MsgArgsType.USER_USERNAME.toString(), userModel.getUsername());
        jsonObj.put(MsgArgsType.USER_MODEL_TYPE.toString(), UserModelType.USER_MODEL.toString());
        jsonObj.put(MsgArgsType.USER_MODEL_DATA.toString(), userModel);

        // 用户
        baseSubMessage.build(CHANNEL,PushSubType.USER.toString(),jsonObj);
        return baseSubMessage;
    }

    /**
     * 构建消息 - 用户角色
     */
    public static BaseSubMessage createUserRolesMsg(String userId, List<String> roles){
        BaseSubMessage baseSubMessage = new BaseSubMessage();
        // 数据
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(MsgArgsType.USER_ID.toString(), userId);
        jsonObj.put(MsgArgsType.USER_MODEL_TYPE.toString(), UserModelType.USER_ROLES_MODEL.toString());
        jsonObj.put(MsgArgsType.USER_MODEL_DATA.toString(), roles);

        // 用户
        baseSubMessage.build(CHANNEL,PushSubType.USER.toString(),jsonObj);
        return baseSubMessage;
    }

    /**
     * 构建消息 - 用户权限
     */
    public static BaseSubMessage createUserPermsMsg(String userId, List<String> perms){
        BaseSubMessage baseSubMessage = new BaseSubMessage();
        // 数据
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(MsgArgsType.USER_ID.toString(), userId);
        jsonObj.put(MsgArgsType.USER_MODEL_TYPE.toString(), UserModelType.USER_PERMS_MODEL.toString());
        jsonObj.put(MsgArgsType.USER_MODEL_DATA.toString(), perms);

        // 用户
        baseSubMessage.build(CHANNEL,PushSubType.USER.toString(),jsonObj);
        return baseSubMessage;
    }

    /**
     * 构建消息 - 用户菜单
     */
    public static BaseSubMessage createUserMenusMsg(String userId, List<MenuModel> menus){
        BaseSubMessage baseSubMessage = new BaseSubMessage();
        // 数据
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(MsgArgsType.USER_ID.toString(), userId);
        jsonObj.put(MsgArgsType.USER_MODEL_TYPE.toString(), UserModelType.USER_MENU_MODEL.toString());
        jsonObj.put(MsgArgsType.USER_MODEL_DATA.toString(), menus);

        // 用户
        baseSubMessage.build(CHANNEL,PushSubType.USER.toString(),jsonObj);
        return baseSubMessage;
    }

}
