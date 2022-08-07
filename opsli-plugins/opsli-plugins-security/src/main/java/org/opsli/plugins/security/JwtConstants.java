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
package org.opsli.plugins.security;


/**
 * JWT 常量
 *
 * @author Parker
 * @date 2021年12月22日16:18:15
 */
public final class JwtConstants {

    /** 请求头的默认前缀 */
    public static final String TOKEN_HEAD = "Bearer ";
    /** jwt token 的认证头部 */
    public static final String TOKEN_HEADER = "T-Authorization";
    /** jwt 刷新token 的认证头部 */
    public static final String REFRESH_TOKEN_HEADER = "R-Authorization";
    /** FORM 表单头部 */
    public static final String FROM_HEADER = "x-from";

    public static final String JWT_CLAIM_TAG = "tag";
    public static final String JWT_CLAIM_KEY_ID = "uid";
    public static final String JWT_CLAIM_KEY_USER_NAME = "username";
    public static final String JWT_CLAIM_KEY_NICK_NAME = "nickname";
    public static final String JWT_CLAIM_KEY_PHONE = "phone";
    public static final String JWT_CLAIM_KEY_EMAIL = "email";
    public static final String JWT_CLAIM_KEY_LOGIN_FROM = "loginFrom";
    public static final String JWT_CLAIM_KEY_LOGIN_IP = "loginIp";

    public static final Integer JWT_SIGNATURE_DELAY = -5;

    private JwtConstants(){}
}
