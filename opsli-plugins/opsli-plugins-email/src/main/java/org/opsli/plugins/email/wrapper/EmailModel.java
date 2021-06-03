package org.opsli.plugins.email.wrapper;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件服务
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmailModel {

    /** 收件人 */
    private String to;

    /** 主题 */
    private String subject;

    /** 内容 */
    private String content;

}