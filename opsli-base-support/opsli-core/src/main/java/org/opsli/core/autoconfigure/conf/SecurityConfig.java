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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Map;


/**
 * Security 配置
 *
 * @author Parker
 * @date  2022年07月14日12:57:33
 **/
@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthProperties authProperties;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final UidUserDetailDetailServiceImpl uidUserDetailDetailService;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .headers()
                // 默认关闭 Security 的 xss防护，与系统本身的 xss防护冲突
                .xssProtection().disable()
                // 允许Iframe加载
                .frameOptions().disable()
            .and()
            // 关闭csrf token认证不需要csrf防护
            .csrf().disable()
            // 关闭Session会话管理器 JWT 不需要
            .sessionManagement().disable()
            // 关闭记住我功能 JWT 不需要
            .rememberMe().disable();

        // 初始化 initAuthorizeRequests
        this.initAuthorizeRequests(http);
    }

    /**
     * 设置 认证相关 URL
     * @param http http
     */
    private void initAuthorizeRequests(HttpSecurity http) throws Exception {
        // 设置URL白名单
        List<String> permitAll = authProperties.getUrlExclusion().getPermitAll();
        if(null != permitAll){
            String[] urlExclusionArray = permitAll.toArray(new String[0]);
            http.authorizeRequests()
                    // URL 白名单
                    .antMatchers(urlExclusionArray).permitAll();
        }

        // 除上面外的所有请求全部需要鉴权认证
        http.authorizeRequests()
                .anyRequest().authenticated();

        // 添加过滤器
        // 注意 自定义 Filter 不要交给 Spring管理
        http.addFilterBefore(new JwtAuthenticationTokenFilter(uidUserDetailDetailService),
                UsernamePasswordAuthenticationFilter.class);



        // 异常处理
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);
    }

    /**
     * 认证管理器
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected AuthenticationManager authenticationManager() {
        // 设置 多Provider
        Map<String, AuthenticationProvider> providerMap =
                SpringUtil.getBeansOfType(AuthenticationProvider.class);
        List<AuthenticationProvider> authenticationProviderList =
                Lists.newArrayListWithCapacity(providerMap.size());
        for (Map.Entry<String, AuthenticationProvider> providerEntry : providerMap.entrySet()) {
            authenticationProviderList.add(providerEntry.getValue());
        }

        ProviderManager authenticationManager = new ProviderManager(authenticationProviderList);
        //不擦除认证密码，擦除会导致TokenBasedRememberMeServices因为找不到Credentials再调用UserDetailsService而抛出UsernameNotFoundException
        authenticationManager.setEraseCredentialsAfterAuthentication(false);
        return authenticationManager;
    }

}
