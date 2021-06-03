package org.opsli.plugins.email.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import org.opsli.common.enums.DictType;
import org.opsli.plugins.email.EmailPlugin;
import org.opsli.plugins.email.conf.EmailConfig;
import org.opsli.plugins.email.exception.EmailPluginException;
import org.opsli.plugins.email.msg.EmailMsg;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * 邮件 接口实现类
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
@Service
public class EmailPluginImpl implements EmailPlugin {

    @Override
    public String send(String to, String subject, String content, EmailConfig emailConfig) {
        // 发送邮件
        return this.send(Collections.singletonList(to), subject , content, emailConfig);
    }

    @Override
    public String send(Collection<String> tos, String subject, String content,
                       EmailConfig emailConfig) {
        // 发送邮件
        return this.send(tos, subject ,content, false, emailConfig);
    }

    @Override
    public String send(String to, String subject, String content,
                       boolean isHtml, EmailConfig emailConfig) {
        // 发送邮件
        return this.send(Collections.singletonList(to), subject ,
                content, isHtml, emailConfig);
    }

    @Override
    public String send(Collection<String> tos, String subject, String content,
                       boolean isHtml, EmailConfig emailConfig) {

        // 校验发送邮件数据是否正确
        this.verify(tos, subject, content);

        // 获得配置信息
        MailAccount mailAccount = this.getMailAccount(emailConfig);

        // 发送邮件
        return MailUtil.send(mailAccount, tos , subject ,content, isHtml);
    }

    /**
     * 验证
     * @param tos 收件人(可多人发送)
     * @param subject 主题
     * @param content 内容
     */
    private void verify(Collection<String> tos, String subject, String content){
        // 校验发送邮件数据是否正确
        for (String to : tos) {
            if(StrUtil.isEmpty(to)){
                // 收件人不可为空
                throw new EmailPluginException(EmailMsg.EXCEPTION_MODEL_TO_NULL);
            }
            if(StrUtil.isEmpty(subject)){
                // 主题不可为空
                throw new EmailPluginException(EmailMsg.EXCEPTION_MODEL_SUBJECT_NULL);
            }
            if(StrUtil.isEmpty(content)){
                // 内容不可为空
                throw new EmailPluginException(EmailMsg.EXCEPTION_MODEL_CONTENT_NULL);
            }
        }
    }

    /**
     * 获得 配置信息
     * @return MailAccount
     */
    private MailAccount getMailAccount(EmailConfig emailConfig){
        if(emailConfig == null){
            // 邮件服务初始化异常
            throw new EmailPluginException(EmailMsg.EXCEPTION_CONFIG_INIT_NULL);
        }

        MailAccount mailAccount = new MailAccount();
        mailAccount.setAuth(true);
        mailAccount.setHost(emailConfig.getSmtp());
        mailAccount.setPort(emailConfig.getPort());
        mailAccount.setSslEnable(
                DictType.NO_YES_YES.getValue().equals(emailConfig.getSslEnable()));
        mailAccount.setUser(emailConfig.getAccount());
        mailAccount.setPass(emailConfig.getPassword());
        mailAccount.setFrom(emailConfig.getAddresser());
        return mailAccount;
    }
}
