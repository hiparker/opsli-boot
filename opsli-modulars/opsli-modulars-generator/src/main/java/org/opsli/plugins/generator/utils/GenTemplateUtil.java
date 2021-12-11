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
package org.opsli.plugins.generator.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.convert.Convert;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.cache.SecurityCache;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.ThrowExceptionUtil;
import org.opsli.modulars.generator.template.service.IGenTemplateDetailService;
import org.opsli.modulars.generator.template.wrapper.GenTemplateDetailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * 代码模板
 *
 * @author parker
 * @date 2020-09-22 11:17
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class GenTemplateUtil {

    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;

    /** 缓存前缀 NAME */
    private static final String CACHE_PREFIX_NAME = "hash#{}:gen:template:";

    /** 代码模板明细 Service */
    private static IGenTemplateDetailService genTemplateDetailService;

    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据模板ID 模板明细列表
     * @param parentId 模板ID
     * @return List
     */
    public static List<GenTemplateDetailModel> getTemplateDetailList(String parentId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(CACHE_PREFIX_NAME + parentId);

        Map<String, Object> templateCache = SecurityCache.hGetAll(redisTemplate, cacheKey, (k) -> {
            // 处理数据库查询数据
            List<GenTemplateDetailModel> listByParent = genTemplateDetailService.findListByParent(parentId);
            Map<String, Object> templateMap = Maps.newHashMapWithExpectedSize(listByParent.size());
            for (GenTemplateDetailModel genTemplateDetailModel : listByParent) {
                templateMap.put(genTemplateDetailModel.getId(), genTemplateDetailModel);
            }
            return templateMap;
        });


        // 处理集合数据
        List<GenTemplateDetailModel> wrapperModels = handleDictList(templateCache);
        return sortWrappers(wrapperModels);
    }

    /**
     * 模板排序
     * @param wrapperModels 字典Model
     * @return List
     */
    private static List<GenTemplateDetailModel> sortWrappers(List<GenTemplateDetailModel> wrapperModels) {
        // 非法判读
        if(CollUtil.isEmpty(wrapperModels)){
            return ListUtil.empty();
        }

        return ListUtil.sort(wrapperModels,
                (o1, o2) -> CompareUtil.compare(o1.getFileName(), o2.getFileName()));
    }


    // ===============


    /**
     * 删除 字典
     * @param model 字典模型
     * @return boolean
     */
    public static boolean del(GenTemplateDetailModel model){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(model == null){
            return true;
        }

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(CACHE_PREFIX_NAME + model.getParentId());

        return SecurityCache.hDel(redisTemplate, cacheKey, model.getId());
    }

    /**
     * 删除 parentId 下所有模板
     * @param parentId 模板ID
     * @return boolean
     */
    public static boolean delAll(String parentId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(CACHE_PREFIX_NAME + parentId);

        return SecurityCache.remove(redisTemplate, cacheKey);
    }

    /***
     * 处理返回模板明细集合
     * @param tempMap Map
     * @return List
     */
    public static List<GenTemplateDetailModel> handleDictList(Map<String, Object> tempMap){
        List<GenTemplateDetailModel> wrapperModels = Lists.newArrayList();
        if(CollUtil.isNotEmpty(tempMap)){
            for (Map.Entry<String, Object> entry : tempMap.entrySet()) {
                // 赋值
                Object data = entry.getValue();
                GenTemplateDetailModel templateDetailModel = Convert.convert(GenTemplateDetailModel.class, data);
                wrapperModels.add(templateDetailModel);
            }
         }

        // 返回排序后 list
        return CollUtil.isNotEmpty(wrapperModels)?sortWrappers(wrapperModels):wrapperModels;
    }


    // ===================================

    /**
     * 初始化
     */
    @Autowired
    public  void init(IGenTemplateDetailService genTemplateDetailService,
                      RedisTemplate<String, Object> redisTemplate) {
        GenTemplateUtil.genTemplateDetailService = genTemplateDetailService;
        GenTemplateUtil.redisTemplate = redisTemplate;

        IS_INIT = true;
    }

}
