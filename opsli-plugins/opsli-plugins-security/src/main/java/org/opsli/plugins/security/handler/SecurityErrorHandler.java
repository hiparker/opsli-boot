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
package org.opsli.plugins.security.handler;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opsli.plugins.security.exception.errorcode.AuthErrorCodeEnum;
import org.opsli.plugins.security.utils.WebUtils;
import org.opsli.plugins.security.vo.AuthResultWrapper;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Security认证异常 处理器
 * @author Parker
 * @date 2022-07-17 12:57 PM
 **/
@Slf4j
@AllArgsConstructor
@Component
public class SecurityErrorHandler implements LoginAccessDeniedListener {

    @Override
    public boolean handle(Object loginModel, HttpServletRequest request, HttpServletResponse response, Exception e) {
        if(e instanceof BadCredentialsException){
            // 凭证不正确
            AuthResultWrapper<?> customResultWrapper =
                    AuthResultWrapper.getCustomResultWrapper(AuthErrorCodeEnum.AUTH_WRONG_PASSWORD);
            WebUtils.renderString(request, response, JSONUtil.toJsonStr(customResultWrapper));
            return false;
        }else if(e instanceof LockedException){
            // 账号锁定
            AuthResultWrapper<?> customResultWrapper =
                    AuthResultWrapper.getCustomResultWrapper(AuthErrorCodeEnum.AUTH_ACCOUNT_LOCKED);
            WebUtils.renderString(request, response, JSONUtil.toJsonStr(customResultWrapper));
            return false;
        }else if(e instanceof DisabledException){
            // 账号未启用
            AuthResultWrapper<?> customResultWrapper =
                    AuthResultWrapper.getCustomResultWrapper(AuthErrorCodeEnum.AUTH_ACCOUNT_DISABLED);
            WebUtils.renderString(request, response, JSONUtil.toJsonStr(customResultWrapper));
            return false;
        }else if(e instanceof AccountExpiredException){
            // 账号过期
            AuthResultWrapper<?> customResultWrapper =
                    AuthResultWrapper.getCustomResultWrapper(AuthErrorCodeEnum.AUTH_ACCOUNT_EXPIRED);
            WebUtils.renderString(request, response, JSONUtil.toJsonStr(customResultWrapper));
            return false;
        }else if(e instanceof CredentialsExpiredException){
            // 凭证过期
            AuthResultWrapper<?> customResultWrapper =
                    AuthResultWrapper.getCustomResultWrapper(AuthErrorCodeEnum.AUTH_ACCOUNT_CREDENTIALS_EXPIRED);
            WebUtils.renderString(request, response, JSONUtil.toJsonStr(customResultWrapper));
            return false;
        }

        return true;
    }

}
