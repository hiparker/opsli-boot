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
package org.opsli.modulars.system.login.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 登录 Token 传输实体
 *
 * @author Parker
 * @since 2021-12-27
 */
@Getter
@Setter
@Builder
public class AuthAccessAndRefreshTokenDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 刷新Token */
    private String refreshToken;

    /** 认证Token */
    private String accessToken;

    /** 失效时间戳 */
    private Long expiresAtTs;

}
