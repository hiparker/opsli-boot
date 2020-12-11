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

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.dict.DictWrapper;
import org.opsli.common.enums.ExcelOperate;
import org.opsli.plugins.excel.ExcelPlugin;
import org.opsli.plugins.excel.annotation.ExcelInfo;
import org.opsli.plugins.excel.exception.ExcelPluginException;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.utils
 * @Author: Parker
 * @CreateTime: 2020-09-23 21:53
 * @Description: ExcelUtil
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class ExcelUtil extends ExcelPlugin {

    @Override
    public <T> List<T> readExcel(MultipartFile excel, Class<T> rowModel) throws ExcelPluginException {
        List<T> ts = super.readExcel(excel, rowModel);
        // 处理数据
        return this.handleDatas(ts, rowModel, ExcelOperate.READ);
    }

    @Override
    public <T> List<T> readExcel(MultipartFile excel, Class<T> rowModel, String sheetName) throws ExcelPluginException {
        List<T> ts = super.readExcel(excel, rowModel, sheetName);
        // 处理数据
        return this.handleDatas(ts, rowModel, ExcelOperate.READ);
    }

    @Override
    public <T> List<T> readExcel(MultipartFile excel, Class<T> rowModel, String sheetName, int headLineNum) throws ExcelPluginException {
        List<T> ts = super.readExcel(excel, rowModel, sheetName, headLineNum);
        // 处理数据
        return this.handleDatas(ts, rowModel, ExcelOperate.READ);
    }

    @Override
    public <T> void writeExcel(HttpServletResponse response, List<T> list, String fileName, String sheetName, Class<T> classType, ExcelTypeEnum excelTypeEnum) throws ExcelPluginException {
        // 处理数据
        List<T> ts = this.handleDatas(list, classType, ExcelOperate.WRITE);
        super.writeExcel(response, ts, fileName, sheetName, classType, excelTypeEnum);
    }

    /**
     * 处理字典
     * @param datas
     * @param typeClazz
     * @param operate
     * @param <T>
     * @return
     */
    private <T> List<T> handleDatas(List<T> datas, Class<T> typeClazz, ExcelOperate operate){

        // 空处理
        if(datas == null || datas.size() == 0){
            return datas;
        }

        // 字段名 - 字典code
        Map<String,String> fieldAndTypeCode = Maps.newHashMap();
        // 字典code - 字典值
        Map<String,List<DictWrapper>> typeCodeAndValue = null;

        Field[] fields = ReflectUtil.getFields(typeClazz);
        for (Field field : fields) {
            ExcelInfo excelInfo = field.getAnnotation(ExcelInfo.class);
            if(excelInfo != null){
                // 字典
                String dictType = excelInfo.dictType();
                if(StringUtils.isNotEmpty(dictType)){
                    fieldAndTypeCode.put(field.getName(), dictType);
                }
            }
        }

        // 如果有字典
        if(fieldAndTypeCode.size() != 0){
            typeCodeAndValue = this.getDictMap(fieldAndTypeCode);
        }

        // 数据字典赋值
        for (T data : datas) {
            // 处理字典
            this.handleDict(data, operate, fieldAndTypeCode, typeCodeAndValue);
        }


        return datas;
    }

    // ========================= 处理字典 =========================

    /**
     * 处理字典
     * @param data 数据
     * @param operate excel操作类型
     * @param fieldAndTypeCode 字段名 - 字典code
     * @param typeCodeAndValue 字典code - 字典值
     * @param <T>
     * @return
     */
    private <T> void handleDict(T data, ExcelOperate operate, Map<String,String> fieldAndTypeCode,
                    Map<String,List<DictWrapper>> typeCodeAndValue
                ){
        // 如果没有字典 则直接退出
        if(fieldAndTypeCode.size() == 0 || typeCodeAndValue == null || typeCodeAndValue.size() == 0){
            return;
        }

        // 数据字典赋值
        for (Map.Entry<String, String> entry : fieldAndTypeCode.entrySet()) {
            try {
                String fieldName = entry.getKey();
                String typeCode = entry.getValue();
                String fieldValue = (String) ReflectUtil.getFieldValue(data, fieldName);
                List<DictWrapper> dictWrapperModels = typeCodeAndValue.get(typeCode);
                // 匹配字典
                String dictVal = this.matchingDict(dictWrapperModels, fieldValue, operate);
                if(StringUtils.isEmpty(dictVal)){
                    continue;
                }
                // 赋值
                ReflectUtil.setFieldValue(data, fieldName, dictVal);
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 获得字典缓存Map
     * @param fieldAndTypeCode
     * @return
     */
    public Map<String,List<DictWrapper>> getDictMap(Map<String,String> fieldAndTypeCode){
        // 字典code - 字典值
        Map<String,List<DictWrapper>> typeCodeAndValue = Maps.newHashMap();
        // 取Redis 值
        for (Map.Entry<String, String> entry : fieldAndTypeCode.entrySet()) {
            String typeCode = entry.getValue();
            List<DictWrapper> dictWrapperList = DictUtil.getDictList(typeCode);
            // 如果字典 List 为空 则走下一个
            if(dictWrapperList == null || dictWrapperList.size() == 0){
                continue;
            }
            typeCodeAndValue.put(typeCode, dictWrapperList);
        }
        return typeCodeAndValue;
    }

    /**
     * 匹配字典数据
     * @param dictWrapperModels
     * @param fieldValue
     * @param operate
     * @return
     */
    private String matchingDict(List<DictWrapper> dictWrapperModels, String fieldValue, ExcelOperate operate){
        String val = "";
        for (DictWrapper dictWrapperModel : dictWrapperModels) {
            // 读操作
            if(ExcelOperate.READ == operate){
                // 判断 Excel 读入 字典名称是否与 当前字典匹配
                if(dictWrapperModel.getDictName().equals(fieldValue)){
                    val = dictWrapperModel.getDictValue();
                    break;
                }
            }
            // 写操作
            else if(ExcelOperate.WRITE == operate){
                // 判断 Excel 写出 字典值是否与 当前字典匹配
                if(dictWrapperModel.getDictValue().equals(fieldValue)){
                    val = dictWrapperModel.getDictName();
                    break;
                }
            }
        }
        return val;
    }

    // ========================= 反射字段 =========================


}
