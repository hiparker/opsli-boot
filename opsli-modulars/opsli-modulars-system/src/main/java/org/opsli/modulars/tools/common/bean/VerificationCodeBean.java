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
package org.opsli.modulars.tools.common.bean;

import cn.hutool.core.map.MapUtil;
import com.jfinal.kit.Kv;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.core.options.EmailConfigFactory;
import org.opsli.core.options.SmsAliYunCaptchaConfigFactory;
import org.opsli.core.options.SmsAliYunConfigFactory;
import org.opsli.core.utils.EnjoyUtil;
import org.opsli.core.utils.VerificationCodeUtil;
import org.opsli.plugins.email.EmailPlugin;
import org.opsli.plugins.sms.SmsFactory;
import org.opsli.plugins.sms.enums.SmsType;
import org.opsli.plugins.sms.model.SmsModel;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * 验证码Bean
 *
 * @author 周鹏程
 * @date 2022-08-02 1:00 PM
 **/
@Slf4j
@AllArgsConstructor
@Component
public class VerificationCodeBean {
    private static final String EMAIL_FTL = "/email/email_code.ftl";
    private final EmailPlugin emailPlugin;
    private final GlobalProperties globalProperties;

    /**
     * 发送邮件验证码
     * @param email 邮箱地址
     */
    public void sendEmailCode(String email, String type){
        // 发送邮件
        VerificationCodeUtil.VerificationCodeModel verificationCodeModel =
                VerificationCodeUtil.createEmailCode(email, type);
        String subject = globalProperties.getSystemName() + " - 验证邮件";
        Kv kv = Kv.create()
                .set("systemName", globalProperties.getSystemName())
                .set("verificationCode", verificationCodeModel.getVerificationCode())
                .set("expiredDate", verificationCodeModel.getExpiredDate())
                .set("expiredMinute", verificationCodeModel.getExpiredMinute());
        String content = EnjoyUtil.render(EMAIL_FTL, kv);

        emailPlugin
                .send(email, subject, content, true,
                        EmailConfigFactory.INSTANCE.getConfig());
    }

    /**
     * 发送手机验证码
     * @param mobile 手机号
     */
    public void sendMobileCode(String mobile, String type){
        // 发送短信
        VerificationCodeUtil.VerificationCodeModel verificationCodeModel =
                VerificationCodeUtil.createMobileCode(mobile, type);

        SmsAliYunConfigFactory.SmsAliYunConfigOption aliYunConfigOption =
                SmsAliYunConfigFactory.INSTANCE.getConfig();

        SmsAliYunCaptchaConfigFactory.SmsAliYunCaptchaConfigOption aliYunCaptchaConfigOption =
                SmsAliYunCaptchaConfigFactory.INSTANCE.getConfig();

        Map<String, String> templateParam = MapUtil.newHashMap(1);
        templateParam.put("code", verificationCodeModel.getVerificationCode());
        SmsModel smsModel = SmsModel.builder()
                .accessKey(aliYunConfigOption.getAccessKey())
                .accessKeySecret(aliYunConfigOption.getAccessKeySecret())
                .signName(aliYunCaptchaConfigOption.getSign())
                .templateCode(aliYunCaptchaConfigOption.getTemplateCode())
                .templateParam(templateParam)
                .tels(Collections.singletonList(mobile))
                .build();
        SmsFactory.sendSms(SmsType.ALIYUN, smsModel);
    }

}
