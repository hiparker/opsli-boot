package org.opsli.common.enums;

/**
 * 参数类型
 *
 * @author Parker
 */
public enum OptionsType {

    /** 参数类型 */

    /** 目前支持 RSA SM2 ECIES 3种模式 */
    CRYPTO_ASYMMETRIC("crypto_asymmetric", "加解密-非对称"),

    ;

    private final String code;
    private final String desc;

    public static OptionsType getCacheType(String cacheType) {
        OptionsType[] var1 = values();
        for (OptionsType type : var1) {
            if (type.code.equalsIgnoreCase(cacheType)) {
                return type;
            }
        }
        return null;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    // =================

    OptionsType(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }
}