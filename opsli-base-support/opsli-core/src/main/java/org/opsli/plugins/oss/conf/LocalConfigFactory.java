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
package org.opsli.plugins.oss.conf;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.opsli.common.annotation.OptionDict;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.enums.ValidatorType;
import org.opsli.core.utils.OptionsUtil;

import java.io.Serializable;

/**
 * 本地存储配置工程
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
public enum LocalConfigFactory implements ConfigFactory<LocalConfigFactory.LocalConfig> {

    /** 实例对象 */
    INSTANCE;

    /**
     * 获得配置信息
     * @return LocalConfig
     */
    @Override
    public LocalConfig getConfig() {
        LocalConfig config = new LocalConfig();
        // 获得缓存参数配置
        OptionsUtil.getOptionByBean(config);

        return config;
    }


    // =======================

    /**
     * 本地存储配置
     *
     * @author Parker
     */
    @Data
    public static class LocalConfig implements Serializable {

        private static final long serialVersionUID = 1L;

        /** 域名 */
        @ApiModelProperty(value = "域名")
        @Validator({ValidatorType.IS_NOT_NULL})
        @ValidatorLenMax(100)
        @OptionDict("storage_local_domain")
        private String domain;

        /** 前缀 */
        @ApiModelProperty(value = "前缀")
        @ValidatorLenMax(100)
        @OptionDict("storage_local_path_prefix")
        private String pathPrefix;

    }

}
