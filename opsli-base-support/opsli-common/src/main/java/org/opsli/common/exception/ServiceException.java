package org.opsli.common.exception;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.base.excption
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:41
 * @Description: 框架总异常
 */
public class ServiceException extends RuntimeException{

    private Integer code;

    private String errorMessage;

    public ServiceException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public ServiceException(BaseMsg msg) {
        super(msg.getMessage());
        this.code = msg.getCode();
        this.errorMessage = msg.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
