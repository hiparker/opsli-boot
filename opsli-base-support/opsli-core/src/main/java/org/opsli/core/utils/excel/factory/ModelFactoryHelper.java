package org.opsli.core.utils.excel.factory;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import javassist.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.core.utils.ExcelUtil;
import org.opsli.plugins.excel.annotation.ExcelInfo;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 助手类工厂 优化在jdk9+ JPMS  中使用
 * 主要用于在excel导出时 大规模匹配字典类
 *
 * @author Pace
 * @date 2025-06-01
 */
@Slf4j
public final class ModelFactoryHelper {

    /**
     * Model helper 字典 - 使用ConcurrentHashMap提高并发安全性
     */
    static private final Map<Class<?>, AbstractModelHelper> MODEL_HELPER_MAP = new ConcurrentHashMap<>();

    /**
     * 私有化构造函数
     */
    private ModelFactoryHelper() {}

    /**
     * 获得 Model 帮助类
     * @param modelClazz 模型 class
     * @return AbstractModelHelper
     */
    public static AbstractModelHelper getModelHelper(Class<?> modelClazz) throws Exception {
        if (null == modelClazz) {
            return null;
        }

        // 检查缓存
        return MODEL_HELPER_MAP.computeIfAbsent(modelClazz, clazz -> {
            try {
                return generateModelHelper(clazz);
            } catch (Exception e) {
                log.error("Failed to generate model helper for class: " + clazz.getName(), e);
                throw new RuntimeException("Failed to generate model helper: " + e.getMessage(), e);
            }
        });
    }

    /**
     * 生成模型助手类
     * @param modelClazz 模型类
     * @return 助手类实例
     */
    private static AbstractModelHelper generateModelHelper(Class<?> modelClazz) throws Exception {
        ClassPool classPool = ClassPool.getDefault();

        // 添加必要的类路径
        classPool.appendClassPath(new ClassClassPath(modelClazz));
        classPool.appendClassPath(new ClassClassPath(AbstractModelHelper.class));
        classPool.appendSystemPath();

        // 导入必要的包
        String[] importPackages = {
                StringUtils.class.getName(),
                ReflectUtil.class.getName(),
                Field.class.getName(),
                JSONObject.class.getName(),
                JSONUtil.class.getName(),
                ExcelUtil.class.getName(),
                ExcelInfo.class.getName(),
                ApiWrapper.class.getName(),
                modelClazz.getName()
        };

        for (String packageName : importPackages) {
            classPool.importPackage(packageName);
        }

        // 助手类名称
        final String entityHelperName = modelClazz.getName() + "_Helper";

        // 获取抽象助手类
        CtClass abstractEntityHelper = classPool.getCtClass(AbstractModelHelper.class.getName());

        // 检查是否已存在这个类，避免重复创建
        try {
            CtClass existingClass = classPool.getCtClass(entityHelperName);
            if (existingClass != null) {
                existingClass.detach(); // 分离已存在的类，允许重新定义
            }
        } catch (NotFoundException ignored) {
            // 类不存在，可以继续创建
        }

        // 构建助手类
        CtClass helperClazz = classPool.makeClass(entityHelperName, abstractEntityHelper);

        // 添加字段属性
        CtField f1 = CtField.make("private static final JSONObject fieldNameAndTypeCodeDict = JSONUtil.createObj();", helperClazz);
        helperClazz.addField(f1);

        // 添加构造函数 - 修复语法，使用传统for循环而不是foreach
        CtConstructor constructor = new CtConstructor(new CtClass[0], helperClazz);
        // 构造函数方法体
        final String constructorStr = "{"
                + " Field[] fields = ReflectUtil.getFields(" + modelClazz.getName() + ".class);"
                + "  for (int i = 0; i < fields.length; i++) { "  // 修复这里，使用传统for循环
                + "    Field field = fields[i];"
                + "    ExcelInfo excelInfo = field.getAnnotation(ExcelInfo.class);"
                + "    if(excelInfo != null){"
                + "      String dictType = excelInfo.dictType();"
                + "      if(StringUtils.isNotEmpty(dictType)){"
                + "          fieldNameAndTypeCodeDict.putOpt(field.getName(), dictType);"
                + "      }"
                + "    }"
                + "  }"
                + "}";
        constructor.setBody(constructorStr);
        helperClazz.addConstructor(constructor);

        Field[] fields = ReflectUtil.getFields(modelClazz);

        // 创建 导入对象方法体
        StringBuilder createByImportSb = new StringBuilder();
        createByImportSb.append("public void transformByImport(JSONObject dictJson, ApiWrapper wrapper){").append("\n");
        createByImportSb.append("  ").append(modelClazz.getName()).append(" model = (").append(modelClazz.getName()).append(") wrapper;").append("\n");
        createByImportSb.append("  if(model == null || dictJson == null){ return;}").append("\n");

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            ExcelInfo excelInfo = field.getAnnotation(ExcelInfo.class);
            if (excelInfo == null) {
                continue;
            }

            // 字典
            String dictType = excelInfo.dictType();
            if (StringUtils.isEmpty(dictType)) {
                continue;
            }

            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), modelClazz);
            Method writeMethod = pd.getWriteMethod();
            Method readMethod = pd.getReadMethod();

            createByImportSb.append("  String dictValue").append(i).append(" = \"\";").append("\n")
                    .append("  if(dictJson.getJSONObject(fieldNameAndTypeCodeDict.getStr(\"").append(field.getName()).append("\")) != null &&")
                    .append(" dictJson.getJSONObject(fieldNameAndTypeCodeDict.getStr(\"").append(field.getName()).append("\")")
                    .append(").getJSONObject(ExcelUtil.DICT_NAME_KEY) != null ){").append("\n")
                    .append("    dictValue").append(i).append(" = dictJson.getJSONObject(fieldNameAndTypeCodeDict.getStr(\"").append(field.getName()).append("\")")
                    .append(").getJSONObject(ExcelUtil.DICT_NAME_KEY).getStr(")
                    .append("model.").append(readMethod.getName()).append("());").append("\n")
                    .append("  }").append("\n");

            createByImportSb.append("  model.").append(writeMethod.getName()).append("(dictValue").append(i).append(");").append("\n");
        }
        createByImportSb.append("}");

        CtMethod createByImportMethod = CtNewMethod.make(createByImportSb.toString(), helperClazz);
        helperClazz.addMethod(createByImportMethod);

        // 创建 导出对象方法体
        StringBuilder createByExportSb = new StringBuilder();
        createByExportSb.append("public void transformByExport(JSONObject dictJson, ApiWrapper wrapper){").append("\n");
        createByExportSb.append("  ").append(modelClazz.getName()).append(" model = (").append(modelClazz.getName()).append(") wrapper;").append("\n");
        createByExportSb.append("  if(model == null || dictJson == null){ return;}").append("\n");

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            ExcelInfo excelInfo = field.getAnnotation(ExcelInfo.class);
            if (excelInfo == null) {
                continue;
            }

            // 字典
            String dictType = excelInfo.dictType();
            if (StringUtils.isEmpty(dictType)) {
                continue;
            }

            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), modelClazz);
            Method writeMethod = pd.getWriteMethod();
            Method readMethod = pd.getReadMethod();

            createByExportSb.append("  String dictValue").append(i).append(" = \"\";").append("\n")
                    .append("  if(dictJson.getJSONObject(fieldNameAndTypeCodeDict.getStr(\"").append(field.getName()).append("\")) != null &&")
                    .append(" dictJson.getJSONObject(fieldNameAndTypeCodeDict.getStr(\"").append(field.getName()).append("\")")
                    .append(").getJSONObject(ExcelUtil.DICT_VALUE_KEY) != null ){").append("\n")
                    .append("    dictValue").append(i).append(" = dictJson.getJSONObject(fieldNameAndTypeCodeDict.getStr(\"").append(field.getName()).append("\")")
                    .append(").getJSONObject(ExcelUtil.DICT_VALUE_KEY).getStr(")
                    .append("model.").append(readMethod.getName()).append("());").append("\n")
                    .append("  }").append("\n");

            createByExportSb.append("  model.").append(writeMethod.getName()).append("(dictValue").append(i).append(");").append("\n");
        }
        createByExportSb.append("}");

        CtMethod createByExportMethod = CtNewMethod.make(createByExportSb.toString(), helperClazz);
        helperClazz.addMethod(createByExportMethod);

        // 使用自定义类加载器而不是直接调用toClass()
        byte[] byteCode = helperClazz.toBytecode();


        // 生成文件 测试查看使用
        //helperClazz.writeFile("/Users/system/Desktop/saas 部署/测试");

        // 释放CtClass资源
        helperClazz.detach();

        // 使用自定义类加载器加载类
        ModelHelperClassLoader modelHelperClassLoader = new ModelHelperClassLoader(modelClazz.getClassLoader());
        Class<?> javaClazz = modelHelperClassLoader.defineClass(entityHelperName, byteCode);

        // 创建帮助对象实例
        return (AbstractModelHelper) javaClazz.getDeclaredConstructor().newInstance();
    }

    /**
     * 自定义类加载器 - 用于绕过Java 9+模块系统的限制
     */
    private static class ModelHelperClassLoader extends ClassLoader {
        public ModelHelperClassLoader(ClassLoader parent) {
            super(parent);
        }

        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }

    public static void main(String[] args) {
        try {
            // 使用类全名
            Class<?> tenantModelClass = Class.forName("org.opsli.api.wrapper.system.tenant.TenantModel");
            AbstractModelHelper helper = ModelFactoryHelper.getModelHelper(tenantModelClass);
            System.out.println("成功创建助手类: " + helper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
