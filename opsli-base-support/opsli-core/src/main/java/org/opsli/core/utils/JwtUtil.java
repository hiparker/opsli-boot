package org.opsli.core.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.common.constants.SignConstants;
import org.opsli.common.constants.TokenTypeConstants;
import org.opsli.common.exception.JwtException;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.msg.JwtMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * JWT工具类
 *
 * @author 孙志强
 * @date 2020/05/29 21:23
 */
@Slf4j
@Order(UTIL_ORDER)
@Lazy(false)
@Component
public class JwtUtil {

    /**
     * 过期时间改为从配置文件获取 120 分钟 两小时
     */
    public static int EXPIRE_MILLISECOND;

    /**
     * JWT认证加密私钥(Base64加密)
     */
    private static String ENCRYPT_JWT_INITIAL_SECRET;

    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;

    /**
     * 校验验证帐号加JWT私钥解密 是否正确
     * @param token Token
     * @return boolean 是否正确
     */
    public static boolean verify(String token) {
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

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
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

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
     * @param isExpire 是否过期
     * @return java.lang.String 返回加密的Token
     */
    public static String sign(String tokenType, String account, String userId, boolean isExpire) {
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 时间戳
        long currentTimeMillis = System.currentTimeMillis();
        // 帐号加JWT私钥加密
        String secret = account + Base64.decodeStr(ENCRYPT_JWT_INITIAL_SECRET);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTCreator.Builder builder = JWT.create()
                .withClaim(SignConstants.TYPE, tokenType)
                .withClaim(SignConstants.ACCOUNT, account)
                .withClaim(SignConstants.USER_ID, userId)
                .withClaim(SignConstants.TIMESTAMP, String.valueOf(currentTimeMillis));

        // 如果为是 则开启失效时间
        if(isExpire){
            // 时间偏移，取最终失效时间
            Date expireDate = DateUtil.offsetMillisecond(DateUtil.date(currentTimeMillis), EXPIRE_MILLISECOND);
            // token 过期时间
            builder.withExpiresAt(expireDate);
        }

        // 附带account帐号信息
        return builder.sign(algorithm);
    }


    // ==================

    /**
     * 初始化
     */
    @Autowired
    public void init(GlobalProperties globalProperties){
        if(globalProperties != null && globalProperties.getAuth() != null
                && globalProperties.getAuth().getToken() != null
            ){
            // 获得 Token失效时间
            JwtUtil.EXPIRE_MILLISECOND = globalProperties.getAuth()
                    .getToken().getEffectiveTime() * 60 * 1000;

            // 获得 Token初始盐值
            JwtUtil.ENCRYPT_JWT_INITIAL_SECRET = globalProperties.getAuth()
                    .getToken().getSecret();
        }

        IS_INIT = true;
    }

    // ==================

    public static void main(String[] args) {
        String token = JwtUtil.sign(TokenTypeConstants.TYPE_SYSTEM, "test","123123", true);

        boolean verify = JwtUtil.verify(token);
        System.out.println(token);
        System.out.println(verify);
    }
}
