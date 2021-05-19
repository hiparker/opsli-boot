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
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Maps;
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

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Excel 工具类
 *
 * @author parker
 * @date 2020-09-22 11:17
 */
@Slf4j
public final class ExcelUtil {

    /** 字典KEY */
    public static final String DICT_NAME_KEY = "dictName";
    public static final String DICT_VALUE_KEY = "dictValue";
    /** 字段字典Map */
    private static final Map<Class<?>, JSONObject> FIELD_DICT_MAP = Maps.newHashMap();

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
        // 处理数据
        return this.handleDatas(ts, rowModel, ExcelOperate.READ);
    }

    public <T> List<T> radExcel(MultipartFile excel, Class<T> rowModel, String sheetName) throws ExcelPluginException {
        List<T> ts = ExcelUtilSingletonHolder.EXCEL_PLUGIN.readExcel(excel, rowModel, sheetName);
        // 处理数据
        return this.handleDatas(ts, rowModel, ExcelOperate.READ);
    }

    public <T> List<T> readExcel(MultipartFile excel, Class<T> rowModel, String sheetName, int headLineNum) throws ExcelPluginException {
        List<T> ts = ExcelUtilSingletonHolder.EXCEL_PLUGIN.readExcel(excel, rowModel, sheetName, headLineNum);
        // 处理数据
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

    public <T> void writeExcel(HttpServletResponse response, List<T> list, String fileName, String sheetName, Class<T> classType, ExcelTypeEnum excelTypeEnum) throws ExcelPluginException {
        // 处理数据
        List<T> ts = this.handleDatas(list, classType, ExcelOperate.WRITE);
        ExcelUtilSingletonHolder.EXCEL_PLUGIN.writeExcel(response, ts, fileName, sheetName, classType, excelTypeEnum);
    }

    /**
     * 处理字典
     * @param datas 数据
     * @param typeClazz 数据CLazz
     * @param operate 操作方式
     * @param <T> 泛型
     * @return List<T>
     */
    public <T> List<T> handleDatas(List<T> datas, Class<T> typeClazz, ExcelOperate operate){
        // 计时器
        TimeInterval timer = DateUtil.timer();
        // 空处理
        if(datas == null || datas.size() == 0){
            return datas;
        }

        try {
            JSONObject fieldsJson = this.getFields(typeClazz);
            JSONObject fieldsDictJson = this.getFieldsDict(fieldsJson);

            // 获得 helper类
            AbstractModelHelper modelHelper = ModelFactoryHelper.getModelHelper(typeClazz);
            // 字典赋值
            for (T data : datas) {
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
            }

        }catch (Exception e){
            log.error(e.getMessage(), e);
            return datas;
        }finally {
            // 花费毫秒数
            long timerCount = timer.interval();
            log.info("Excel 处理数据耗时："+ DateUtil.formatBetween(timerCount));
        }

        return datas;
    }

    /**
     * 获得字段字典Code
     *
     * @param clazz 类clazz
     * @return JSONObject
     */
    public JSONObject getFields(Class<?> clazz){
        // 加入内部缓存 防止每次导出都重复反射对象
        JSONObject fieldNameAndTypeCodeDict = FIELD_DICT_MAP.get(clazz);
        if(!JSONUtil.isNull(fieldNameAndTypeCodeDict)){
            return fieldNameAndTypeCodeDict;
        }

        fieldNameAndTypeCodeDict = JSONUtil.createObj();
        Field[] fields = ReflectUtil.getFields(clazz);
        for (Field field : fields) {
            ExcelInfo excelInfo = field.getAnnotation(ExcelInfo.class);
            if (excelInfo != null) {
                // 字典
                String dictType = excelInfo.dictType();
                if (StringUtils.isNotEmpty(dictType)) {
                    fieldNameAndTypeCodeDict.putOpt(field.getName(), dictType);
                }
            }
        }
        FIELD_DICT_MAP.put(clazz, fieldNameAndTypeCodeDict);
        return fieldNameAndTypeCodeDict;
    }


    /**
     * 获得字段字典
     *
     * @param fieldNameAndTypeCodeDict 字段
     * @return JSONObject
     */
    public JSONObject getFieldsDict(final JSONObject fieldNameAndTypeCodeDict){
        // 非空判断
        if(JSONUtil.isNull(fieldNameAndTypeCodeDict)){
            return null;
        }

        JSONObject dictJson = JSONUtil.createObj();

        try {
            // 取字典 值
            for (String s : fieldNameAndTypeCodeDict.keySet()) {
                String key = Convert.toStr(s);
                String typeCode = fieldNameAndTypeCodeDict.getStr(key);
                List<DictWrapper> dictWrapperList = DictUtil.getDictList(typeCode);
                // 如果字典 List 为空 则走下一个
                if (CollUtil.isEmpty(dictWrapperList)) {
                    continue;
                }

                JSONObject dictObject = JSONUtil.createObj();
                JSONObject nameJsonObject = JSONUtil.createObj();
                JSONObject valueJsonObject = JSONUtil.createObj();
                for (DictWrapper wrapper : dictWrapperList) {
                    JSONObject dictWrapper = JSONUtil.parseObj(wrapper);
                    if (!JSONUtil.isNull(dictWrapper)) {
                        nameJsonObject.putOpt(wrapper.getDictName(), wrapper.getDictValue());
                        valueJsonObject.putOpt(wrapper.getDictValue(), wrapper.getDictName());
                    }
                }

                dictObject.putOpt(DICT_NAME_KEY, nameJsonObject);
                dictObject.putOpt(DICT_VALUE_KEY, valueJsonObject);

                dictJson.putOpt(typeCode, dictObject);
            }
        }catch (Exception ignored){}
        return dictJson;
    }


    private static <T extends ApiWrapper> T cast(Object msg){
        if(null == msg){
            return null;
        }
        return (T) msg;
    }

}
