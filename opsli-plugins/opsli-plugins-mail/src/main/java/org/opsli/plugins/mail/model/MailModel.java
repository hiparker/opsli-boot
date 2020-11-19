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
package org.opsli.plugins.mail.model;

import lombok.Data;
import lombok.ToString;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.mail.model
 * @Author: Parker
 * @CreateTime: 2020-09-13 18:46
 * @Description: 邮件传输类
 */
@Data
@ToString
public class MailModel {

    /** 收件人 */
    private String to;

    /** 邮件主题 */
    private String subject;

    /** 邮件内容 */
    private String content;

}
