package org.opsli.common.api;


/**
 * 用于存放当前线程下 Token
 *
 * @author parker
 * @date 2020-09-15
 */
public class TokenThreadLocal {

    /** 临时线程存储 token 容器 */
    private static final ThreadLocal<String> tokenData = new ThreadLocal<>();

    public static void put(String token) {
        if (tokenData.get() == null) {
            tokenData.set(token);
        }
    }

    public static String get() {
        return tokenData.get();
    }

    public static void remove() {
        try {
            tokenData.remove();
        }catch (Exception e){}
    }
}
