package org.opsli.plugins.mail;

import org.opsli.plugins.mail.model.MailModel;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.mail.handler
 * @Author: Parker
 * @CreateTime: 2020-09-13 18:51
 * @Description: 邮件执行器
 */
public interface MailPlugin {

    /**
     * 发送邮件
     * @param mailModel
     */
    void send(MailModel mailModel);

}
