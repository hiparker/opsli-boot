package org.opsli.core.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.constants.SignConstants;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * User Token Util
 *
 * @author parker
 */
@Slf4j
@Component
public class UserTokenUtil {


    /** 缓存前缀 */
    private static final String PREFIX = "opsli:ticket:";

    /** Redis插件 */
    private static RedisPlugin redisPlugin;

    /**
     * 根据 user 创建Token
     * @param user
     * @return
     */
    public static ResultVo<Map<String,Object>> createToken(UserModel user) {
        if (user == null) {
            return ResultVo.error("生成Token失败");
        }

        Map<String,Object> map = Maps.newHashMapWithExpectedSize(2);
        try {

            // 生效时间
            int expire = Integer.parseInt(
                    String.valueOf(JwtUtil.EXPIRE)
            );

            // 生成 Token 包含 username userId timestamp
            String signToken = JwtUtil.sign(user.getUsername(), user.getId());

            // 生成MD5 16进制码 用于缩减存储
            String signTokenHex = new Md5Hash(signToken).toHex();

            // 获得当前时间戳时间
            long timestamp = Long.parseLong(JwtUtil.getClaim(signToken, SignConstants.TIMESTAMP));
            DateTime currDate = DateUtil.date(timestamp);

            // 获得失效偏移量时间
            DateTime dateTime = DateUtil.offsetMillisecond(currDate, expire);
            long endTimestamp = dateTime.getTime();

            // token 缓存真实失效时间 建议大于 最终时间 -- 多加了20分钟的失效时间
            // 在redis存一份 token 是为了防止 认为造假
            boolean tokenFlag = redisPlugin.put(PREFIX + signTokenHex, endTimestamp, expire + 20);
            if(tokenFlag){
                map.put("token", signToken);
                map.put("expire", endTimestamp);
                return ResultVo.success(map);
            }
            return ResultVo.error("生成Token失败");
        }catch (Exception e){
            log.error(e.getMessage() , e);
            return ResultVo.error(e.getMessage());
        }
    }

    /**
     * 根据 Token 获得用户ID
     * @param token
     * @return
     */
    public static String getUserIdByToken(String token) {
        if(StringUtils.isEmpty(token)) return null;
        String userId = "";
        try {
            userId = JwtUtil.getClaim(token, SignConstants.USER_ID);
        }catch (Exception e){}
        return userId;
    }

    /**
     * 根据 Token 获得 username
     * @param token
     * @return
     */
    public static String getUserNameByToken(String token) {
        if(StringUtils.isEmpty(token)) return null;
        String username = "";
        try {
            username = JwtUtil.getClaim(token, SignConstants.ACCOUNT);
        }catch (Exception e){}
        return username;
    }



    /**
     * 退出登陆
     * @param token
     */
    public static void logout(String token) {
        if(StringUtils.isEmpty(token)) return;
        try {
            // 生成MD5 16进制码 用于缩减存储
            String signTokenHex = new Md5Hash(token).toHex();

            redisPlugin.del(PREFIX + signTokenHex);
        }catch (Exception e){}
    }

    /**
     * 验证 token
     * @param token
     */
    public static boolean verify(String token) {
        if(StringUtils.isEmpty(token)) return false;

        try {
            // 1. 校验是否是有效的 token
            boolean tokenVerify = JwtUtil.verify(token);
            if(!tokenVerify){
                return false;
            }

            // 2. 校验当前缓存中token是否失效
            // 生成MD5 16进制码 用于缩减存储
            String signTokenHex = new Md5Hash(token).toHex();

            Long  endTimestamp = (Long) redisPlugin.get(PREFIX + signTokenHex);
            if(endTimestamp == null){
                return false;
            }

            // JWT 自带过期校验 无需多做处理

        }catch (TokenExpiredException e){
            return false;
        }catch (Exception e){
            return false;
        }
        return true;
    }


    // ==========================

    /**
     * 获取请求的token
     */
    public static String getRequestToken(HttpServletRequest httpRequest){
        //从header中获取token
        String token = httpRequest.getHeader("token");

        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(token)){
            token = httpRequest.getParameter("token");
        }

        return token;
    }

    @Autowired
    public  void setRedisPlugin(RedisPlugin redisPlugin) {
        UserTokenUtil.redisPlugin = redisPlugin;
    }

}
