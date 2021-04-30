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
package org.opsli.plugins.email.enums;

/**
 * Email 参数类型
 *
 * @author Parker
 */
public enum EmailType {

    /** SMTP 服务 */
    EMAIL_SMTP("email_smtp", "SMTP地址"),
    EMAIL_PORT("email_port", "SMTP端口"),
    EMAIL_SSL_ENABLE("email_ssl_enable", "开启SSL认证"),
    EMAIL_ACCOUNT("email_account", "邮箱账号"),
    EMAIL_PASSWORD("email_password", "邮箱账号密码"),
    EMAIL_ADDRESSER("email_addresser", "发件人"),

    ;

    private final String code;
    private final String desc;

    public static EmailType getType(String cacheType) {
        EmailType[] var1 = values();
        for (EmailType type : var1) {
            if (type.code.equalsIgnoreCase(cacheType)) {
                return type;
            }
        }
        return null;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    // =================

    EmailType(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }
}
