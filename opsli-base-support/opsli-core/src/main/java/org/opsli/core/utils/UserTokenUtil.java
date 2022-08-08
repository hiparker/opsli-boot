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
package org.opsli.core.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.constants.RedisConstants;
import org.opsli.common.constants.SignConstants;
import org.opsli.common.constants.TokenConstants;
import org.opsli.common.constants.TokenTypeConstants;
import org.opsli.common.enums.LoginLimitRefuse;
import org.opsli.common.enums.LoginModelType;
import org.opsli.common.exception.TokenException;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.core.base.dto.LoginUserDto;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.holder.UserContextHolder;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.msg.TokenMsg;
import org.opsli.plugins.redis.RedisPlugin;
import org.opsli.plugins.security.JwtConstants;
import org.opsli.plugins.security.exception.AuthException;
import org.opsli.plugins.security.exception.errorcode.AuthErrorCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * User Token Util
 *
 * @author Parker
 * @date 2017-05-20 14:41
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class UserTokenUtil {

    /** token 缓存名 */
    public static final String TOKEN_NAME = JwtConstants.TOKEN_HEADER;
    /** 限制登录数量 -1 为无限大 */
    public static final int ACCOUNT_LIMIT_INFINITE = -1;
    /** 登录配置信息 */
    public static GlobalProperties.Auth.Login LOGIN_PROPERTIES;
    public static GlobalProperties.Auth.Token TOKEN_PROPERTIES;
    /** Redis插件 */
    private static RedisPlugin redisPlugin;
    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;

    /**
     * 根据 user 创建Token
     * @param loginUser 用户
     * @return String
     */
    public static String createAccessToken(LoginUserDto loginUser) {
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 2022-07-22 用户票据新增 终端缓存 多终端下不影响
        String ticketSetKey = CacheUtil.formatKey(
                RedisConstants.PREFIX_TICKET +
                        loginUser.getLoginFrom() + ":" + loginUser.getUsername());

        GlobalProperties.Auth.Login loginProperties = UserTokenUtil.LOGIN_PROPERTIES;
        // 如果当前登录开启 数量限制
        if(loginProperties.getLimitCount() > ACCOUNT_LIMIT_INFINITE){
            // 当前用户已存在 Token数量
            Long ticketLen = redisPlugin.sSize(ticketSetKey);
            if(ticketLen !=null && ticketLen >= loginProperties.getLimitCount()){
                // 如果是拒绝后者 则直接抛出异常
                if(LoginLimitRefuse.AFTER == loginProperties.getLimitRefuse()){
                    // 生成Token失败 您的账号已在其他设备登录
                    throw new TokenException(TokenMsg.EXCEPTION_TOKEN_CREATE_LIMIT_ERROR);
                }
                // 如果是拒绝前者 则弹出前者
                else {
                    redisPlugin.sPop(ticketSetKey);
                }
            }
        }


        // 认证Token
        String accessToken = JWTBizUtil.generateAccessToken(loginUser,
                TOKEN_PROPERTIES.getSecret(), TOKEN_PROPERTIES.getEffectiveTime());

        // 获得当前Token时间戳
        Date expiredDateFromToken = JWTBizUtil.getExpiredDateFromToken(accessToken);

        // 在redis存一份 token 是为了防止 人为造假
        // 保存用户token
        redisPlugin.sPut(ticketSetKey, accessToken);
        // 设置该用户全部token失效时间， 如果这时又有新设备登录 则续命
        redisPlugin.expireAt(ticketSetKey, expiredDateFromToken);

        return accessToken;
    }

    /**
     * 获得登陆凭证信息
     * @return String
     */
    public static Optional<LoginUserDto> getLoginUserDto() {
        String token = UserContextHolder.getToken().orElseThrow(() -> new TokenException(
                TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY));
        return getLoginUserDto(token);
    }

    /**
     * 根据 Token 获得登陆凭证信息
     * @return String
     */
    public static Optional<LoginUserDto> getLoginUserDto(String token) {
        if(StrUtil.isBlank(token)){
            return Optional.empty();
        }

        LoginUserDto loginUserFromToken = JWTBizUtil.getLoginUserFromToken(token);
        return Optional.ofNullable(loginUserFromToken);
    }

    /**
     * 根据 Token 获得用户ID
     * @return String
     */
    public static String getUserIdByToken() {
        String token = UserContextHolder.getToken().orElseThrow(() -> new TokenException(
                TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY));

        return getUserIdByToken(token);
    }
    /**
     * 根据 Token 获得用户ID
     * @param token token
     * @return String
     */
    public static String getUserIdByToken(String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        String userId = "";
        try {
            LoginUserDto loginUserFromToken = JWTBizUtil.getLoginUserFromToken(token);
            userId = loginUserFromToken.getUid();
        }catch (Exception ignored){}
        return userId;
    }


    /**
     * 根据 Token 获得 用户名
     * @return String
     */
    public static String getUserNameByToken() {
        String token = UserContextHolder.getToken().orElseThrow(() -> new TokenException(
                TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY));
        return getUserNameByToken(token);
    }
    /**
     * 根据 Token 获得 用户名
     * @param token token
     * @return String
     */
    public static String getUserNameByToken(String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        String username = "";
        try {
            LoginUserDto loginUserFromToken = JWTBizUtil.getLoginUserFromToken(token);
            username = loginUserFromToken.getUsername();
        }catch (Exception ignored){}
        return username;
    }

    /**
     * 根据 Token 获得 租户ID
     * @return String
     */
    public static String getTenantIdByToken() {
        String token = UserContextHolder.getToken().orElseThrow(() -> new TokenException(
                TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY));
        return getTenantIdByToken(token);
    }

    /**
     * 根据 Token 获得 租户ID
     * @param token token
     * @return String
     */
    public static String getTenantIdByToken(String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        String username = "";
        try {
            LoginUserDto loginUserFromToken = JWTBizUtil.getLoginUserFromToken(token);
            username = loginUserFromToken.getTenantId();
        }catch (Exception ignored){}
        return username;
    }

    /**
     * 退出登陆
     * @param token token
     */
    public static void logout(String token) {
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);


        if(StringUtils.isEmpty(token)){
            return;
        }
        try {
            LoginUserDto loginUserDto = getLoginUserDto(token)
                    .orElseThrow(()-> new AuthException(AuthErrorCodeEnum.AUTH_AUTH_INVALID));

            // 2022-07-22 用户票据新增 终端缓存 多终端下不影响
            String ticketSetKey = CacheUtil.formatKey(
                    RedisConstants.PREFIX_TICKET +
                            loginUserDto.getLoginFrom() + ":" + loginUserDto.getUsername());

            // 获得要退出用户
            UserModel user = UserUtil.getUser(loginUserDto.getUid());
            if(user != null){
                // 删除Token信息
                redisPlugin.sRemove(ticketSetKey, token);

                // 如果缓存中 无该用户任何Token信息 则删除用户缓存
                Long size = redisPlugin.sSize(ticketSetKey);
                if(size == null || size == 0L) {
                    // 删除相关信息
                    UserUtil.refreshUser(user);
                    UserUtil.refreshUserRoles(user.getId());
                    UserUtil.refreshUserAllPerms(user.getId());
                    UserUtil.refreshUserMenus(user.getId());
                    UserUtil.refreshUserOrgs(user.getId());
                    UserUtil.refreshUserDefRole(user.getId());
                    UserUtil.refreshUserDefOrg(user.getId());
                }
            }

        }catch (Exception ignored){}
    }

    /**
     * 验证 token
     * @param token token
     */
    public static void verify(String token) {
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);


        if(StringUtils.isEmpty(token)){
            throw new AuthException(AuthErrorCodeEnum.AUTH_AUTH_INVALID);
        }


        // 1. 校验是否是有效的 token
        // 开启续命模式 如果为续命模式 则不验证失效时间
        boolean reviveMode = LOGIN_PROPERTIES.getReviveMode() != null && LOGIN_PROPERTIES.getReviveMode();
        if(reviveMode){
            JWTBizUtil.verifyBySign(token, TOKEN_PROPERTIES.getSecret());
        }else {
            JWTBizUtil.verify(token, TOKEN_PROPERTIES.getSecret());
        }


        LoginUserDto loginUserDto = getLoginUserDto(token)
                .orElseThrow(()-> new AuthException(AuthErrorCodeEnum.AUTH_AUTH_INVALID));

        // 2022-07-22 用户票据新增 终端缓存 多终端下不影响
        String ticketSetKey = CacheUtil.formatKey(
                RedisConstants.PREFIX_TICKET +
                        loginUserDto.getLoginFrom() + ":" + loginUserDto.getUsername());

        // 2. 校验当前缓存中token是否失效
        boolean hashKey = redisPlugin.sHashKey(ticketSetKey, token);
        if(!hashKey){
            throw new AuthException(AuthErrorCodeEnum.AUTH_AUTH_INVALID);
        }

        // 3. 校验通过后 如果开启续命模式 则整体延长登录时效
        if(BooleanUtil.isTrue(LOGIN_PROPERTIES.getReviveMode())){
            // 设置该用户全部token失效时间， 如果这时又有新设备登录 则续命
            redisPlugin.expire(ticketSetKey,
                    TOKEN_PROPERTIES.getEffectiveTime(), TimeUnit.MILLISECONDS);
        }
    }


    /**
     * 获得当前失败次数
     * @param principal 主键凭证
     */
    public static long getSlipCount(String principal){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 登陆类型
        LoginModelType loginModelType = LoginModelType.getTypeByStr(principal);

        // 获得当前失败次数
        String key = CacheUtil.formatKey(RedisConstants.PREFIX_ACCOUNT_SLIP_COUNT
                + loginModelType.name().toLowerCase() + ":" + principal);
        long slipCount = 0L;
        Object obj = redisPlugin.get(key);
        if(obj != null){
            try {
                slipCount = Convert.convert(Long.class, obj);
            }catch (Exception ignored){}
        }
        return slipCount;
    }


    // ==========================

    /**
     * 获取请求的token
     */
    public static String getRequestToken(HttpServletRequest httpRequest){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);


        //从header中获取token
        String token = httpRequest.getHeader(TOKEN_NAME);

        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(token)){
            token = httpRequest.getParameter(TOKEN_NAME);
        }

        return token;
    }

    /**
     * 初始化
     */
    @Autowired
    public void init(GlobalProperties globalProperties, RedisPlugin redisPlugin){
        if(globalProperties != null && globalProperties.getAuth() != null
                && globalProperties.getAuth().getLogin() != null
            ){
            // 登录配置信息
            UserTokenUtil.TOKEN_PROPERTIES = globalProperties.getAuth().getToken();
            // 登录配置信息
            UserTokenUtil.LOGIN_PROPERTIES = globalProperties.getAuth().getLogin();
        }

        // Redis 插件
        UserTokenUtil.redisPlugin = redisPlugin;

        IS_INIT = true;
    }


}
