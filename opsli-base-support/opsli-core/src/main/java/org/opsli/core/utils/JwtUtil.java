package org.opsli.core.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.opsli.core.autoconfigure.GlobalProperties;
import org.opsli.common.constants.SignConstants;
import org.opsli.common.constants.TokenTypeConstants;
import org.opsli.common.exception.JwtException;
import org.opsli.core.msg.JwtMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * JWT工具类
 * @author sunzhiqiang23
 * @date 2020/05/29 21:23
 */
@Slf4j
@Order(UTIL_ORDER)
@Lazy(false)
@Component
@AutoConfigureAfter({GlobalProperties.class})
public class JwtUtil {


    /**
     * 过期时间改为从配置文件获取 120 分钟 两小时
     */
    public static long EXPIRE;

    /**
     * JWT认证加密私钥(Base64加密)
     */
    private static String ENCRYPT_JWT_INITIAL_SECRET;


    /**
     * 校验验证帐号加JWT私钥解密 是否正确
     * @param token Token
     * @return boolean 是否正确
     */
    public static boolean verify(String token) {
        String secret = getClaim(token, SignConstants.ACCOUNT) + Base64.decodeStr(ENCRYPT_JWT_INITIAL_SECRET);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
        return true;
    }

    /**
     * 获得Token中的信息
     * @param token token
     * @param claim 字段
     * @return java.lang.String
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            // 解密异常
            String msg = StrUtil.format(JwtMsg.EXCEPTION_DECODE.getMessage(), e.getMessage());
            throw new JwtException(JwtMsg.EXCEPTION_DECODE.getCode(), msg);
        }
    }

    /**
     * 生成签名
     * @param tokenType token类型
     * @param account 帐号
     * @param userId 用户ID
     * @return java.lang.String 返回加密的Token
     */
    public static String sign(String tokenType, String account, String userId) {
        // 帐号加JWT私钥加密
        String secret = account + Base64.decodeStr(ENCRYPT_JWT_INITIAL_SECRET);
        // 此处过期时间是以毫秒为单位，所以乘以1000
        Date date = new Date(System.currentTimeMillis() + EXPIRE);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带account帐号信息
        return JWT.create()
                .withClaim(SignConstants.TYPE, tokenType)
                .withClaim(SignConstants.ACCOUNT, account)
                .withClaim(SignConstants.USER_ID, userId)
                .withClaim(SignConstants.TIMESTAMP, String.valueOf(System.currentTimeMillis()))
                // token 过期时间
                .withExpiresAt(date)
                .sign(algorithm);
    }


    // ==================

    @Autowired
    public void setExpire(GlobalProperties globalProperties) {
        // 获得 Token失效时间
        if(globalProperties != null && globalProperties.getAuth() != null
            && globalProperties.getAuth().getToken() != null
            ){
            EXPIRE = globalProperties.getAuth()
                    .getToken().getEffectiveTime() * 60 * 1000;
        }
    }

    @Autowired
    public void setEncryptJwtInitialSecret(GlobalProperties globalProperties) {
        // 获得 Token初始盐值
        if(globalProperties != null && globalProperties.getAuth() != null
                && globalProperties.getAuth().getToken() != null
        ){
            ENCRYPT_JWT_INITIAL_SECRET = globalProperties.getAuth()
                    .getToken().getSecret();
        }
    }

    // ==================

    public static void main(String[] args) {
        String token = JwtUtil.sign(TokenTypeConstants.TYPE_SYSTEM, "test","123123");

        boolean verify = JwtUtil.verify(token);
        System.out.println(token);
        System.out.println(verify);
    }
}
