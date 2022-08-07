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
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.dict.DictDetailApi;
import org.opsli.api.wrapper.system.dict.DictDetailModel;
import org.opsli.api.wrapper.system.dict.DictWrapper;
import org.opsli.common.constants.RedisConstants;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.cache.SecurityCache;
import org.opsli.core.msg.CoreMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
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
    /** Redis */
    private static RedisTemplate<String, Object> redisTemplate;

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
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_DICT_VALUE + typeCode);

        Object cache = SecurityCache.hGet(redisTemplate, cacheKey, dictValue, (k) -> {
            // 查询数据库 并保存到缓存内
            ResultWrapper<List<DictDetailModel>> resultVo = dictDetailApi.findListByTypeCode(typeCode);
            if (!ResultWrapper.isSuccess(resultVo)) {
                return null;
            }

            List<DictDetailModel> dictDetailModels = resultVo.getData();
            for (DictDetailModel model : dictDetailModels) {
                if (model.getDictValue().equals(dictValue)) {
                    // 转化Model
                    return DictUtil.formatModel(model);
                }
            }
            return null;
        });

        if(null == cache){
            return defaultVal;
        }
        DictWrapper dictWrapper = Convert.convert(DictWrapper.class, cache);
        return dictWrapper.getDictName();
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
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_DICT_NAME + typeCode);

        Object cache = SecurityCache.hGet(redisTemplate, cacheKey, dictName, (k) -> {
            // 查询数据库 并保存到缓存内
            ResultWrapper<List<DictDetailModel>> resultVo = dictDetailApi.findListByTypeCode(typeCode);
            if (!ResultWrapper.isSuccess(resultVo)) {
                return null;
            }

            List<DictDetailModel> dictDetailModels = resultVo.getData();
            for (DictDetailModel model : dictDetailModels) {
                if (model.getDictName().equals(dictName)) {
                    // 转化Model
                    return DictUtil.formatModel(model);
                }
            }
            return null;
        });

        if(null == cache){
            return defaultVal;
        }
        DictWrapper dictWrapper = Convert.convert(DictWrapper.class, cache);
        return dictWrapper.getDictValue();
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
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_DICT_VALUE + typeCode);

        Map<String, Object> dictCacheMap = SecurityCache.hGetAll(redisTemplate, cacheKey, (k) -> {
            // 查询数据库 并保存到缓存内
            ResultWrapper<List<DictDetailModel>> resultVo = dictDetailApi.findListByTypeCode(typeCode);
            if (!ResultWrapper.isSuccess(resultVo)) {
                return null;
            }

            List<DictDetailModel> dictDetailModels = resultVo.getData();
            Map<String, Object> dictMap = Maps.newHashMapWithExpectedSize(dictDetailModels.size());
            for (DictDetailModel model : dictDetailModels) {
                dictMap.put(model.getDictValue(), model);
            }
            return dictMap;
        });

        List<DictWrapper> dictWrappers = handleDictList(dictCacheMap, typeCode);

        // 排序
        return sortDictWrappers(dictWrappers);
    }




    // ===============




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

        // 缓存Key
        String cacheKeyByValue = CacheUtil.formatKey(
                RedisConstants.PREFIX_DICT_VALUE + model.getTypeCode());
        // 缓存Key
        String cacheKeyByName = CacheUtil.formatKey(
                RedisConstants.PREFIX_DICT_NAME + model.getTypeCode());


        // 计数器
        int count = 2;
        {
            boolean tmp = SecurityCache.hDel(redisTemplate, cacheKeyByValue, model.getDictValue());
            if(tmp){
                count--;
            }
        }

        {
            boolean tmp = SecurityCache.hDel(redisTemplate, cacheKeyByName, model.getDictName());
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

        // 缓存Key
        String cacheKeyByValue = CacheUtil.formatKey(
                RedisConstants.PREFIX_DICT_VALUE + typeCode);
        // 缓存Key
        String cacheKeyByName = CacheUtil.formatKey(
                RedisConstants.PREFIX_DICT_NAME + typeCode);

        return SecurityCache.remove(redisTemplate, cacheKeyByValue, cacheKeyByName);
    }


    /**
     * 格式化 字典
     * @param model 字典模型
     */
    private static DictWrapper formatModel(DictDetailModel model){
        DictWrapper dictWrapperModel = new DictWrapper();
        dictWrapperModel.setTypeCode(model.getTypeCode());
        dictWrapperModel.setDictName(model.getDictName());
        dictWrapperModel.setDictValue(model.getDictValue());
        dictWrapperModel.setModel(model);
        return dictWrapperModel;
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
    public  void init(DictDetailApi dictDetailApi,
                      RedisTemplate<String, Object> redisTemplate) {
        DictUtil.dictDetailApi = dictDetailApi;
        DictUtil.redisTemplate = redisTemplate;
        IS_INIT = true;
    }

}
