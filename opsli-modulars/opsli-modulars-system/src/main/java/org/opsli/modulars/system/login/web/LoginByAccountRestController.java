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
import org.opsli.common.annotation.Limiter;
import org.opsli.common.enums.AlertType;
import org.opsli.common.enums.LoginModelType;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.utils.CaptchaUtil;
import org.opsli.core.utils.ValidatorUtil;
import org.opsli.api.base.encrypt.EncryptModel;
import org.opsli.modulars.system.login.dto.LoginModel;
import org.opsli.modulars.system.login.handler.before.LoginModelVerifyCaptchaBeforeHandler;
import org.opsli.modulars.system.login.handler.before.LoginModelVerifyTempLockedBeforeHandler;
import org.opsli.modulars.system.login.handler.error.BizServiceErrorHandler;
import org.opsli.modulars.system.login.handler.success.*;
import org.opsli.core.utils.CryptoUtil;
import org.opsli.plugins.security.authentication.EmailPasswordAuthenticationToken;
import org.opsli.plugins.security.authentication.MobilePasswordAuthenticationToken;
import org.opsli.plugins.security.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 账号 + 密码 登录
 * 不需要继承 api 接口
 *
 * @author parker
 * @date 2020-05-23 13:30
 */
@Api(tags = "登录相关")
@Slf4j
@RestController
public class LoginByAccountRestController {

    private LoginHandler<LoginModel> loginHandler;

    /**
     * 验证码
     *
     * @param uuid 随机UUID
     */
    @Limiter(alertType = AlertType.ALERT)
    @ApiOperation(value = "验证码", notes = "验证码")
    @GetMapping("captcha")
    public void captcha(String uuid, HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        if(out != null){
            response.setHeader("Cache-Control", "no-store, no-cache");
            //生成图片验证码
            CaptchaUtil.createCaptcha(uuid, out);
        }
    }

    @Limiter
    @ApiOperation(value = "账号+密码 登录", notes = "账号+密码 登录")
    @PostMapping("/system/login")
    public void login(@RequestBody EncryptModel encryptModel){
        // 验证加密登录对象
        ValidatorUtil.verify(encryptModel);

        // 解密对象
        Object dataToObj = CryptoUtil
                .asymmetricDecryptToObj(encryptModel.getEncryptData());

        // 转换模型
        LoginModel loginModel = WrapperUtil.transformInstance(dataToObj, LoginModel.class);

        // 验证登录对象
        ValidatorUtil.verify(loginModel);

        loginHandler.login(loginModel,
                model -> {
                    String principal = loginModel.getPrincipal();
                    LoginModelType loginModelType = LoginModelType.getTypeByStr(principal);
                    Authentication authentication = null;
                    switch (loginModelType) {
                        case MOBILE:
                            authentication =
                                    new MobilePasswordAuthenticationToken(principal, model.getPassword());
                            break;
                        case EMAIL:
                            authentication =
                                    new EmailPasswordAuthenticationToken(principal, model.getPassword());
                            break;
                        case ACCOUNT:
                            authentication =
                                    new UsernamePasswordAuthenticationToken(principal, model.getPassword());
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
            LoginModelVerifyTempLockedBeforeHandler loginModelVerifyTempLockedBeforeHandler,
            LoginModelVerifyCaptchaBeforeHandler loginModelVerifyCaptchaBeforeHandler,
            LoginModelCreateAccessTokenHandler loginModelCreateAccessTokenHandler,
            LoginSuccessAfterVerifyHandler loginSuccessAfterVerifyHandler,
            LoginClearErrorSuccessHandler loginClearErrorSuccessHandler,
            LoginModelClearCaptchaSuccessHandler loginModelClearCaptchaSuccessHandler,
            LoginSuccessLogHandler loginSuccessLogHandler,
            AuthServiceErrorHandler authServiceErrorHandler,
            BizServiceErrorHandler bizServiceErrorHandler,
            OtherErrorHandler otherErrorHandler,
            AuthErrorHandler authErrorHandler,
            SecurityErrorHandler securityErrorHandler ){

        loginHandler = new LoginHandler.Builder<LoginModel>()
                .initAuthenticationManager(authenticationManager)
                .initLoginModelClass(LoginModel.class)
                // 前置处理器
                .before()
                    // 验证账号是否临时冻结
                    .addListener(loginModelVerifyTempLockedBeforeHandler)
                    // 超过登陆失败次数 需要校验验证码
                    .addListener(loginModelVerifyCaptchaBeforeHandler)
                .and()
                // 成功处理
                .accessSuccess()
                    // 返回 记录登录日志信息
                    .addListener(loginSuccessLogHandler)
                    // 清除错误日志
                    .addListener(loginClearErrorSuccessHandler)
                    // 清除对应验证码信息
                    .addListener(loginModelClearCaptchaSuccessHandler)
                    // 登陆成功后 验证用户其他相关信息
                    .addListener(loginSuccessAfterVerifyHandler)
                    // 返回 accessToken
                    .addListener(loginModelCreateAccessTokenHandler)
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
