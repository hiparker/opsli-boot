/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.plugins.mail.handler;

import cn.hutool.core.lang.Validator;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.exception.EmptyException;
import org.opsli.plugins.mail.MailPlugin;
import org.opsli.plugins.mail.exception.MailPluginException;
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
public class MailPlugInImpl implements MailPlugin {

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
            throw new MailPluginException(MailMsg.EXCEPTION_UNKNOWN);
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
