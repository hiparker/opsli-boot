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

import cn.hutool.core.convert.Convert;
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
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.creater.strategy.create.backend
 * @Author: Parker
 * @CreateTime: 2020-11-20 17:30
 * @Description: Vue代码构建器
 */
public enum VueCodeBuilder {

    INSTANCE;

    /** 虚拟根路径 */
    private static final String BASE_API_PATH;
    static {
        Props props = new Props("application.yaml");
        BASE_API_PATH = props.getStr("server.servlet.api.path.global-prefix","/api/v1");
    }

    /**
     * 生成 Index
     * @param builderModelTmp
     * @return
     */
    public Map<String,String> createIndex(CreaterBuilderModel builderModelTmp, String dataStr){
        CreaterBuilderModel builderModel =
                WrapperUtil.cloneTransformInstance(builderModelTmp, CreaterBuilderModel.class);
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
        for (int i = 0; i < columnList.size(); i++) {
            if(StringUtils.isNotBlank(columnList.get(i).getQueryType()) &&
                    columnList.get(i).getIzShowList().equals(DictType.NO_YES_YES.getCode())
                ){
                queryList.add(columnList.get(i));
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
     * 生成 Index
     * @param builderModelTmp
     * @return
     */
    public Map<String,String> createEdit(CreaterBuilderModel builderModelTmp, String dataStr){
        CreaterBuilderModel builderModel =
                WrapperUtil.cloneTransformInstance(builderModelTmp, CreaterBuilderModel.class);
        List<CreaterTableColumnModel> columnList = builderModel.getModel().getColumnList();
        List<List<CreaterTableColumnModel>> formList = new ArrayList<>();
        Map<String,List<String>> valiDict = new HashMap<>();
        Set<String> validataTypes = new HashSet<>();
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
                if(DictType.NO_YES_YES.getCode().equals(columnModel.getIzNotNull())){
                    validateTypeSet.add(ValiArgsType.IS_NOT_NULL.toString());
                }

                List<String> validateTypeList = new ArrayList<>(validateTypes.length);
                for (String vali : validateTypeSet) {
                    validateTypeList.add(
                            HumpUtil.underlineToHump(
                                    vali.toLowerCase()
                            )
                    );
                }

                validataTypes.addAll(validateTypeList);
                valiDict.put(columnModel.getFieldName(), validateTypeList);
            }
        }

        // 处理数据
        if(columnList.size() == 1){
            if(columnList.get(0).getIzShowForm().equals(DictType.NO_YES_YES.getCode()) &&
                StringUtils.isNotBlank(columnList.get(0).getShowType())
                ){
                List<CreaterTableColumnModel> formTmpList = new ArrayList<>();
                formTmpList.add(columnList.get(0));
                formList.add(formTmpList);
            }
        }else{
            for (int i = 0; i < columnList.size(); i+=2) {
                List<CreaterTableColumnModel> formTmpList = new ArrayList<>();
                if(columnList.get(i).getIzShowForm().equals(DictType.NO_YES_YES.getCode()) &&
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

        StringBuilder validataTypeStb = new StringBuilder();
        for (String validataType : validataTypes) {
            validataTypeStb.append(validataType);
            validataTypeStb.append(", ");
        }

        String codeStr = EnjoyUtil.render("/foreend/components/TemplateEdit.html",
                this.createKv(builderModel)
                        .set("formList", formList)
                        .set("valiDict", valiDict)
                        .set("validataTypes", validataTypeStb.toString())
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
     * 生成 Api
     * @param builderModelTmp
     * @return
     */
    public Map<String,String> createApi(CreaterBuilderModel builderModelTmp, String dataStr){
        CreaterBuilderModel builderModel =
                WrapperUtil.cloneTransformInstance(builderModelTmp, CreaterBuilderModel.class);
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
     * @param builderModel
     * @return
     */
    private Kv createKv(CreaterBuilderModel builderModel){
        return Kv.by("data", builderModel)
                .set("currTime", DateUtil.now());
    }


}
