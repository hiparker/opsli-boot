package org.opsli.common.exception;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.mail.exception
 * @Author: Parker
 * @CreateTime: 2020-09-14 18:44
 * @Description: Jwt 异常
 */
public class JwtException extends ServiceException {

    public JwtException(Integer code, String errorMessage) {
        super(code, errorMessage);
    }

    public JwtException(BaseMsg msg) {
        super(msg);
    }
}
