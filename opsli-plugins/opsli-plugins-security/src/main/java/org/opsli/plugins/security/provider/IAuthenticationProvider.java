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
package org.opsli.plugins.security.provider;

import org.opsli.plugins.security.authentication.AfterAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 认证授权器
 *
 * @author Parker
 * @date 2022-07-18 11:18 AM
 **/
public interface IAuthenticationProvider {

    /**
     * 获得授权TokenClass
     * @return Class<? extends Authentication>
     */
    Class<? extends Authentication> getAuthenticationTokenClass();

    /**
     * 创建认证成功授权
     * @param unAuthenticationToken 授权前 AuthenticationToken
     * @param user 用户信息
     * @return Authentication
     */
    default Authentication createSuccessAuthentication(Authentication unAuthenticationToken, UserDetails user) {
        AfterAuthenticationToken result =
                new AfterAuthenticationToken(user,
                        unAuthenticationToken.getCredentials(), user.getAuthorities());
        result.setDetails(unAuthenticationToken.getDetails());
        return result;
    }

}
