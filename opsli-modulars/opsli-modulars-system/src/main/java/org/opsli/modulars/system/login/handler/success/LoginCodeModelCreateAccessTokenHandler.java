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

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.core.base.dto.LoginUserDto;
import org.opsli.core.utils.JWTBizUtil;
import org.opsli.core.utils.UserTokenUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.login.dto.LoginCodeModel;
import org.opsli.core.utils.CryptoUtil;
import org.opsli.modulars.system.login.vo.AuthAccessTokenDto;
import org.opsli.plugins.security.exception.AuthException;
import org.opsli.plugins.security.exception.errorcode.AuthErrorCodeEnum;
import org.opsli.plugins.security.handler.LoginAccessSuccessListener;
import org.opsli.plugins.security.utils.IpaddrUtil;
import org.opsli.plugins.security.utils.WebUtils;
import org.opsli.plugins.security.vo.AuthResultWrapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 生成认证 Token (LoginModel)
 * @author Parker
 * @date 2022-07-17 12:57 PM
 **/
@AllArgsConstructor
@Component
public class LoginCodeModelCreateAccessTokenHandler implements LoginAccessSuccessListener {

    @Override
    public Class<?> getModelType() {
        return LoginCodeModel.class;
    }

    @Override
    public void handle(
            Object model, Authentication authenticate,
            HttpServletRequest request, HttpServletResponse response) {
        LoginCodeModel loginModel = (LoginCodeModel) model;

        // 1. 获取认证用户
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

        // 2. 获取认证用户其他信息
        UserModel userModel = UserUtil.getUserByUserName(userDetails.getUsername());
        if(null == userModel){
            throw new AuthException(AuthErrorCodeEnum.AUTH_AUTH_INVALID);
        }

        LoginUserDto loginUser = LoginUserDto.builder()
                .uid(userModel.getId())
                .tenantId(userModel.getTenantId())
                .username(userDetails.getUsername())
                .nickname(userModel.getRealName())
                .loginIp(IpaddrUtil.getClientIdBySingle(request))
                .loginFrom(loginModel.getLoginFrom())
                .mobile(userModel.getMobile())
                .email(userModel.getUsername())
                .build();

        String accessToken = UserTokenUtil.createAccessToken(loginUser);

        // 数据传输DTO
        AuthAccessTokenDto accessTokenDto = AuthAccessTokenDto.builder()
                .accessToken(accessToken)
                .expiresAtTs(JWTBizUtil.getExpiredDateFromToken(accessToken).getTime())
                .build();

        // 对称加密数据
        String symmetricEncryptToStr = CryptoUtil.symmetricEncryptToStr(accessTokenDto);

        AuthResultWrapper<String> successResultWrapper =
                AuthResultWrapper.getSuccessResultWrapper(symmetricEncryptToStr);

        WebUtils.renderString(request, response, JSONUtil.toJsonStr(successResultWrapper));
    }

}
