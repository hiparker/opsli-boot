package org.opsli.core.utils;

import cn.hutool.setting.dialect.Props;
import cn.hutool.setting.dialect.PropsUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.constants.SignConstants;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * JWT工具类
 * @author sunzhiqiang23
 * @date 2020/05/29 21:23
 */
@Slf4j
public final class JwtUtil {


    /**
     * 过期时间改为从配置文件获取 120 分钟 两小时
     */
    public static final long EXPIRE;

    /**
     * JWT认证加密私钥(Base64加密)
     */
    private static final String encryptJWTKey="a30ade6452725123436288ccae58570738ee";

    static {
        Props props = PropsUtil.get("application.yaml");
        // token 有效时间
        EXPIRE = props.getLong("opsli.token-effective-time", 120L) * 60 * 1000;
    }


    /**
     * 校验验证帐号加JWT私钥解密 是否正确
     * @param token Token
     * @return boolean 是否正确
     */
    public static boolean verify(String token) {
        try {
            String secret = getClaim(token, SignConstants.ACCOUNT) + Base64ConvertUtil.decode(encryptJWTKey);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("认证解密异常:" + e.getMessage());
        }
    }

    /**
     * 获得Token中的信息
     * @param token
     * @param claim
     * @return java.lang.String
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            throw new RuntimeException("解密异常:" + e.getMessage());
        }
    }

    /**
     * 生成签名
     * @param account 帐号
     * @return java.lang.String 返回加密的Token
     */
    public static String sign(String account, String userId) {
        try {
            // 帐号加JWT私钥加密
            String secret = account + Base64ConvertUtil.decode(encryptJWTKey);
            // 此处过期时间是以毫秒为单位，所以乘以1000
            Date date = new Date(System.currentTimeMillis() + EXPIRE);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带account帐号信息
            return JWT.create()
                    .withClaim(SignConstants.ACCOUNT, account)
                    .withClaim(SignConstants.USER_ID, userId)
                    .withClaim(SignConstants.TIMESTAMP, String.valueOf(System.currentTimeMillis()))
                    // token 过期时间
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("加密异常:" + e.getMessage());
        }
    }


    public static void main(String[] args) {
        String aaaaaa = JwtUtil.sign("aaaaaa","123123");

        boolean verify = JwtUtil.verify(aaaaaa);
        System.out.println(aaaaaa);
        System.out.println(verify);
    }

    // ==================
    private JwtUtil(){}
}
