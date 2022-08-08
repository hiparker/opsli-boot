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
package org.opsli.plugins.sms;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.opsli.plugins.sms.service.SmsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Redisson自动化配置
 *
 * @author Parker
 * @date 2019/6/19 下午11:55
 */
@Slf4j
@Configuration
public class SmsConfig {

    @Bean
    public void initRedisPushSubHandler(){
        Map<String, SmsService> beansOfType = SpringUtil.getBeansOfType(SmsService.class);
        for (Map.Entry<String, SmsService> smsServiceEntry : beansOfType.entrySet()) {
            SmsService smsService = smsServiceEntry.getValue();
            // 加入集合
            SmsFactory.put(smsService);
        }
    }

}

