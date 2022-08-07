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

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.opsli.common.constants.RedisConstants;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.utils.UserTokenUtil;
import org.opsli.modulars.system.login.dto.LoginModel;
import org.opsli.common.enums.LoginModelType;
import org.opsli.plugins.redis.RedisPlugin;
import org.opsli.plugins.security.exception.AuthException;
import org.opsli.plugins.security.handler.LoginBeforeListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 验证账号临时锁定 (LoginModel)
 * @author Parker
 * @date 2022-07-17 12:57 PM
 **/
@AllArgsConstructor
@Component
public class LoginModelVerifyTempLockedBeforeHandler implements LoginBeforeListener {

    private final RedisPlugin redisPlugin;

    @Override
    public Class<?> getModelType() {
        return LoginModel.class;
    }

    @Override
    public void handle(Object model) {

        // 失败锁定时间
        Integer slipLockSpeed = UserTokenUtil.LOGIN_PROPERTIES.getSlipLockSpeed();

        LoginModel loginModel = (LoginModel) model;

        String principal = loginModel.getPrincipal();

        // 登陆类型
        LoginModelType loginModelType = LoginModelType.getTypeByStr(loginModel.getPrincipal());

        String key = CacheUtil.formatKey(RedisConstants.PREFIX_ACCOUNT_SLIP_LOCK
                + loginModelType.name().toLowerCase() + ":" + principal);

        // 判断账号是否临时锁定
        Long loseTimeMillis = Convert.toLong(redisPlugin.get(key));
        if(loseTimeMillis != null){
            Date currDate = DateUtil.date();
            DateTime loseDate = DateUtil.date(loseTimeMillis);
            // 偏移5分钟
            DateTime currLoseDate = DateUtil.offsetSecond(loseDate, slipLockSpeed);

            // 计算失效剩余时间( 分 )
            long betweenM = DateUtil.between(currLoseDate, currDate, DateUnit.MINUTE);
            if(betweenM > 0){
                String msg = StrUtil.format(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCK.getMessage()
                        ,betweenM + "分钟");
                throw new AuthException(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCK.getCode(), msg);
            }else{
                // 计算失效剩余时间( 秒 )
                long betweenS = DateUtil.between(currLoseDate, currDate, DateUnit.SECOND);
                String msg = StrUtil.format(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCK.getMessage()
                        ,betweenS + "秒");
                throw new AuthException(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCK.getCode(), msg);
            }
        }
    }

}
