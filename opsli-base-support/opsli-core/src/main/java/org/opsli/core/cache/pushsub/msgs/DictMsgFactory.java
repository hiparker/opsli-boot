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
import org.opsli.api.wrapper.system.dict.DictWrapper;
import org.opsli.core.cache.pushsub.enums.CacheHandleType;
import org.opsli.core.cache.pushsub.enums.DictModelType;
import org.opsli.core.cache.pushsub.enums.MsgArgsType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.core.cache.pushsub.receiver.RedisPushSubReceiver;
import org.opsli.plugins.redis.pushsub.entity.BaseSubMessage;

import java.util.List;

/**
 * 字典消息
 *
 * @author Parker
 * @date 2020-09-15
 */
@Data
@Accessors(chain = true)
public final class DictMsgFactory extends BaseSubMessage{

    /** 通道 */
    private static final String CHANNEL = RedisPushSubReceiver.BASE_CHANNEL+RedisPushSubReceiver.CHANNEL;

    private DictMsgFactory(){}

    /**
     * 构建消息 字典
     * @param dictWrapperModel 字典模型
     * @param cacheHandleType 缓存类型
     * @return 消息
     */
    public static BaseSubMessage createMsg(DictWrapper dictWrapperModel, CacheHandleType cacheHandleType){
        BaseSubMessage baseSubMessage = new BaseSubMessage();
        // 数据
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(MsgArgsType.DICT_MODEL.toString(), dictWrapperModel);
        jsonObj.put(MsgArgsType.DICT_MODEL_TYPE.toString(), DictModelType.OBJECT);
        jsonObj.put(MsgArgsType.DICT_TYPE.toString(), cacheHandleType.toString());

        // DICT 字典
        baseSubMessage.build(CHANNEL,PushSubType.DICT.toString(),jsonObj);
        return baseSubMessage;
    }

    /**
     * 构建消息 字典
     * @param dictWrapperModels 字典模型集合
     * @param cacheHandleType 缓存类型
     * @return 消息
     */
    public static BaseSubMessage createMsg(List<DictWrapper> dictWrapperModels, CacheHandleType cacheHandleType){
        BaseSubMessage baseSubMessage = new BaseSubMessage();
        // 数据
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(MsgArgsType.DICT_MODELS.toString(), dictWrapperModels);
        jsonObj.put(MsgArgsType.DICT_MODEL_TYPE.toString(), DictModelType.COLLECTION);
        jsonObj.put(MsgArgsType.DICT_TYPE.toString(), cacheHandleType.toString());

        // DICT 字典
        baseSubMessage.build(CHANNEL,PushSubType.DICT.toString(),jsonObj);
        return baseSubMessage;
    }
}
