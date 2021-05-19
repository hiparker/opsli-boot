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
package org.opsli.core.utils.excel.factory;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import javassist.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.api.wrapper.system.tenant.TenantModel;
import org.opsli.core.utils.ExcelUtil;
import org.opsli.plugins.excel.annotation.ExcelInfo;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 助手类工厂
 *
 * @author Parker
 * @date 2020-09-16
 */
@Slf4j
public final class ModelFactoryHelper {

    /**
     * Model helper 字典
     */
    static private final Map<Class<?>,AbstractModelHelper> MODEL_HELPER_MAP = new HashMap<>();

    /**
     * 私有化构造函数
     */
    private ModelFactoryHelper(){}

    /**
     * 获得 Model 帮助类
     * @param modelClazz 模型 class
     * @return AbstractModelHelper
     */
    public static AbstractModelHelper getModelHelper(Class<?> modelClazz) throws Exception{
        // 这里需要全新设计,
        // 接下来就该请出 javassist 了!

        if(null == modelClazz){
            return null;
        }

        AbstractModelHelper helper = MODEL_HELPER_MAP.get(modelClazz);

        // 如果字典map中 存在 则直接返回对象 不需要二次创建 避免性能过度损耗
        if(null != helper){
            return helper;
        }

        // 使用 Javassist 动态生成 Java 字节码
        ///////////////////////////////////////////////////////////////////////
        ClassPool clazzPool = ClassPool.getDefault();
        clazzPool.insertClassPath(new ClassClassPath(StringUtils.class));
        clazzPool.insertClassPath(new ClassClassPath(ReflectUtil.class));
        clazzPool.insertClassPath(new ClassClassPath(Field.class));
        clazzPool.insertClassPath(new ClassClassPath(Iterator.class));
        clazzPool.insertClassPath(new ClassClassPath(Convert.class));
        clazzPool.insertClassPath(new ClassClassPath(JSONObject.class));
        clazzPool.insertClassPath(new ClassClassPath(JSONUtil.class));
        clazzPool.insertClassPath(new ClassClassPath(ExcelUtil.class));
        clazzPool.insertClassPath(new ClassClassPath(ExcelInfo.class));
        clazzPool.insertClassPath(new ClassClassPath(ApiWrapper.class));
        clazzPool.insertClassPath(new ClassClassPath(modelClazz));

        // 添加class 系统路径
        clazzPool.appendSystemPath();

        // 导包
        // import cn.hutool.core.convert.Convert
        clazzPool.importPackage(StringUtils.class.getName());
        clazzPool.importPackage(ReflectUtil.class.getName());
        clazzPool.importPackage(Field.class.getName());
        clazzPool.importPackage(Iterator.class.getName());
        clazzPool.importPackage(Convert.class.getName());
        clazzPool.importPackage(JSONObject.class.getName());
        clazzPool.importPackage(JSONUtil.class.getName());
        clazzPool.importPackage(ExcelUtil.class.getName());
        clazzPool.importPackage(ExcelInfo.class.getName());
        clazzPool.importPackage(ApiWrapper.class.getName());
        clazzPool.importPackage(modelClazz.getName());

        // 抽象助手类
        CtClass abstractEntityHelper = clazzPool.getCtClass(AbstractModelHelper.class.getName());
        // 助手类名称
        final String entityHelperName = modelClazz.getName()+"_Helper";
        // 构建助手类
        CtClass helperClazz = clazzPool.makeClass(entityHelperName, abstractEntityHelper);
        // 添加字段属性
        CtField f1 = CtField.make("private static final JSONObject fieldNameAndTypeCodeDict = JSONUtil.createObj();", helperClazz);
        helperClazz.addField(f1);


        // 添加构造函数
        // 创建方法体 ---------------

        CtConstructor constructor = new CtConstructor(new CtClass[0],helperClazz);
        // 空的方法体
        final String constructorStr = "{" +
                " Field[] fields = ReflectUtil.getFields(" + modelClazz.getName() + ".class);" +
                "  for (int i = 0; i < fields.length; i++) { " +
                "    Field field = fields[i];" +
                "    ExcelInfo excelInfo = field.getAnnotation(ExcelInfo.class);" +
                "    if(excelInfo != null){" +
                "      String dictType = excelInfo.dictType();" +
                "      if(StringUtils.isNotEmpty(dictType)){" +
                "          fieldNameAndTypeCodeDict.putOpt(field.getName(), dictType);" +
                "      }" +
                "    }" +
                "  }" +
                "}";
        constructor.setBody(constructorStr);
        helperClazz.addConstructor(constructor);

        Field[] fields = ReflectUtil.getFields(modelClazz);

        // 创建 导入对象方法体 ---------------

        // 解析方法
        StringBuilder createByImportSb = new StringBuilder();
        createByImportSb.append("public void transformByImport(JSONObject dictJson, ApiWrapper wrapper){").append("\n");
        createByImportSb.append(modelClazz.getName()).append(" model = (").append(modelClazz.getName()).append(") wrapper;").append("\n");
        createByImportSb.append("if(model == null){ return;}").append("\n");
        createByImportSb.append("if(dictJson == null){ return;}").append("\n");

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            ExcelInfo excelInfo = field.getAnnotation(ExcelInfo.class);
            if(excelInfo == null){
                continue;
            }

            // 字典
            String dictType = excelInfo.dictType();
            if(StringUtils.isEmpty(dictType)) {
                continue;
            }

            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), modelClazz);
            // 获得SET方法
            Method writeMethod = pd.getWriteMethod();
            // 获得GET方法
            Method readMethod = pd.getReadMethod();

            createByImportSb.append("String dictValue").append(i).append(" = \"\";").append("\n")
                    .append("if(dictJson.getJSONObject(fieldNameAndTypeCodeDict.getStr(\"").append(field.getName()).append("\")").append(") != null &&")
                    .append("dictJson.getJSONObject(fieldNameAndTypeCodeDict.getStr(\"").append(field.getName()).append("\")")
                    .append(").getJSONObject(ExcelUtil.DICT_NAME_KEY) != null ){").append("\n")
                    .append("dictValue").append(i).append(" = dictJson.getJSONObject(fieldNameAndTypeCodeDict.getStr(\"").append(field.getName()).append("\")")
                    .append(").getJSONObject(ExcelUtil.DICT_NAME_KEY).getStr(")
                    .append("model.").append(readMethod.getName()).append("());").append("\n")
                    .append("}").append("\n");

            createByImportSb.append("model.").append(writeMethod.getName()).append("(").append("dictValue").append(i).append(");").append("\n");
        }
        createByImportSb.append("}");
        CtMethod createByImportMethod = CtNewMethod.make(createByImportSb.toString(), helperClazz);
        // 添加方法到clazz中
        helperClazz.addMethod(createByImportMethod);

        // 创建 导出对象方法体 ---------------

        // 解析方法
        StringBuilder createByExportSb = new StringBuilder();
        createByExportSb.append("public void transformByExport(JSONObject dictJson, ApiWrapper wrapper){").append("\n");
        createByExportSb.append(modelClazz.getName()).append(" model = (").append(modelClazz.getName()).append(") wrapper;").append("\n");
        createByExportSb.append("if(model == null){ return;}").append("\n");
        createByExportSb.append("if(dictJson == null){ return;}").append("\n");

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            ExcelInfo excelInfo = field.getAnnotation(ExcelInfo.class);
            if(excelInfo == null){
                continue;
            }

            // 字典
            String dictType = excelInfo.dictType();
            if(StringUtils.isEmpty(dictType)) {
                continue;
            }

            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), modelClazz);
            // 获得SET方法
            Method writeMethod = pd.getWriteMethod();
            // 获得GET方法
            Method readMethod = pd.getReadMethod();

            createByExportSb.append("String dictValue").append(i).append(" = \"\";").append("\n")
                    .append("if(dictJson.getJSONObject(fieldNameAndTypeCodeDict.getStr(\"").append(field.getName()).append("\")").append(") != null &&")
                    .append("dictJson.getJSONObject(fieldNameAndTypeCodeDict.getStr(\"").append(field.getName()).append("\")")
                    .append(").getJSONObject(ExcelUtil.DICT_VALUE_KEY) != null ){").append("\n")
                    .append("dictValue").append(i).append(" = dictJson.getJSONObject(fieldNameAndTypeCodeDict.getStr(\"").append(field.getName()).append("\")")
                    .append(").getJSONObject(ExcelUtil.DICT_VALUE_KEY).getStr(")
                    .append("model.").append(readMethod.getName()).append("());").append("\n")
                    .append("}").append("\n");

            createByExportSb.append("model.").append(writeMethod.getName()).append("(").append("dictValue").append(i).append(");").append("\n");
        }
        createByExportSb.append("}");
        CtMethod createByExportMethod = CtNewMethod.make(createByExportSb.toString(), helperClazz);
        // 添加方法到clazz中
        helperClazz.addMethod(createByExportMethod);

        // 生成文件 测试查看使用
        //helperClazz.writeFile("C:/Users/zhoupengcheng/Desktop/test");

        // 获取 JAVA 类
        Class<?> javaClazz = helperClazz.toClass();

        // 创建帮助对象实例
        ///////////////////////////////////////////////////////////////////////
        helper = (AbstractModelHelper) javaClazz.newInstance();

        // 将实例对象 添加至 map 字典中
        MODEL_HELPER_MAP.put(modelClazz, helper);

        return helper;
    }


    public static void main(String[] args) throws Exception {
        ModelFactoryHelper.getModelHelper(TenantModel.class);
    }
}
