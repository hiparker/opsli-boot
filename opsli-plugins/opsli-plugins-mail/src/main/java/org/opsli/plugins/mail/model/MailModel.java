package org.opsli.plugins.mail.model;

import lombok.Data;
import lombok.ToString;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.mail.model
 * @Author: Parker
 * @CreateTime: 2020-09-13 18:46
 * @Description: 邮件传输类
 */
@Data
@ToString
public class MailModel {

    /** 收件人 */
    private String to;

    /** 邮件主题 */
    private String subject;

    /** 邮件内容 */
    private String content;

}
