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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.wrapper.system.dict.DictWrapper;
import org.opsli.common.constants.CacheConstants;
import org.opsli.common.constants.DictConstants;
import org.opsli.common.enums.CacheType;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.enums.CacheHandleType;
import org.opsli.core.cache.pushsub.enums.DictModelType;
import org.opsli.core.cache.pushsub.enums.MsgArgsType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.plugins.cache.EhCachePlugin;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 字典消息处理
 *
 * @author Parker
 * @date 2020-09-16
 */
@Slf4j
public class DictHandler implements RedisPushSubHandler{

    @Autowired
    private EhCachePlugin ehCachePlugin;

    @Override
    public PushSubType getType() {
        return PushSubType.DICT;
    }

    @Override
    public void handler(JSONObject msgJson) {
        DictModelType dictModelType = DictModelType.valueOf((String) msgJson.get(MsgArgsType.DICT_MODEL_TYPE.toString()));
        CacheHandleType type = CacheHandleType.valueOf((String) msgJson.get(MsgArgsType.DICT_TYPE.toString()));

        if(DictModelType.COLLECTION == dictModelType){
            Object dictListObj = msgJson.get(MsgArgsType.DICT_MODELS.toString());
            List<DictWrapper> dictWrappers = Convert.toList(DictWrapper.class, dictListObj);
            if(CollUtil.isNotEmpty(dictWrappers)){
                for (DictWrapper dictWrapper : dictWrappers) {
                    this.handler(dictWrapper, type);
                }
            }
        } else if(DictModelType.OBJECT == dictModelType){
            JSONObject jsonObject = msgJson.getJSONObject(MsgArgsType.DICT_MODEL.toString());
            if(jsonObject == null){
                return;
            }

            DictWrapper dictWrapperModel = jsonObject.toJavaObject(DictWrapper.class);
            this.handler(dictWrapperModel, type);
        }
    }


    /**
     * 真正处理 - 只是处理自己本地的缓存
     * @param dictWrapperModel model
     * @param type 类型
     */
    private void handler(DictWrapper dictWrapperModel, CacheHandleType type){

        // 解析 key
        String ehKeyByName = CacheUtil.handleKey(CacheType.EDEN_HASH, DictConstants.CACHE_PREFIX_NAME +
                        dictWrapperModel.getTypeCode() + ":" + dictWrapperModel.getDictName());
        String ehKeyByValue = CacheUtil.handleKey(CacheType.EDEN_HASH, DictConstants.CACHE_PREFIX_VALUE +
                dictWrapperModel.getTypeCode() + ":" + dictWrapperModel.getDictValue());

        // 缓存更新
        if(CacheHandleType.UPDATE == type){
            ehCachePlugin.delete(CacheConstants.EHCACHE_SPACE, ehKeyByName);
            ehCachePlugin.delete(CacheConstants.EHCACHE_SPACE, ehKeyByValue);

            // 统一转换为 JSONObject
            String jsonStr = JSONObject.toJSONString(dictWrapperModel.getModel());
            JSONObject value = JSONObject.parseObject(jsonStr);
            ehCachePlugin.put(CacheConstants.EHCACHE_SPACE, ehKeyByName, value);
            ehCachePlugin.put(CacheConstants.EHCACHE_SPACE, ehKeyByValue, value);
        }
        // 缓存删除
        else if(CacheHandleType.DELETE == type){
            ehCachePlugin.delete(CacheConstants.EHCACHE_SPACE, ehKeyByName);
            ehCachePlugin.delete(CacheConstants.EHCACHE_SPACE, ehKeyByValue);
        }
    }

}
