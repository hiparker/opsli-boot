package opsli.plugins.crypto.spring.crypto;

/**
 * 加密解密器 可自行实现
 *
 * @author zhangylo
 */
public interface ICrypto {

    /**
     * 加密
     *
     * @param value 加密前的值
     * @param key   秘钥
     * @return 加密后的值
     * @throws Exception
     */
    String encrypt(String value, String key) throws Exception;

    /**
     * 解密
     *
     * @param value 解密前的值
     * @param key   秘钥
     * @return 解密后的值
     * @throws Exception
     */
    String decrypt(String value, String key) throws Exception;
}
