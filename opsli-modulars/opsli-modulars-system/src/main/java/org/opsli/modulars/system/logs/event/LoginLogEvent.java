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
package org.opsli.modulars.system.logs.event;

import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.wrapper.system.logs.LoginLogsModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.logs.service.ILoginLogsService;
import org.opsli.modulars.system.user.service.IUserService;
import org.opsli.plugins.security.eventbus.ISecurityEventConsumer;
import org.springframework.stereotype.Component;

/**
 * 记录登陆日志信息
 *
 * @author Parker
 * @date 2022-07-20 10:37:58
 */
@Slf4j
@AllArgsConstructor
@Component
public class LoginLogEvent implements ISecurityEventConsumer<LoginLogsModel> {

	private final IUserService iUserService;
	private final ILoginLogsService iLoginLogsService;

	@Override
	@Subscribe
	public void consumer(LoginLogsModel userLoginModel) {
		//log.info("登陆用户信息 => {}", userLoginModel);
		UserModel userModel = UserUtil.getUserByUserName(userLoginModel.getUsername());
		if(null == userModel){
			return;
		}

		// 保存用户最后登录IP
		userModel.setLoginIp(userLoginModel.getRemoteAddr());
		iUserService.updateLoginIp(userModel);

		// 记录用户登录日志 如果系统较大 可考虑 Elastic 的 filebeat
		// 小系统 直接存在 mysql就好
		iLoginLogsService.insert(userLoginModel);
	}

}
