package org.opsli.plugins.mail.exception;

import org.opsli.common.base.msg.BaseMsg;
import org.opsli.common.exception.ServiceException;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.mail.exception
 * @Author: Parker
 * @CreateTime: 2020-09-13 18:44
 * @Description: 邮件异常
 */
public class MailException extends ServiceException {

    public MailException(Integer code, String errorMessage) {
        super(code, errorMessage);
    }

    public MailException(BaseMsg msg) {
        super(msg);
    }
}
