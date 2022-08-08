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
package org.opsli.plugins.sms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.javaer.aliyun.sms.SmsClient;
import cn.javaer.aliyun.sms.SmsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.opsli.plugins.sms.enums.SmsType;
import org.opsli.plugins.sms.exceptions.SmsException;
import org.opsli.plugins.sms.model.SmsModel;
import org.opsli.plugins.sms.msg.SmsMsgCodeEnum;
import org.opsli.plugins.sms.service.SmsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 阿里云 sms发送
 *
 * @author yanxiaojian
 * @date 2021年8月27日14:09:13
 */
@Slf4j
@Service
public class AliYunSmsServiceImpl implements SmsService {


    @Override
    public SmsType getType() {
        return SmsType.ALIYUN;
    }

    @Override
    public void sendSms(SmsModel smsModel) {
        // 发送短信
        this.sendSms(smsModel.getAccessKey(), smsModel.getAccessKeySecret(), smsModel.getSignName(), smsModel.getTemplateCode(), smsModel.getTemplateParam(),
                smsModel.getTels());
    }

    /**
     * 发送信息
     * @param accessKey accessKey
     * @param accessKeySecret accessKeySecret
     * @param templateCode SMS_153055065
     * @param templateParam {"code":"1111"}
     * @param phoneNumbers 支持对多个手机号码发送短信，手机号码之间以英文逗号（,）分隔。上限为1000个手机号码
     */
    private void sendSms(String accessKey, String accessKeySecret, String signName, String templateCode, Map<String, String> templateParam, List<String> phoneNumbers){
        // 验证参数
        verify(accessKey, accessKeySecret, signName, templateCode, templateParam, phoneNumbers);

        SmsClient smsClient = new SmsClient(accessKey, accessKeySecret);

        // 发送信息
        SmsTemplate smsTemplate = SmsTemplate.builder()
                .signName(signName)
                .templateCode(templateCode)
                .templateParam(templateParam)
                .phoneNumbers(phoneNumbers)
                .build();
        smsClient.send(smsTemplate);
    }

    /**
     * 验证
     * @param signName 签名
     * @param templateCode 模版编号
     * @param templateParam 模版参数
     * @param phoneNumbers 手机号
     */
    private void verify(
            String accessKey, String accessKeySecret,
            String signName, String templateCode,
            Map<String, String> templateParam, List<String> phoneNumbers){

        if(StrUtil.isEmpty(accessKey)){
            // accessKey不可为空
            throw new SmsException(SmsMsgCodeEnum.CODE_ERROR_SMS_ACCESS_KEY_NULL);
        }
        if(StrUtil.isEmpty(accessKeySecret)){
            // accessKeySecret不可为空
            throw new SmsException(SmsMsgCodeEnum.CODE_ERROR_SMS_ACCESS_KEY_SECRET_NULL);
        }
        if(StrUtil.isEmpty(signName)){
            // 签名不可为空
            throw new SmsException(SmsMsgCodeEnum.CODE_ERROR_SMS_SIG_NAME_NULL);
        }

        if(StrUtil.isEmpty(signName)){
            // 签名不可为空
            throw new SmsException(SmsMsgCodeEnum.CODE_ERROR_SMS_SIG_NAME_NULL);
        }

        if(StrUtil.isEmpty(templateCode)){
            // 模版编号不可为空
            throw new SmsException(SmsMsgCodeEnum.CODE_ERROR_SMS_TEMPLATE_CODE_NULL);
        }

        if(CollUtil.isEmpty(phoneNumbers)){
            // 手机号不可为空
            throw new SmsException(SmsMsgCodeEnum.CODE_ERROR_SMS_PHONE_NUMBERS_NULL);
        }
        for (String tel : phoneNumbers) {
            boolean isPhone = PhoneUtil.isPhone(tel);
            if(!isPhone){
                // 不是座机号码+手机号码（CharUtil中国）+ 400 + 800电话 + 手机号号码（香港）
                throw new SmsException(SmsMsgCodeEnum.EXCEPTION_IS_PHONE);
            }
        }
    }

}
