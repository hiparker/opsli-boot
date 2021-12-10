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
package org.opsli.core.filters.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.hotdata.EnableHotData;
import org.opsli.common.annotation.hotdata.HotDataDel;
import org.opsli.common.annotation.hotdata.HotDataPut;
import org.opsli.common.constants.CacheConstants;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.cache.pushsub.entity.CacheDataEntity;
import org.opsli.core.cache.pushsub.enums.CacheHandleType;
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

    @Autowired
    private RedisPlugin redisPlugin;

    @Pointcut("@annotation(org.opsli.common.annotation.hotdata.HotDataPut)")
    public void hotDataPut() {
    }

    @Pointcut("@annotation(org.opsli.common.annotation.hotdata.HotDataDel)")
    public void hotDataDel() {
    }

    /**
     * 切如 更新数据
     * @param point point
     * @return Object
     * @throws Throwable 异常
     */
    @Around("hotDataPut()")
    public Object hotDataPutProcess(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Object returnValue = point.proceed(args);
        // 判断 方法上是否使用 EnableHotData注解 如果没有表示开启热数据 则直接跳过
        Annotation annotation = point.getTarget().getClass().getAnnotation(EnableHotData.class);
        if(annotation == null){
            return returnValue;
        }

        List<CacheDataEntity> cacheDataEntityList = this.putHandlerData(point, returnValue);
        // 非法判断
        if(CollUtil.isEmpty(cacheDataEntityList)){
            return returnValue;
        }

        for (CacheDataEntity cacheDataEntity : cacheDataEntityList) {
            // 更新缓存数据
            // 热点数据
            boolean putRet = CacheUtil.put(CacheConstants.HOT_DATA_PREFIX +":"+ cacheDataEntity.getKey(),
                    returnValue);
            if(putRet){
                // 广播缓存数据 - 通知其他服务器同步数据
                redisPlugin.sendMessage(
                        CacheDataMsgFactory.createMsg(
                                cacheDataEntity, returnValue, CacheHandleType.UPDATE)
                );
            }
        }

        return returnValue;
    }


    /**
     * 切如 删除数据 和 逻辑删除上
     * @param point point
     * @return Object
     * @throws Throwable 异常
     */
    @Around("hotDataDel()")
    public Object hotDataDelProcess(ProceedingJoinPoint point) throws Throwable {
        Object[] args= point.getArgs();
        Object returnValue = point.proceed(args);
        // 判断 方法上是否使用 EnableHotData注解 如果没有表示开启热数据 则直接跳过
        Annotation annotation = point.getTarget().getClass().getAnnotation(EnableHotData.class);
        if(annotation == null){
            return returnValue;
        }

        // 删除状态判断
        try {
            Boolean ret = (Boolean) returnValue;
            if(ret == null || !ret){
                return returnValue;
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return returnValue;
        }

        List<CacheDataEntity> cacheDataEntityList = this.delHandlerData(point, args);
        // 非法判断
        if(CollUtil.isEmpty(cacheDataEntityList)){
            return returnValue;
        }

        for (CacheDataEntity cacheDataEntity : cacheDataEntityList) {
            // 更新缓存数据 - 删除缓存
            boolean delRet = CacheUtil.del(CacheConstants.HOT_DATA_PREFIX +":"+ cacheDataEntity.getKey());
            if(delRet){
                // 广播缓存数据 - 通知其他服务器同步数据
                redisPlugin.sendMessage(
                        CacheDataMsgFactory.createMsg(
                                cacheDataEntity, null, CacheHandleType.DELETE)
                );
            }
        }

        return returnValue;
    }


    // ===========================================================================


    /***
     * PUT 处理数据
     * @param point point
     */
    private List<CacheDataEntity> putHandlerData(ProceedingJoinPoint point, Object returnValue){
        // 这里 只对 继承了 ApiWrapper 的类做处理
        if(!(returnValue instanceof ApiWrapper)){
            return null;
        }

        // 消息集合 后续可能会考虑 多消息存储
        List<CacheDataEntity> cacheDataEntities = Lists.newArrayListWithCapacity(1);

        // 报错不处理
        try {
            // 获得方法
            Method objMethod = this.getMethod(point);
            if(objMethod == null) {
                return null;
            }

            // 获取注解参数
            HotDataPut aCache = objMethod.getAnnotation(HotDataPut.class);
            if(aCache != null){
                // 这里 只对 继承了 BaseEntity 的类做处理
                ApiWrapper apiWrapper = (ApiWrapper) returnValue;

                CacheDataEntity ret = new CacheDataEntity(apiWrapper.getId());
                // 存放数据
                this.putCacheData(cacheDataEntities, ret);

                return cacheDataEntities;
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }


    /***
     * DEL 处理数据
     * @param point point
     */
    private List<CacheDataEntity> delHandlerData(ProceedingJoinPoint point, Object[] args){
        if(args == null || args.length == 0){
            return null;
        }

        // 消息集合
        List<CacheDataEntity> cacheDataEntities = Lists.newArrayListWithCapacity(args.length);

        // 报错不处理
        try {
            // 获得方法
            Method objMethod = this.getMethod(point);
            if(objMethod == null) {
                return null;
            }

            // 获取注解参数
            HotDataDel aCache= objMethod.getAnnotation(HotDataDel.class);
            if(aCache != null){

                List<String> keyList = null;

                // 处理数据
                for (Object arg : args) {
                    if (arg instanceof ApiWrapper) {
                        // key 存储ID
                        ApiWrapper apiWrapper = Convert.convert(ApiWrapper.class, arg);
                        keyList = Convert.toList(String.class, apiWrapper.getId());
                    } else if (arg instanceof Collection) {
                        try {
                            keyList = Lists.newArrayList();
                            List<ApiWrapper> baseEntityList = Convert.toList(ApiWrapper.class, arg);
                            for (ApiWrapper baseEntity : baseEntityList) {
                                keyList.add(baseEntity.getId());
                            }
                        }catch (Exception e){
                            log.error(e.getMessage(),e);
                        }
                    }else {
                        keyList = Convert.toList(String.class, arg);
                    }
                }

                if(keyList != null && CollUtil.isNotEmpty(keyList)){
                    for (String key : keyList) {
                        CacheDataEntity ret = new CacheDataEntity(key);
                        // 存放数据
                        this.putCacheData(cacheDataEntities, ret);
                    }
                }

                return cacheDataEntities;
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }


    // =====================

    /**
     * 获得方法
     * @param point point
     * @return Method 方法
     */
    private Method getMethod(ProceedingJoinPoint point){
        Method m = null;
        try {
            String methodName= point.getSignature().getName();
            Class<?> classTarget= point.getTarget().getClass();
            Class<?>[] par=((MethodSignature) point.getSignature()).getParameterTypes();
            m = classTarget.getMethod(methodName, par);
        }catch (Exception ignored){}
        return m;
    }

    /**
     * 存放数据
     * @param cacheDataList 缓存数据集合
     * @param cacheData 缓存数据
     */
    private void putCacheData(List<CacheDataEntity> cacheDataList, CacheDataEntity cacheData){
        // 非法判断
        if(cacheDataList == null){
            return;
        }
        cacheDataList.add(cacheData);
    }


}
