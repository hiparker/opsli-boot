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
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSONObject;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.msg.CoreMsg;

import java.util.Collection;

/**
 * 非对称加密 RSA 工具类
 *
 * @author 周鹏程
 */
public enum AsymmetricCryptoUtil {

    /** 实例 */
    INSTANCE;

    /** RSA KEY */
    private final String rsaKey = "data";

    /** RSA对象 */
    private final RSA rsa;

    AsymmetricCryptoUtil(){
        // 初始化RSA对象
        this.rsa = this.createRsa();
    }

    /**
     * 生成 RSA 对象
     * @return RSA
     */
    public RSA createRsa(){
        return new RSA();
    }

    /**
     * 生成 RSA 对象
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return RSA
     */
    public RSA createRsa(String publicKey, String privateKey){
        return new RSA(privateKey, publicKey);
    }


    /**
     * 获得公钥
     * @return String
     */
    public String getPublicKey(){
        return rsa.getPublicKeyBase64();
    }

    /**
     * RSA 加密数据
     * @param data 数据
     * @return String
     */
    public String encryptedData(Object data){
        return this.encryptedData(this.rsa, data);
    }


    /**
     * RSA 加密数据
     * @param rsa RSA
     * @param data 数据
     * @return String
     */
    public String encryptedData(RSA rsa, Object data){
        String encryptedStr;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(rsaKey, data);
            encryptedStr = rsa.encryptBase64(StrUtil.bytes(jsonObject.toString(), CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        }catch (Exception e){
            // 加密失败
            throw new ServiceException(CoreMsg.OTHER_EXCEPTION_RSA_EN);
        }
        return encryptedStr;
    }


    /**
     * RSA 解密数据
     * @param data 数据
     * @return Object
     */
    public Object decryptedDataToObj(String data){
        return this.decryptedDataToObj(this.rsa, data);
    }

    /**
     * RSA 解密数据
     * @param rsa RSA
     * @param data 数据
     * @return Object
     */
    public Object decryptedDataToObj(RSA rsa, String data){
        Object obj;
        String decryptedData = AsymmetricCryptoUtil.INSTANCE.decryptedData(rsa, data);
        try{
            obj = JSONObject.parse(decryptedData);
        }catch (Exception e){
            // RSA非对称解密反射失败
            throw new ServiceException(CoreMsg.OTHER_EXCEPTION_RSA_REFLEX);
        }
        return obj;
    }



    /**
     * RSA 解密数据
     * @param data
     * @return String
     */
    public String decryptedData(String data){
        return this.decryptedData(this.rsa, data);
    }



    /**
     * RSA 解密数据
     * @param rsa RSA
     * @param data 数据
     * @return String
     */
    public String decryptedData(RSA rsa,String data){
        //解密,因为编码传值时有空格出现
        data = data.replaceAll(" ", "+");
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
        return decryptStr;
    }

}
