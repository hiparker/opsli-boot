package org.opsli.core.utils;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.dict.DictModel;
import org.opsli.common.annotation.DictType;
import org.opsli.common.enums.ExcelOperate;
import org.opsli.plugins.excel.ExcelPlugin;
import org.opsli.plugins.excel.exception.ExcelPluginException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.utils
 * @Author: Parker
 * @CreateTime: 2020-09-23 21:53
 * @Description: ExcelUtil
 */
@Slf4j
@Component
public class ExcelUtil extends ExcelPlugin {

    @Override
    public <T> List<T> readExcel(MultipartFile excel, Class<T> rowModel) throws ExcelPluginException {
        List<T> ts = super.readExcel(excel, rowModel);
        // 处理数据
        return this.handleDict(ts, rowModel, ExcelOperate.READ);
    }

    @Override
    public <T> List<T> readExcel(MultipartFile excel, Class<T> rowModel, String sheetName) throws ExcelPluginException {
        List<T> ts = super.readExcel(excel, rowModel, sheetName);
        // 处理数据
        return this.handleDict(ts, rowModel, ExcelOperate.READ);
    }

    @Override
    public <T> List<T> readExcel(MultipartFile excel, Class<T> rowModel, String sheetName, int headLineNum) throws ExcelPluginException {
        List<T> ts = super.readExcel(excel, rowModel, sheetName, headLineNum);
        // 处理数据
        return this.handleDict(ts, rowModel, ExcelOperate.READ);
    }

    @Override
    public <T> void writeExcel(HttpServletResponse response, List<T> list, String fileName, String sheetName, Class<T> classType, ExcelTypeEnum excelTypeEnum) throws ExcelPluginException {
        // 处理数据
        List<T> ts = this.handleDict(list, classType, ExcelOperate.WRITE);
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
    private <T> List<T> handleDict(List<T> datas, Class<T> typeClazz, ExcelOperate operate){

        // 空处理
        if(datas == null || datas.size() == 0){
            return datas;
        }

        // 字段名 - 字典code
        Map<String,String> fieldAndTypeCode = Maps.newHashMap();
        // 字典code - 字典值
        Map<String,List<DictModel>> typeCodeAndValue = Maps.newHashMap();

        Field[] fields = ReflectUtil.getFields(typeClazz);
        for (Field field : fields) {
            DictType dictType = field.getAnnotation(DictType.class);
            if(dictType != null){
                String typeCode = dictType.value();
                fieldAndTypeCode.put(field.getName(), typeCode);
            }
        }

        // 如果没有字典 则直接返回
        if(fieldAndTypeCode.size() == 0){
            return datas;
        }

        // 取Redis 值
        for (Map.Entry<String, String> entry : fieldAndTypeCode.entrySet()) {
            String typeCode = entry.getValue();
            List<DictModel> dictList = DictUtil.getDictList(typeCode);
            // 如果字典 List 为空 则走下一个
            if(dictList == null || dictList.size() == 0) continue;
            typeCodeAndValue.put(typeCode, dictList);
        }

        // 数据字典赋值
        for (T data : datas) {
            for (Map.Entry<String, String> entry : fieldAndTypeCode.entrySet()) {
                try {
                    String fieldName = entry.getKey();
                    String typeCode = entry.getValue();
                    String fieldValue = (String) ReflectUtil.getFieldValue(data, fieldName);
                    List<DictModel> dictModels = typeCodeAndValue.get(typeCode);
                    // 匹配字典
                    String dictVal = this.matchingDict(dictModels, fieldValue, operate);
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
        return datas;
    }

    /**
     * 匹配字典数据
     * @param dictModels
     * @param fieldValue
     * @param operate
     * @return
     */
    private String matchingDict(List<DictModel> dictModels, String fieldValue, ExcelOperate operate){
        String val = "";
        for (DictModel dictModel : dictModels) {
            // 读操作
            if(ExcelOperate.READ == operate){
                // 判断 Excel 读入 字典名称是否与 当前字典匹配
                if(dictModel.getDictName().equals(fieldValue)){
                    val = dictModel.getDictValue();
                    break;
                }
            }
            // 写操作
            else if(ExcelOperate.WRITE == operate){
                // 判断 Excel 写出 字典值是否与 当前字典匹配
                if(dictModel.getDictValue().equals(fieldValue)){
                    val = dictModel.getDictName();
                    break;
                }
            }
        }
        return val;
    }
}
