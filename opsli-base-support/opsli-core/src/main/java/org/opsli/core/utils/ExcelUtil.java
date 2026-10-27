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
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.api.wrapper.system.dict.DictWrapper;
import org.opsli.common.enums.ExcelOperate;
import org.opsli.core.utils.excel.factory.AbstractModelHelper;
import org.opsli.core.utils.excel.factory.ModelFactoryHelper;
import org.opsli.plugins.excel.ExcelPlugin;
import org.opsli.plugins.excel.annotation.ExcelInfo;
import org.opsli.plugins.excel.exception.ExcelPluginException;
import org.opsli.plugins.excel.listener.BatchExcelListener;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Excel 工具类 - 性能优化版本
 *
 * @author Pace
 * @date 2025-06-01 11:17
 */
@Slf4j
public final class ExcelUtil {

    /** 字典KEY */
    public static final String DICT_NAME_KEY = "dictName";
    public static final String DICT_VALUE_KEY = "dictValue";

    /** 字段字典缓存 - 使用 Guava Cache 提供过期和大小限制 */
    private static final Cache<Class<?>, JSONObject> FIELD_DICT_CACHE = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    /** 字典数据缓存 - 避免重复查询字典 */
    private static final Cache<String, Map<String, String>> DICT_DATA_CACHE = CacheBuilder.newBuilder()
            .maximumSize(5000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    /** ModelHelper 缓存 */
    private static final Map<Class<?>, AbstractModelHelper> MODEL_HELPER_CACHE = new ConcurrentHashMap<>();

    private ExcelUtil(){}

    /** 静态内部类，里面实例化外部类 */
    private static class ExcelUtilSingletonHolder {
        private static final ExcelPlugin EXCEL_PLUGIN = new ExcelPlugin();
        private static final ExcelUtil INSTANCE = new ExcelUtil();
    }

    /**
     * 获得实例对象
     * @return ExcelUtil
     */
    public static ExcelUtil getInstance(){
        return ExcelUtilSingletonHolder.INSTANCE;
    }

    // ================================================

    public <T> List<T> readExcel(MultipartFile excel, Class<T> rowModel) throws ExcelPluginException {
        List<T> ts = ExcelUtilSingletonHolder.EXCEL_PLUGIN.readExcel(excel, rowModel);
        return this.handleDatas(ts, rowModel, ExcelOperate.READ);
    }

    public <T> List<T> readExcel(MultipartFile excel, Class<T> rowModel, String sheetName) throws ExcelPluginException {
        List<T> ts = ExcelUtilSingletonHolder.EXCEL_PLUGIN.readExcel(excel, rowModel, sheetName);
        return this.handleDatas(ts, rowModel, ExcelOperate.READ);
    }

    public <T> List<T> readExcel(MultipartFile excel, Class<T> rowModel, String sheetName, int headLineNum) throws ExcelPluginException {
        List<T> ts = ExcelUtilSingletonHolder.EXCEL_PLUGIN.readExcel(excel, rowModel, sheetName, headLineNum);
        return this.handleDatas(ts, rowModel, ExcelOperate.READ);
    }

    ///////////////////////

    public <T> void readExcelByListener(MultipartFile excel, Class<T> rowModel,
                                        BatchExcelListener<T> batchExcelListener) throws ExcelPluginException {
        ExcelUtilSingletonHolder.EXCEL_PLUGIN.readExcelByListener(excel, rowModel, batchExcelListener);
    }

    public <T> void readExcelByListener(MultipartFile excel, Class<T> rowModel, String sheetName,
                                        BatchExcelListener<T> batchExcelListener) throws ExcelPluginException {
        ExcelUtilSingletonHolder.EXCEL_PLUGIN.readExcelByListener(excel, rowModel, sheetName, batchExcelListener);
    }

    public <T> void readExcelByListener(MultipartFile excel, Class<T> rowModel, String sheetName, int headLineNum,
                                        BatchExcelListener<T> batchExcelListener) throws ExcelPluginException {
        ExcelUtilSingletonHolder.EXCEL_PLUGIN.readExcelByListener(excel, rowModel, sheetName, headLineNum, batchExcelListener);
    }

    ///////////////////////

    public <T> void writeExcel(HttpServletResponse response, List<T> list, String fileName, String sheetName,
                               Class<T> classType, ExcelTypeEnum excelTypeEnum) throws ExcelPluginException {
        List<T> ts = this.handleDatas(list, classType, ExcelOperate.WRITE);
        ExcelUtilSingletonHolder.EXCEL_PLUGIN.writeExcel(response, ts, fileName, sheetName, classType, excelTypeEnum);
    }

    /**
     * 处理字典 - 性能优化版本
     * @param datas 数据
     * @param typeClazz 数据CLazz
     * @param operate 操作方式
     * @param <T> 泛型
     * @return List<T>
     */
    public <T> List<T> handleDatas(List<T> datas, Class<T> typeClazz, ExcelOperate operate){
        if (CollUtil.isEmpty(datas)) {
            return datas;
        }

        TimeInterval timer = DateUtil.timer();

        try {
            // 获取字段字典配置（使用缓存）
            JSONObject fieldsJson = this.getFieldsFromCache(typeClazz);
            if (JSONUtil.isNull(fieldsJson) || fieldsJson.isEmpty()) {
                return datas;
            }

            // 获取字典数据（使用缓存）
            JSONObject fieldsDictJson = this.getFieldsDictFromCache(fieldsJson);
            if (JSONUtil.isNull(fieldsDictJson)) {
                return datas;
            }

            // 获得 helper类（使用缓存）
            AbstractModelHelper modelHelper = getModelHelperFromCache(typeClazz);

            // 批量处理数据
            this.batchProcessData(datas, modelHelper, fieldsDictJson, operate);

        } catch (Exception e) {
            log.error("Excel数据处理失败: {}", e.getMessage(), e);
            return datas;
        } finally {
            long timerCount = timer.interval();
            log.info("Excel 处理数据耗时：{} ms, 数据量：{}", timerCount, datas.size());
        }

        return datas;
    }

    /**
     * 批量处理数据
     */
    private <T> void batchProcessData(List<T> datas, AbstractModelHelper modelHelper,
                                      JSONObject fieldsDictJson, ExcelOperate operate) {
        // 使用并行流处理大量数据，小量数据使用串行流
        if (datas.size() > 1000) {
            datas.parallelStream().forEach(data -> processData(data, modelHelper, fieldsDictJson, operate));
        } else {
            datas.forEach(data -> processData(data, modelHelper, fieldsDictJson, operate));
        }
    }

    /**
     * 处理单个数据
     */
    private <T> void processData(T data, AbstractModelHelper modelHelper,
                                 JSONObject fieldsDictJson, ExcelOperate operate) {
        try {
            switch (operate) {
                case READ:
                    modelHelper.transformByImport(fieldsDictJson, cast(data));
                    break;
                case WRITE:
                    modelHelper.transformByExport(fieldsDictJson, cast(data));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.warn("处理数据失败: {}", e.getMessage());
        }
    }

    /**
     * 从缓存获取字段配置
     */
    private JSONObject getFieldsFromCache(Class<?> clazz) {
        try {
            return FIELD_DICT_CACHE.get(clazz, () -> this.buildFieldsConfig(clazz));
        } catch (ExecutionException e) {
            log.error("获取字段配置失败: {}", e.getMessage(), e);
            return this.buildFieldsConfig(clazz);
        }
    }

    /**
     * 构建字段配置
     */
    private JSONObject buildFieldsConfig(Class<?> clazz) {
        JSONObject fieldNameAndTypeCodeDict = JSONUtil.createObj();

        // 使用缓存的反射结果
        Field[] fields = ReflectUtil.getFields(clazz);

        for (Field field : fields) {
            ExcelInfo excelInfo = field.getAnnotation(ExcelInfo.class);
            if (excelInfo != null && StringUtils.isNotEmpty(excelInfo.dictType())) {
                fieldNameAndTypeCodeDict.putOpt(field.getName(), excelInfo.dictType());
            }
        }

        return fieldNameAndTypeCodeDict;
    }

    /**
     * 从缓存获取字典数据
     */
    private JSONObject getFieldsDictFromCache(JSONObject fieldNameAndTypeCodeDict) {
        if (JSONUtil.isNull(fieldNameAndTypeCodeDict) || fieldNameAndTypeCodeDict.isEmpty()) {
            return null;
        }

        JSONObject dictJson = JSONUtil.createObj();

        try {
            // 批量获取所有需要的字典类型
            Set<String> dictTypes = fieldNameAndTypeCodeDict.values()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet());

            // 批量加载字典数据
            Map<String, JSONObject> dictDataMap = this.batchLoadDictData(dictTypes);

            // 构建最终的字典JSON
            for (Map.Entry<String, Object> entry : fieldNameAndTypeCodeDict.entrySet()) {
                String typeCode = entry.getValue().toString();
                JSONObject dictObject = dictDataMap.get(typeCode);
                if (dictObject != null) {
                    dictJson.putOpt(typeCode, dictObject);
                }
            }
        } catch (Exception e) {
            log.error("获取字典数据失败: {}", e.getMessage(), e);
        }

        return dictJson;
    }

    /**
     * 批量加载字典数据
     */
    private Map<String, JSONObject> batchLoadDictData(Set<String> dictTypes) {
        Map<String, JSONObject> result = new HashMap<>();

        for (String dictType : dictTypes) {
            try {
                // 先从缓存获取name->value映射
                Map<String, String> nameToValue = DICT_DATA_CACHE.get(dictType + "_name",
                        () -> this.loadDictNameToValue(dictType));

                // 再从缓存获取value->name映射
                Map<String, String> valueToName = DICT_DATA_CACHE.get(dictType + "_value",
                        () -> this.loadDictValueToName(dictType));

                if (!nameToValue.isEmpty() || !valueToName.isEmpty()) {
                    JSONObject dictObject = JSONUtil.createObj();
                    dictObject.putOpt(DICT_NAME_KEY, JSONUtil.parseObj(nameToValue));
                    dictObject.putOpt(DICT_VALUE_KEY, JSONUtil.parseObj(valueToName));
                    result.put(dictType, dictObject);
                }
            } catch (ExecutionException e) {
                log.warn("加载字典数据失败 [{}]: {}", dictType, e.getMessage());
            }
        }

        return result;
    }

    /**
     * 加载字典name->value映射
     */
    private Map<String, String> loadDictNameToValue(String dictType) {
        List<DictWrapper> dictWrapperList = DictUtil.getDictList(dictType);
        if (CollUtil.isEmpty(dictWrapperList)) {
            return Collections.emptyMap();
        }

        return dictWrapperList.stream()
                .filter(Objects::nonNull)
                .filter(wrapper -> StringUtils.isNotEmpty(wrapper.getDictName())
                        && StringUtils.isNotEmpty(wrapper.getDictValue()))
                .collect(Collectors.toMap(
                        DictWrapper::getDictName,
                        DictWrapper::getDictValue,
                        (existing, replacement) -> existing // 处理重复key
                ));
    }

    /**
     * 加载字典value->name映射
     */
    private Map<String, String> loadDictValueToName(String dictType) {
        List<DictWrapper> dictWrapperList = DictUtil.getDictList(dictType);
        if (CollUtil.isEmpty(dictWrapperList)) {
            return Collections.emptyMap();
        }

        return dictWrapperList.stream()
                .filter(Objects::nonNull)
                .filter(wrapper -> StringUtils.isNotEmpty(wrapper.getDictName())
                        && StringUtils.isNotEmpty(wrapper.getDictValue()))
                .collect(Collectors.toMap(
                        DictWrapper::getDictValue,
                        DictWrapper::getDictName,
                        (existing, replacement) -> existing // 处理重复key
                ));
    }

    /**
     * 从缓存获取ModelHelper
     */
    private <T> AbstractModelHelper getModelHelperFromCache(Class<T> typeClazz) {
        return MODEL_HELPER_CACHE.computeIfAbsent(typeClazz, clazz -> {
            try {
                return ModelFactoryHelper.getModelHelper(clazz);
            } catch (Exception e) {
                throw new RuntimeException("获取ModelHelper失败", e);
            }
        });
    }


    /**
     * 获得字段字典Code - 保持向后兼容
     * @deprecated 使用 getFieldsFromCache 替代
     */
    @Deprecated
    public JSONObject getFields(Class<?> clazz) {
        return getFieldsFromCache(clazz);
    }

    /**
     * 获得字段字典 - 保持向后兼容
     * @deprecated 使用 getFieldsDictFromCache 替代
     */
    @Deprecated
    public JSONObject getFieldsDict(final JSONObject fieldNameAndTypeCodeDict) {
        return getFieldsDictFromCache(fieldNameAndTypeCodeDict);
    }

    /**
     * 清理缓存
     */
    public static void clearCache() {
        FIELD_DICT_CACHE.invalidateAll();
        DICT_DATA_CACHE.invalidateAll();
        MODEL_HELPER_CACHE.clear();
        log.info("Excel工具类缓存已清理");
    }

    /**
     * 获取缓存统计信息
     */
    public static String getCacheStats() {
        return String.format("字段缓存: %s, 字典缓存: %s, Helper缓存: %d",
                FIELD_DICT_CACHE.stats(),
                DICT_DATA_CACHE.stats(),
                MODEL_HELPER_CACHE.size());
    }

    @SuppressWarnings("unchecked")
    private static <T extends ApiWrapper> T cast(Object msg) {
        return msg == null ? null : (T) msg;
    }
}
