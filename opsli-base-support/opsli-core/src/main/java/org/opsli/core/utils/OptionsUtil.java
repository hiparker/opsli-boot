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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.options.OptionsApi;
import org.opsli.api.wrapper.system.options.OptionsModel;
import org.opsli.common.annotation.OptionDict;
import org.opsli.common.constants.RedisConstants;
import org.opsli.common.enums.OptionsType;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.cache.SecurityCache;
import org.opsli.core.msg.CoreMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * 参数工具类
 * Hash 永久缓存
 *
 * @author Parker
 * @date 2020-09-22 11:17
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class OptionsUtil {

    /** 参数 Api */
    private static OptionsApi optionsApi;

    /** 实体类字段 */
    private static final Map<Class<?>, Field[]> ENTITY_FIELD_MAP = Maps.newHashMap();

    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;

    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据 optionsType 枚举 获得参数
     * @param optionsType 枚举类
     * @return model
     */
    public static OptionsModel getOptionByCode(OptionsType optionsType){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(optionsType == null){
            return null;
        }
        return OptionsUtil.getOptionByCode(optionsType.getCode());
    }

    /**
     * 根据 bean 对象 获得参数
     * @param beanObj 对象
     * @return T
     */
    public static <T> T getOptionByBean(T beanObj){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(beanObj == null){
            return null;
        }

        // 判断是否是Bean对象
        boolean isBean = BeanUtil.isBean(beanObj.getClass());
        if(!isBean){
            return null;
        }

        // 字段缓存 减少每次更新 反射
        Field[] fields = ENTITY_FIELD_MAP.get(beanObj.getClass());
        if(fields == null){
            fields = ReflectUtil.getFields(beanObj.getClass());
            ENTITY_FIELD_MAP.put(beanObj.getClass(), fields);
        }


        for (Field f : fields) {
            // 处理注解字段
            OptionDict optionField = f.getAnnotation(OptionDict.class);
            if (optionField != null) {
                String optionCode = optionField.value();
                // 获得配置
                OptionsModel option = getOptionByCode(optionCode);
                if(option != null){
                    BeanUtil.setProperty(beanObj, f.getName(), option.getOptionValue());
                }
            }
        }

        return beanObj;
    }

    /**
     * 根据 optionCode 获得参数
     * @param optionCode 参数编号
     * @return OptionsModel
     */
    public static OptionsModel getOptionByCode(String optionCode){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_OPTIONS_CODE);

        Object cache = SecurityCache.hGet(redisTemplate, cacheKey, optionCode, (k) -> {
            // 查询数据库
            ResultWrapper<OptionsModel> resultVo = optionsApi.getByCode(optionCode);
            if(!ResultWrapper.isSuccess(resultVo)){
                return null;
            }
            return resultVo.getData();
        });

        return Convert.convert(OptionsModel.class, cache);
    }

    // ============== 刷新缓存 ==============

    /**
     * 刷新参数 - 删就完了
     * @param option 参数
     * @return boolean
     */
    public static boolean refreshOption(OptionsModel option){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(option == null || StringUtils.isEmpty(option.getOptionCode())){
            return true;
        }

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_OPTIONS_CODE);

        // 删除缓存
        return SecurityCache.hDel(redisTemplate, cacheKey, option.getOptionCode());
    }

    /**
     * 处理缓存参数数据
     * @param optionsMap List
     * @return List
     */
    public static List<OptionsModel> handleOptionsList(Map<String, Object> optionsMap){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(CollUtil.isEmpty(optionsMap)){
            return null;
        }

        List<OptionsModel> optionsModels = Lists.newArrayList();

        for (Map.Entry<String, Object> entry : optionsMap.entrySet()) {
            OptionsModel convert = Convert.convert(OptionsModel.class, entry.getValue());
            optionsModels.add(convert);
        }

        return optionsModels;
    }

    /**
     * 转换参数数据 - Map
     * @param optionsModels Map
     * @return Map
     */
    public static Map<String, OptionsModel> convertOptionsMap(List<OptionsModel> optionsModels){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 这里不管有没有 都返回一个集合 防止多做一步null处理
        Map<String, OptionsModel> optionsModelMap = Maps.newHashMap();

        // List 转换 Map
        if(CollUtil.isNotEmpty(optionsModels)){
            for (OptionsModel optionsModel : optionsModels) {
                optionsModelMap.put(optionsModel.getOptionCode(), optionsModel);
            }
        }
        return optionsModelMap;
    }

    // =====================================

    /**
     * 初始化
     */
    @Autowired
    public void init(OptionsApi optionsApi,
                     RedisTemplate<String, Object> redisTemplate) {
        OptionsUtil.optionsApi = optionsApi;
        OptionsUtil.redisTemplate = redisTemplate;
        IS_INIT = true;
    }
}
