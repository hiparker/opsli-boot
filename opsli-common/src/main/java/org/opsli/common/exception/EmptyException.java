package org.opsli.common.exception;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.base.excption
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:41
 * @Description: 框架总异常
 */
public class EmptyException extends ServiceException{

    private Integer code;

    private String errorMessage;

    public EmptyException() {
        super(400, "请求数据不完整或格式错误！");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
