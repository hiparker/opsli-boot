package org.opsli.core.cache.pushsub.receiver;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.utils.PackageUtil;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.core.cache.pushsub.handler.RedisPushSubHandler;
import org.opsli.core.msg.CoreMsg;
import org.opsli.plugins.redis.pushsub.entity.BaseSubMessage;
import org.opsli.plugins.redis.pushsub.receiver.BaseReceiver;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.redis.entity
 * @Author: Parker
 * @CreateTime: 2020-09-15 14:50
 * @Description: Redis 消息订阅 更新本地缓存
 *
 * 字典缓存更新
 *
 */
@Slf4j
public class RedisPushSubReceiver extends BaseReceiver {

    /** 监听信道 */
    public static final String CHANNEL = "opsli";

    /** 处理方法集合 */
    private static final Map<PushSubType, RedisPushSubHandler> HANDLER_MAP = new HashMap<>();

    static {
        // 拿到state包下 实现了 SystemEventState 接口的,所有子类
        Set<Class<?>> clazzSet = PackageUtil.listSubClazz(RedisPushSubHandler.class.getPackage().getName(),
                true,
                RedisPushSubHandler.class
        );

        for (Class<?> aClass : clazzSet) {
            // 位运算 去除抽象类
            if((aClass.getModifiers() & Modifier.ABSTRACT) != 0){
                continue;
            }

            try {
                RedisPushSubHandler handler = (RedisPushSubHandler) aClass.newInstance();

                // 加入集合
                HANDLER_MAP.put(handler.getType(),handler);

            } catch (Exception e){
                log.error(CoreMsg.REDIS_EXCEPTION_PUSH_SUB.getMessage());
            }
        }
    }


    public RedisPushSubReceiver() {
        super(CHANNEL);
    }

    @Override
    public void receiveMessage(String msg) {
        if(msg == null || "".equals(msg)){
            return;
        }
        // 替换 转意符
        String replaceAll = msg.replaceAll("\\\\", "");
        String substring = replaceAll.substring(1, replaceAll.length() - 1);
        JSONObject msgJson = JSONObject.parseObject(substring);
        String type = (String) msgJson.get(BaseSubMessage.BASE_TYPE);
        PushSubType pt = PushSubType.valueOf(type);
        RedisPushSubHandler redisPushSubHandler = HANDLER_MAP.get(pt);
        if(redisPushSubHandler == null){
            return;
        }
        redisPushSubHandler.handler(msgJson);
    }

}
