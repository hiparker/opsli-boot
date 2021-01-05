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
package org.opsli.core.creater.strategy.create;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.ApiFlag;
import org.opsli.common.enums.ValiArgsType;
import org.opsli.common.utils.HumpUtil;
import org.opsli.common.utils.Props;
import org.opsli.common.utils.ZipUtils;
import org.opsli.core.creater.strategy.create.backend.JavaCodeBuilder;
import org.opsli.core.creater.strategy.create.foreend.VueCodeBuilder;
import org.opsli.core.creater.strategy.create.readme.ReadMeBuilder;
import org.opsli.modulars.creater.column.wrapper.CreaterTableColumnModel;
import org.opsli.modulars.creater.createrlogs.wrapper.CreaterBuilderModel;
import org.opsli.modulars.creater.table.wrapper.CreaterTableAndColumnModel;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.creater.strategy.create.backend
 * @Author: Parker
 * @CreateTime: 2020-11-20 17:30
 * @Description: Java代码构建器
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
        Props props = new Props("creater.yaml");
        EXCLUDE_FIELDS = props.getList("opsli.exclude-fields");
        API_PATH = ApiFlag.class.getPackage().getName();
    }

    /**
     * 构建
     */
    public void build(CreaterBuilderModel builderModel, HttpServletResponse response){
        if(builderModel == null){
            return;
        }

        String dataStr = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");

        CreaterTableAndColumnModel model = builderModel.getModel();


        // 数据库表名转驼峰
        model.setTableName(
                HumpUtil.captureName(
                        HumpUtil.underlineToHump(
                                model.getTableName()
                        )
                )
        );

        List<CreaterTableColumnModel> columnList = model.getColumnList();
        //遍历排除字段
        columnList.removeIf(tmp -> EXCLUDE_FIELDS.contains(tmp.getFieldName()));


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream out = this.getOutputStream(response, dataStr);
        try {
            if(out != null){
                List<Map<String, String>> fileList = new ArrayList<>();
                // 处理后端代码 ====================
                // entity
                fileList.add(JavaCodeBuilder.INSTANCE.createEntity(builderModel, dataStr));
                // mapper
                fileList.add(JavaCodeBuilder.INSTANCE.createMapper(builderModel, dataStr));
                // mapper xml
                fileList.add(JavaCodeBuilder.INSTANCE.createMapperXML(builderModel, dataStr));
                // service
                fileList.add(JavaCodeBuilder.INSTANCE.createService(builderModel, dataStr));
                // service impl
                fileList.add(JavaCodeBuilder.INSTANCE.createServiceImpl(builderModel, dataStr));
                // web
                fileList.add(JavaCodeBuilder.INSTANCE.createWeb(builderModel, dataStr));
                // model
                fileList.add(JavaCodeBuilder.INSTANCE.createModel(builderModel, dataStr));
                // api
                fileList.add(JavaCodeBuilder.INSTANCE.createRestApi(builderModel, dataStr));

                // 处理前端代码 ====================
                // index
                fileList.add(VueCodeBuilder.INSTANCE.createIndex(builderModel, dataStr));
                // edit
                fileList.add(VueCodeBuilder.INSTANCE.createEdit(builderModel, dataStr));
                // import
                fileList.add(VueCodeBuilder.INSTANCE.createImport(builderModel, dataStr));
                // 前api
                fileList.add(VueCodeBuilder.INSTANCE.createApi(builderModel, dataStr));

                // 处理 ReadMe
                fileList.add(ReadMeBuilder.INSTANCE.createReadMe(builderModel, dataStr));

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
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            return response.getOutputStream();
        } catch (IOException ignored) {}
        return null;
    }

    public static void main(String[] args) {
        System.out.println(ValiArgsType.IS_NOT_NULL.toString());
    }

}
