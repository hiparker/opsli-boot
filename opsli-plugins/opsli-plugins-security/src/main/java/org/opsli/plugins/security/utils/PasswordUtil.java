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
package org.opsli.plugins.security.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码工具类
 *
 * @author Parker
 * @date 2022-07-19 10:21 AM
 **/
public final class PasswordUtil {

    private static final String PREFIX = "TS{";
    private static final String SUFFIX = "}";

    /**
     * 加密密码
     *
     * @param passwordEncoder encoder
     * @param rawPassword 原始密码
     * @return String
     */
    public static String encode(PasswordEncoder passwordEncoder, String rawPassword){
        long timeMillis = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX).append(Base64.encode(String.valueOf(timeMillis))).append(SUFFIX)
                .append(passwordEncoder.encode(rawPassword));
        return sb.toString();
    }

    /**
     * 验证密码是否正确
     *
     * @param passwordEncoder encoder
     * @param rawPassword 原始密码
     * @param encodedPassword 加密密码
     * @return String
     */
    public static boolean matches(
            PasswordEncoder passwordEncoder, String rawPassword, String encodedPassword){

        // 获得原始密码
        String timeMillisStr = StrUtil.subBetween(encodedPassword, PREFIX, SUFFIX);
        String replaceEncodedPassword =
                encodedPassword.replace(PREFIX + timeMillisStr + SUFFIX, "");

        // 判断密码是否正确
        return passwordEncoder.matches(rawPassword, replaceEncodedPassword);
    }

    /**
     * 判断凭证是否过期
     *
     * @param credentials 原始密码
     * @param expiredDayCount 过期天数
     * @return String
     */
    public static boolean isCredentialsNonExpired(String credentials, int expiredDayCount){
        // 如果小于0 默认为不处理
        if(expiredDayCount < 0){
            return true;
        }

        // 获得原始密码
        String timeMillisStr = Base64.decodeStr(
                StrUtil.subBetween(credentials, PREFIX, SUFFIX));

        // 当前时间
        DateTime currDate = DateUtil.date();

        // 创建时间
        DateTime createDate = DateUtil.date(Long.parseLong(timeMillisStr));

        // 过期时间
        DateTime expiredDate = DateUtil.offsetDay(
                DateUtil.date(Long.parseLong(timeMillisStr)), expiredDayCount);

        // 如果 创建时间 大于 当前时间 || 过期时间 大于 当前时间 都判定凭证失效
        return DateUtil.compare(createDate, currDate) <= 0 &&
                DateUtil.compare(expiredDate, currDate) >= 0;
    }

    //private

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "Aa123456.";

        String encode = encode(passwordEncoder, rawPassword);
        System.out.println(encode);

        boolean isTrue = matches(passwordEncoder, rawPassword, encode);
        System.out.println(isTrue);

        boolean isNonExpired = isCredentialsNonExpired(encode, 1);
        System.out.println(isNonExpired);
    }

    private PasswordUtil(){}
}
