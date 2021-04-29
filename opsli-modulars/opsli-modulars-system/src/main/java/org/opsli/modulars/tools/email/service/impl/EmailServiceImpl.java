package org.opsli.modulars.tools.email.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import org.opsli.api.wrapper.system.options.OptionsModel;
import org.opsli.common.enums.DictType;
import org.opsli.core.utils.OptionsUtil;
import org.opsli.core.utils.ValidationUtil;
import org.opsli.modulars.tools.email.enums.EmailType;
import org.opsli.modulars.tools.email.service.IEmailService;
import org.opsli.modulars.tools.email.wrapper.EmailModel;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.tools.email.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:34
 * @Description: 邮件 接口实现类
 */
@Service
public class EmailServiceImpl implements IEmailService {


    @Override
    public String send(String to, String subject, String content) {
        // 发送邮件
        return this.send(Collections.singletonList(to), subject ,content);
    }

    @Override
    public String send(Collection<String> tos, String subject, String content) {
        // 发送邮件
        return this.send(tos, subject ,content, false);
    }

    @Override
    public String send(String to, String subject, String content, boolean isHtml) {
        // 发送邮件
        return this.send(Collections.singletonList(to), subject ,content, isHtml);
    }

    @Override
    public String send(Collection<String> tos, String subject, String content, boolean isHtml) {

        // 校验发送邮件数据是否正确
        for (String to : tos) {
            EmailModel emailModel = new EmailModel();
            emailModel.setTo(to);
            emailModel.setSubject(subject);
            emailModel.setContent(content);
            ValidationUtil.verify(emailModel);
        }

        // 获得配置信息
        MailAccount mailAccount = this.getMailAccount();

        // 发送邮件
        return MailUtil.send(mailAccount, tos , subject ,content, isHtml);
    }

    /**
     * 获得 配置信息
     * @return MailAccount
     */
    private MailAccount getMailAccount(){
        // 获得配置数据
        OptionsModel smtp = OptionsUtil.getOptionByCode(EmailType.EMAIL_SMTP.getCode());
        OptionsModel port = OptionsUtil.getOptionByCode(EmailType.EMAIL_PORT.getCode());
        OptionsModel sslEnable = OptionsUtil.getOptionByCode(EmailType.EMAIL_SSL_ENABLE.getCode());
        OptionsModel account = OptionsUtil.getOptionByCode(EmailType.EMAIL_ACCOUNT.getCode());
        OptionsModel password = OptionsUtil.getOptionByCode(EmailType.EMAIL_PASSWORD.getCode());
        OptionsModel addresser = OptionsUtil.getOptionByCode(EmailType.EMAIL_ADDRESSER.getCode());

        MailAccount mailAccount = new MailAccount();
        mailAccount.setAuth(true);
        if(smtp != null){
            mailAccount.setHost(smtp.getOptionValue());
        }
        if(port != null){
            mailAccount.setPort(Convert.toInt(port.getOptionValue()));
        }
        if(sslEnable != null){
            mailAccount.setSslEnable(
                    DictType.NO_YES_YES.getValue().equals(sslEnable.getOptionValue()));
        }
        if(account != null){
            mailAccount.setUser(account.getOptionValue());
        }
        if(password != null){
            mailAccount.setPass(password.getOptionValue());
        }
        if(addresser != null){
            mailAccount.setFrom(addresser.getOptionValue());
        }

        return mailAccount;
    }
}
