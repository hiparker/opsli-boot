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
package org.opsli.core.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * 核心类 - 消息
 *
 * @author Parker
 * @date 2020-09-13 19:36
 */
public enum CoreMsg implements BaseMsg {

    /** SQL */
    SQL_EXCEPTION_UPDATE(10100,"更新数据失败，是否刷新页面重试？"),
    SQL_EXCEPTION_INSERT(10100,"新增数据失败，是否刷新页面重试？"),
    SQL_EXCEPTION_DELETE(10100,"删除数据失败，是否刷新页面重试？"),
    SQL_EXCEPTION_INTEGRITY_CONSTRAINT_VIOLATION(10105,"数据主键冲突或者已有该数据！"),
    SQL_EXCEPTION_NOT_HAVE_DEFAULT_VALUE(10106,"数据异常：{} 字段没有默认值！"),
    SQL_EXCEPTION_UNKNOWN(10106,"数据异常：未知异常，请联系系统管理员 {}"),

    /**
     * Redis
     */
    REDIS_EXCEPTION_PUSH_SUB(10200,"Redis 订阅通道失败！"),
    REDIS_EXCEPTION_LOCK(10201,"无法申领分布式锁"),

    /**
     * Excel
     */
    EXCEL_EXPORT_SUCCESS(200,"Excel 导出成功！  -  数据行数：{}  -  耗时：{}"),
    EXCEL_EXPORT_ERROR(10301,"Excel 导出失败！  -  耗时：{}  -  失败信息：{}"),
    EXCEL_IMPORT_SUCCESS(200,"EXCEL 导入成功！  -  耗时：{}"),
    EXCEL_IMPORT_ERROR(10303,"Excel导入失败!   -  耗时：{}  -  失败信息：{}"),
    EXCEL_IMPORT_NO(10304,"导入对象为空"),
    EXCEL_FILE_NULL(10305,"请选择文件"),
    EXCEL_HANDLE_MAX(10700, "超出最大操作数量, 当前数据[{}]条，允许最大阈值[{}]条"),


    /**
     * 缓存
     */
    CACHE_PUNCTURE_EXCEPTION(10405, "当期服务繁忙，客官请稍微再次尝试！"),
    CACHE_DEL_EXCEPTION(10406, "无法清除缓存，请稍后再试"),


    /** 演示模式 */
    EXCEPTION_ENABLE_DEMO(10600,"演示模式不允许操作"),

    /** 其他 */
    OTHER_EXCEPTION_LIMITER(10700,"当前系统繁忙，请稍后再试"),
    OTHER_EXCEPTION_CRYPTO_EN(10702,"加密失败"),
    OTHER_EXCEPTION_CRYPTO_DE(10703,"解密失败"),
    OTHER_EXCEPTION_CRYPTO_REFLEX(10704,"解密反射失败"),
    OTHER_EXCEPTION_UTILS_INIT(10705,"系统工具类暂未初始化"),


    ;

    private final int code;
    private final String message;

    CoreMsg(int code,String message){
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
