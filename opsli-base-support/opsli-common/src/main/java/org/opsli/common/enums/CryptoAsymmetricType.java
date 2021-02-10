package org.opsli.common.enums;

/**
 * 非对称算法类型
 *
 * @author Parker
 */
public enum CryptoAsymmetricType {

    /** 非对称算法类型 */

    RSA("RSA", "RSA 算法"),
    SM2("SM2", "SM2 算法"),
    ECIES("ECIES", "ECIES 算法"),

    ;

    private final String code;
    private final String desc;

    public static CryptoAsymmetricType getCryptoType(String code) {
        CryptoAsymmetricType[] var1 = values();
        for (CryptoAsymmetricType type : var1) {
            if (type.code.equalsIgnoreCase(code)) {
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

    CryptoAsymmetricType(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }
}
