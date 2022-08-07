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
import org.opsli.core.utils.OptionsUtil;
import org.opsli.core.utils.ValidatorUtil;

import java.io.Serializable;

/**
 * 阿里云短信-验证码配置
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
public enum SmsAliYunCaptchaConfigFactory {

    /** 实例对象 */
    INSTANCE;

    /**
     * 获得配置信息
     * @return LocalConfig
     */
    public SmsAliYunCaptchaConfigOption getConfig() {
        SmsAliYunCaptchaConfigOption option = new SmsAliYunCaptchaConfigOption();
        // 获得缓存参数配置
        OptionsUtil.getOptionByBean(option);
        // 验证配置
        ValidatorUtil.verify(option);

        // 转化对象
        return option;
    }


    // =======================

    /**
     * 阿里云短信验证码配置
     *
     * @author Parker
     */
    @Data
    public static class SmsAliYunCaptchaConfigOption implements Serializable {

        private static final long serialVersionUID = 1L;

        /** 模版编码 */
        @ApiModelProperty(value = "模版编码")
        @Validator({ValidatorType.IS_NOT_NULL})
        @OptionDict("sms_aliyun_captcha_template_code")
        private String templateCode;

        /** 签名 */
        @ApiModelProperty(value = "签名")
        @Validator({ValidatorType.IS_NOT_NULL})
        @OptionDict("sms_aliyun_captcha_sign")
        private String sign;

    }

}
