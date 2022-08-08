package opsli.plugins.crypto.spring.crypto.impl;


import lombok.extern.slf4j.Slf4j;
import opsli.plugins.crypto.CryptoPlugin;
import opsli.plugins.crypto.enums.CryptoSymmetricType;
import opsli.plugins.crypto.model.CryptoSymmetric;
import opsli.plugins.crypto.spring.crypto.ICrypto;
import opsli.plugins.crypto.strategy.CryptoSymmetricService;

/**
 * DES 加密解密器
 *
 * @author Parker
 * @date 2022-08-07 17:33
 */
@Slf4j
public class DESCrypto implements ICrypto {

    /**
     * 加密
     *
     * @param value 加密前的值
     * @param key   秘钥
     * @return 加密后的值
     */
    @Override
    public String encrypt(String value, String key) throws Exception {
        CryptoSymmetric cryptoSymmetric = CryptoSymmetric.builder()
                .cryptoType(CryptoSymmetricType.DES)
                .privateKey(key)
                .build();
        CryptoSymmetricService symmetric = CryptoPlugin.getSymmetric();
        return symmetric.encrypt(cryptoSymmetric, value);
    }

    /**
     * 解密
     *
     * @param value 解密前的值
     * @param key   秘钥
     * @return 解密后的值
     */
    @Override
    public String decrypt(String value, String key) {
        CryptoSymmetric cryptoSymmetric = CryptoSymmetric.builder()
                .cryptoType(CryptoSymmetricType.DES)
                .privateKey(key)
                .build();
        CryptoSymmetricService symmetric = CryptoPlugin.getSymmetric();
        return symmetric.decrypt(cryptoSymmetric, value);
    }
}
