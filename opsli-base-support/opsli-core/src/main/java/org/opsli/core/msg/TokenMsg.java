package org.opsli.core.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.msg
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:36
 * @Description: Token - 消息
 */
public enum TokenMsg implements BaseMsg {

    /**
     * Token
     */
    EXCEPTION_TOKEN_CREATE_ERROR(12000,"生成Token失败"),
    EXCEPTION_TOKEN_LOSE_EFFICACY(401,"Token失效，请重新登录"),


    /**
     * 登陆
     */
    EXCEPTION_LOGIN_CAPTCHA(12100,"验证码不正确！"),
    EXCEPTION_LOGIN_ACCOUNT_NO(12101,"账号或密码不正确！"),
    EXCEPTION_LOGIN_ACCOUNT_LOCKED(12102,"账号已被锁定,请联系管理员！"),
    EXCEPTION_LOGOUT_ERROR(12103,"登出失败，没有授权Token！"),
    EXCEPTION_LOGOUT_SUCCESS(12104,"登出成功！"),

    /**
     * 其他
     */
    EXCEPTION_USER_NULL(12200, "用户为空"),

    ;

    private int code;
    private String message;

    TokenMsg(int code, String message){
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
