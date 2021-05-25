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
package org.opsli.core.generator.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.enums.DictType;
import org.opsli.common.enums.ValiArgsType;
import org.opsli.common.utils.HumpUtil;
import org.opsli.modulars.generator.column.wrapper.GenTableColumnModel;
import org.opsli.modulars.generator.logs.wrapper.GenBuilderModel;
import org.opsli.modulars.generator.table.wrapper.GenTableAndColumnModel;

import java.util.List;

/***
 * 代码生成器 处理工具类
 *
 * @author parker
 * @date 2020-11-18 13:21
 */
public final class GeneratorHandleUtil {

    /** 后端 验证前缀 */
    private static final String BACKEND_VALIDATE_TYPE_PREFIX = "ValiArgsType.";

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
                HumpUtil.captureName(
                        HumpUtil.underlineToHump(
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

            for (GenTableColumnModel columnModel : columnList) {
                // 1. 数据库字段名转驼峰
                columnModel.setFieldHumpName(
                        HumpUtil.underlineToHump(
                                columnModel.getFieldName()
                        )
                );

                // 2. 后端字段验证
                // 处理验证器
                String validateType = columnModel.getValidateType();
                if(StringUtils.isNotBlank(validateType)){
                    String[] validateTypes = validateType.split(",");
                    StringBuilder stb = new StringBuilder();
                    boolean izNotNull = false;
                    // 如果非空 则开启非空验证
                    if(DictType.NO_YES_YES.getValue().equals(columnModel.getIzNotNull())){
                        izNotNull = true;
                        stb.append(BACKEND_VALIDATE_TYPE_PREFIX).append(ValiArgsType.IS_NOT_NULL);
                    }

                    for (int i = 0; i < validateTypes.length; i++) {
                        String type = validateTypes[i];
                        if(izNotNull){
                            stb.append(", ");
                            izNotNull = false;
                        }
                        if(!ValiArgsType.IS_NOT_NULL.equals(ValiArgsType.valueOf(type))){
                            stb.append(BACKEND_VALIDATE_TYPE_PREFIX).append(type);
                        }
                        if(i < validateTypes.length -1 ){
                            stb.append(", ");
                        }
                    }
                    columnModel.setBackendValidateType(stb.toString());
                }
            }
        }

        return builderModel;
    }

    // =================

    private GeneratorHandleUtil(){}
}
