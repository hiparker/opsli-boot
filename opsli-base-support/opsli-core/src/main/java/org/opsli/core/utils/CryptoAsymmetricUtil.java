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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.other.crypto.OtherCryptoAsymmetricRestApi;
import org.opsli.api.wrapper.system.other.crypto.OtherCryptoAsymmetricModel;
import org.opsli.common.enums.CryptoAsymmetricType;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.msg.CoreMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.utils
 * @Author: Parker
 * @CreateTime: 2020-09-19 20:03
 * @Description: 非对称加密工具类
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class CryptoAsymmetricUtil {

    /** 前缀 */
    public static final String PREFIX_CODE = "crypto:asymmetric:code:";

    /** 非对称加密 Api */
    private static OtherCryptoAsymmetricRestApi otherCryptoAsymmetricRestApi;

    /** Crypto KEY */
    private static final String CRYPTO_KEY = "data";

    /**
     * 根据 cryptoAsymmetricType 枚举 获得参数
     * @param cryptoAsymmetricType
     * @return
     */
    public static OtherCryptoAsymmetricModel getCryptoAsymmetric(final CryptoAsymmetricType cryptoAsymmetricType){
        if(cryptoAsymmetricType == null){
            return null;
        }

        String typeCode = cryptoAsymmetricType.getCode();

        // 缓存Key
        String cacheKey = PREFIX_CODE + typeCode;

        // 先从缓存里拿
        OtherCryptoAsymmetricModel model = CacheUtil.getTimed(OtherCryptoAsymmetricModel.class, cacheKey);
        if (model != null){
            return model;
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKey);
        if(hasNilFlag){
            return null;
        }

        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKey)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return null;
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            model = CacheUtil.getTimed(OtherCryptoAsymmetricModel.class, cacheKey);
            if (model != null){
                return model;
            }

            // 查询数据库
            ResultVo<OtherCryptoAsymmetricModel> resultVo = otherCryptoAsymmetricRestApi.getByCryptoType(typeCode);
            if(resultVo.isSuccess()){
                model = resultVo.getData();
                // 存入缓存
                CacheUtil.put(cacheKey, model);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKey);
        }

        if(model == null){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKey);
            return null;
        }

        return model;
    }


    // ============== 刷新缓存 ==============

    /**
     * 刷新参数 - 删就完了
     * @param model
     * @return
     */
    public static boolean refresh(final OtherCryptoAsymmetricModel model){
        if(model == null || StringUtils.isEmpty(model.getCryptoType())){
            return true;
        }

        // 计数器
        int count = 0;

        OtherCryptoAsymmetricModel tmpModel = CacheUtil.getTimed(OtherCryptoAsymmetricModel.class, PREFIX_CODE + model.getCryptoType());
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_CODE + model.getCryptoType());

        // 只要不为空 则执行刷新
        if (hasNilFlag){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(PREFIX_CODE + model.getCryptoType());
            if(tmp){
                count--;
            }
        }

        if(tmpModel != null){
            count++;
            // 先删除
            boolean tmp = CacheUtil.del(PREFIX_CODE + model.getCryptoType());
            if(tmp){
                count--;
            }
        }

        return count == 0;
    }


    /**
     * 创建公私钥
     * @param cryptoAsymmetricType 枚举
     * @return
     */
    public static OtherCryptoAsymmetricModel create(final CryptoAsymmetricType cryptoAsymmetricType){
        OtherCryptoAsymmetricModel model = new OtherCryptoAsymmetricModel();
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
     * @param cryptoAsymmetricType 枚举
     * @return
     */
    public static String encrypt(final CryptoAsymmetricType cryptoAsymmetricType, final Object data){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CRYPTO_KEY, data);

        // 原始/加密 数据
        String encryptedStr = jsonObject.toString();

        OtherCryptoAsymmetricModel cryptoAsymmetric = getCryptoAsymmetric(cryptoAsymmetricType);

        // 如果找不到 公私钥 直接返回原始数据
        if(cryptoAsymmetric == null){
            return encryptedStr;
        }

        try {
            switch (cryptoAsymmetricType){
                case RSA:
                    RSA rsa = SecureUtil.rsa(cryptoAsymmetric.getPrivateKey(), cryptoAsymmetric.getPublicKey());
                    encryptedStr = rsa.encryptBase64(
                            StrUtil.bytes(encryptedStr, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
                    break;
                case SM2:
                    SM2 sm2 = SmUtil.sm2(cryptoAsymmetric.getPrivateKey(), cryptoAsymmetric.getPublicKey());
                    encryptedStr = sm2.encryptBase64(
                            StrUtil.bytes(encryptedStr, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
                    break;
                case ECIES:
                    ECIES ecies = new ECIES(cryptoAsymmetric.getPrivateKey(), cryptoAsymmetric.getPublicKey());
                    encryptedStr = ecies.encryptBase64(
                            StrUtil.bytes(encryptedStr, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            // 加密失败
            throw new ServiceException(CoreMsg.OTHER_EXCEPTION_RSA_EN);
        }
        return encryptedStr;
    }

    /**
     * 加密数据
     * @param cryptoAsymmetricType 枚举
     * @return
     */
    public static String decrypt(final CryptoAsymmetricType cryptoAsymmetricType, final String data){
        if(StringUtils.isEmpty(data)){
            return null;
        }

        OtherCryptoAsymmetricModel cryptoAsymmetric = getCryptoAsymmetric(cryptoAsymmetricType);
        // 如果找不到 公私钥 直接返回原始数据
        if(cryptoAsymmetric == null){
            return data;
        }

        String decryptStr;
        try {
            switch (cryptoAsymmetricType){
                case RSA:
                    RSA rsa = SecureUtil.rsa(cryptoAsymmetric.getPrivateKey(), cryptoAsymmetric.getPublicKey());
                    decryptStr = rsa.decryptStr(data, KeyType.PrivateKey);
                    break;
                case SM2:
                    SM2 sm2 = SmUtil.sm2(cryptoAsymmetric.getPrivateKey(), cryptoAsymmetric.getPublicKey());
                    decryptStr = sm2.decryptStr(data, KeyType.PrivateKey);
                    break;
                case ECIES:
                    ECIES ecies = new ECIES(cryptoAsymmetric.getPrivateKey(), cryptoAsymmetric.getPublicKey());
                    decryptStr = ecies.decryptStr(data, KeyType.PrivateKey);
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            // 加密失败
            throw new ServiceException(CoreMsg.OTHER_EXCEPTION_RSA_EN);
        }



        //解密,因为编码传值时有空格出现
        String decryptStr;
        try{
            String tmp = rsa.decryptStr(data, KeyType.PrivateKey);
            JSONObject jsonObject = JSONObject.parseObject(tmp);
            Object obj = jsonObject.get(rsaKey);
            if(obj instanceof Collection){
                decryptStr = jsonObject.getJSONArray(rsaKey).toJSONString();
            }else{
                decryptStr = jsonObject.getJSONObject(rsaKey).toJSONString();
            }
        }catch (Exception e){
            // 解密失败
            throw new ServiceException(CoreMsg.OTHER_EXCEPTION_RSA_DE);
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CRYPTO_KEY, data);

        // 原始/加密 数据
        String encryptedStr = jsonObject.toString();

        OtherCryptoAsymmetricModel cryptoAsymmetric = getCryptoAsymmetric(cryptoAsymmetricType);

        // 如果找不到 公私钥 直接返回原始数据
        if(cryptoAsymmetric == null){
            return encryptedStr;
        }

        try {
            switch (cryptoAsymmetricType){
                case RSA:
                    RSA rsa = SecureUtil.rsa(cryptoAsymmetric.getPrivateKey(), cryptoAsymmetric.getPublicKey());
                    encryptedStr = rsa.encryptBase64(
                            StrUtil.bytes(encryptedStr, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
                    break;
                case SM2:
                    SM2 sm2 = SmUtil.sm2(cryptoAsymmetric.getPrivateKey(), cryptoAsymmetric.getPublicKey());
                    encryptedStr = sm2.encryptBase64(
                            StrUtil.bytes(encryptedStr, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
                    break;
                case ECIES:
                    ECIES ecies = new ECIES(cryptoAsymmetric.getPrivateKey(), cryptoAsymmetric.getPublicKey());
                    encryptedStr = ecies.encryptBase64(
                            StrUtil.bytes(encryptedStr, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            // 加密失败
            throw new ServiceException(CoreMsg.OTHER_EXCEPTION_RSA_EN);
        }
        return encryptedStr;
    }




    // =====================================

    @Autowired
    public static void setOtherCryptoAsymmetricRestApi(OtherCryptoAsymmetricRestApi otherCryptoAsymmetricRestApi) {
        CryptoAsymmetricUtil.otherCryptoAsymmetricRestApi = otherCryptoAsymmetricRestApi;
    }

}
