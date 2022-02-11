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
package opsli.plugins.crypto.strategy.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import opsli.plugins.crypto.enums.CryptoSymmetricType;
import opsli.plugins.crypto.exception.CryptoException;
import opsli.plugins.crypto.model.CryptoSymmetric;
import opsli.plugins.crypto.msg.CryptoMsg;
import opsli.plugins.crypto.strategy.CryptoSymmetricService;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 对称加密
 *
 * @author Parker
 * @date 2021年5月18日10:53:27
 */
@Slf4j
public class CryptoSymmetricServiceImpl implements CryptoSymmetricService {

    /** 默认缓存个数 超出后流量自动清理 */
    private static final int DEFAULT_CACHE_COUNT = 1000;
    /** 默认缓存时效 超出后自动清理 */
    private static final int DEFAULT_CACHE_TIME = 20;
    /** 加解密执行器缓存 防止多次创建 */
    private static final Map<CryptoSymmetricType, Cache<String, SymmetricCrypto>> LFU_CACHE_MAP;

    static{
        // 初始化缓存类对象
        LFU_CACHE_MAP = Maps.newConcurrentMap();
        for (CryptoSymmetricType asymmetricType : CryptoSymmetricType.values()) {
            LFU_CACHE_MAP.put(asymmetricType,
                    CacheBuilder
                            .newBuilder().maximumSize(DEFAULT_CACHE_COUNT)
                            .expireAfterWrite(DEFAULT_CACHE_TIME, TimeUnit.MINUTES).build()
            );
        }
    }

    /**
     * 创建空模型
     * @return Model
     */
    @Override
    public CryptoSymmetric createNilModel() {
        return new CryptoSymmetric();
    }

    /**
     * 创建公私钥
     * @param cryptoSymmetricType 枚举
     * @return Model
     */
    @Override
    public CryptoSymmetric createKeyModel(final CryptoSymmetricType cryptoSymmetricType){
        SymmetricCrypto cryptoHandler =
                this.createCryptoHandler(cryptoSymmetricType);
        CryptoSymmetric model = this.createNilModel();

        if(cryptoHandler != null){
            SecretKey secretKey = cryptoHandler.getSecretKey();
            String privateKey = Base64.encode(secretKey.getEncoded());
            model.setCryptoType(cryptoSymmetricType);
            model.setPrivateKey(privateKey);
        }
        return model;
    }


    /**
     * 加密数据
     * @param model 加解密模型
     * @param data 数据
     * @return String
     */
    @Override
    public String encrypt(final CryptoSymmetric model, final Object data){
        // 非法验证
        this.verify(model);

        // 原始/加密 数据
        String encryptedStr;
        try {
            encryptedStr = JSONUtil.toJsonStr(data);

            // 创建执行器
            SymmetricCrypto cryptoHandler =
                    this.createCryptoHandler(model);
            if(cryptoHandler == null){
                // 无法获得加解密执行器
                throw new CryptoException(CryptoMsg.CRYPTO_EXCEPTION_HANDLER_NULL);
            }

            // 执行加密操作
            encryptedStr = cryptoHandler.encryptBase64(StrUtil.bytes(encryptedStr, CharsetUtil.CHARSET_UTF_8));

        }catch (JSONException jse){
            // 加密数据转换Json失败
            throw new CryptoException(CryptoMsg.CRYPTO_EXCEPTION_TO_JSON);
        }catch (CryptoException ce){
            // 如果检测到已有异常 则直接抛出
            throw ce;
        }catch (Exception e){
            log.error(e.getMessage(), e);
            // 加密失败
            throw new CryptoException(CryptoMsg.CRYPTO_EXCEPTION_EN);
        }
        return encryptedStr;
    }


    /**
     * 解密数据 - 反射Obj对象
     * @param model 加解密模型
     * @param data 数据
     * @return Object
     */
    @Override
    public Object decryptToObj(final CryptoSymmetric model, final String data){
        Object obj;
        // 解密数据
        String decryptedData = decrypt(model, data);
        // 反射对象
        try{
            obj = JSONUtil.parse(decryptedData);
        }catch (Exception e){
            // 非对称解密反射失败
            throw new CryptoException(CryptoMsg.CRYPTO_EXCEPTION_REFLEX);
        }
        return obj;
    }

    /**
     * 解密数据
     * @param model 加解密模型
     * @param data 数据
     * @return String
     */
    @Override
    public String decrypt(final CryptoSymmetric model, final String data){
        // 非法验证
        this.verify(model);

        // 如果解密内容为空 则返回原内容
        if(StringUtils.isEmpty(data)){
            return data;
        }

        String decryptStr;
        try {
            // 创建执行器
            SymmetricCrypto cryptoHandler =
                    this.createCryptoHandler(model);
            if(cryptoHandler == null){
                // 无法获得加解密执行器
                throw new CryptoException(CryptoMsg.CRYPTO_EXCEPTION_HANDLER_NULL);
            }

            // 处理数据
            String currData = data.replaceAll(" ", "+");
            // 解密数据 - 返回Json 格式String
            decryptStr = cryptoHandler.decryptStr(currData, CharsetUtil.CHARSET_UTF_8);
        }catch (CryptoException ce){
            // 如果检测到已有异常 则直接抛出
            throw ce;
        }catch (Exception e){
            // 解密失败
            throw new CryptoException(CryptoMsg.CRYPTO_EXCEPTION_DE);
        }

        return decryptStr;
    }


    /**
     * 验证
     * @param model 加解密模型
     */
    private void verify(CryptoSymmetric model){
        // 非法验证
        if(model == null ||
            model.getCryptoType() == null ||
            StringUtils.isEmpty(model.getPrivateKey())
            ){
            // 配置信息未初始化
            throw new CryptoException(CryptoMsg.CRYPTO_EXCEPTION_MODEL_NULL);
        }
    }

    /**
     * 创建 加解密执行器
     * 这里使用了缓存池 来防止对象被疯狂创建 减少服务压力
     *
     * @param model 加解密模型
     * @return 执行器
     */
    private SymmetricCrypto createCryptoHandler(final CryptoSymmetric model){
        // 非法验证
        if(model == null ||
                model.getCryptoType() == null ||
                StringUtils.isEmpty(model.getPrivateKey())
            ){
            return null;
        }

        Cache<String, SymmetricCrypto> cryptoCache =
                LFU_CACHE_MAP.get(model.getCryptoType());


        SymmetricCrypto cryptoHandler = null;
        try {
            // 查询并设置缓存
            cryptoHandler = cryptoCache.get(model.getPrivateKey(), () -> {
                SymmetricCrypto tmp = null;
                byte[] keyBytes = Base64.decode(model.getPrivateKey());
                switch (model.getCryptoType()) {
                    case AES:{
                        tmp = new AES(Mode.ECB, Padding.PKCS5Padding, keyBytes);
                        break;
                    }
                    case DES:{
                        tmp = new DES(Mode.ECB, Padding.PKCS5Padding, keyBytes);
                        break;
                    }
                    case DE_SEDE:{
                        tmp = SecureUtil.desede(keyBytes);
                        break;
                    }
                    case SM4:{
                        tmp = SmUtil.sm4(keyBytes);
                        break;
                    }
                    default:
                        break;
                }

                return tmp;
            });
        }catch (ExecutionException e){
            log.error(e.getMessage(), e);
        }

        return cryptoHandler;
    }

    /**
     * 创建 加解密执行器
     * @param cryptoSymmetricType 枚举
     * @return Model
     */
    private SymmetricCrypto createCryptoHandler(final CryptoSymmetricType cryptoSymmetricType){
        SymmetricCrypto cryptoHandler = null;
        switch (cryptoSymmetricType){
            case AES:{
                cryptoHandler = SecureUtil.aes();
                break;
            }
            case DES:{
                cryptoHandler = SecureUtil.des();
                break;
            }
            case DE_SEDE:{
                cryptoHandler = SecureUtil.desede();
                break;
            }
            case SM4:{
                cryptoHandler = SmUtil.sm4();
                break;
            }
            default:
                break;
        }

        return cryptoHandler;
    }

}
