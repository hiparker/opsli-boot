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
package org.opsli.plugins.generator.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * 代码生成器 - 消息
 *
 * @author parker
 * @date 2020-09-13 19:36
 */
public enum GeneratorMsg implements BaseMsg {

    /**
     * 表
     */
    EXCEPTION_TABLE_NAME_REPEAT(50000,"表名重复"),

    /**
     * 字段
     */
    EXCEPTION_TABLE_COLUMN_FIELD_NAME_REPEAT(50001,"字段名重复"),

    /**
     * 同步
     */
    EXCEPTION_SYNC_NULL(50100,"同步表失败，暂无该表"),
    EXCEPTION_SYNC_CORE(50101,"系统核心关键表不允许同步"),

    /**
     * 导入
     */
    EXCEPTION_IMPORT_NULL(50120,"未选中表，无法导入"),
    EXCEPTION_IMPORT_TABLE_NULL(50121,"暂无{}该表"),
    EXCEPTION_IMPORT_FIELD_NULL(50122,"暂未获得表字段"),


    /**
     * 生成
     */
    EXCEPTION_CREATE_NULL(50140,"生成失败，数据为空"),
    EXCEPTION_CREATE_TABLE_NULL(50141,"生成失败，暂无表数据"),
    EXCEPTION_CREATE_FIELD_NULL(50142,"生成失败，暂无表字段"),
    EXCEPTION_CREATE_MENU_CODE_NULL(50143,"生成菜单失败，请先生成代码"),
    EXCEPTION_CREATE_MENU_PARENT_NULL(50144,"上级菜单不可为空"),

    /**
     * 模板
     */
    EXCEPTION_TEMPLATE_NAME_REPEAT(50150,"模板名称重复"),
    EXCEPTION_TEMPLATE_AT_LEAST_ONE(50151,"代码模板同一表类型下，至少保障有一个模板"),
    EXCEPTION_TEMPLATE_COPY_NULL(50152,"暂无该模板"),


    /**
     * 其他
     */
    EXCEPTION_OTHER_NULL(50200,"暂无数据"),
    EXCEPTION_NOT_ENABLE(50201,"代码生成器未启用"),

    ;

    private final int code;
    private final String message;

    GeneratorMsg(int code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
