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
package org.opsli.modulars.system.login.handler.before;

import lombok.AllArgsConstructor;
import org.opsli.core.utils.CaptchaUtil;
import org.opsli.core.utils.UserTokenUtil;
import org.opsli.modulars.system.login.dto.LoginModel;
import org.opsli.plugins.security.handler.LoginBeforeListener;
import org.springframework.stereotype.Component;

/**
 * 验证账号与验证码 (LoginModel)
 * @author Parker
 * @date 2022-07-17 12:57 PM
 **/
@AllArgsConstructor
@Component
public class LoginModelVerifyCaptchaBeforeHandler implements LoginBeforeListener {

    @Override
    public Class<?> getModelType() {
        return LoginModel.class;
    }

    @Override
    public void handle(Object model) {
        LoginModel loginModel = (LoginModel) model;

        // 获得失败次数
        long slipCount = UserTokenUtil.getSlipCount(loginModel.getPrincipal());

        // 失败次数超过 验证次数阈值 开启验证码验证
        if(slipCount >= UserTokenUtil.LOGIN_PROPERTIES.getSlipVerifyCount()){
            CaptchaUtil.validate(loginModel.getUuid(), loginModel.getVerificationCode());
        }
    }

}
