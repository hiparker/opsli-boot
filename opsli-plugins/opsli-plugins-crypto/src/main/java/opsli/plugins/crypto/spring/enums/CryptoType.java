package opsli.plugins.crypto.spring.enums;

/**
 * 加密解密枚举
 *
 * @author zhangylo
 */
public enum CryptoType {

    /**
     * ENCRYPT 加密
     * DECRYPT 解密
     */
    ENCRYPT("com/encrypt"), DECRYPT("decrypt");

    /**
     * 对应加密器方法名称
     */
    private String method;

    CryptoType(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
