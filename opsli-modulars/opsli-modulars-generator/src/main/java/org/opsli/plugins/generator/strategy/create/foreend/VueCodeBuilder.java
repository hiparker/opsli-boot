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
package org.opsli.plugins.generator.strategy.create.foreend;

import cn.hutool.core.date.DateUtil;
import com.jfinal.kit.Kv;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.utils.Props;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.common.utils.ZipUtils;
import org.opsli.plugins.generator.strategy.create.CodeBuilder;
import org.opsli.plugins.generator.utils.EnjoyUtil;
import org.opsli.modulars.generator.logs.wrapper.GenBuilderModel;

import java.util.*;

/**
 * Vue代码构建器
 *
 * @author parker
 * @date 2020-09-13 19:36
 */
public enum VueCodeBuilder {

    /** 实例对象 */
    INSTANCE;

    /** 验证方法前缀 */
    private static final String VALIDATE_PREFIX = "validate_";
    /** 虚拟根路径 */
    private static final String BASE_API_PATH;
    static {
        Props props = new Props("application.yaml");
        BASE_API_PATH = props.getStr("server.servlet.api.path.global-prefix","/api/v1");
    }

    /**
     * 生成 Index
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createIndex(GenBuilderModel builderModelTmp, String dataStr){
        GenBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, GenBuilderModel.class, true);

        String codeStr = EnjoyUtil.render("/foreend/index/TemplateIndex.html",
                this.createKv(builderModel)
        );

        StringBuilder path = new StringBuilder();
        path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.FOREEND_PATH)
                .append("/").append("vue")
                .append("/").append("views").append("/").append("modules")
                .append("/").append(builderModel.getModuleName()).append("/");
        if(StringUtils.isNotBlank(builderModel.getSubModuleName())){
            path = new StringBuilder();
            path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.FOREEND_PATH)
                    .append("/").append("vue")
                    .append("/").append("views").append("/").append("modules")
                    .append("/").append(builderModel.getModuleName())
                    .append("/").append(builderModel.getSubModuleName()).append("/");
        }
        Map<String,String> entityMap = new HashMap<>();
        entityMap.put(ZipUtils.FILE_PATH, path.toString());
        entityMap.put(ZipUtils.FILE_NAME, "index.vue");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }


    /**
     * 生成 Edit
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createEdit(GenBuilderModel builderModelTmp, String dataStr){
        GenBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, GenBuilderModel.class, true);

        String codeStr = EnjoyUtil.render("/foreend/components/TemplateEdit.html",
                this.createKv(builderModel));

        StringBuilder path = new StringBuilder();
        path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.FOREEND_PATH)
                .append("/").append("vue")
                .append("/").append("views").append("/").append("modules")
                .append("/").append(builderModel.getModuleName())
                .append("/").append("components").append("/");
        if(StringUtils.isNotBlank(builderModel.getSubModuleName())){
            path = new StringBuilder();
            path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.FOREEND_PATH)
                    .append("/").append("vue")
                    .append("/").append("views").append("/").append("modules")
                    .append("/").append(builderModel.getModuleName())
                    .append("/").append(builderModel.getSubModuleName())
                    .append("/").append("components").append("/");
        }
        Map<String,String> entityMap = new HashMap<>();
        entityMap.put(ZipUtils.FILE_PATH, path.toString());
        entityMap.put(ZipUtils.FILE_NAME, builderModel.getModel().getTableHumpName()+"ManagementEdit.vue");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }


    /**
     * 生成 Import
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createImport(GenBuilderModel builderModelTmp, String dataStr){
        GenBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, GenBuilderModel.class, true);

        String codeStr = EnjoyUtil.render("/foreend/components/TemplateImport.html",
                this.createKv(builderModel)
        );

        StringBuilder path = new StringBuilder();
        path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.FOREEND_PATH)
                .append("/").append("vue")
                .append("/").append("views").append("/").append("modules")
                .append("/").append(builderModel.getModuleName())
                .append("/").append("components").append("/");
        if(StringUtils.isNotBlank(builderModel.getSubModuleName())){
            path = new StringBuilder();
            path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.FOREEND_PATH)
                    .append("/").append("vue")
                    .append("/").append("views").append("/").append("modules")
                    .append("/").append(builderModel.getModuleName())
                    .append("/").append(builderModel.getSubModuleName())
                    .append("/").append("components").append("/");
        }
        Map<String,String> entityMap = new HashMap<>();
        entityMap.put(ZipUtils.FILE_PATH, path.toString());
        entityMap.put(ZipUtils.FILE_NAME, builderModel.getModel().getTableHumpName()+"ManagementImport.vue");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }

    /**
     * 生成 Api
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createApi(GenBuilderModel builderModelTmp, String dataStr){
        GenBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, GenBuilderModel.class, true);

        String codeStr = EnjoyUtil.render("/foreend/api/TemplateApi.html",
                this.createKv(builderModel)
                .set("apiPath", BASE_API_PATH)
        );

        StringBuilder path = new StringBuilder();
        path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.FOREEND_PATH)
                .append("/").append("vue").append("/").append("api")
                .append("/").append(builderModel.getModuleName()).append("/");
        if(StringUtils.isNotBlank(builderModel.getSubModuleName())){
            path = new StringBuilder();
            path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.FOREEND_PATH)
                    .append("/").append("vue").append("/").append("api")
                    .append("/").append(builderModel.getModuleName())
                    .append("/").append(builderModel.getSubModuleName()).append("/");
        }
        Map<String,String> entityMap = new HashMap<>();
        entityMap.put(ZipUtils.FILE_PATH, path.toString());
        entityMap.put(ZipUtils.FILE_NAME, builderModel.getModel().getTableHumpName()+"Management.js");
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
                .set("currTime", DateUtil.now());
    }


}
