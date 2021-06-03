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
package org.opsli.core.options;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.opsli.common.annotation.OptionDict;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.enums.ValidatorType;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.utils.OptionsUtil;
import org.opsli.core.utils.ValidatorUtil;
import org.opsli.plugins.email.conf.EmailConfig;

import java.io.Serializable;

/**
 * 邮件配置
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
public enum EmailConfigFactory {

    /** 实例对象 */
    INSTANCE;

    /**
     * 获得配置信息
     * @return LocalConfig
     */
    public EmailConfig getConfig() {
        EmailConfigOption option = new EmailConfigOption();
        // 获得缓存参数配置
        OptionsUtil.getOptionByBean(option);
        // 验证配置
        ValidatorUtil.verify(option);

        // 转化对象
        return WrapperUtil.transformInstance(option, EmailConfig.class);
    }


    // =======================

    /**
     * 邮件配置
     *
     * @author Parker
     */
    @Data
    public static class EmailConfigOption implements Serializable {

        private static final long serialVersionUID = 1L;

        /** SMTP地址 */
        @ApiModelProperty(value = "SMTP地址")
        @Validator({ValidatorType.IS_NOT_NULL})
        @OptionDict("email_smtp")
        private String smtp;

        /** SMTP端口 */
        @ApiModelProperty(value = "SMTP端口")
        @Validator({ValidatorType.IS_NOT_NULL})
        @OptionDict("email_port")
        private Integer port;

        /** 开启SSL认证 */
        @ApiModelProperty(value = "开启SSL认证")
        @Validator({ValidatorType.IS_NOT_NULL})
        @OptionDict("email_ssl_enable")
        private String sslEnable;

        /** 邮箱账号 */
        @ApiModelProperty(value = "邮箱账号")
        @Validator({ValidatorType.IS_NOT_NULL})
        @OptionDict("email_account")
        private String account;

        /** 邮箱密码 */
        @ApiModelProperty(value = "邮箱密码")
        @Validator({ValidatorType.IS_NOT_NULL})
        @OptionDict("email_password")
        private String password;

        /** 发件人 */
        @ApiModelProperty(value = "发件人")
        @Validator({ValidatorType.IS_NOT_NULL})
        @OptionDict("email_addresser")
        private String addresser;
    }

}
