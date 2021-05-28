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
package org.opsli.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.dict.DictDetailApi;
import org.opsli.api.wrapper.system.dict.DictDetailModel;
import org.opsli.api.wrapper.system.dict.DictWrapper;
import org.opsli.common.constants.DictConstants;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.msg.CoreMsg;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * 字典工具类
 *
 * @author parker
 * @date 2020-09-22 11:17
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class DictUtil {

    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;

    /** 字典Service */
    private static DictDetailApi dictDetailApi;

    /**
     * 根据 字典值 取 字典名称
     * @param typeCode 字典类型Code
     * @param dictValue 字典值
     * @param defaultVal 默认值
     * @return String
     */
    public static String getDictNameByValue(String typeCode, String dictValue, String defaultVal){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = DictConstants.CACHE_PREFIX_VALUE + typeCode;
        // 缓存Key + VALUE
        String cacheKeyVal = cacheKey + ":" + dictValue;

        // 字典名称
        String dictName = null;

        DictDetailModel cacheModel = CacheUtil.getHash(DictDetailModel.class, cacheKey,
                dictValue);
        // 如果缓存有值 直接返回
        if (cacheModel != null &&
                StringUtils.isNotEmpty(cacheModel.getDictName())){
            return cacheModel.getDictName();
        }

        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKeyVal);
        if(hasNilFlag){
            return defaultVal;
        }

        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKeyVal)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return null;
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            cacheModel = CacheUtil.getHash(DictDetailModel.class, cacheKey,
                    dictValue);
            // 如果缓存有值 直接返回
            if (cacheModel != null &&
                    StringUtils.isNotEmpty(cacheModel.getDictName())){
                return cacheModel.getDictName();
            }

            // 查询数据库 并保存到缓存内
            ResultVo<List<DictDetailModel>> resultVo = dictDetailApi.findListByTypeCode(typeCode);
            if(resultVo.isSuccess()){
                List<DictDetailModel> dictDetailModels = resultVo.getData();
                for (DictDetailModel model : dictDetailModels) {
                    if(model.getDictValue().equals(dictValue)){
                        // 保存至缓存
                        DictWrapper dictWrapper = DictUtil.putByModel(model);
                        // 缓存名
                        dictName = dictWrapper.getDictName();
                        break;
                    }
                }
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);
            return defaultVal;
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKeyVal);
        }

        // 如果名称还是 为空 则赋默认值
        if(StringUtils.isEmpty(dictName)){
            // 加入缓存防穿透
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKeyVal);
            dictName = defaultVal;
        }
        return dictName;
    }

    /**
     * 根据 字典名称 取 字典值
     * @param typeCode 字典类型Code
     * @param dictName 字典名称
     * @param defaultVal 默认值
     * @return String
     */
    public static String getDictValueByName(String typeCode, String dictName, String defaultVal){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = DictConstants.CACHE_PREFIX_NAME + typeCode;
        // 缓存Key + VALUE
        String cacheKeyVal = cacheKey + ":" + dictName;

        // 字典值
        String dictValue = null;

        DictDetailModel cacheModel = CacheUtil.getHash(DictDetailModel.class, cacheKey,
                dictName);
        // 如果缓存有值 直接返回
        if (cacheModel != null &&
                StringUtils.isNotEmpty(cacheModel.getDictValue())){
            return cacheModel.getDictValue();
        }

        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKeyVal);
        if(hasNilFlag){
            return defaultVal;
        }

        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKeyVal)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return null;
            }

            cacheModel = CacheUtil.getHash(DictDetailModel.class, cacheKey,
                    dictName);
            // 如果缓存有值 直接返回
            if (cacheModel != null &&
                    StringUtils.isNotEmpty(cacheModel.getDictValue())){
                return cacheModel.getDictValue();
            }

            // 查询数据库 并保存到缓存内
            ResultVo<List<DictDetailModel>> resultVo = dictDetailApi.findListByTypeCode(typeCode);
            if(resultVo.isSuccess()){
                List<DictDetailModel> dictDetailModels = resultVo.getData();
                for (DictDetailModel model : dictDetailModels) {
                    if(model.getDictName().equals(dictName)){
                        // 保存至缓存
                        DictWrapper dictWrapper = DictUtil.putByModel(model);
                        // 值
                        dictValue = dictWrapper.getDictValue();
                        break;
                    }
                }
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);
            return defaultVal;
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKeyVal);
        }

        // 如果值还是 为空 则赋默认值
        if(StringUtils.isEmpty(dictValue)){
            // 加入缓存防穿透
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKeyVal);
            dictValue = defaultVal;
        }
        return dictValue;
    }

    /**
     * 根据字典code 获得字典列表
     * @param typeCode 类型编号
     * @return List
     */
    public static List<DictWrapper> getDictList(String typeCode){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = DictConstants.CACHE_PREFIX_NAME + typeCode;

        // 处理集合数据
        List<DictWrapper> dictWrapperModels = handleDictList(
                    CacheUtil.getHashAll(cacheKey), typeCode);
        if(CollUtil.isNotEmpty(dictWrapperModels)){
            return sortDictWrappers(dictWrapperModels);
        }

        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKey);
        if(hasNilFlag){
            return sortDictWrappers(dictWrapperModels);
        }

        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKey)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return sortDictWrappers(dictWrapperModels);
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            // 处理集合数据
            dictWrapperModels = handleDictList(
                    CacheUtil.getHashAll(cacheKey), typeCode);
            if(CollUtil.isNotEmpty(dictWrapperModels)){
                return sortDictWrappers(dictWrapperModels);
            }


            // 查询数据库 并保存到缓存内
            ResultVo<List<DictDetailModel>> resultVo = dictDetailApi.findListByTypeCode(typeCode);
            if(resultVo.isSuccess()){
                List<DictDetailModel> dictDetailModels = resultVo.getData();
                // 处理数据库查询数据
                if(CollUtil.isNotEmpty(dictDetailModels)){
                    dictWrapperModels = Lists.newArrayListWithCapacity(dictDetailModels.size());
                    for (DictDetailModel model : dictDetailModels) {
                        // 保存至缓存
                        DictWrapper dictWrapper = DictUtil.putByModel(model);
                        dictWrapperModels.add(dictWrapper);
                    }

                    return sortDictWrappers(dictWrapperModels);
                }
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKey);
        }

        // 如果值还是 为空 则赋默认值
        if(CollUtil.isEmpty(dictWrapperModels)){
            // 加入缓存防穿透
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKey);
        }

        // 排序
        return sortDictWrappers(dictWrapperModels);
    }

    /**
     * 字典排序
     * @param dictWrapperModels 字典Model
     * @return List
     */
    private static List<DictWrapper> sortDictWrappers(List<DictWrapper> dictWrapperModels) {
        // 非法判读
        if(dictWrapperModels == null){
            return null;
        }

        ListUtil.sort(dictWrapperModels, (o1, o2) -> {
            int oInt1 = Integer.MAX_VALUE;
            int oInt2 = Integer.MAX_VALUE;
            if(o1 != null && o1.getModel() != null){
                oInt1 = o1.getModel().getSortNo()==null?oInt1:o1.getModel().getSortNo();
            }
            if(o2 != null && o2.getModel() != null){
                oInt2 = o2.getModel().getSortNo()==null?oInt2:o2.getModel().getSortNo();
            }
            return Integer.compare(oInt1, oInt2);
        });
        return dictWrapperModels;
    }


    // ===============


    /**
     * 删除 字典
     * @param model 字典模型
     */
    private static DictWrapper putByModel(DictDetailModel model){
        DictWrapper dictWrapperModel = new DictWrapper();
        dictWrapperModel.setTypeCode(model.getTypeCode());
        dictWrapperModel.setDictName(model.getDictName());
        dictWrapperModel.setDictValue(model.getDictValue());
        dictWrapperModel.setModel(model);
        // 保存至缓存
        DictUtil.put(dictWrapperModel);
        return dictWrapperModel;
    }

    /**
     * 删除 字典
     * @param model 字典模型
     */
    public static void put(DictWrapper model){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 清除缓存
        DictUtil.del(model);

        CacheUtil.putHash(DictConstants.CACHE_PREFIX_NAME + model.getTypeCode(),
                model.getDictName(), model.getModel());
        CacheUtil.putHash(DictConstants.CACHE_PREFIX_VALUE + model.getTypeCode(),
                model.getDictValue(), model.getModel());
    }

    /**
     * 删除 字典
     * @param model 字典模型
     * @return boolean
     */
    public static boolean del(DictWrapper model){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(model == null){
            return true;
        }

        boolean hasNilFlagByName = CacheUtil.hasNilFlag(DictConstants.CACHE_PREFIX_NAME +
                model.getTypeCode() + ":" + model.getDictName());
        boolean hasNilFlagByValue = CacheUtil.hasNilFlag(DictConstants.CACHE_PREFIX_VALUE +
                model.getTypeCode() + ":" + model.getDictValue());

        DictWrapper dictByName = CacheUtil.getHash(DictWrapper.class,
                DictConstants.CACHE_PREFIX_NAME + model.getTypeCode(),
                model.getDictName());
        DictWrapper dictByValue = CacheUtil.getHash(DictWrapper.class,
                DictConstants.CACHE_PREFIX_VALUE + model.getTypeCode(),
                model.getDictValue());

        // 计数器
        int count = 0;
        if (hasNilFlagByName){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(DictConstants.CACHE_PREFIX_NAME +
                    model.getTypeCode() + ":" + model.getDictName());
            if(tmp){
                count--;
            }
        }

        if (hasNilFlagByValue){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(DictConstants.CACHE_PREFIX_VALUE +
                    model.getTypeCode() + ":" + model.getDictValue());
            if(tmp){
                count--;
            }
        }

        if (dictByName != null){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delHash(DictConstants.CACHE_PREFIX_NAME +
                    model.getTypeCode(), model.getDictName());
            if(tmp){
                count--;
            }
        }

        if (dictByValue != null){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delHash(DictConstants.CACHE_PREFIX_VALUE +
                    model.getTypeCode(), model.getDictValue());
            if(tmp){
                count--;
            }
        }

        return count == 0;
    }

    /**
     * 删除 typeCode 下所有字典
     * @param typeCode 字典编号
     * @return boolean
     */
    public static boolean delAll(String typeCode){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        List<DictWrapper> dictWrapperList = DictUtil.getDictList(typeCode);
        if(CollUtil.isEmpty(dictWrapperList)){
            return true;
        }

        // 计数器
        int count = dictWrapperList.size();
        for (DictWrapper dictWrapperModel : dictWrapperList) {
            boolean tmp = DictUtil.del(dictWrapperModel);
            if(tmp){
                count--;
            }
        }
        return count == 0;
    }

    /***
     * 处理返回字典集合
     * @param dictMap Map
     * @param typeCode 类型编号
     * @return List
     */
    public static List<DictWrapper> handleDictList(Map<String, Object> dictMap, String typeCode){
        List<DictWrapper> dictWrapperModels = Lists.newArrayList();
        if(CollUtil.isNotEmpty(dictMap)){
            for (Map.Entry<String, Object> entry : dictMap.entrySet()) {
                // 赋值
                Object data = entry.getValue();

                DictDetailModel model = Convert.convert(DictDetailModel.class, data);
                DictWrapper dictWrapperModel = new DictWrapper();
                dictWrapperModel.setTypeCode(typeCode);
                dictWrapperModel.setDictName(model.getDictName());
                dictWrapperModel.setDictValue(model.getDictValue());
                dictWrapperModel.setDictValue(model.getDictValue());
                dictWrapperModel.setModel(model);
                dictWrapperModels.add(dictWrapperModel);
            }
         }

        // 返回排序后 list
        return CollUtil.isNotEmpty(dictWrapperModels)?sortDictWrappers(dictWrapperModels):dictWrapperModels;
    }


    // ===================================

    /**
     * 初始化
     */
    @Autowired
    public  void init(DictDetailApi dictDetailApi) {
        DictUtil.dictDetailApi = dictDetailApi;

        IS_INIT = true;
    }

}
