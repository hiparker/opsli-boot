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
package org.opsli.core.creater.strategy.create.backend;

import cn.hutool.core.date.DateUtil;
import com.jfinal.kit.Kv;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.enums.DictType;
import org.opsli.common.enums.ValiArgsType;
import org.opsli.common.utils.HumpUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.common.utils.ZipUtils;
import org.opsli.core.creater.strategy.create.CodeBuilder;
import org.opsli.core.creater.utils.EnjoyUtil;
import org.opsli.modulars.creater.column.wrapper.CreaterTableColumnModel;
import org.opsli.modulars.creater.createrlogs.wrapper.CreaterBuilderModel;

import java.util.HashMap;
import java.util.List;
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
    public Map<String,String> createEntity(CreaterBuilderModel builderModelTmp, String dataStr){
        CreaterBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, CreaterBuilderModel.class, true);
        List<CreaterTableColumnModel> columnList = builderModel.getModel().getColumnList();
        // 处理数据
        for (CreaterTableColumnModel columnModel : columnList) {
            // 数据库字段名转驼峰
            columnModel.setFieldName(
                    HumpUtil.underlineToHump(
                            columnModel.getFieldName()
                    )
            );
        }

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
                builderModel.getModel().getTableName()+".java");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }

    /**
     * 生成Mapper
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createMapper(CreaterBuilderModel builderModelTmp, String dataStr){
        CreaterBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, CreaterBuilderModel.class, true);
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
                builderModel.getModel().getTableName()+"Mapper.java");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }

    /**
     * 生成MapperXML
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createMapperXML(CreaterBuilderModel builderModelTmp, String dataStr){
        CreaterBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, CreaterBuilderModel.class, true);
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
                builderModel.getModel().getTableName()+"Mapper.xml");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }

    /**
     * 生成Service
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createService(CreaterBuilderModel builderModelTmp, String dataStr){
        CreaterBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, CreaterBuilderModel.class, true);
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
                "I"+builderModel.getModel().getTableName()+"Service.java");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }


    /**
     * 生成 Service Impl
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createServiceImpl(CreaterBuilderModel builderModelTmp, String dataStr){
        CreaterBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, CreaterBuilderModel.class, true);
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
                builderModel.getModel().getTableName()+"ServiceImpl.java");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }

    /**
     * 生成 Web
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createWeb(CreaterBuilderModel builderModelTmp, String dataStr){
        CreaterBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, CreaterBuilderModel.class, true);
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
                builderModel.getModel().getTableName()+"RestController.java");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }


    /**
     * 生成Model
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createModel(CreaterBuilderModel builderModelTmp, String dataStr){
        CreaterBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, CreaterBuilderModel.class, true);
        // 处理数据
        List<CreaterTableColumnModel> columnList = builderModel.getModel().getColumnList();
        for (CreaterTableColumnModel columnModel : columnList) {
            // 数据库字段名转驼峰
            columnModel.setFieldName(
                    HumpUtil.underlineToHump(
                            columnModel.getFieldName()
                    )
            );

            // 处理验证器
            String validateType = columnModel.getValidateType();
            if(StringUtils.isNotBlank(validateType)){
                String[] validateTypes = validateType.split(",");
                StringBuilder stb = new StringBuilder();
                boolean izNotNull = false;
                // 如果非空 则开启非空验证
                if(DictType.NO_YES_YES.getValue().equals(columnModel.getIzNotNull())){
                    izNotNull = true;
                    stb.append("ValiArgsType.").append(ValiArgsType.IS_NOT_NULL);
                }

                for (int i = 0; i < validateTypes.length; i++) {
                    String type = validateTypes[i];
                    if(izNotNull){
                        stb.append(", ");
                        izNotNull = false;
                    }
                    if(!ValiArgsType.IS_NOT_NULL.equals(ValiArgsType.valueOf(type))){
                        stb.append("ValiArgsType.").append(type);
                    }
                    if(i < validateTypes.length -1 ){
                        stb.append(", ");
                    }
                }
                columnModel.setValidateType(stb.toString());
            }
        }

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
                builderModel.getModel().getTableName()+"Model.java");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }


    /**
     * 生成RestApi
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createRestApi(CreaterBuilderModel builderModelTmp, String dataStr){
        CreaterBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, CreaterBuilderModel.class, true);
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
                builderModel.getModel().getTableName()+"RestApi.java");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }

    /**
     * 创建 Kv
     * @param builderModel Build 模型
     * @return Kv
     */
    private Kv createKv(CreaterBuilderModel builderModel){
        return Kv.by("data", builderModel)
                .set("currTime", DateUtil.now())
                .set("apiPath", CodeBuilder.API_PATH);
    }


}
