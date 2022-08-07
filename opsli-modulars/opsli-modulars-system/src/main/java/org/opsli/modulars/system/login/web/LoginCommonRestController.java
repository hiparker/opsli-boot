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

import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.wrapper.system.logs.LoginLogsModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.annotation.Limiter;
import org.opsli.common.exception.TokenException;
import org.opsli.core.base.dto.LoginUserDto;
import org.opsli.core.holder.UserContextHolder;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.utils.UserTokenUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.logs.factory.UserLoginLogFactory;
import org.opsli.plugins.security.eventbus.SpringSecurityEventBus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 登陆 公共服务
 * 不需要继承 api 接口
 *
 * @author parker
 * @date 2020-05-23 13:30
 */
@Api(tags = "登录相关")
@Slf4j
@AllArgsConstructor
@RestController
public class LoginCommonRestController {

    private final SpringSecurityEventBus springSecurityEventBus;

    /**
     * 获得当前登录失败次数
     */
    @Limiter
    @ApiOperation(value = "获得当前登录失败次数", notes = "获得当前登录失败次数")
    @GetMapping("/system/slipCount")
    public ResultWrapper<?> slipCount(String username){
        // 获得当前失败次数
        long slipCount = UserTokenUtil.getSlipCount(username);
        Map<String, Object> ret = Maps.newHashMap();
        ret.put("base", UserTokenUtil.LOGIN_PROPERTIES.getSlipVerifyCount());
        ret.put("curr", slipCount);
        return ResultWrapper.getSuccessResultWrapper(ret);
    }


    /**
     * 登出
     */
    @Limiter
    @ApiOperation(value = "登出", notes = "登出")
    @PostMapping("/system/logout")
    public ResultWrapper<?> logout(HttpServletRequest request) {
        String token = UserContextHolder.getToken().orElseThrow(() -> new TokenException(
                TokenMsg.AUTH_CREDENTIALS_INVALID));

        LoginUserDto loginUserDto = UserTokenUtil.getLoginUserDto(token)
                .orElseThrow(() -> new TokenException(TokenMsg.AUTH_CREDENTIALS_INVALID));

        UserModel userModel = UserUtil.getUserByUserName(loginUserDto.getUsername());
        if(null == userModel){
            return ResultWrapper.getErrorResultWrapper();
        }

        // 异步记录信息
        LoginLogsModel userLoginModel = UserLoginLogFactory
                .getUserLoginModel(request, userModel, false);
        userLoginModel.setLoginFrom(loginUserDto.getLoginFrom());

        springSecurityEventBus.post(userLoginModel);

        UserTokenUtil.logout(token);

        return ResultWrapper
                .getSuccessResultWrapperByMsg(
                        TokenMsg.EXCEPTION_LOGOUT_SUCCESS.getMessage());
    }

}
