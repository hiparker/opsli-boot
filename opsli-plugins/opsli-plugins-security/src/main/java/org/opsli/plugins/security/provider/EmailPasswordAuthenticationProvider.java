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
package org.opsli.plugins.security.provider;

import lombok.AllArgsConstructor;
import org.opsli.plugins.security.authentication.EmailPasswordAuthenticationToken;
import org.opsli.plugins.security.checker.DefaultPostAuthenticationChecks;
import org.opsli.plugins.security.checker.DefaultPreAuthenticationChecks;
import org.opsli.plugins.security.eventbus.SpringSecurityEventBus;
import org.opsli.plugins.security.eventdto.BadCredentials;
import org.opsli.plugins.security.exception.AuthException;
import org.opsli.plugins.security.exception.AuthServiceException;
import org.opsli.plugins.security.exception.errorcode.AuthErrorCodeEnum;
import org.opsli.plugins.security.service.ILoadUserDetailService;
import org.opsli.plugins.security.service.LoadUserDetailServiceFactory;
import org.opsli.plugins.security.utils.PasswordUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * 邮箱+密码 验证器
 *
 * @author Pace
 * @date 2022年07月18日10:41:50
 */
@AllArgsConstructor
@Component
public class EmailPasswordAuthenticationProvider implements AuthenticationProvider,IAuthenticationProvider {

    private final LoadUserDetailServiceFactory loadUserDetailServiceFactory;
    private final DefaultPreAuthenticationChecks defaultPreAuthenticationChecks;
    private final DefaultPostAuthenticationChecks defaultPostAuthenticationChecks;
    private final PasswordEncoder passwordEncoder;
    private final SpringSecurityEventBus springSecurityEventBus;


    @Override
    public Class<? extends Authentication> getAuthenticationTokenClass() {
        return EmailPasswordAuthenticationToken.class;
    }

    /**
     * 指定所代理的 authenticationToken 类
     * 注：很重要要不然不生效
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return this.getAuthenticationTokenClass().isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication unAuthenticationToken) throws AuthenticationException {
        ILoadUserDetailService loadUserDetailService =
                loadUserDetailServiceFactory.getUserDetailService(this.getAuthenticationTokenClass())
                // ERROR => 未加载到 用户服务
                .orElseThrow(() -> new AuthServiceException(AuthErrorCodeEnum.AUTH_NOT_FIND_USER_SERVICE));

        // 原始密码
        String rawPassword = (String) unAuthenticationToken.getCredentials();

        // 此时的authentication还没认证，获取邮箱号码
        UserDetails user =
                loadUserDetailService.loadUserByPrincipal(unAuthenticationToken.getPrincipal())
                // ERROR => 未获取到用户
                .orElseThrow(() -> new AuthException(AuthErrorCodeEnum.AUTH_NOT_FIND_USER));

        // 校验密码是否正确
        boolean check = PasswordUtil.matches(passwordEncoder, rawPassword, user.getPassword());
        if(!check){
            // 发生 EventBus
            BadCredentials badCredentials = BadCredentials.builder()
                    .principal((String) unAuthenticationToken.getPrincipal())
                    .credentials(rawPassword)
                    .build();
            springSecurityEventBus.post(badCredentials);

            // ERROR => 密码不正确
            throw new BadCredentialsException(AuthErrorCodeEnum.AUTH_WRONG_PASSWORD.getMessage());
        }

        // 检查其他信息
        defaultPreAuthenticationChecks.check(user);
        defaultPostAuthenticationChecks.check(user);

        return createSuccessAuthentication(unAuthenticationToken, user);
    }

}
