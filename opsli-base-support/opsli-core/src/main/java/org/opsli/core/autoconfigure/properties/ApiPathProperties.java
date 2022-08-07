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
package org.opsli.core.autoconfigure.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 接口路径前缀配置
 *
 * @author Parker
 * @date 2020-09-15
 */
@Configuration
@ConfigurationProperties(prefix = ApiPathProperties.PROP_PREFIX)
@Data
@EqualsAndHashCode(callSuper = false)
public class ApiPathProperties {

    public static final String PROP_PREFIX = "server.servlet.api.path";

    /** 专门针对 Controller层接口路径前缀全局配置 */
    private String globalPrefix;

}
