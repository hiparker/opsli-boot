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
package org.opsli.plugins.generator.strategy.create;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.Kv;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.ApiFlag;
import org.opsli.common.enums.DictType;
import org.opsli.common.utils.MessUtil;
import org.opsli.common.utils.Props;
import org.opsli.common.utils.ZipUtils;
import org.opsli.modulars.generator.logs.wrapper.GenBuilderModel;
import org.opsli.modulars.generator.template.wrapper.GenTemplateDetailModel;
import org.opsli.plugins.generator.enums.CodeType;
import org.opsli.plugins.generator.factory.GeneratorFactory;
import org.opsli.plugins.generator.utils.EnjoyUtil;
import org.opsli.plugins.generator.utils.GenTemplateUtil;
import org.opsli.plugins.generator.utils.GeneratorHandleUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Java代码构建器
 *
 * @author parker
 * @date 2020-09-13 19:36
 */
@Slf4j
public enum CodeBuilder {

    /** 实例对象 */
    INSTANCE;

    /** Java文件后缀 */
    public static final String JAVA_SUFFIX = "java";
    /** 文件名前缀 */
    public static final String FILE_NAME = "OPSLI-CodeCreate";
    /** 文件夹前缀 */
    private static final String FOLDER_PREFIX = "/";
    /** 文件夹点前缀 */
    private static final String POINT_PREFIX = ".";
    /** 基础路径前缀 */
    public static final String BASE_PATH = "/代码生成-";

    /** 排除字段 */
    private static final List<String> EXCLUDE_FIELDS;
    /** API 地址 */
    public static final String API_PATH;
    static {
        Props props = new Props("generator.yaml");
        EXCLUDE_FIELDS = props.getList("opsli.exclude-fields");
        API_PATH = ApiFlag.class.getPackage().getName();
    }

    /**
     * 构建
     */
    public void build(GenBuilderModel builderModel, HttpServletResponse response){
        if(builderModel == null){
            return;
        }

        String dateStr = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");

        // 处理表数据
        GenBuilderModel genBuilderModel = GeneratorHandleUtil.handleData(builderModel, EXCLUDE_FIELDS);
        if(genBuilderModel == null){
            return;
        }


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStream out = this.getOutputStream(response, dateStr);
        try {
            if(out != null){
                List<GenTemplateDetailModel> templateDetailList =
                        GenTemplateUtil.getTemplateDetailList(genBuilderModel.getTemplateId());

                List<Map<String, String>> fileList =
                        Lists.newArrayListWithCapacity(templateDetailList.size());

                // 循环处理代码模板
                for (GenTemplateDetailModel templateDetailModel : templateDetailList) {
                    fileList.add(
                            this.createCode(genBuilderModel, templateDetailModel, dateStr)
                    );
                }

                // 生成zip文件
                ZipUtils.toZip(fileList, byteArrayOutputStream);

                // 输出流文件
                IoUtil.write(out,true, byteArrayOutputStream.toByteArray());
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 生成MapperXML
     * @param builderModel Build 模型
     * @param templateModel 模板模型
     * @param dataStr 数据字符串
     * @return Map
     */
    private Map<String,String> createCode(final GenBuilderModel builderModel,
                                          final GenTemplateDetailModel templateModel, final String dataStr){
        if(builderModel == null){
            return MapUtil.empty();
        }


        // 获得分类名称
        CodeType codeType = CodeType.getCodeType(templateModel.getType());
        String typeName = codeType.getDesc();

        // 基础路径
        String basePath = CodeBuilder.BASE_PATH + dataStr + FOLDER_PREFIX + typeName;
        // Path路径
        String path = templateModel.getPath();
        if(StrUtil.isNotEmpty(path)){
            // 替换占位符
            path = handleReplace(path, builderModel);
            // 处理路径 前后加 [/]
            path = this.handlePath(path);
        }

        // 代码
        String codeStr = EnjoyUtil.renderByStr(templateModel.getFileContent(),
                this.createKv(builderModel)
        );

        // 模板文件后缀
        String templateFileSuffix = FileUtil.getSuffix(templateModel.getFileName());

        // 判断是否是Java 文件
        if(JAVA_SUFFIX.equals(templateFileSuffix)){
            codeStr = GeneratorFactory.getJavaHeadAnnotation() + codeStr;
        }

        // 生成文件名
        String fileName = builderModel.getModel().getTableHumpName();

        // 判断是否忽略文件名 （忽略默认采用 表名+后缀名）
        if(DictType.NO_YES_YES.getValue().equals(templateModel.getIgnoreFileName())){
            fileName += StrUtil.prependIfMissing(templateFileSuffix, POINT_PREFIX);
        }else {
            fileName = handleReplace(templateModel.getFileName(), builderModel);
        }

        Map<String,String> entityMap = Maps.newHashMapWithExpectedSize(3);
        entityMap.put(ZipUtils.FILE_PATH, basePath + path);
        entityMap.put(ZipUtils.FILE_NAME, fileName);
        entityMap.put(ZipUtils.FILE_DATA, codeStr);

        return entityMap;
    }

    /**
     * 创建 Kv
     * @param builderModel Build 模型
     * @return Kv
     */
    private Kv createKv(GenBuilderModel builderModel){
        return Kv.by("data", builderModel)
                .set("currTime", DateUtil.now())
                .set("apiPath", API_PATH);
    }

    /**
     * 处理 Path 路径
     * @param path 路径
     * @return String
     */
    private String handlePath(String path) {
        if(StrUtil.isEmpty(path)){
            return path;
        }

        // . 转换为 [/]
        path = StrUtil.replace(path, POINT_PREFIX, FOLDER_PREFIX);

        // 如果 第一位不是 / 则加 /
        path = StrUtil.prependIfMissing(path, FOLDER_PREFIX);

        // 如果最后一位 是 / 则减 /
        path = StrUtil.appendIfMissing(path, FOLDER_PREFIX);

        // 去除 [//]
        return StrUtil.replace(path, "//", FOLDER_PREFIX);
    }

    /**
     * 处理替换占位符
     * @param str 原字符串
     * @param builderModel 模型数据
     * @return String
     */
    private String handleReplace(String str, GenBuilderModel builderModel){
        // 非法处理
        if(StrUtil.isEmpty(str) || builderModel == null){
            return str;
        }

        String prefix = "${";
        String suffix = "}";
        List<String> placeholderList = MessUtil.getPlaceholderList(str);
        for (String placeholderField : placeholderList) {
            Object property = BeanUtil.getProperty(builderModel, placeholderField);
            str = StrUtil.replace(str,
                    prefix + placeholderField + suffix,
                    Convert.toStr(property));
        }
        return str;
    }

    /**
     * 导出文件时为Writer生成OutputStream
     */
    private OutputStream getOutputStream(HttpServletResponse response, String dataStr){
        //创建本地文件
        try {
            String fileName = FILE_NAME +"-"+ dataStr+".zip";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setHeader("Cache-Control", "no-store, no-cache");
            return response.getOutputStream();
        } catch (IOException ignored) {}
        return null;
    }

    public static void main(String[] args) {

        String aaa = "aaaaaaaaab${bb}bbbbbbbb${bbaa}bbbbbbbccccccccccccc";
        List<String> placeholderList = MessUtil.getPlaceholderList(aaa);
        for (String s : placeholderList) {
            System.out.println(s);
        }
    }

}
