package org.opsli.plugins.email.conf;

import lombok.Data;

import java.io.Serializable;

/**
 * 本地存储配置
 *
 * @author Parker
 */
@Data
public class EmailConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /** SMTP地址 */
    private String smtp;

    /** SMTP端口 */
    private Integer port;

    /** 开启SSL认证 */
    private String sslEnable;

    /** 邮箱账号 */
    private String account;

    /** 邮箱密码 */
    private String password;

    /** 发件人 */
    private String addresser;

}