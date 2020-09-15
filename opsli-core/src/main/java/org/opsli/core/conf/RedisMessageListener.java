package org.opsli.core.conf;

import lombok.extern.slf4j.Slf4j;
import org.opsli.core.cache.pushsub.receiver.RedisPushSubReceiver;
import org.opsli.plugins.redis.pushsub.receiver.BaseReceiver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import javax.annotation.Resource;

/**
 * @author  : parker
 * @date  : 2020-09-15
 *
 * 消息订阅 配置
 *
 **/
@Slf4j
@Configuration
@ConditionalOnProperty(name = "spring.redis.pushsub.enable", havingValue = "true")
public class RedisMessageListener {

    @Resource
    private LettuceConnectionFactory factory;

    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     * @return
     */
 
    @Bean
    public RedisMessageListenerContainer container() {
        RedisPushSubReceiver receiver = new RedisPushSubReceiver();
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        //订阅了的通道
        container.addMessageListener(listenerAdapter(new RedisPushSubReceiver()), new PatternTopic(receiver.getListenerChannel()));

        return container;
    }
    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     *
     * 想要处理消息 需要重写 消息接受方法
     *
     * @return
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(RedisPushSubReceiver baseReceiver) {
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        //也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage 可以自己到源码里面看
        //receiveMessage就是对应消费者那边的消费方法吗,而Receiver是自己弄的一个消费者类
        return new MessageListenerAdapter(baseReceiver, "receiveMessage");
    }

}
