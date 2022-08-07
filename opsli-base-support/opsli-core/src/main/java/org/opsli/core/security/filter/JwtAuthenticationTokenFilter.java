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
package org.opsli.core.security.filter;

import lombok.AllArgsConstructor;
import org.opsli.core.base.dto.LoginUserDto;
import org.opsli.core.security.service.UidUserDetailDetailServiceImpl;
import org.opsli.core.utils.UserTokenUtil;
import org.opsli.plugins.security.authentication.AfterAuthenticationToken;
import org.opsli.plugins.security.exception.AuthException;
import org.opsli.plugins.security.exception.errorcode.AuthErrorCodeEnum;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 认证 拦截器
 * 注： 不要降 自定义filter 交给 Spring管理
 * @author Parker
 * @date 2022年07月22日16:16:42
 */
@AllArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final UidUserDetailDetailServiceImpl uidUserDetailDetailService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 获取token
        String token = UserTokenUtil.getRequestToken(request);
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }

        // 验证Token
        UserTokenUtil.verify(token);

        // 获得登陆用户信息
        LoginUserDto loginUserDto = UserTokenUtil.getLoginUserDto(token)
                // 认证无效
                .orElseThrow(() -> new AuthException(AuthErrorCodeEnum.AUTH_AUTH_INVALID));

        // 这里用Uid 获取用户信息，因为涉及到超管切换租户身份
        // 非 租户系统 可以直接使用 用户名获取信息
        UserDetails userDetails = uidUserDetailDetailService.loadUserByPrincipal(loginUserDto.getUid())
                // 认证无效
                .orElseThrow(() -> new AuthException(AuthErrorCodeEnum.AUTH_AUTH_INVALID));

        AfterAuthenticationToken authenticationToken =
                new AfterAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //放行
        filterChain.doFilter(request, response);
    }
}
