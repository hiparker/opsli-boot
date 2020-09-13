package org.opsli.plugins.mail.handler;

import cn.hutool.core.lang.Validator;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.exception.EmptyException;
import org.opsli.plugins.mail.MailHandler;
import org.opsli.plugins.mail.exception.MailException;
import org.opsli.plugins.mail.model.MailModel;
import org.opsli.plugins.mail.msg.MailMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.mail.handler
 * @Author: Parker
 * @CreateTime: 2020-09-13 18:52
 * @Description: 邮件执行器 实现类
 */
@Slf4j
@Service
public class MailHandlerImpl implements MailHandler {

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void send(MailModel mailModel) {
        try {
            MimeMessage message= javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(username);
            helper.setTo(mailModel.getTo());
            helper.setSubject(mailModel.getSubject());
            helper.setText(mailModel.getContent(),true);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("邮件发送异常：{}",e.getMessage());
            throw new MailException(MailMsg.EXCEPTION_UNKNOWN);
        }
        log.info("邮件发送 - 发送至：{} - 主题：{}", mailModel.getTo(),mailModel.getSubject());
    }

    /**
     * 校验发送邮件的请求参数
     */
    private void validationMailModel(MailModel mailModel) {
        if(Validator.isEmpty(mailModel) ||
                Validator.isEmpty(mailModel.getTo()) ||
                Validator.isEmpty(mailModel.getSubject()) ||
                Validator.isEmpty(mailModel.getContent())){
            throw new EmptyException();
        }
    }
}
