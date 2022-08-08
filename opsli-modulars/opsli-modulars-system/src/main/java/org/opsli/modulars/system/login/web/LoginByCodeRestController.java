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
package org.opsli.modulars.system.login.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.encrypt.EncryptModel;
import org.opsli.common.annotation.Limiter;
import org.opsli.common.enums.LoginModelType;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.utils.ValidatorUtil;
import org.opsli.modulars.system.login.dto.LoginCodeModel;
import org.opsli.modulars.system.login.handler.before.LoginCodeModelVerifyCodeBeforeHandler;
import org.opsli.modulars.system.login.handler.error.BizServiceErrorHandler;
import org.opsli.modulars.system.login.handler.success.LoginClearErrorSuccessHandler;
import org.opsli.modulars.system.login.handler.success.LoginCodeModelCreateAccessTokenHandler;
import org.opsli.modulars.system.login.handler.success.LoginSuccessAfterVerifyHandler;
import org.opsli.modulars.system.login.handler.success.LoginSuccessLogHandler;
import org.opsli.core.utils.CryptoUtil;
import org.opsli.plugins.security.authentication.EmailCodeAuthenticationToken;
import org.opsli.plugins.security.authentication.MobileCodeAuthenticationToken;
import org.opsli.plugins.security.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 手机号/邮箱+验证码 登录
 * 不需要继承 api 接口
 *
 * @author parker
 * @date 2020-05-23 13:30
 */
@Api(tags = "登录相关")
@Slf4j
@RestController
public class LoginByCodeRestController {

    private LoginHandler<LoginCodeModel> loginHandler;


    @Limiter
    @ApiOperation(value = "手机号/邮箱+验证码 登录", notes = "手机号/邮箱+验证码 登录")
    @PostMapping("/system/login-by-code")
    public void login(@RequestBody EncryptModel encryptModel){
        // 验证加密登录对象
        ValidatorUtil.verify(encryptModel);

        // 解密对象
        Object dataToObj = CryptoUtil
                .asymmetricDecryptToObj(encryptModel.getEncryptData());

        // 转换模型
        LoginCodeModel loginCodeModel = WrapperUtil.transformInstance(dataToObj, LoginCodeModel.class);

        // 验证登录对象
        ValidatorUtil.verify(loginCodeModel);

        loginHandler.login(loginCodeModel,
                model -> {
                    String principal = loginCodeModel.getPrincipal();
                    LoginModelType loginModelType = LoginModelType.getTypeByStr(principal);
                    Authentication authentication = null;
                    switch (loginModelType) {
                        case MOBILE:
                            authentication =
                                    new MobileCodeAuthenticationToken(principal, model.getVerificationCode());
                            break;
                        case EMAIL:
                            authentication =
                                    new EmailCodeAuthenticationToken(principal, model.getVerificationCode());
                            break;
                        default:
                            break;
                    }
                    return authentication;
                }
        );
    }

    /**
     * 初始化
     */
    @Autowired
    public void init(
            AuthenticationManager authenticationManager,
            LoginCodeModelVerifyCodeBeforeHandler loginCodeModelVerifyCodeBeforeHandler,
            LoginCodeModelCreateAccessTokenHandler loginCodeModelCreateAccessTokenHandler,
            LoginSuccessAfterVerifyHandler loginSuccessAfterVerifyHandler,
            LoginClearErrorSuccessHandler loginClearErrorSuccessHandler,
            LoginSuccessLogHandler loginSuccessLogHandler,
            AuthServiceErrorHandler authServiceErrorHandler,
            BizServiceErrorHandler bizServiceErrorHandler,
            OtherErrorHandler otherErrorHandler,
            AuthErrorHandler authErrorHandler,
            SecurityErrorHandler securityErrorHandler ){

        loginHandler = new LoginHandler.Builder<LoginCodeModel>()
                .initAuthenticationManager(authenticationManager)
                .initLoginModelClass(LoginCodeModel.class)
                // 前置处理器
                .before()
                    // 校验验证码
                    .addListener(loginCodeModelVerifyCodeBeforeHandler)
                .and()
                // 成功处理
                .accessSuccess()
                    // 返回 记录登录日志信息
                    .addListener(loginSuccessLogHandler)
                    // 清除错误日志
                    .addListener(loginClearErrorSuccessHandler)
                    // 登陆成功后 验证用户其他相关信息
                    .addListener(loginSuccessAfterVerifyHandler)
                    // 返回 accessToken
                    .addListener(loginCodeModelCreateAccessTokenHandler)
                .and()
                // 异常处理
                .accessDenied()
                    // 增加认证服务异常处理器
                    .addListener(authServiceErrorHandler)
                    // 增加认证异常处理器
                    .addListener(authErrorHandler)
                    // 增加账户认证异常处理器
                    .addListener(securityErrorHandler)
                    // 增加内部业务异常处理器
                    .addListener(bizServiceErrorHandler)
                    // 增加其他未捕获异常处理器
                    .addListener(otherErrorHandler)
                .and()
                .build();
    }

}
