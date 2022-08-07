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
package org.opsli.plugins.security;

import lombok.extern.slf4j.Slf4j;
import org.opsli.plugins.security.properties.AuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Redisson自动化配置
 *
 * @author Parker
 * @date 2019/6/19 下午11:55
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class SecurityConfig {

    /**
     * org.opsli.plugins.security.checker.DefaultPostAuthenticationChecks\
     *   org.opsli.plugins.security.checker.DefaultPreAuthenticationChecks\
     *   org.opsli.plugins.security.eventbus.SpringSecurityEventBus\
     *   org.opsli.plugins.security.handler.AuthErrorHandler\
     *   org.opsli.plugins.security.handler.AuthServiceErrorHandler\
     *   org.opsli.plugins.security.handler.OtherErrorHandler\
     *   org.opsli.plugins.security.handler.SecurityErrorHandler\
     *   org.opsli.plugins.security.provider.EmailCodeAuthenticationProvider\
     *   org.opsli.plugins.security.provider.EmailPasswordAuthenticationProvider\
     *   org.opsli.plugins.security.provider.MobileCodeAuthenticationProvider\
     *   org.opsli.plugins.security.provider.MobilePasswordAuthenticationProvider\
     *   org.opsli.plugins.security.provider.UsernamePasswordAuthenticationProvider\
     *   org.opsli.plugins.security.service.LoadUserDetailServiceFactory
      */

    /**
     * 密码解析器
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

