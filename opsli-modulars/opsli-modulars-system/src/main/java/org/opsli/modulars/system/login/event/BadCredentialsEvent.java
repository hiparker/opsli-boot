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
package org.opsli.modulars.system.login.event;

import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.constants.RedisConstants;
import org.opsli.common.enums.LoginModelType;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.utils.UserTokenUtil;
import org.opsli.plugins.redis.RedisPlugin;
import org.opsli.plugins.security.eventbus.ISecurityEventConsumer;
import org.opsli.plugins.security.eventdto.BadCredentials;
import org.springframework.stereotype.Component;

/**
 * 登陆凭证错误事件
 *
 * @author Parker
 * @date 2022-07-20 10:37:58
 */
@Slf4j
@AllArgsConstructor
@Component
public class BadCredentialsEvent implements ISecurityEventConsumer<BadCredentials> {

	private final RedisPlugin redisPlugin;

	/**
	 * 如果 选择凭证模式登陆 且失败的情况下 超过一定次数 冻结一段时间账号
	 * @param badCredentials 事件
	 */
	@Override
	@Subscribe
	public void consumer(BadCredentials badCredentials) {
		// 失败锁定时间
		Integer slipLockSpeed = UserTokenUtil.LOGIN_PROPERTIES.getSlipLockSpeed();
		// 失败次数
		Integer slipCount = UserTokenUtil.LOGIN_PROPERTIES.getSlipCount();

		// 登陆类型
		LoginModelType loginModelType =
				LoginModelType.getTypeByStr(badCredentials.getPrincipal());

		// 锁定数量 缓存Key
		String countKey = CacheUtil.formatKey(RedisConstants.PREFIX_ACCOUNT_SLIP_COUNT
				+ loginModelType.name().toLowerCase() + ":" + badCredentials.getPrincipal());
		// 锁定 缓存Key
		String localKey = CacheUtil.formatKey(RedisConstants.PREFIX_ACCOUNT_SLIP_LOCK
				+ loginModelType.name().toLowerCase() + ":" + badCredentials.getPrincipal());

		// 如果失败次数 超过阈值 则锁定账号
		Long slipNum = redisPlugin.increment(countKey);
		if (slipNum != null){
			// 设置失效时间为 5分钟
			redisPlugin.expire(countKey, slipLockSpeed);

			// 如果确认 都失败 则存入临时缓存
			if(slipNum >= slipCount){
				long currentTimeMillis = System.currentTimeMillis();
				// 存入Redis
				redisPlugin.put(localKey, currentTimeMillis, slipLockSpeed);
			}
		}
	}

}
