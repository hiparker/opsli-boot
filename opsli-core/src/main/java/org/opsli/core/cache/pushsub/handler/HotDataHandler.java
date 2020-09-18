package org.opsli.core.cache.pushsub.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.enums.CacheType;
import org.opsli.core.cache.pushsub.enums.MsgArgsType;
import org.opsli.core.cache.pushsub.enums.PushSubType;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.handler
 * @Author: Parker
 * @CreateTime: 2020-09-15 16:24
 * @Description: 热数据处理
 */
@Slf4j
public class HotDataHandler implements RedisPushSubHandler{

    @Override
    public PushSubType getType() {
        return PushSubType.HOT_DATA;
    }

    @Override
    public void handler(JSONObject msgJson) {
        String key = (String) msgJson.get(MsgArgsType.CACHE_DATA_KEY.toString());
        Object value = msgJson.get(MsgArgsType.CACHE_DATA_VALUE.toString());
        CacheType type = CacheType.valueOf((String )msgJson.get(MsgArgsType.CACHE_DATA_TYPE.toString()));

        // 缓存更新
        if(CacheType.UPDATE == type){
            CacheUtil.putByKeyOriginal(key, value);
        }
        // 缓存删除
        else if(CacheType.DELETE == type){
            CacheUtil.del(key);
        }

    }

}
