package org.opsli.core.cache.pushsub.msgs;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;
import org.opsli.core.cache.pushsub.enums.CacheType;
import org.opsli.core.cache.pushsub.enums.MsgArgsType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.core.cache.pushsub.receiver.RedisPushSubReceiver;
import org.opsli.plugins.redis.pushsub.entity.BaseSubMessage;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.msgs
 * @Author: Parker
 * @CreateTime: 2020-09-15 16:50
 * @Description: 字典消息
 */

@Data
@Accessors(chain = true)
public final class CacheDataMsgFactory extends BaseSubMessage{

    /** 通道 */
    private static final String CHANNEL = RedisPushSubReceiver.BASE_CHANNEL+RedisPushSubReceiver.CHANNEL;

    private CacheDataMsgFactory(){}

    /**
     * 构建消息
     */
    public static BaseSubMessage createMsg(PushSubType py,String key, Object value, CacheType cacheType){
        BaseSubMessage baseSubMessage = new BaseSubMessage();
        // 数据
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(MsgArgsType.CACHE_DATA_KEY.toString(),key);
        jsonObj.put(MsgArgsType.CACHE_DATA_VALUE.toString(),value);
        jsonObj.put(MsgArgsType.CACHE_DATA_TYPE.toString(),cacheType.toString());

        // 热点数据 - 系统数据
        baseSubMessage.build(CHANNEL,py.toString(),jsonObj);
        return baseSubMessage;
    }

}
