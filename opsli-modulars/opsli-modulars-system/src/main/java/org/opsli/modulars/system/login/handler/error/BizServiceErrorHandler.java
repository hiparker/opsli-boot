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
package org.opsli.modulars.system.login.handler.error;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.exception.ServiceException;
import org.opsli.plugins.security.handler.LoginAccessDeniedListener;
import org.opsli.plugins.security.utils.WebUtils;
import org.opsli.plugins.security.vo.AuthResultWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 业务异常 处理器
 * @author Parker
 * @date 2022-07-17 12:57 PM
 **/
@Slf4j
@AllArgsConstructor
@Component
public class BizServiceErrorHandler implements LoginAccessDeniedListener {

    @Override
    public boolean handle(Object loginModel, HttpServletRequest request, HttpServletResponse response, Exception e) {
        if(!(e instanceof ServiceException)){
            return true;
        }

        ServiceException se = (ServiceException) e;

        Integer code = se.getCode();
        String errorMessage = se.getErrorMessage();

        // 记录告警日志
        log.warn("认证服务异常（ServiceException） => 认证信息：{}  异常编码：{} 异常：{}",
                JSONUtil.toJsonStr(loginModel),
                code,
                errorMessage);

        AuthResultWrapper<?> resultVo =
                AuthResultWrapper.getCustomResultWrapper(code, errorMessage);

        WebUtils.renderString(request, response, JSONUtil.toJsonStr(resultVo));

        return false;
    }

}
