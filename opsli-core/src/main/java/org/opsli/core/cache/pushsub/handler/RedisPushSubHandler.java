package org.opsli.core.cache.pushsub.handler;

import com.alibaba.fastjson.JSONObject;
import org.opsli.core.cache.pushsub.enums.PushSubType;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.receiver
 * @Author: Parker
 * @CreateTime: 2020-09-15 15:11
 * @Description: 标示类 用于获得 消息未知
 */
public interface RedisPushSubHandler {


    PushSubType getType();

    /**
     * 消息处理
     * @param msgJson
     */
    void handler(JSONObject msgJson);

}
