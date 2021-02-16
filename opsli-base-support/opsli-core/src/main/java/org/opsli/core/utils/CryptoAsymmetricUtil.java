/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.core.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.ECIES;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.SM2;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.options.OptionsModel;
import org.opsli.common.enums.CryptoAsymmetricType;
import org.opsli.common.enums.OptionsType;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.msg.CoreMsg;

import java.util.Collection;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.utils
 * @Author: Parker
 * @CreateTime: 2020-09-19 20:03
 * @Description: 非对称加密工具类
 */
@Slf4j
public final class CryptoAsymmetricUtil {

    /** Crypto KEY */
    private static final String CRYPTO_KEY = "data";

    /**
     * 创建公私钥
     * @param cryptoAsymmetricType 枚举
     * @return Model
     */
    public static CryptoAsymmetricUtil.CryptoAsymmetric create(final CryptoAsymmetricType cryptoAsymmetricType){
        CryptoAsymmetricUtil.CryptoAsymmetric model = new CryptoAsymmetricUtil.CryptoAsymmetric();
        model.setCryptoType(cryptoAsymmetricType.getCode());
        switch (cryptoAsymmetricType){
            case RSA:
                RSA rsa = SecureUtil.rsa();
                model.setPublicKey(rsa.getPublicKeyBase64());
                model.setPrivateKey(rsa.getPrivateKeyBase64());
                break;
            case SM2:
                SM2 sm2 = SmUtil.sm2();
                model.setPublicKey(sm2.getPublicKeyBase64());
                model.setPrivateKey(sm2.getPrivateKeyBase64());
                break;
            case ECIES:
                ECIES ecies = new ECIES();
                model.setPublicKey(ecies.getPublicKeyBase64());
                model.setPrivateKey(ecies.getPrivateKeyBase64());
                break;
            default:
                break;
        }
        return model;
    }


    /**
     * 加密数据
     * @param data 数据
     * @return String
     */
    public static String encrypt(final Object data){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CRYPTO_KEY, data);

        // 原始/加密 数据
        String encryptedStr = jsonObject.toString();


        try {
            // 加解密方式
            OptionsModel cryptoAsymmetric = OptionsUtil.getOptionByCode(OptionsType.CRYPTO_ASYMMETRIC);
            // 公钥
            OptionsModel cryptoAsymmetricPublicKey =
                    OptionsUtil.getOptionByCode(OptionsType.CRYPTO_ASYMMETRIC_PUBLIC_KEY);
            // 私钥
            OptionsModel cryptoAsymmetricPrivateKey =
                    OptionsUtil.getOptionByCode(OptionsType.CRYPTO_ASYMMETRIC_PRIVATE_KEY);

            // 非法验证
            if(cryptoAsymmetric == null || cryptoAsymmetricPublicKey == null ||
                    cryptoAsymmetricPrivateKey == null
                ){
                throw new RuntimeException();
            }

            // 加解密方式枚举
            CryptoAsymmetricType cryptoType = CryptoAsymmetricType.getCryptoType(
                    cryptoAsymmetric.getOptionValue());
            // 非法验证
            if(cryptoType == null){
                throw new RuntimeException();
            }


            switch (cryptoType){
                case RSA:
                    RSA rsa = SecureUtil.rsa(cryptoAsymmetricPrivateKey.getOptionValue(),
                            cryptoAsymmetricPublicKey.getOptionValue());
                    encryptedStr = rsa.encryptBase64(
                            StrUtil.bytes(encryptedStr, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
                    break;
                case SM2:
                    SM2 sm2 = SmUtil.sm2(cryptoAsymmetricPrivateKey.getOptionValue(),
                            cryptoAsymmetricPublicKey.getOptionValue());
                    encryptedStr = sm2.encryptBase64(
                            StrUtil.bytes(encryptedStr, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
                    break;
                case ECIES:
                    ECIES ecies = new ECIES(cryptoAsymmetricPrivateKey.getOptionValue(),
                            cryptoAsymmetricPublicKey.getOptionValue());
                    encryptedStr = ecies.encryptBase64(
                            StrUtil.bytes(encryptedStr, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
                    break;
                default:
                    throw new RuntimeException();
            }
        }catch (Exception e){
            // 加密失败
            throw new ServiceException(CoreMsg.OTHER_EXCEPTION_CRYPTO_EN);
        }
        return encryptedStr;
    }


    /**
     * RSA 解密数据
     * @param data 数据
     * @return Object
     */
    public static Object decryptToObj(final String data){
        Object obj;
        String decryptedData = decrypt(data);
        try{
            obj = JSONObject.parse(decryptedData);
        }catch (Exception e){
            // 非对称解密反射失败
            throw new ServiceException(CoreMsg.OTHER_EXCEPTION_CRYPTO_REFLEX);
        }
        return obj;
    }

    /**
     * 解密数据
     * @param data 数据
     * @return String
     */
    public static String decrypt(final String data){
        String decryptStr;
        try {
            if(StringUtils.isEmpty(data)){
                throw new RuntimeException();
            }

            // 加解密方式
            OptionsModel cryptoAsymmetric = OptionsUtil.getOptionByCode(OptionsType.CRYPTO_ASYMMETRIC);
            // 公钥
            OptionsModel cryptoAsymmetricPublicKey =
                    OptionsUtil.getOptionByCode(OptionsType.CRYPTO_ASYMMETRIC_PUBLIC_KEY);
            // 私钥
            OptionsModel cryptoAsymmetricPrivateKey =
                    OptionsUtil.getOptionByCode(OptionsType.CRYPTO_ASYMMETRIC_PRIVATE_KEY);

            // 非法验证
            if(cryptoAsymmetric == null || cryptoAsymmetricPublicKey == null ||
                    cryptoAsymmetricPrivateKey == null
            ){
                throw new RuntimeException();
            }

            // 加解密方式枚举
            CryptoAsymmetricType cryptoType = CryptoAsymmetricType.getCryptoType(
                    cryptoAsymmetric.getOptionValue());
            // 非法验证
            if(cryptoType == null){
                throw new RuntimeException();
            }

            String tmp;
            String currData = data.replaceAll(" ", "+");
            switch (cryptoType){
                case RSA:
                    RSA rsa = SecureUtil.rsa(cryptoAsymmetricPrivateKey.getOptionValue(), cryptoAsymmetricPublicKey.getOptionValue());
                    tmp = rsa.decryptStr(data, KeyType.PrivateKey);
                    break;
                case SM2:
                    SM2 sm2 = SmUtil.sm2(cryptoAsymmetricPrivateKey.getOptionValue(), cryptoAsymmetricPublicKey.getOptionValue());
                    tmp = sm2.decryptStr(currData, KeyType.PrivateKey);
                    break;
                case ECIES:
                    ECIES ecies = new ECIES(cryptoAsymmetricPrivateKey.getOptionValue(), cryptoAsymmetricPublicKey.getOptionValue());
                    tmp = ecies.decryptStr(currData, KeyType.PrivateKey);
                    break;
                default:
                    throw new RuntimeException();
            }

            // 转换对象
            JSONObject jsonObject = JSONObject.parseObject(tmp);
            Object obj = jsonObject.get(CRYPTO_KEY);
            if(obj instanceof Collection){
                decryptStr = jsonObject.getJSONArray(CRYPTO_KEY).toJSONString();
            }else{
                decryptStr = jsonObject.getJSONObject(CRYPTO_KEY).toJSONString();
            }
        }catch (Exception e){
            // 解密失败
            throw new ServiceException(CoreMsg.OTHER_EXCEPTION_CRYPTO_DE);
        }

        return decryptStr;
    }

    // =====================================

    @Data
    public static class CryptoAsymmetric {


        /** 加解密类别 */
        private String cryptoType;

        /** 公钥 */
        private String publicKey;

        /** 私钥 */
        private String privateKey;
    }

    private CryptoAsymmetricUtil(){}

}
