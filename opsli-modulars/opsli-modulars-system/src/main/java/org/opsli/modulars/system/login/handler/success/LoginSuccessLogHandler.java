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
import org.opsli.api.wrapper.system.logs.LoginLogsModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.enums.LoginFromEnum;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.logs.factory.UserLoginLogFactory;
import org.opsli.plugins.security.eventbus.SpringSecurityEventBus;
import org.opsli.plugins.security.handler.LoginAccessSuccessListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆成功日志执行器
 * @author Parker
 * @date 2022-07-17 12:57 PM
 **/
@AllArgsConstructor
@Component
public class LoginSuccessLogHandler implements LoginAccessSuccessListener {

    private final SpringSecurityEventBus springSecurityEventBus;

    @Override
    public void handle(
            Object model, Authentication authenticate,
            HttpServletRequest request, HttpServletResponse response) {
        // 1. 获取认证用户
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

        // 2. 获取认证用户其他信息
        UserModel userModel = UserUtil.getUserByUserName(userDetails.getUsername());
        if(null == userModel){
            return;
        }

        // 3. 生成登陆日志
        LoginLogsModel userLoginModel =
                UserLoginLogFactory.getUserLoginModel(request, userModel, true);

        // 设置登陆来源
        LoginFromEnum loginFrom = LoginFromEnum.getByBean(model);
        userLoginModel.setLoginFrom(loginFrom.getType());

        springSecurityEventBus.post(userLoginModel);
    }

}
