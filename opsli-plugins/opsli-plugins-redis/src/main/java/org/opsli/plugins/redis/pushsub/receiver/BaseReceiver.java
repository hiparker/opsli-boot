package org.opsli.plugins.redis.pushsub.receiver;


import com.alibaba.fastjson.JSONObject;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.redis.receiver
 * @Author: Parker
 * @CreateTime: 2020-09-15 14:49
 * @Description: Redis 消息订阅实现基类
 */
public abstract class BaseReceiver {

    public static final String BASE_CHANNEL = "listener:msg:";
    private String channel;

    public BaseReceiver(String channel){
        this.channel = BASE_CHANNEL+channel;
    }

    /**
     * 获得监听信道
     * @return
     */
    public String getListenerChannel(){
        return this.channel;
    }

    /**
     * 获得消息
     * @param msg
     */
    public abstract void receiveMessage(String msg);

}
