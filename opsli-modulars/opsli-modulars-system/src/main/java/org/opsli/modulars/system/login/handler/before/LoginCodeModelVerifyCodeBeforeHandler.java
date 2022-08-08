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
import org.opsli.common.enums.LoginModelType;
import org.opsli.common.enums.VerificationTypeEnum;
import org.opsli.core.utils.VerificationCodeUtil;
import org.opsli.modulars.system.login.dto.LoginCodeModel;
import org.opsli.plugins.security.handler.LoginBeforeListener;
import org.springframework.stereotype.Component;

/**
 * 验证账号与验证码 (LoginModel)
 * @author Parker
 * @date 2022-07-17 12:57 PM
 **/
@AllArgsConstructor
@Component
public class LoginCodeModelVerifyCodeBeforeHandler implements LoginBeforeListener {

    @Override
    public Class<?> getModelType() {
        return LoginCodeModel.class;
    }

    @Override
    public void handle(Object model) {
        LoginCodeModel loginCodeModel = (LoginCodeModel) model;

        String principal = loginCodeModel.getPrincipal();

        // 校验验证码
        LoginModelType loginModelType = LoginModelType.getTypeByStr(loginCodeModel.getPrincipal());
        if(LoginModelType.EMAIL == loginModelType){
            VerificationCodeUtil
                    .checkEmailCode(
                            principal, loginCodeModel.getVerificationCode(), VerificationTypeEnum.LOGIN.getType());
        }else if(LoginModelType.MOBILE == loginModelType){
            VerificationCodeUtil
                    .checkMobileCode(
                            principal, loginCodeModel.getVerificationCode(), VerificationTypeEnum.LOGIN.getType());
        }
    }

}
