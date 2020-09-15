package org.opsli.core.cache.pushsub.handler;

import com.alibaba.fastjson.JSONObject;
import org.opsli.core.cache.pushsub.enums.PushSubType;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.handler
 * @Author: Parker
 * @CreateTime: 2020-09-15 16:24
 * @Description: 字典消息处理
 */
public class DictHandler implements RedisPushSubHandler{

    @Override
    public PushSubType getType() {
        return PushSubType.DICT;
    }

    @Override
    public void handler(JSONObject msgJson) {
        System.out.println(msgJson.toJSONString());
    }

}
