package org.opsli.core.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.msg
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:36
 * @Description: 核心类 - 消息
 */
public enum JwtMsg implements BaseMsg {

    EXCEPTION_TOKEN(11000,"认证解密异常: {}"),
    EXCEPTION_DECODE(10101,"解密异常: {}"),
    EXCEPTION_ENCODE(10102,"加密异常: {}"),


    ;

    private int code;
    private String message;

    JwtMsg(int code, String message){
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
