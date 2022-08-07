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
package org.opsli.core.filters.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.opsli.core.holder.UserContextHolder;
import org.opsli.core.utils.UserTokenUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户权限拦截器,支持自解析 jwt token
 *
 * @author Parker
 * @date 2021年12月22日16:35:20
 */
@Slf4j
public class UserAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler){

        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        try {
            String requestToken = UserTokenUtil.getRequestToken(request);
            UserContextHolder.setToken(requestToken);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
        // 上下文属性值清除，防止内存泄漏
        UserContextHolder.clear();
    }

}
