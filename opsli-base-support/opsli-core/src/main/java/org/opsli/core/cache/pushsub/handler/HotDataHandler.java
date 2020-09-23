package org.opsli.core.cache.pushsub.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.constants.CacheConstants;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.enums.CacheType;
import org.opsli.core.cache.pushsub.enums.MsgArgsType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.plugins.cache.EhCachePlugin;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.handler
 * @Author: Parker
 * @CreateTime: 2020-09-15 16:24
 * @Description: 热数据处理
 */
@Slf4j
public class HotDataHandler implements RedisPushSubHandler{

    @Autowired
    EhCachePlugin ehCachePlugin;

    @Override
    public PushSubType getType() {
        return PushSubType.HOT_DATA;
    }

    @Override
    public void handler(JSONObject msgJson) {
        String key = (String) msgJson.get(MsgArgsType.CACHE_DATA_KEY.toString());
        Object value = msgJson.get(MsgArgsType.CACHE_DATA_VALUE.toString());
        CacheType type = CacheType.valueOf((String )msgJson.get(MsgArgsType.CACHE_DATA_TYPE.toString()));

        // 解析 key
        String handleKey = CacheUtil.handleKey(CacheConstants.HOT_DATA, key);

        if(CacheType.UPDATE == type){
            ehCachePlugin.put(CacheConstants.HOT_DATA, handleKey, value);
        }
        // 缓存删除
        else if(CacheType.DELETE == type){
            ehCachePlugin.delete(CacheConstants.HOT_DATA, handleKey);
        }
    }

}
