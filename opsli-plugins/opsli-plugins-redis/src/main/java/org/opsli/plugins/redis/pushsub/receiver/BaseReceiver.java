package org.opsli.plugins.redis.pushsub.receiver;



/**
 * Redis 消息订阅实现基类
 *
 * @author Parker
 * @date 2020-09-15
 */
public abstract class BaseReceiver {

    public static final String BASE_CHANNEL = "listener:msg:";
    private final String channel;

    public BaseReceiver(String channel){
        this.channel = BASE_CHANNEL+channel;
    }

    /**
     * 获得监听信道
     * @return String
     */
    public String getListenerChannel(){
        return this.channel;
    }

    /**
     * 获得消息
     * @param msg 消息
     */
    public abstract void receiveMessage(String msg);

}
