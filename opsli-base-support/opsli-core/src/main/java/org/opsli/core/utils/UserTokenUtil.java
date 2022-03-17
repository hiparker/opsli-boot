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
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.constants.RedisConstants;
import org.opsli.common.constants.SignConstants;
import org.opsli.common.constants.TokenConstants;
import org.opsli.common.constants.TokenTypeConstants;
import org.opsli.common.enums.LoginLimitRefuse;
import org.opsli.common.exception.TokenException;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.holder.UserContextHolder;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.msg.TokenMsg;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    public static final String TOKEN_NAME = TokenConstants.ACCESS_TOKEN;
    /** 限制登录数量 -1 为无限大 */
    public static final int ACCOUNT_LIMIT_INFINITE = -1;
    /** 登录配置信息 */
    public static GlobalProperties.Auth.Login LOGIN_PROPERTIES;
    /** Redis插件 */
    private static RedisPlugin redisPlugin;
    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;

    /**
     * 根据 user 创建Token
     * @param user 用户
     * @return UserTokenUtil.TokenRet
     */
    public static ResultVo<UserTokenUtil.TokenRet> createToken(UserModel user) {
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);


        if (user == null) {
            // 生成Token失败
            throw new TokenException(TokenMsg.EXCEPTION_TOKEN_CREATE_ERROR);
        }

        try {

            // 如果当前登录开启 数量限制
            if(LOGIN_PROPERTIES.getLimitCount() > ACCOUNT_LIMIT_INFINITE){
                // 当前用户已存在 Token数量
                Long ticketLen = redisPlugin.sSize(
                        CacheUtil.formatKey(RedisConstants.PREFIX_TICKET + user.getUsername()));
                if(ticketLen !=null && ticketLen >= LOGIN_PROPERTIES.getLimitCount()){
                    // 如果是拒绝后者 则直接抛出异常
                    if(LoginLimitRefuse.AFTER == LOGIN_PROPERTIES.getLimitRefuse()){
                        // 生成Token失败 您的账号已在其他设备登录
                        throw new TokenException(TokenMsg.EXCEPTION_TOKEN_CREATE_LIMIT_ERROR);
                    }
                    // 如果是拒绝前者 则弹出前者
                    else {
                        redisPlugin.sPop(
                                CacheUtil.formatKey(RedisConstants.PREFIX_TICKET + user.getUsername())
                        );
                    }
                }
            }

            // 开启续命模式 如果为续命模式 则不指定Token 的 失效时间
            // 生成 Token 包含 username userId timestamp
            boolean reviveMode = LOGIN_PROPERTIES.getReviveMode() != null && LOGIN_PROPERTIES.getReviveMode();
            String signToken = JwtUtil.sign(
                    TokenTypeConstants.TYPE_SYSTEM,
                    user.getUsername(),
                    user.getId(),
                    user.getTenantId(),
                    !reviveMode);

            // 获得当前Token时间戳
            long timestamp = Convert.toLong(
                    JwtUtil.getClaim(signToken, SignConstants.TIMESTAMP));
            // 获得失效偏移量时间
            long endTimestamp = DateUtil.offsetMillisecond(
                    DateUtil.date(timestamp), JwtUtil.EXPIRE_MILLISECOND).getTime();

            // 在redis存一份 token 是为了防止 人为造假
            // 保存用户token
            Long saveLong = redisPlugin.sPut(
                    CacheUtil.formatKey(RedisConstants.PREFIX_TICKET + user.getUsername()),
                    signToken
            );
            if(saveLong != null && saveLong > 0){
                // 设置该用户全部token失效时间， 如果这时又有新设备登录 则续命
                redisPlugin.expire(
                        CacheUtil.formatKey(RedisConstants.PREFIX_TICKET + user.getUsername()),
                        JwtUtil.EXPIRE_MILLISECOND, TimeUnit.MILLISECONDS);

                TokenRet tokenRet = new TokenRet();
                tokenRet.setToken(signToken);
                tokenRet.setEndTimestamp(endTimestamp);

                return ResultVo.success(tokenRet);
            }

        }catch (TokenException te){
            throw te;
        }catch (Exception e){
            log.error(e.getMessage() , e);
        }
        // 生成Token失败
        throw new TokenException(TokenMsg.EXCEPTION_TOKEN_CREATE_ERROR);
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
            userId = JwtUtil.getClaim(token, SignConstants.USER_ID);
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
            username = JwtUtil.getClaim(token, SignConstants.ACCOUNT);
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
            username = JwtUtil.getClaim(token, SignConstants.TENANT_ID);
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
            // 获得要退出用户
            String userId = getUserIdByToken(token);
            UserModel user = UserUtil.getUser(userId);
            if(user != null){
                // 删除Token信息
                redisPlugin.sRemove(
                        CacheUtil.formatKey(RedisConstants.PREFIX_TICKET + user.getUsername()),
                        token);

                // 如果缓存中 无该用户任何Token信息 则删除用户缓存
                Long size = redisPlugin.sSize(
                        CacheUtil.formatKey(RedisConstants.PREFIX_TICKET + user.getUsername())
                );
                if(size == null || size == 0L) {
                    // 删除相关信息
                    UserUtil.refreshUser(user);
                    UserUtil.refreshUserRoles(user.getId());
                    UserUtil.refreshUserAllPerms(user.getId());
                    UserUtil.refreshUserMenus(user.getId());
                    UserUtil.refreshUserOrgs(user.getId());
                    UserUtil.refreshUserDefRole(userId);
                    UserUtil.refreshUserDefOrg(userId);
                }
            }

        }catch (Exception ignored){}
    }

    /**
     * 验证 token
     * @param token token
     */
    public static boolean verify(String token) {
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);


        if(StringUtils.isEmpty(token)){
            return false;
        }

        try {
            // 1. 校验是否是有效的 token
            boolean tokenVerify = JwtUtil.verify(token);
            if(!tokenVerify){
                return false;
            }

            // 2. 校验当前缓存中token是否失效
            // 生成MD5 16进制码 用于缩减存储
            // 删除相关信息
            String username = getUserNameByToken(token);

            boolean hashKey = redisPlugin.sHashKey(
                    CacheUtil.formatKey(RedisConstants.PREFIX_TICKET + username),
                    token);
            if(!hashKey){
                return false;
            }

            // 3. 校验通过后 如果开启续命模式 则整体延长登录时效
            if(BooleanUtil.isTrue(LOGIN_PROPERTIES.getReviveMode())){
                // 设置该用户全部token失效时间， 如果这时又有新设备登录 则续命
                redisPlugin.expire(
                        CacheUtil.formatKey(RedisConstants.PREFIX_TICKET + username),
                        JwtUtil.EXPIRE_MILLISECOND, TimeUnit.MILLISECONDS);
            }

        } catch (Exception e){
            return false;
        }
        return true;
    }

    // ============================ 锁账号 操作

    /**
     * 验证锁定账号
     * @param username 用户名
     */
    public static void verifyLockAccount(String username){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);


        // 判断账号是否临时锁定
        Long loseTimeMillis = (Long) redisPlugin.get(
                CacheUtil.formatKey(RedisConstants.PREFIX_ACCOUNT_SLIP_LOCK + username));
        if(loseTimeMillis != null){
            Date currDate = DateUtil.date();
            DateTime loseDate = DateUtil.date(loseTimeMillis);
            // 偏移5分钟
            DateTime currLoseDate = DateUtil.offsetSecond(loseDate, LOGIN_PROPERTIES.getSlipLockSpeed());

            // 计算失效剩余时间( 分 )
            long betweenM = DateUtil.between(currLoseDate, currDate, DateUnit.MINUTE);
            if(betweenM > 0){
                String msg = StrUtil.format(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCK.getMessage()
                        ,betweenM + "分钟");
                throw new TokenException(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCK.getCode(), msg);
            }else{
                // 计算失效剩余时间( 秒 )
                long betweenS = DateUtil.between(currLoseDate, currDate, DateUnit.SECOND);
                String msg = StrUtil.format(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCK.getMessage()
                        ,betweenS + "秒");
                throw new TokenException(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCK.getCode(), msg);
            }
        }
    }

    /**
     * 锁定账号
     * @param username 用户名
     */
    public static TokenMsg lockAccount(String username){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);


        // 如果失败次数 超过阈值 则锁定账号
        Long slipNum = redisPlugin.increment(
                CacheUtil.formatKey(RedisConstants.PREFIX_ACCOUNT_SLIP_COUNT + username));
        if (slipNum != null){
            // 设置失效时间为 5分钟
            redisPlugin.expire(
                    CacheUtil.formatKey(RedisConstants.PREFIX_ACCOUNT_SLIP_COUNT + username),
                    LOGIN_PROPERTIES.getSlipLockSpeed());

            // 如果确认 都失败 则存入临时缓存
            if(slipNum >= LOGIN_PROPERTIES.getSlipCount()){
                long currentTimeMillis = System.currentTimeMillis();
                // 存入Redis
                redisPlugin.put(
                        CacheUtil.formatKey(RedisConstants.PREFIX_ACCOUNT_SLIP_LOCK + username),
                        currentTimeMillis, LOGIN_PROPERTIES.getSlipLockSpeed());
            }
        }


        return TokenMsg.EXCEPTION_LOGIN_ACCOUNT_NO;
    }

    /**
     * 获得当前失败次数
     * @param username 用户名
     */
    public static long getSlipCount(String username){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);


        long count = 0L;
        Object obj = redisPlugin.get(
                CacheUtil.formatKey(RedisConstants.PREFIX_ACCOUNT_SLIP_COUNT + username));
        if(obj != null){
            try {
                count = Convert.convert(Long.class, obj);
            }catch (Exception ignored){}
        }
        return count;
    }


    /**
     * 清除锁定账号
     * @param username 用户名
     */
    public static void clearLockAccount(String username){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);


        // 删除失败次数记录
        redisPlugin.del(
                CacheUtil.formatKey(RedisConstants.PREFIX_ACCOUNT_SLIP_COUNT + username));
        // 删除失败次数记录
        redisPlugin.del(
                CacheUtil.formatKey(RedisConstants.PREFIX_ACCOUNT_SLIP_LOCK + username));
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
            UserTokenUtil.LOGIN_PROPERTIES = globalProperties.getAuth().getLogin();
        }

        // Redis 插件
        UserTokenUtil.redisPlugin = redisPlugin;

        IS_INIT = true;
    }


    // =====================

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class TokenRet {

        /** Token */
        @ApiModelProperty(value = "Token")
        private String token;

        /** 失效时间戳 */
        @ApiModelProperty(value = "失效时间戳")
        private Long endTimestamp;

    }

}
