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
package org.opsli.plugins.generator.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.enums.DictType;
import org.opsli.common.enums.ValidatorType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.FieldUtil;
import org.opsli.common.utils.ListDistinctUtil;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.modulars.generator.column.wrapper.GenTableColumnModel;
import org.opsli.modulars.generator.importable.ImportTableUtil;
import org.opsli.modulars.generator.logs.wrapper.GenBuilderModel;
import org.opsli.modulars.generator.table.wrapper.GenTableAndColumnModel;
import org.opsli.plugins.generator.enums.JavaType;
import org.opsli.plugins.generator.msg.GeneratorMsg;

import java.util.List;
import java.util.Map;

/***
 * 代码生成器 处理工具类
 *
 * @author parker
 * @date 2020-11-18 13:21
 */
public final class GeneratorHandleUtil {

    /** 后端 验证前缀 */
    private static final String BACKEND_VALIDATE_TYPE_PREFIX = "ValidatorType.";
    /** 验证方法前缀 */
    private static final String VALIDATE_PREFIX = "validator_";

    /**
     * 处理代码生成器数据
     * @param builderModel 数据
     * @param excludeFields 排除字段
     * @return GenBuilderModel
     */
    public static GenBuilderModel handleData(GenBuilderModel builderModel, List<String> excludeFields){
        if(builderModel == null){
            return null;
        }

        // 非法处理
        if(CollUtil.isEmpty(excludeFields)){
            excludeFields = ListUtil.empty();
        }

        // 处理表信息
        GenTableAndColumnModel model = builderModel.getModel();
        // 数据库表名转驼峰
        model.setTableHumpName(
                FieldUtil.upperFirstLetter(
                        FieldUtil.underlineToHump(
                                model.getTableName()
                        )
                )
        );

        // 表字段数据处理
        List<GenTableColumnModel> columnList = model.getColumnList();
        if(!CollUtil.isEmpty(columnList)){
            // 遍历排除字段
            List<String> finalExcludeFields = excludeFields;
            columnList.removeIf(tmp -> finalExcludeFields.contains(tmp.getFieldName()));

            List<GenTableColumnModel> queryList = Lists.newArrayList();

            for (GenTableColumnModel columnModel : columnList) {
                // 1. 数据库字段名转驼峰
                columnModel.setFieldHumpName(
                        FieldUtil.underlineToHump(
                                columnModel.getFieldName()
                        )
                );

                // 验证集合
                List<String> validateTypeList = Convert.toList(String.class, columnModel.getValidateType());
                // 如果非空 则开启非空验证
                if(DictType.NO_YES_YES.getValue().equals(columnModel.getIzNotNull())){
                    validateTypeList.add(ValidatorType.IS_NOT_NULL.toString());
                }
                // 去空项
                validateTypeList.removeIf(StringUtils::isBlank);
                // 去重复
                validateTypeList = ListDistinctUtil.distinct(validateTypeList);

                // 2. 处理字段验证器
                if(!CollUtil.isEmpty(validateTypeList)){
                    List<String> typeList = Lists.newArrayListWithCapacity(validateTypeList.size());
                    List<String> typeAndCommaList = Lists.newArrayListWithCapacity(validateTypeList.size());
                    for (int i = 0; i < validateTypeList.size(); i++) {
                        String validate = validateTypeList.get(i);
                        String validateAndComma = validate;

                        if(i < validateTypeList.size() - 1 ){
                            validateAndComma+=", ";
                        }

                        typeList.add(validate);
                        typeAndCommaList.add(validateAndComma);
                    }

                    columnModel.setValidateTypeList(typeList);
                    columnModel.setValidateTypeAndCommaList(typeAndCommaList);
                }

                // 3. 处理 Form 表单字段
                if(DictType.NO_YES_YES.getValue().equals(columnModel.getIzShowForm())){
                    if(CollUtil.isEmpty(model.getFormList())){
                        model.setFormList(Lists.newArrayList());
                    }
                    model.getFormList().add(columnModel);
                }

                // 4. 处理 Index 页面字段 - QueryList
                if (StringUtils.isNotBlank(columnModel.getQueryType()) &&
                        DictType.NO_YES_YES.getValue().equals(columnModel.getIzShowList())
                    ) {
                    queryList.add(columnModel);
                }

            }

            // 5. 处理 Index 页面字段 - briefQueryList - moreQueryList
            if(!CollUtil.isEmpty(queryList)){
                if(CollUtil.isEmpty(model.getBriefQueryList())){
                    model.setBriefQueryList(Lists.newArrayList());
                }
                if(CollUtil.isEmpty(model.getMoreQueryList())){
                    model.setMoreQueryList(Lists.newArrayList());
                }

                for (int i = 0; i < queryList.size(); i++) {
                    if(i < 2){
                        model.getBriefQueryList().add(
                                queryList.get(i));
                    }else{
                        model.getMoreQueryList().add(
                                queryList.get(i));
                    }
                }
            }

            // 6. Entity 引入 import
            Map<String, String> javaFieldTypes = ImportTableUtil.getJavaFieldTypes();
            List<String> javaTypeList = Lists.newArrayListWithCapacity(javaFieldTypes.size());
            for (Map.Entry<String, String> entry : javaFieldTypes.entrySet()) {
                javaTypeList.add(entry.getValue());
            }
            List<String> pkgList = JavaType.getPkgList(javaTypeList);
            model.setEntityPkgList(pkgList);

        }

        return builderModel;
    }

    /**
     * 判断 代码生成器 是否启用
     *
     * @param globalProperties 配置类
     */
    public static void judgeGeneratorEnable(GlobalProperties globalProperties){
        // 代码生成器未启用
        if(globalProperties.getGenerator() == null ||
                !globalProperties.getGenerator().getEnable()){
            // 代码生成器未启用
            throw new ServiceException(GeneratorMsg.EXCEPTION_NOT_ENABLE);
        }
    }

    // =================

    private GeneratorHandleUtil(){}
}
