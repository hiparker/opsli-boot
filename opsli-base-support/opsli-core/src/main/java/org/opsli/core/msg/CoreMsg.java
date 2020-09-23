package org.opsli.core.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.msg
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:36
 * @Description: 核心类 - 消息
 */
public enum CoreMsg implements BaseMsg {

    /** SQL */
    SQL_EXCEPTION_UPDATE(10100,"更新数据失败，请稍后再次尝试！"),
    SQL_EXCEPTION_INSERT(10101,"新增数据失败，请稍后再次尝试！"),
    SQL_EXCEPTION_DELETE(10102,"删除数据失败，请稍后再次尝试！"),
    SQL_EXCEPTION_INTEGRITY_CONSTRAINT_VIOLATION(10105,"数据主键冲突或者已有该数据！"),
    SQL_EXCEPTION_NOT_HAVE_DEFAULT_VALUE(10106,"数据异常：{} 字段没有默认值！"),
    SQL_EXCEPTION_UNKNOWN(10106,"数据异常：未知异常，请联系系统管理员 {}"),


    /**
     * Redis
     */
    REDIS_EXCEPTION_PUSH_SUB(10200,"Redis 订阅通道失败！"),

    /**
     * Excel
     */
    EXCEL_EXPORT_SUCCESS(10300,"Excel 导出成功！"),
    EXCEL_EXPORT_ERROR(10301,"Excel 导出失败！{}"),
    EXCEL_IMPORT_SUCCESS(10302,"EXCEL 导入成功！数据行数：{}  耗时(秒)：{}"),
    EXCEL_IMPORT_ERROR(10303,"Excel导入失败：{}  耗时(秒)：{}"),
    EXCEL_IMPORT_NO(10304,""),
    EXCEL_FILE_NULL(10305,"请选择文件"),

    /**
     * 缓存
     */
    CACHE_PUNCTURE_EXCEPTION(10405, "当期服务繁忙，客官请稍微再次尝试！")
    ;

    private int code;
    private String message;

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
