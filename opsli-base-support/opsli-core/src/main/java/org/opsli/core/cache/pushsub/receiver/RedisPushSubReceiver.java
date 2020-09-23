package org.opsli.core.cache.pushsub.receiver;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.enums.SystemInfo;
import org.opsli.common.utils.PackageUtil;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.core.cache.pushsub.handler.RedisPushSubHandler;
import org.opsli.core.msg.CoreMsg;
import org.opsli.plugins.redis.pushsub.entity.BaseSubMessage;
import org.opsli.plugins.redis.pushsub.receiver.BaseReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
@Configuration
public class RedisPushSubReceiver extends BaseReceiver {

    /** 监听信道 */
    public static final String CHANNEL = "opsli";

    /** 处理方法集合 */
    private static final ConcurrentMap<PushSubType, RedisPushSubHandler> HANDLER_MAP = new ConcurrentHashMap<>();


    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    DefaultListableBeanFactory defaultListableBeanFactory;

    public RedisPushSubReceiver() {
        super(CHANNEL);
    }

    @Bean
    public void initRedisPushSubHandler(){
        // 拿到state包下 实现了 SystemEventState 接口的,所有子类
        Set<Class<?>> clazzSet = PackageUtil.listSubClazz(RedisPushSubHandler.class.getPackage().getName(),
                true,
                RedisPushSubHandler.class
        );

        int count = 0;
        for (Class<?> aClass : clazzSet) {
            // 位运算 去除抽象类
            if((aClass.getModifiers() & Modifier.ABSTRACT) != 0){
                continue;
            }

            try {
                Object obj = aClass.newInstance();

                RedisPushSubHandler handler = (RedisPushSubHandler) obj;

                // 加入集合
                HANDLER_MAP.put(handler.getType(),handler);

                //将new出的对象放入Spring容器中
                defaultListableBeanFactory.registerSingleton("redisPushSubHandler"+count, obj);

                //自动注入依赖
                beanFactory.autowireBean(obj);
                
            } catch (Exception e){
                log.error(CoreMsg.REDIS_EXCEPTION_PUSH_SUB.getMessage());
            }
            count ++;
        }
    }


    @Override
    public void receiveMessage(String msg) {
        if(msg == null || "".equals(msg)){
            return;
        }
        long beginTime = System.currentTimeMillis();
        // 替换 转意符
        String replaceAll = msg.replaceAll("\\\\", "");
        String substring = replaceAll.substring(1, replaceAll.length() - 1);
        JSONObject msgJson = JSONObject.parseObject(substring);
        String type = (String) msgJson.get(BaseSubMessage.BASE_TYPE);
        String identifier = (String) msgJson.get(BaseSubMessage.BASE_ID);
        // 本机不广播
//        if(SystemInfo.INSTANCE.getSystemID().equals(identifier)){
//            return;
//        }
        PushSubType pt = PushSubType.valueOf(type);
        RedisPushSubHandler redisPushSubHandler = HANDLER_MAP.get(pt);
        if(redisPushSubHandler == null){
            return;
        }
        redisPushSubHandler.handler(msgJson);
        long endTime = System.currentTimeMillis();
        log.info("订阅节点更新缓存  耗时(毫秒):{}",(endTime-beginTime));
    }

}
