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
package org.opsli.plugins.generator.strategy.create.backend;

import cn.hutool.core.date.DateUtil;
import com.jfinal.kit.Kv;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.common.utils.ZipUtils;
import org.opsli.plugins.generator.strategy.create.CodeBuilder;
import org.opsli.plugins.generator.utils.EnjoyUtil;
import org.opsli.modulars.generator.logs.wrapper.GenBuilderModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Java代码构建器
 *
 * @author parker
 * @date 2020-09-13 19:36
 */
public enum JavaCodeBuilder {

    /** 实例对象 */
    INSTANCE;

    /**
     * 生成Entity
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createEntity(GenBuilderModel builderModelTmp, String dataStr){
        GenBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, GenBuilderModel.class, true);

        String codeStr = EnjoyUtil.render("/backend/entity/TemplateEntity.html",
                this.createKv(builderModel)
        );

        StringBuilder path = new StringBuilder();
        path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                .append("/").append(builderModel.getPackageName().replaceAll("\\.","/"))
                .append("/").append(builderModel.getModuleName())
                .append("/").append("entity").append("/");
        if(StringUtils.isNotBlank(builderModel.getSubModuleName())){
            path = new StringBuilder();
            path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                    .append("/").append(builderModel.getPackageName().replaceAll("\\.","/"))
                    .append("/").append(builderModel.getModuleName())
                    .append("/").append(builderModel.getSubModuleName())
                    .append("/").append("entity").append("/");
        }
        Map<String,String> entityMap = new HashMap<>();
        entityMap.put(ZipUtils.FILE_PATH, path.toString());
        entityMap.put(ZipUtils.FILE_NAME,
                builderModel.getModel().getTableHumpName()+".java");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }

    /**
     * 生成Mapper
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createMapper(GenBuilderModel builderModelTmp, String dataStr){
        GenBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, GenBuilderModel.class, true);
        String codeStr = EnjoyUtil.render("/backend/mapper/TemplateMapper.html",
                this.createKv(builderModel)
        );

        StringBuilder path = new StringBuilder();
        path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                .append("/").append(builderModel.getPackageName().replaceAll("\\.","/"))
                .append("/").append(builderModel.getModuleName())
                .append("/").append("mapper").append("/");
        if(StringUtils.isNotBlank(builderModel.getSubModuleName())){
            path = new StringBuilder();
            path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                    .append("/").append(builderModel.getPackageName().replaceAll("\\.","/"))
                    .append("/").append(builderModel.getModuleName())
                    .append("/").append(builderModel.getSubModuleName())
                    .append("/").append("mapper").append("/");
        }
        Map<String,String> entityMap = new HashMap<>();
        entityMap.put(ZipUtils.FILE_PATH, path.toString());
        entityMap.put(ZipUtils.FILE_NAME,
                builderModel.getModel().getTableHumpName()+"Mapper.java");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }

    /**
     * 生成MapperXML
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createMapperXML(GenBuilderModel builderModelTmp, String dataStr){
        GenBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, GenBuilderModel.class, true);
        String codeStr = EnjoyUtil.render("/backend/mapper/xml/TemplateMapperXML.html",
                this.createKv(builderModel)
        );

        StringBuilder path = new StringBuilder();
        path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                .append("/").append(builderModel.getPackageName().replaceAll("\\.","/"))
                .append("/").append(builderModel.getModuleName())
                .append("/").append("mapper")
                .append("/").append("xml").append("/");
        if(StringUtils.isNotBlank(builderModel.getSubModuleName())){
            path = new StringBuilder();
            path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                    .append("/").append(builderModel.getPackageName().replaceAll("\\.","/"))
                    .append("/").append(builderModel.getModuleName())
                    .append("/").append(builderModel.getSubModuleName())
                    .append("/").append("mapper")
                    .append("/").append("xml").append("/");
        }
        Map<String,String> entityMap = new HashMap<>();
        entityMap.put(ZipUtils.FILE_PATH, path.toString());
        entityMap.put(ZipUtils.FILE_NAME,
                builderModel.getModel().getTableHumpName()+"Mapper.xml");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }

    /**
     * 生成Service
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createService(GenBuilderModel builderModelTmp, String dataStr){
        GenBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, GenBuilderModel.class, true);
        String codeStr = EnjoyUtil.render("/backend/service/TemplateService.html",
                this.createKv(builderModel)
        );

        StringBuilder path = new StringBuilder();
        path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                .append("/").append(builderModel.getPackageName().replaceAll("\\.","/"))
                .append("/").append(builderModel.getModuleName())
                .append("/").append("service").append("/");
        if(StringUtils.isNotBlank(builderModel.getSubModuleName())){
            path = new StringBuilder();
            path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                    .append("/").append(builderModel.getPackageName().replaceAll("\\.","/"))
                    .append("/").append(builderModel.getModuleName())
                    .append("/").append(builderModel.getSubModuleName())
                    .append("/").append("service").append("/");
        }
        Map<String,String> entityMap = new HashMap<>();
        entityMap.put(ZipUtils.FILE_PATH, path.toString());
        entityMap.put(ZipUtils.FILE_NAME,
                "I"+builderModel.getModel().getTableHumpName()+"Service.java");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }


    /**
     * 生成 Service Impl
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createServiceImpl(GenBuilderModel builderModelTmp, String dataStr){
        GenBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, GenBuilderModel.class, true);
        String codeStr = EnjoyUtil.render("/backend/service/impl/TemplateServiceImpl.html",
                this.createKv(builderModel)
        );

        StringBuilder path = new StringBuilder();
        path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                .append("/").append(builderModel.getPackageName().replaceAll("\\.","/"))
                .append("/").append(builderModel.getModuleName())
                .append("/").append("service")
                .append("/").append("impl").append("/");
        if(StringUtils.isNotBlank(builderModel.getSubModuleName())){
            path = new StringBuilder();
            path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                    .append("/").append(builderModel.getPackageName().replaceAll("\\.","/"))
                    .append("/").append(builderModel.getModuleName())
                    .append("/").append(builderModel.getSubModuleName())
                    .append("/").append("service")
                    .append("/").append("impl").append("/");
        }
        Map<String,String> entityMap = new HashMap<>();
        entityMap.put(ZipUtils.FILE_PATH, path.toString());
        entityMap.put(ZipUtils.FILE_NAME,
                builderModel.getModel().getTableHumpName()+"ServiceImpl.java");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }

    /**
     * 生成 Web
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createWeb(GenBuilderModel builderModelTmp, String dataStr){
        GenBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, GenBuilderModel.class, true);
        String codeStr = EnjoyUtil.render("/backend/web/TemplateRestController.html",
                this.createKv(builderModel)
        );

        StringBuilder path = new StringBuilder();
        path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                .append("/").append(builderModel.getPackageName().replaceAll("\\.","/"))
                .append("/").append(builderModel.getModuleName())
                .append("/").append("web").append("/");
        if(StringUtils.isNotBlank(builderModel.getSubModuleName())){
            path = new StringBuilder();
            path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                    .append("/").append(builderModel.getPackageName().replaceAll("\\.","/"))
                    .append("/").append(builderModel.getModuleName())
                    .append("/").append(builderModel.getSubModuleName())
                    .append("/").append("web").append("/");
        }
        Map<String,String> entityMap = new HashMap<>();
        entityMap.put(ZipUtils.FILE_PATH, path.toString());
        entityMap.put(ZipUtils.FILE_NAME,
                builderModel.getModel().getTableHumpName()+"RestController.java");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }


    /**
     * 生成Model
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createModel(GenBuilderModel builderModelTmp, String dataStr){
        GenBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, GenBuilderModel.class, true);

        String codeStr = EnjoyUtil.render("/backend/model/TemplateModel.html",
                this.createKv(builderModel)
        );

        StringBuilder path = new StringBuilder();
        path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                .append("/").append(CodeBuilder.API_PATH.replaceAll("\\.","/"))
                .append("/").append("wrapper")
                .append("/").append(builderModel.getModuleName()).append("/");
        if(StringUtils.isNotBlank(builderModel.getSubModuleName())){
            path = new StringBuilder();
            path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                    .append("/").append(CodeBuilder.API_PATH.replaceAll("\\.","/"))
                    .append("/").append("wrapper")
                    .append("/").append(builderModel.getModuleName())
                    .append("/").append(builderModel.getSubModuleName()).append("/");
        }
        Map<String,String> entityMap = new HashMap<>();
        entityMap.put(ZipUtils.FILE_PATH, path.toString());
        entityMap.put(ZipUtils.FILE_NAME,
                builderModel.getModel().getTableHumpName()+"Model.java");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }


    /**
     * 生成RestApi
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createRestApi(GenBuilderModel builderModelTmp, String dataStr){
        GenBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, GenBuilderModel.class, true);
        String codeStr = EnjoyUtil.render("/backend/api/TemplateRestApi.html",
                this.createKv(builderModel)
        );

        StringBuilder path = new StringBuilder();
        path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                .append("/").append(CodeBuilder.API_PATH.replaceAll("\\.","/"))
                .append("/").append("web")
                .append("/").append(builderModel.getModuleName()).append("/");
        if(StringUtils.isNotBlank(builderModel.getSubModuleName())){
            path = new StringBuilder();
            path.append(CodeBuilder.BASE_PATH).append(dataStr).append(CodeBuilder.BACKEND_PATH)
                    .append("/").append(CodeBuilder.API_PATH.replaceAll("\\.","/"))
                    .append("/").append("web")
                    .append("/").append(builderModel.getModuleName())
                    .append("/").append(builderModel.getSubModuleName()).append("/");
        }
        Map<String,String> entityMap = new HashMap<>();
        entityMap.put(ZipUtils.FILE_PATH, path.toString());
        entityMap.put(ZipUtils.FILE_NAME,
                builderModel.getModel().getTableHumpName()+"RestApi.java");
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
                .set("apiPath", CodeBuilder.API_PATH);
    }


}
