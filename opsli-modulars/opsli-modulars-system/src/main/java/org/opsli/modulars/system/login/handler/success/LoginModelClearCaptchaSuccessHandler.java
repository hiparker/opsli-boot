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
package org.opsli.modulars.system.login.handler.success;

import lombok.AllArgsConstructor;
import org.opsli.core.utils.CaptchaUtil;
import org.opsli.core.utils.UserTokenUtil;
import org.opsli.modulars.system.login.dto.LoginModel;
import org.opsli.plugins.security.handler.LoginAccessSuccessListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆成功后 清除验证码信息 (LoginModel)
 * @author Parker
 * @date 2022-07-17 12:57 PM
 **/
@AllArgsConstructor
@Component
public class LoginModelClearCaptchaSuccessHandler implements LoginAccessSuccessListener {

    @Override
    public Class<?> getModelType() {
        return LoginModel.class;
    }

    @Override
    public void handle(
            Object model, Authentication authenticate,
            HttpServletRequest request, HttpServletResponse response) {

        LoginModel loginModel = (LoginModel) model;

        // 获得失败次数
        long slipCount = UserTokenUtil.getSlipCount(loginModel.getPrincipal());

        if(slipCount >= UserTokenUtil.LOGIN_PROPERTIES.getSlipVerifyCount()){
            // 删除验证过后验证码
            CaptchaUtil.delCaptcha(loginModel.getUuid());
        }
    }

}
