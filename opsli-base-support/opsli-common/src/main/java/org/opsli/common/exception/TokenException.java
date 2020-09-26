package org.opsli.common.exception;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.mail.exception
 * @Author: Parker
 * @CreateTime: 2020-09-14 18:44
 * @Description: 认证 异常
 */
public class TokenException extends ServiceException {

    public TokenException(Integer code, String errorMessage) {
        super(code, errorMessage);
    }

    public TokenException(BaseMsg msg) {
        super(msg);
    }
}
