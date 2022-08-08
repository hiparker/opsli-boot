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
package org.opsli.plugins.sms.service;


import org.opsli.plugins.sms.enums.SmsType;
import org.opsli.plugins.sms.model.SmsModel;

/**
 * sms发送服务
 *
 * @author 周鹏程
 * @date 2021年8月27日14:08:09
 */
public interface SmsService {


    /**
     * 获得类型
     * @return SmsType
     */
    SmsType getType();

    /**
     * 发送消息
     * @param smsModel 短信model
     */
     void sendSms(SmsModel smsModel);

}
