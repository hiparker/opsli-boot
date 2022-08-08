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
package org.opsli.plugins.security.exception.handler;

import cn.hutool.json.JSONUtil;
import org.opsli.plugins.security.exception.AuthException;
import org.opsli.plugins.security.utils.WebUtils;
import org.opsli.plugins.security.vo.AuthResultWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 内部认证 异常处理
 *
 * @author Parker
 * @date 2022年07月22日16:31:16
 */
@Component
public class AuthEntryHandlerImpl implements AuthEntryHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AuthException authException) throws IOException, ServletException {
        Integer code = authException.getCode();
        String errorMessage = authException.getErrorMessage();

        // 权限不足
        AuthResultWrapper<?> customResultWrapper =
                AuthResultWrapper.getCustomResultWrapper(code, errorMessage);

        WebUtils.renderString(request, response, JSONUtil.toJsonStr(customResultWrapper));
    }
}
