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
package org.opsli.core.creater.strategy.create.foreend;

import cn.hutool.core.date.DateUtil;
import com.jfinal.kit.Kv;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.enums.DictType;
import org.opsli.common.enums.ValiArgsType;
import org.opsli.common.utils.HumpUtil;
import org.opsli.common.utils.Props;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.common.utils.ZipUtils;
import org.opsli.core.creater.strategy.create.CodeBuilder;
import org.opsli.core.creater.utils.EnjoyUtil;
import org.opsli.modulars.creater.column.wrapper.CreaterTableColumnModel;
import org.opsli.modulars.creater.createrlogs.wrapper.CreaterBuilderModel;

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
    public Map<String,String> createIndex(CreaterBuilderModel builderModelTmp, String dataStr){
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

        List<CreaterTableColumnModel> queryList = new ArrayList<>(2);
        // 简单检索
        List<CreaterTableColumnModel> briefQueryList = new ArrayList<>(2);
        // 更多检索
        List<CreaterTableColumnModel> moreQueryList = new ArrayList<>();
        for (CreaterTableColumnModel createrTableColumnModel : columnList) {
            if (StringUtils.isNotBlank(createrTableColumnModel.getQueryType()) &&
                    createrTableColumnModel.getIzShowList().equals(DictType.NO_YES_YES.getValue())
            ) {
                queryList.add(createrTableColumnModel);
            }
        }
        // 筛选数据
        for (int i = 0; i < queryList.size(); i++) {
            if(i < 2){
                briefQueryList.add(queryList.get(i));
            }else{
                moreQueryList.add(queryList.get(i));
            }
        }


        String codeStr = EnjoyUtil.render("/foreend/index/TemplateIndex.html",
                this.createKv(builderModel)
                .set("briefQueryList", briefQueryList)
                .set("moreQueryList", moreQueryList)
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
    public Map<String,String> createEdit(CreaterBuilderModel builderModelTmp, String dataStr){
        CreaterBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, CreaterBuilderModel.class, true);
        List<CreaterTableColumnModel> columnList = builderModel.getModel().getColumnList();
        List<List<CreaterTableColumnModel>> formList = new ArrayList<>();
        Map<String,List<String>> valiDict = new HashMap<>();
        Set<String> validateTypesSet = new HashSet<>();
        // 处理验证数据
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
                Set<String> validateTypeSet = new HashSet<>(Arrays.asList(validateTypes));
                // 如果非空 则开启非空验证
                if(DictType.NO_YES_YES.getValue().equals(columnModel.getIzNotNull())){
                    validateTypeSet.add(ValiArgsType.IS_NOT_NULL.toString());
                }

                List<String> validateTypeList = new ArrayList<>(validateTypes.length);
                for (String validate : validateTypeSet) {
                    validateTypeList.add(
                            HumpUtil.underlineToHump(
                                    VALIDATE_PREFIX + validate.toLowerCase()
                            )
                    );
                }

                validateTypesSet.addAll(validateTypeList);
                valiDict.put(columnModel.getFieldName(), validateTypeList);
            }
        }

        // 处理数据
        if(columnList.size() == 1){
            if(DictType.NO_YES_YES.getValue().equals(columnList.get(0).getIzShowForm()) &&
                StringUtils.isNotBlank(columnList.get(0).getShowType())
                ){
                List<CreaterTableColumnModel> formTmpList = new ArrayList<>();
                formTmpList.add(columnList.get(0));
                formList.add(formTmpList);
            }
        }else{
            for (int i = 0; i < columnList.size(); i+=2) {
                List<CreaterTableColumnModel> formTmpList = new ArrayList<>();
                if(DictType.NO_YES_YES.getValue().equals(columnList.get(i).getIzShowForm()) &&
                        StringUtils.isNotBlank(columnList.get(i).getShowType())
                ){
                    formTmpList.add(columnList.get(i));
                    if(i+1 < columnList.size()){
                        formTmpList.add(columnList.get(i+1));
                    }
                    formList.add(formTmpList);
                }
            }
        }

        StringBuilder validateTypeStb = new StringBuilder();
        for (String validateType : validateTypesSet) {
            validateTypeStb.append(validateType);
            validateTypeStb.append(", ");
        }

        String codeStr = EnjoyUtil.render("/foreend/components/TemplateEdit.html",
                this.createKv(builderModel)
                        .set("formList", formList)
                        .set("valiDict", valiDict)
                        .set("validateTypes", validateTypeStb.toString())
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
        entityMap.put(ZipUtils.FILE_NAME, builderModel.getModel().getTableName()+"ManagementEdit.vue");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }


    /**
     * 生成 Import
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createImport(CreaterBuilderModel builderModelTmp, String dataStr){
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
        entityMap.put(ZipUtils.FILE_NAME, builderModel.getModel().getTableName()+"ManagementImport.vue");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }

    /**
     * 生成 Api
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createApi(CreaterBuilderModel builderModelTmp, String dataStr){
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
        entityMap.put(ZipUtils.FILE_NAME, builderModel.getModel().getTableName()+"Management.js");
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
                .set("currTime", DateUtil.now());
    }


}
