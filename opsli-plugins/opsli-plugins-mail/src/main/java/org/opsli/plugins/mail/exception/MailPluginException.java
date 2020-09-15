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
public class MailPluginException extends ServiceException {

    public MailPluginException(Integer code, String errorMessage) {
        super(code, errorMessage);
    }

    public MailPluginException(BaseMsg msg) {
        super(msg);
    }
}
