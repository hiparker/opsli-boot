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
package org.opsli.core.autoconfigure.conf;

import cn.hutool.extra.spring.SpringUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.opsli.core.security.filter.JwtAuthenticationTokenFilter;
import org.opsli.core.security.service.UidUserDetailDetailServiceImpl;
import org.opsli.plugins.security.exception.handler.AccessDeniedHandlerImpl;
import org.opsli.plugins.security.exception.handler.AuthenticationEntryPointImpl;
import org.opsli.plugins.security.properties.AuthProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Map;

/**
 * Security 配置
 *
 * @author Pace
 * @date  2025年05月31日17:02:33
 **/
@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthProperties authProperties;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final UidUserDetailDetailServiceImpl uidUserDetailDetailService;

    /**
     * 配置安全过滤器链
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 配置请求头
                .headers(headers -> headers
                        // 默认关闭 Security 的 xss防护，与系统本身的 xss防护冲突
                        .httpStrictTransportSecurity(HeadersConfigurer.HstsConfig::disable)
                        // 允许Iframe加载
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                // 关闭csrf token认证不需要csrf防护
                .csrf(AbstractHttpConfigurer::disable)
                // 关闭Session会话管理器 JWT 不需要
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 关闭记住我功能 JWT 不需要
                .rememberMe(AbstractHttpConfigurer::disable);

        // 初始化 授权请求配置
        this.initAuthorizeRequests(http);

        return http.build();
    }

    /**
     * 设置 认证相关 URL
     * @param http http
     */
    private void initAuthorizeRequests(HttpSecurity http) throws Exception {
        // 设置URL白名单
        List<String> permitAll = authProperties.getUrlExclusion().getPermitAll();
        http.authorizeHttpRequests(authz -> {
            // 处理其他白名单路径
            if (permitAll != null && !permitAll.isEmpty()) {
                String[] urlArray = permitAll.toArray(new String[0]);
                authz.requestMatchers(urlArray).permitAll();
            }

            // 其他请求需要认证
            authz.anyRequest().authenticated();
        });

        // 添加过滤器
        // 注意 自定义 Filter 不要交给 Spring管理
        http.addFilterBefore(new JwtAuthenticationTokenFilter(uidUserDetailDetailService),
                UsernamePasswordAuthenticationFilter.class);

        // 异常处理
        http.exceptionHandling(exceptions -> exceptions
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
        );
    }

    /**
     * 主要的认证管理器 - 使用自定义的多Provider实现
     * 移除了默认的 AuthenticationManager Bean，避免循环依赖
     */
    @Bean
    @Primary
    public AuthenticationManager authenticationManager() {
        // 延迟获取 AuthenticationProvider，避免循环依赖
        return new LazyAuthenticationManager();
    }

    /**
     * 延迟初始化的认证管理器，避免循环依赖
     */
    private static class LazyAuthenticationManager implements AuthenticationManager {

        private volatile AuthenticationManager delegate;

        @Override
        public org.springframework.security.core.Authentication authenticate(
                org.springframework.security.core.Authentication authentication)
                throws org.springframework.security.core.AuthenticationException {

            if (delegate == null) {
                synchronized (this) {
                    if (delegate == null) {
                        delegate = createAuthenticationManager();
                    }
                }
            }
            return delegate.authenticate(authentication);
        }

        private AuthenticationManager createAuthenticationManager() {
            // 获取所有的 AuthenticationProvider
            Map<String, AuthenticationProvider> providerMap =
                    SpringUtil.getBeansOfType(AuthenticationProvider.class);

            List<AuthenticationProvider> authenticationProviderList =
                    Lists.newArrayListWithCapacity(providerMap.size());

            for (Map.Entry<String, AuthenticationProvider> providerEntry : providerMap.entrySet()) {
                authenticationProviderList.add(providerEntry.getValue());
            }

            // 如果没有自定义Provider，使用默认配置
            if (authenticationProviderList.isEmpty()) {
                try {
                    AuthenticationConfiguration authConfig =
                            SpringUtil.getBean(AuthenticationConfiguration.class);
                    return authConfig.getAuthenticationManager();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create default AuthenticationManager", e);
                }
            }

            ProviderManager authenticationManager = new ProviderManager(authenticationProviderList);
            // 不擦除认证密码，擦除会导致TokenBasedRememberMeServices因为找不到Credentials再调用UserDetailsService而抛出UsernameNotFoundException
            authenticationManager.setEraseCredentialsAfterAuthentication(false);
            return authenticationManager;
        }
    }
}
