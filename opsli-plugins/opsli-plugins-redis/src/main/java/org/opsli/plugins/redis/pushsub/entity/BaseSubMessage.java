package org.opsli.plugins.redis.pushsub.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BaseSubMessage implements RedisPushSubMessage{

    public static final String BASE_TYPE = "TYPE";

    /** 发布订阅频道名称 */
    protected String channel;

    protected String json;


    /**
     * 构造函数 转换json
     * @param channel 通道
     * @param type 类型
     * @param jsonObj 数据
     */
    public void build(String channel, String type, JSONObject jsonObj) {
        if(channel == null || type == null || jsonObj == null){
            return;
        }
        jsonObj.put(BASE_TYPE, type);
        this.json = jsonObj.toString();
        this.channel = channel;
    }

}