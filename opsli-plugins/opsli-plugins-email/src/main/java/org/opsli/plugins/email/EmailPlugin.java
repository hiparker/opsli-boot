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
package org.opsli.plugins.email;


import org.opsli.plugins.email.conf.EmailConfig;

import java.util.Collection;


/**
 * 邮件接口
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
public interface EmailPlugin {


    /**
     * 发送邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param emailConfig 配置
     * @return String
     */
    String send(String to, String subject, String content, EmailConfig emailConfig);

    /**
     * 发送邮件
     * @param tos 收件人(可多人发送)
     * @param subject 主题
     * @param content 内容
     * @param emailConfig 配置
     * @return String
     */
    String send(Collection<String> tos, String subject, String content, EmailConfig emailConfig);

    /**
     * 发送邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param isHtml 是否 Html内容
     * @param emailConfig 配置
     * @return String
     */
    String send(String to, String subject, String content, boolean isHtml, EmailConfig emailConfig);

    /**
     * 发送邮件
     * @param tos 收件人
     * @param subject 主题
     * @param content 内容
     * @param isHtml 是否 Html内容
     * @param emailConfig 配置
     * @return String
     */
    String send(Collection<String> tos, String subject, String content, boolean isHtml, EmailConfig emailConfig);


}
