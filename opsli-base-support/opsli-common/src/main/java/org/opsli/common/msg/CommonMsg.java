package org.opsli.common.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.msg
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:36
 * @Description: 核心类 - 消息
 */
public enum CommonMsg implements BaseMsg {


    /** Controller 参数默认序列化 */
    EXCEPTION_CONTROLLER_MODEL(10100,"序列化对象失败！"),



    ;

    private int code;
    private String message;

    CommonMsg(int code, String message){
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
