package org.opsli.core.utils;


import org.opsli.common.exception.TokenException;
import org.opsli.core.msg.TokenMsg;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * 生成token
 *
 * @author Parker

 * @date 2017-05-20 14:41
 */
public final class TokenGenerator {

    public static String generateValue() {
        return generateValue(UUID.randomUUID().toString());
    }

    private static final char[] hexCode = "0123456789abcdef".toCharArray();


    /**
     * 生成Token
     * @param param
     * @return
     */
    public static String generateValue(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            // 生成Token失败
            throw new TokenException(TokenMsg.EXCEPTION_TOKEN_CREATE_ERROR);
        }
    }



    public static String toHexString(byte[] data) {
        if(data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length*2);
        for ( byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }


    // =========================
    private TokenGenerator(){}
}
