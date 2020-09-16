package org.opsli.core.cache.pushsub.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.constants.CacheConstants;
import org.opsli.core.cache.pushsub.enums.CacheType;
import org.opsli.core.cache.pushsub.enums.MsgArgsType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.plugins.cache.EhCachePlugin;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.handler
 * @Author: Parker
 * @CreateTime: 2020-09-15 16:24
 * @Description: 字典消息处理
 */
@Slf4j
public class HotDataHandler implements RedisPushSubHandler{

    /** 热点数据缓存时间 */
    @Value("${cache.ttl-hot-data}")
    private int ttlHotData;

    @Autowired
    private RedisPlugin redisPlugin;
    @Autowired
    private EhCachePlugin cachePlugin;

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
            // 存入EhCache
            cachePlugin.put(CacheConstants.HOT_DATA,key, value);
            // 存入Redis
            redisPlugin.put(key, value, ttlHotData);
        }
        // 缓存删除
        else if(CacheType.DELETE == type){
            // 存入EhCache
            cachePlugin.delete(CacheConstants.HOT_DATA,key);
            // 存入Redis
            redisPlugin.del(key);
        }

    }

}
