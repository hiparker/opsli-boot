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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.opsli.plugins.security.JwtConstants;
import org.opsli.plugins.security.LoginUserInfo;

import java.util.Date;
import java.util.Map;

/**
 * JWT 的工具类：包含了创建和解码的工具
 *
 * @author Parker
 * @date 2021年12月22日20:01:23
 */
public final class JWTUtil {

    /**
     * 刷新 Token
     *
     * @param userInfo      用户信息
     * @param secret        盐
     * @param expireMinutes 过期时间,单位分钟
     * @return String
     */
    public static String generateRefreshToken(LoginUserInfo userInfo, String secret, Integer expireMinutes) {
        Map<String, Object> payloadsMap = BeanUtil.beanToMap(userInfo);
        payloadsMap.put(JwtConstants.JWT_CLAIM_TAG, Tag.REFRESH_TOKEN.getTag());

        // 生成新Token
        return generate(payloadsMap, secret, expireMinutes);
    }

    /**
     * 认证 Token
     *
     * @param refreshToken  刷新Token
     * @param secret        盐
     * @param expireMinutes 过期时间,单位分钟
     * @return String
     */
    public static String generateAuthToken(String refreshToken, String secret, Integer expireMinutes) {
        // 验证 刷新Token 是否失效
        verify(refreshToken, secret);

        // 获得 载荷数据
        LoginUserInfo loginUserFromToken = getLoginUserFromToken(refreshToken);
        Map<String, Object> payloadsMap = BeanUtil.beanToMap(loginUserFromToken);
        payloadsMap.put(JwtConstants.JWT_CLAIM_TAG, Tag.TOKEN.getTag());

        // 生成新Token
        return generate(payloadsMap, secret, expireMinutes);
    }

    /**
     * 认证 Token
     *
     * @param userInfo      用户信息
     * @param secret        盐
     * @param expireMinutes 过期时间,单位分钟
     * @return String
     */
    public static String generateAuthToken(LoginUserInfo userInfo, String secret, Integer expireMinutes) {
        Map<String, Object> payloadsMap = BeanUtil.beanToMap(userInfo);
        payloadsMap.put(JwtConstants.JWT_CLAIM_TAG, Tag.TOKEN.getTag());

        // 生成新Token
        return generate(payloadsMap, secret, expireMinutes);
    }


    /**
     * 生成Token
     *
     * @param payloadsMap   载荷数据
     * @param secret        盐
     * @param expireMinutes 过期时间,单位分钟
     * @return String
     */
    private static String generate(Map<String, Object> payloadsMap, String secret, Integer expireMinutes) {
        byte[] keys = Base64.encode(secret).getBytes();
        JWTSigner jwtSigner = JWTSignerUtil.hs256(keys);

        // 签发时间
        Date issuedAt = DateUtil.date();

        return JWT.create()
                // 载荷数据
                .addPayloads(payloadsMap)
                // 签发时间
                .setIssuedAt(DateUtil.offsetMinute(issuedAt, JwtConstants.JWT_SIGNATURE_DELAY))
                // 过期时间
                .setExpiresAt(DateUtil.offsetMinute(issuedAt, expireMinutes))
                // 签名
                .sign(jwtSigner);
    }


    /**
     * 校验Token 是否正确
     *
     * @param token  Token
     * @param secret 盐
     */
    public static void verify(String token, String secret) {
        byte[] keys = Base64.encode(secret).getBytes();
        JWTSigner jwtSigner = JWTSignerUtil.hs256(keys);

        JWTValidator validator = JWTValidator.of(token);
        try {
            // 验证签名
            validator.validateAlgorithm(jwtSigner);
        } catch (Exception e) {
            throw new ValidateException(100524, "Token无效，请重新登录");
        }

        try {
            // 验证时间
            validator.validateDate();
        } catch (Exception e) {
            throw new ValidateException(100525, "Token已过期，请重新登录", e);
        }
    }

    /**
     * 获得登陆用户信息
     *
     * @param token Token
     */
    public static LoginUserInfo getLoginUserFromToken(String token) {
        JSONObject payloads = JWT.of(token)
                .getPayloads();
        // 移除标识
        payloads.remove(JwtConstants.JWT_CLAIM_TAG);
        return payloads.toBean(LoginUserInfo.class);
    }

    /**
     * 获得登陆用户信息 字符串
     *
     * @param token Token
     */
    public static String getLoginUserStrFromToken(String token) {
        JSONObject payloads = JWT.of(token)
                .getPayloads();
        // 移除标识
        payloads.remove(JwtConstants.JWT_CLAIM_TAG);
        return payloads.toString();
    }

    /**
     * 获得Token的签名时间
     *
     * @param token Token
     */
    public static Date getIssuedDateFromToken(String token) {
        // 获得失效时间
        return JWT.of(token).getPayload()
                .getClaimsJson().getDate(JWTPayload.ISSUED_AT);
    }

    /**
     * 获得Token的失效时间
     *
     * @param token Token
     */
    public static Date getExpiredDateFromToken(String token) {
        // 获得失效时间
        return JWT.of(token).getPayload()
                .getClaimsJson().getDate(JWTPayload.EXPIRES_AT);
    }

    /**
     * 判断是否是刷新Token
     *
     * @param token Token
     */
    public static boolean isRefreshToken(String token) {
        String tag = Tag.TOKEN.getTag();
        try {
            tag = JWT.of(token).getPayload()
                    .getClaimsJson().getStr(JwtConstants.JWT_CLAIM_TAG);
        } catch (Exception ignored) {
        }
        return Tag.REFRESH_TOKEN.getTag().equals(tag);
    }

    /**
     * 标签
     */
    private enum Tag {

        /**
         * 刷新Token
         */
        REFRESH_TOKEN("0"),
        /**
         * 认证Token
         */
        TOKEN("1");

        private final String tag;

        Tag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }


    /**
     * 私有化构造函数
     */
    private JWTUtil() {}


    public static void main(String[] args) {
        String secret = "8128212";

        LoginUserInfo loginUserInfo = new LoginUserInfo();
        loginUserInfo.setLoginIp("127.0.0.1");
        loginUserInfo.setMobile("18888888888");
        loginUserInfo.setEmail("meet.parker@foxmail.com");
        loginUserInfo.setUsername("u_111111");
        loginUserInfo.setNickname("Parker");
        loginUserInfo.setUid("12321321L");

        // 刷新Token 一个月失效
        String refreshToken = JWTUtil.generateRefreshToken(loginUserInfo, secret, 43200);
        // 认证Token 两小时失效
        String fastToken = JWTUtil.generateAuthToken(refreshToken, secret, 120);
        // 认证Token 十年失效
        String slowToken = JWTUtil.generateAuthToken(refreshToken, secret, 5256000);
//        for (int i = 0; i < 100; i++) {
//            long l = System.currentTimeMillis();
//            token = JwtUtil.generate(loginUserInfo, secret, 3);
//            System.out.println("普通Token耗时：" + (System.currentTimeMillis() - l));
//        }

        System.out.println("刷新Token 一个月失效");
        System.out.println(JwtConstants.TOKEN_HEAD + refreshToken);
        System.out.println("");
        System.out.println("认证Token 两小时失效");
        System.out.println(JwtConstants.TOKEN_HEAD + fastToken);
        System.out.println("");
        System.out.println("认证Token 十年失效");
        System.out.println(JwtConstants.TOKEN_HEAD + slowToken);
        System.out.println("");


        LoginUserInfo loginUserByToken = JWTUtil.getLoginUserFromToken(refreshToken);
        System.out.println(JSONUtil.toJsonStr(loginUserByToken));

        Date date = JWTUtil.getExpiredDateFromToken(refreshToken);
        System.out.println(DateUtil.formatDateTime(date));

        // 刷新Token
//        for (int i = 0; i < 100; i++) {
//            long l = System.currentTimeMillis();
//            token = JWTUtil.refresh(refreshToken, secret, 3);
//            System.out.println("刷新Token耗时：" + (System.currentTimeMillis() - l));
//        }


        ThreadUtil.sleep(2000);
        JWTUtil.verify(refreshToken, secret);
    }

}
