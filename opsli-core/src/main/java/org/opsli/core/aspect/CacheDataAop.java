package org.opsli.core.aspect;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.opsli.common.annotation.EnableHotData;
import org.opsli.common.annotation.HotDataDel;
import org.opsli.common.annotation.HotDataPut;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.common.constants.CacheConstants;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.entity.CacheDataEntity;
import org.opsli.core.cache.pushsub.enums.CacheType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.core.cache.pushsub.msgs.CacheDataMsgFactory;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

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

    /**
     * 切如 更新数据
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("hotDataPut()")
    public Object hotDataPutProcess(ProceedingJoinPoint point) throws Throwable {
        Object[] args= point.getArgs();
        Object returnValue = point.proceed(args);
        // 判断 方法上是否使用 HotData注解 如果没有表示开启热数据 则直接跳过
        Annotation annotation = point.getTarget().getClass().getAnnotation(EnableHotData.class);
        if(annotation == null){
            return returnValue;
        }


        // ====== 如果 使用了 EnableHotData ，表示开启热数据加载 则执行下段代码
        CacheDataEntity cacheDataEntity = this.putHandlerData(point, returnValue);
        if(cacheDataEntity != null){
            // 更新缓存数据
            // 热点数据
            if(CacheConstants.HOT_DATA.equals(cacheDataEntity.getCacheName())){
                CacheUtil.putByKeyOriginal(cacheDataEntity.getKey(), returnValue);
            }
            // 永久数据
            else if(CacheConstants.EDEN_DATA.equals(cacheDataEntity.getCacheName())) {
                CacheUtil.putEdenByKeyOriginal(cacheDataEntity.getKey(), returnValue);
            }

            // 广播缓存数据 - 通知其他服务器同步数据
            redisPlugin.sendMessage(
                    CacheDataMsgFactory.createMsg(cacheDataEntity.getType(),
                            cacheDataEntity.getKey(), returnValue, CacheType.UPDATE)
            );
        }

        return returnValue;
    }


    /**
     * 切如 删除数据 和 逻辑删除上
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("hotDataDel()")
    public Object hotDataDelProcess(ProceedingJoinPoint point) throws Throwable {
        Object[] args= point.getArgs();
        Object returnValue = point.proceed(args);
        // 判断 方法上是否使用 HotData注解 如果没有表示开启热数据 则直接跳过
        Annotation annotation = point.getTarget().getClass().getAnnotation(EnableHotData.class);
        if(annotation == null){
            return returnValue;
        }

        // ====== 如果 使用了 EnableHotData ，表示开启热数据加载 则执行下段代码
        List<CacheDataEntity> cacheDataEntityList = this.delHandlerData(point, args);
        if(cacheDataEntityList != null && cacheDataEntityList.size() > 0){
            for (CacheDataEntity cacheDataEntity : cacheDataEntityList) {
                // 更新缓存数据 - 删除缓存
                CacheUtil.del(cacheDataEntity.getKey());

                // 广播缓存数据 - 通知其他服务器同步数据
                redisPlugin.sendMessage(
                        CacheDataMsgFactory.createMsg(cacheDataEntity.getType(),
                                cacheDataEntity.getKey(), returnValue, CacheType.DELETE)
                );
            }
        }

        return returnValue;
    }


    // ===========================================================================


    /***
     * PUT 处理数据
     * @param point
     */
    private CacheDataEntity putHandlerData(ProceedingJoinPoint point, Object returnValue){
        CacheDataEntity ret;
        // 返回值为空直接
        if(returnValue == null){
            return null;
        }
        // 这里 只对 继承了 BaseEntity 的类做处理
        if(!(returnValue instanceof BaseEntity)){
            return null;
        }
        // 报错不处理
        try {
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
                    return null;
                }

                try {
                    // 这里 只对 继承了 BaseEntity 的类做处理
                    BaseEntity baseEntity = (BaseEntity) returnValue;

                    // key 存储ID
                    String key = keyBuf.append(baseEntity.getId()).toString();

                    ret = new CacheDataEntity();
                    ret.setKey(key);
                    ret.setType(type);
                    ret.setCacheName(aCache.name());

                    return ret;
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }


    /***
     * DEL 处理数据
     * @param point
     */
    private List<CacheDataEntity> delHandlerData(ProceedingJoinPoint point, Object[] args){
        if(args == null || args.length == 0){
            return null;
        }

        // DEL 消息集合
        List<CacheDataEntity> cacheDataEntities = Lists.newArrayListWithCapacity(args.length);

        // 报错不处理
        try {
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
                    return null;
                }

                try {

                    // 处理数据
                    for (Object arg : args) {
                        if (arg instanceof String) {
                            // key 存储ID
                            String key = keyBuf.toString() + arg;
                            CacheDataEntity ret = new CacheDataEntity();
                            ret.setKey(key);
                            ret.setType(type);
                            ret.setCacheName(aCache.name());
                            cacheDataEntities.add(ret);
                        } else if (arg instanceof String[]) {
                            String[] ids = (String[]) arg;
                            for (String id : ids) {
                                // key 存储ID
                                String key = keyBuf.toString() + id;
                                CacheDataEntity ret = new CacheDataEntity();
                                ret.setKey(key);
                                ret.setType(type);
                                ret.setCacheName(aCache.name());
                                cacheDataEntities.add(ret);
                            }
                        } else if (arg instanceof BaseEntity) {
                            // key 存储ID
                            BaseEntity baseEntity = (BaseEntity) arg;
                            String key = keyBuf.toString() + baseEntity.getId();
                            CacheDataEntity ret = new CacheDataEntity();
                            ret.setKey(key);
                            ret.setType(type);
                            ret.setCacheName(aCache.name());
                            cacheDataEntities.add(ret);
                        } else if (arg instanceof Collection) {
                            try {
                                Collection<BaseEntity> baseEntityList = (Collection<BaseEntity>) arg;
                                for (BaseEntity baseEntity : baseEntityList) {
                                    // key 存储ID
                                    String key = keyBuf.toString() + baseEntity.getId();
                                    CacheDataEntity ret = new CacheDataEntity();
                                    ret.setKey(key);
                                    ret.setType(type);
                                    ret.setCacheName(aCache.name());
                                    cacheDataEntities.add(ret);
                                }
                            }catch (Exception e){
                                log.error(e.getMessage(),e);
                            }
                        }
                    }
                    return cacheDataEntities;
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

}
