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
package org.opsli.plugins.security.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 认证配置
 *
 * @author Parker
 * @date 2020-09-15
 */
@Component
@ConfigurationProperties(prefix = AuthProperties.PROP_PREFIX)
@Data
@EqualsAndHashCode(callSuper = false)
public class AuthProperties {

    public static final String PROP_PREFIX = "opsli.auth";

    /** 排除URL */
    private UrlExclusion urlExclusion;

    /** 凭证过期时间 -1 为不处理 */
    private int credentialsExpired = -1;

    @Data
    public static class UrlExclusion {

        /** 无限制 */
        private List<String> permitAll;

    }
}
