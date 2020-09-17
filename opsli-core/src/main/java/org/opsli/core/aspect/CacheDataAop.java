package org.opsli.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.opsli.common.annotation.HotDataDel;
import org.opsli.common.annotation.HotDataPut;
import org.opsli.common.base.entity.BaseEntity;
import org.opsli.common.constants.CacheConstants;
import org.opsli.core.cache.pushsub.enums.CacheType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.core.cache.pushsub.msgs.CacheDataMsgFactory;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static org.opsli.common.constants.OrderConstants.HOT_DATA_ORDER;

/**
 * 热点数据 拦截处理
 *
 * @author parker
 * @date 2020-09-16
 */
@Slf4j
@Order(HOT_DATA_ORDER)
@Aspect
@Component
public class CacheDataAop {

    /** 热点数据前缀 */
    public static final String PREFIX_NAME = "opsli:";

    @Autowired
    private RedisPlugin redisPlugin;

    @Pointcut("@annotation(org.opsli.common.annotation.HotDataPut)")
    public void hotDataPut() {
    }

    @Pointcut("@annotation(org.opsli.common.annotation.HotDataDel)")
    public void hotDataDel() {
    }

    @Around("hotDataPut()")
    public Object hotDataPutProcess(ProceedingJoinPoint point) throws Throwable {
        Object[] args= point.getArgs();
        Object returnValue = point.proceed(args);
        // 将
        if(returnValue != null){
            String methodName= point.getSignature().getName();
            Class<?> classTarget= point.getTarget().getClass();
            Class<?>[] par=((MethodSignature) point.getSignature()).getParameterTypes();
            Method objMethod = classTarget.getMethod(methodName, par);
            // 获取注解参数
            HotDataPut aCache= objMethod.getAnnotation(HotDataPut.class);
            if(aCache != null){
                // 类型
                PushSubType type;
                // key 前缀
                StringBuilder keyBuf = new StringBuilder(PREFIX_NAME);
                // 热点数据
                if(CacheConstants.HOT_DATA.equals(aCache.name())){
                    keyBuf.append(CacheConstants.HOT_DATA).append(":");
                    type = PushSubType.HOT_DATA;
                }
                // 系统数据
                else if(CacheConstants.EDEN_DATA.equals(aCache.name())){
                    keyBuf.append(CacheConstants.EDEN_DATA).append(":");
                    type = PushSubType.EDEN_DATA;
                } else {
                    // 如果都不是 则直接退出 不走缓存
                    return returnValue;
                }

                try {
                    // 这里 只对 继承了 BaseEntity 的类做处理
                    BaseEntity baseEntity = (BaseEntity) returnValue;

                    // key 存储ID
                    String key = keyBuf.append(baseEntity.getId()).toString();

                    // 广播数据
                    redisPlugin.sendMessage(
                            CacheDataMsgFactory.createMsg(type, key, returnValue, CacheType.UPDATE)
                    );
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return returnValue;
    }



    @Around("hotDataDel()")
    public Object hotDataDelProcess(ProceedingJoinPoint point) throws Throwable {
        Object[] args= point.getArgs();
        Object returnValue = point.proceed(args);
        // 将
        if(returnValue != null){
            String methodName= point.getSignature().getName();
            Class<?> classTarget= point.getTarget().getClass();
            Class<?>[] par=((MethodSignature) point.getSignature()).getParameterTypes();
            Method objMethod = classTarget.getMethod(methodName, par);
            // 获取注解参数
            HotDataDel aCache= objMethod.getAnnotation(HotDataDel.class);
            if(aCache != null){
                // 类型
                PushSubType type;
                // key 前缀
                StringBuilder keyBuf = new StringBuilder(PREFIX_NAME);
                // 热点数据
                if(CacheConstants.HOT_DATA.equals(aCache.name())){
                    keyBuf.append(CacheConstants.HOT_DATA).append(":");
                    type = PushSubType.HOT_DATA;
                }
                // 系统数据
                else if(CacheConstants.EDEN_DATA.equals(aCache.name())){
                    keyBuf.append(CacheConstants.EDEN_DATA).append(":");
                    type = PushSubType.EDEN_DATA;
                } else {
                    // 如果都不是 则直接退出 不走缓存
                    return returnValue;
                }

                try {
                    // 这里 只对 继承了 BaseEntity 的类做处理
                    BaseEntity baseEntity = (BaseEntity) returnValue;

                    // key 存储ID
                    String key = keyBuf.append(baseEntity.getId()).toString();

                    // 广播数据
                    redisPlugin.sendMessage(
                            CacheDataMsgFactory.createMsg(type, key, returnValue, CacheType.DELETE)
                    );
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return returnValue;
    }

}
