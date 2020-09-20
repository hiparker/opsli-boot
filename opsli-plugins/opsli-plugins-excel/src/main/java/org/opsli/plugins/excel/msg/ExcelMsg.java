package org.opsli.plugins.excel.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.mail.msg
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:54
 * @Description: Redis消息
 */
public enum ExcelMsg implements BaseMsg {

    /** Excel 异常 */
    EXCEPTION_FILE_FORMAT(90000,"文件格式错误！"),
    EXCEPTION_CREATE_ERROR(90000,"创建文件失败！"),
    ;


    private int code;
    private String message;

    ExcelMsg(int code, String message){
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
