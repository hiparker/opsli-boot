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

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.*;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import opsli.plugins.crypto.enums.CryptoAsymmetricType;
import opsli.plugins.crypto.exception.CryptoException;
import opsli.plugins.crypto.model.CryptoAsymmetric;
import opsli.plugins.crypto.msg.CryptoMsg;
import opsli.plugins.crypto.strategy.CryptoAsymmetricService;
import org.apache.commons.lang3.StringUtils;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.utils
 * @Author: Parker
 * @CreateTime: 2020-09-19 20:03
 * @Description: 非对称加密工具类
 */
@Slf4j
public class CryptoAsymmetricAsymmetricImpl implements CryptoAsymmetricService {

    /**
     * 创建空模型
     * @return Model
     */
    @Override
    public CryptoAsymmetric createNilModel() {
        return new CryptoAsymmetric();
    }

    /**
     * 创建公私钥
     * @param cryptoAsymmetricType 枚举
     * @return Model
     */
    @Override
    public CryptoAsymmetric createKeyModel(final CryptoAsymmetricType cryptoAsymmetricType){
        CryptoAsymmetric model = this.createNilModel();
        model.setCryptoType(cryptoAsymmetricType);
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
     * @param model 加解密模型
     * @param data 数据
     * @return String
     */
    @Override
    public String encrypt(final CryptoAsymmetric model, final Object data){
        // 非法验证
        if(model == null ||
                model.getCryptoType() == null ||
                StringUtils.isEmpty(model.getPrivateKey()) ||
                StringUtils.isEmpty(model.getPublicKey())
            ){
            // 配置信息未初始化
            throw new CryptoException(CryptoMsg.CRYPTO_EXCEPTION_MODEL_NULL);
        }

        // 原始/加密 数据
        String encryptedStr;
        try {
            encryptedStr = JSONUtil.toJsonStr(data);

            // 创建执行器
            AbstractAsymmetricCrypto<?> cryptoHandler = this.createCryptoHandler(model);
            if(cryptoHandler == null){
                // 无法获得加解密执行器
                throw new CryptoException(CryptoMsg.CRYPTO_EXCEPTION_HANDLER_NULL);
            }

            // 执行加密操作
            encryptedStr = cryptoHandler.encryptBase64(
                    StrUtil.bytes(encryptedStr, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);

        }catch (JSONException jse){
            // 加密数据转换Json失败
            throw new CryptoException(CryptoMsg.CRYPTO_EXCEPTION_TO_JSON);
        }catch (CryptoException ce){
            // 如果检测到已有异常 则直接抛出
            throw ce;
        }catch (Exception e){
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
    public Object decryptToObj(final CryptoAsymmetric model, final String data){
        Object obj;
        String decryptedData = decrypt(model, data);
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
    public String decrypt(final CryptoAsymmetric model, final String data){
        // 非法验证
        if(model == null ||
                model.getCryptoType() == null ||
                StringUtils.isEmpty(model.getPrivateKey()) ||
                StringUtils.isEmpty(model.getPublicKey())
            ){
            // 配置信息未初始化
            throw new CryptoException(CryptoMsg.CRYPTO_EXCEPTION_MODEL_NULL);
        }

        // 如果解密内容为空 则返回原内容
        if(StringUtils.isEmpty(data)){
            return data;
        }

        String decryptStr;
        try {
            // 创建执行器
            AbstractAsymmetricCrypto<?> cryptoHandler = this.createCryptoHandler(model);
            if(cryptoHandler == null){
                // 无法获得加解密执行器
                throw new CryptoException(CryptoMsg.CRYPTO_EXCEPTION_HANDLER_NULL);
            }

            // 处理数据
            String currData = data.replaceAll(" ", "+");
            // 解密数据 - 返回Json 格式String
            decryptStr = cryptoHandler.decryptStr(currData, KeyType.PrivateKey);
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
     * 创建 加解密执行器
     * @param model 加解密模型
     * @return 执行器
     */
    private AbstractAsymmetricCrypto<?> createCryptoHandler(final CryptoAsymmetric model){
        AbstractAsymmetricCrypto<?> encryptor = null;
        switch (model.getCryptoType()){
            // 注意 这里 switch 使用的是代码块 方法执行完毕后 直接回收对象
            case RSA:{
                encryptor = SecureUtil.rsa(model.getPrivateKey(), model.getPublicKey());
                break;
            }
            case SM2:{
                encryptor = SmUtil.sm2(model.getPrivateKey(), model.getPublicKey());
                break;
            }
            case ECIES:{
                encryptor = new ECIES(model.getPrivateKey(), model.getPublicKey());
                break;
            }
            default:
                break;
        }
        return encryptor;
    }

}
