/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.core.cache.pushsub.receiver;

import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.opsli.common.enums.SystemInfo;
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
 * Redis 消息订阅 更新本地缓存
 * 字典缓存更新
 *
 * @author Parker
 * @date 2020-09-15
 */
@Slf4j
@Configuration
public class RedisPushSubReceiver extends BaseReceiver {

    /** Spring Bean 前缀 */
    public static final String SPRING_PREFIX = "redisPushSub_";

    /** 监听信道 */
    public static final String CHANNEL = "opsli";

    /** 处理方法集合 */
    private static final ConcurrentMap<PushSubType, RedisPushSubHandler> HANDLER_MAP = new ConcurrentHashMap<>();


    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;

    public RedisPushSubReceiver() {
        super(CHANNEL);
    }

    @Bean
    public void initRedisPushSubHandler(){

        // 拿到state包下 实现了 SystemEventState 接口的,所有子类
        Set<Class<?>> clazzSet = ClassUtil.scanPackageBySuper(
                RedisPushSubHandler.class.getPackage().getName(),
                RedisPushSubHandler.class
        );

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
                defaultListableBeanFactory.registerSingleton(SPRING_PREFIX+aClass.getSimpleName(), obj);

                //自动注入依赖
                beanFactory.autowireBean(obj);

            } catch (Exception e){
                log.error(CoreMsg.REDIS_EXCEPTION_PUSH_SUB.getMessage());
            }
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
        if(SystemInfo.INSTANCE.getSystemID().equals(identifier)){
            return;
        }
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
