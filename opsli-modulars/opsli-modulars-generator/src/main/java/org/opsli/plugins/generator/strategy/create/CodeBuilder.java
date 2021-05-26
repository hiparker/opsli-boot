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

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.ApiFlag;
import org.opsli.common.enums.ValidatorType;
import org.opsli.common.utils.Props;
import org.opsli.common.utils.ZipUtils;
import org.opsli.plugins.generator.strategy.create.backend.JavaCodeBuilder;
import org.opsli.plugins.generator.strategy.create.foreend.VueCodeBuilder;
import org.opsli.plugins.generator.strategy.create.readme.ReadMeBuilder;
import org.opsli.plugins.generator.utils.GeneratorHandleUtil;
import org.opsli.modulars.generator.logs.wrapper.GenBuilderModel;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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

    /** 排除字段 */
    private static final List<String> EXCLUDE_FIELDS;

    public static final String API_PATH;
    public static final String FILE_NAME = "OPSLI-CodeCreate";
    public static final String BASE_PATH = "/代码生成-";
    public static final String BACKEND_PATH = "/后端";
    public static final String FOREEND_PATH = "/前端";


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

        String dataStr = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");

        // 处理表数据
        GenBuilderModel genBuilderModel = GeneratorHandleUtil.handleData(builderModel, EXCLUDE_FIELDS);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream out = this.getOutputStream(response, dataStr);
        try {
            if(out != null){
                List<Map<String, String>> fileList = new ArrayList<>();
                // 处理后端代码 ====================
                // entity
                fileList.add(JavaCodeBuilder.INSTANCE.createEntity(genBuilderModel, dataStr));
                // mapper
                fileList.add(JavaCodeBuilder.INSTANCE.createMapper(genBuilderModel, dataStr));
                // mapper xml
                fileList.add(JavaCodeBuilder.INSTANCE.createMapperXML(genBuilderModel, dataStr));
                // service
                fileList.add(JavaCodeBuilder.INSTANCE.createService(genBuilderModel, dataStr));
                // service impl
                fileList.add(JavaCodeBuilder.INSTANCE.createServiceImpl(genBuilderModel, dataStr));
                // web
                fileList.add(JavaCodeBuilder.INSTANCE.createWeb(genBuilderModel, dataStr));
                // model
                fileList.add(JavaCodeBuilder.INSTANCE.createModel(genBuilderModel, dataStr));
                // api
                fileList.add(JavaCodeBuilder.INSTANCE.createRestApi(genBuilderModel, dataStr));

                // 处理前端代码 ====================
                // index
                fileList.add(VueCodeBuilder.INSTANCE.createIndex(genBuilderModel, dataStr));
                // edit
                fileList.add(VueCodeBuilder.INSTANCE.createEdit(genBuilderModel, dataStr));
                // import
                fileList.add(VueCodeBuilder.INSTANCE.createImport(genBuilderModel, dataStr));
                // 前api
                fileList.add(VueCodeBuilder.INSTANCE.createApi(genBuilderModel, dataStr));

                // 处理 ReadMe
                fileList.add(ReadMeBuilder.INSTANCE.createReadMe(genBuilderModel, dataStr));

                // 生成zip文件
                ZipUtils.toZip(fileList, baos);

                // 输出流文件
                IoUtil.write(out,true, baos.toByteArray());
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
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
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();

    }

}
