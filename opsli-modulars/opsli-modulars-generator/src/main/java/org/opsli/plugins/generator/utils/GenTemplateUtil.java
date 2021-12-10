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
import lombok.extern.slf4j.Slf4j;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.DistributedLockUtil;
import org.opsli.core.utils.ThrowExceptionUtil;
import org.opsli.modulars.generator.template.service.IGenTemplateDetailService;
import org.opsli.modulars.generator.template.wrapper.GenTemplateDetailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
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
    private static final String CACHE_PREFIX_NAME = "gen:template:";

    /** 代码模板明细 Service */
    private static IGenTemplateDetailService genTemplateDetailService;

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
        String cacheKey = CACHE_PREFIX_NAME + parentId;

        // 处理集合数据
        List<GenTemplateDetailModel> wrapperModels = handleDictList(
                    CacheUtil.getHashAll(cacheKey), parentId);
        if(CollUtil.isNotEmpty(wrapperModels)){
            return sortWrappers(wrapperModels);
        }

        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKey);
        if(hasNilFlag){
            return sortWrappers(wrapperModels);
        }

        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKey)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return sortWrappers(wrapperModels);
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            // 处理集合数据
            wrapperModels = handleDictList(
                    CacheUtil.getHashAll(cacheKey), parentId);
            if(CollUtil.isNotEmpty(wrapperModels)){
                return sortWrappers(wrapperModels);
            }


            List<GenTemplateDetailModel> listByParent = genTemplateDetailService.findListByParent(parentId);
            // 处理数据库查询数据
            if(CollUtil.isNotEmpty(listByParent)){
                wrapperModels = listByParent;
                // 计数器
                int count = wrapperModels.size();
                for (GenTemplateDetailModel model : wrapperModels) {
                    // 保存至缓存
                    boolean ret = GenTemplateUtil.put(model);
                    if(ret){
                        count--;
                    }
                }

                // 回滚 清空缓存
                if(count != 0){
                    for (GenTemplateDetailModel model : wrapperModels) {
                        GenTemplateUtil.del(model);
                    }
                }

                return sortWrappers(wrapperModels);
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKey);
        }

        // 如果值还是 为空 则赋默认值
        if(CollUtil.isEmpty(wrapperModels)){
            // 加入缓存防穿透
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKey);
        }

        // 排序
        return sortWrappers(wrapperModels);
    }

    /**
     * 模板排序
     * @param wrapperModels 字典Model
     * @return List
     */
    private static List<GenTemplateDetailModel> sortWrappers(List<GenTemplateDetailModel> wrapperModels) {
        // 非法判读
        if(wrapperModels == null){
            return null;
        }

        return ListUtil.sort(wrapperModels,
                (o1, o2) -> CompareUtil.compare(o1.getFileName(), o2.getFileName()));
    }


    // ===============


    /**
     * 删除 字典
     * @param model 字典模型
     */
    private static boolean put(GenTemplateDetailModel model){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 清除缓存
        GenTemplateUtil.del(model);

        return CacheUtil.putHash(CACHE_PREFIX_NAME + model.getParentId(),
                model.getId(), model);
    }

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

        boolean hasNilFlag = CacheUtil.hasNilFlag(CACHE_PREFIX_NAME +
                model.getParentId() + ":" + model.getId());

        GenTemplateDetailModel templateDetailModel = CacheUtil.getHash(GenTemplateDetailModel.class,
                CACHE_PREFIX_NAME + model.getParentId(),
                model.getId());

        // 计数器
        int count = 0;
        if (hasNilFlag){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(CACHE_PREFIX_NAME +
                    model.getParentId() + ":" + model.getId());
            if(tmp){
                count--;
            }
        }

        if (templateDetailModel != null){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delHash(CACHE_PREFIX_NAME +
                    model.getParentId(), model.getId());
            if(tmp){
                count--;
            }
        }

        return count == 0;
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

        List<GenTemplateDetailModel> wrapperList = GenTemplateUtil.getTemplateDetailList(parentId);
        if(CollUtil.isEmpty(wrapperList)){
            return true;
        }

        // 计数器
        int count = wrapperList.size();
        for (GenTemplateDetailModel wrapperModel : wrapperList) {
            boolean tmp = GenTemplateUtil.del(wrapperModel);
            if(tmp){
                count--;
            }
        }
        return count == 0;
    }

    /***
     * 处理返回模板明细集合
     * @param tempMap Map
     * @param id 模板ID
     * @return List
     */
    public static List<GenTemplateDetailModel> handleDictList(Map<String, Object> tempMap, String id){
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
    public  void init(IGenTemplateDetailService genTemplateDetailService) {
        GenTemplateUtil.genTemplateDetailService = genTemplateDetailService;

        IS_INIT = true;
    }

}
