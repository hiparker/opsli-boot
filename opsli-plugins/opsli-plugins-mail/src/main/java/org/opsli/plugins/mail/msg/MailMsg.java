package org.opsli.plugins.mail.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.mail.msg
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:54
 * @Description: 邮件消息
 */
public enum MailMsg implements BaseMsg {

    /** 未知消息异常 */
    EXCEPTION_UNKNOWN(90001,"邮件发送失败"),
    ;


    private int code;
    private String message;

    MailMsg(int code,String message){
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
