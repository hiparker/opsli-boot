package org.opsli.common.enums;

/**
 * 参数类型
 *
 * @author Parker
 */
public enum OptionsType {

    /** 参数类型 */

    /** 非对称加密 目前支持 RSA SM2 ECIES 3种模式 */
    CRYPTO_ASYMMETRIC("crypto_asymmetric", "加解密-非对称"),
    /** 非对称加密 公钥 */
    CRYPTO_ASYMMETRIC_PUBLIC_KEY("crypto_asymmetric_public_key", "加解密-非对称-公钥"),
    /** 非对称加密 私钥 */
    CRYPTO_ASYMMETRIC_PRIVATE_KEY("crypto_asymmetric_private_key", "加解密-非对称-私钥"),

    ;

    private final String code;
    private final String desc;

    public static OptionsType getType(String cacheType) {
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
