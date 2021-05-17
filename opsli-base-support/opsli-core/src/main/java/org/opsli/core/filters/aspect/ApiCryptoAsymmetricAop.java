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
package org.opsli.core.filters.aspect;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;
import lombok.extern.slf4j.Slf4j;
import opsli.plugins.crypto.CryptoPlugin;
import opsli.plugins.crypto.enums.CryptoAsymmetricType;
import opsli.plugins.crypto.model.CryptoAsymmetric;
import opsli.plugins.crypto.strategy.CryptoAsymmetricService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.opsli.api.base.encrypt.BaseEncrypt;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.wrapper.system.options.OptionsModel;
import org.opsli.common.annotation.ApiCryptoAsymmetric;
import org.opsli.common.enums.OptionsType;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.CryptoAsymmetricUtil;
import org.opsli.core.utils.OptionsUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import static org.opsli.common.constants.OrderConstants.ENCRYPT_ADN_DECRYPT_AOP_SORT;

/**
 * Api非对称加解密 拦截处理
 *
 * @author parker
 * @date 2021-01-23
 */
@Slf4j
@Order(ENCRYPT_ADN_DECRYPT_AOP_SORT)
@Aspect
@Component
public class ApiCryptoAsymmetricAop {

    @Pointcut("@annotation(org.opsli.common.annotation.ApiCryptoAsymmetric)")
    public void encryptAndDecrypt() {
    }

    /**
     * 切如 post 请求
     * @param point point
     */
    @SuppressWarnings("unchecked")
    @Around("encryptAndDecrypt()")
    public Object encryptAndDecryptHandle(ProceedingJoinPoint point) throws Throwable {
        // 获得请求参数
        Object[] args = point.getArgs();
        // 返回结果
        Object returnValue;

        MethodSignature signature = (MethodSignature) point.getSignature();
        // 获得 方法
        Method  method = signature.getMethod();
        // 获得方法注解
        ApiCryptoAsymmetric annotation =
                method.getAnnotation(ApiCryptoAsymmetric.class);
        if(annotation != null){
            // TODO 后期引入 享元池设计 防止过度创建对象 先保障能跑起来 2021年5月17日18:23:56  以下全是要优化的地方
            // 获得非对称加解密 执行器
            CryptoAsymmetricService asymmetric = CryptoPlugin.getAsymmetric();
            // 加解密模型
            CryptoAsymmetric cryptoModel = asymmetric.createNilModel();
            // 获得缓存配置
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
            // TODO 都需要优化掉
            cryptoModel.setCryptoType(cryptoType);
            cryptoModel.setPublicKey(cryptoAsymmetricPublicKey.getOptionValue());
            cryptoModel.setPrivateKey(cryptoAsymmetricPrivateKey.getOptionValue());

            // 1. 拆解请求数据
            // request 解密
            if (annotation.enable() && annotation.requestDecrypt()){
                enterDecrypt(args, method, asymmetric, cryptoModel);
            }

            // 2. 执行方法
            returnValue = point.proceed(args);

            // 3. 返回响应数据
            // response 加密
            if (annotation.enable() && annotation.responseEncrypt()){
                returnValue = resultEncrypt(returnValue, asymmetric, cryptoModel);
            }
            return returnValue;


        }else{
            returnValue = point.proceed(args);
        }
        return returnValue;
    }



    /**
     * 入参解密
     * @param args 入参（集合）
     * @param method 方法
     * @param asymmetric 非对称加解密执行器
     * @param cryptoModel 非对称加解密模型
     */
    private void enterDecrypt(Object[] args, Method method, CryptoAsymmetricService asymmetric, CryptoAsymmetric cryptoModel) {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            // 参数校验
            if(arg instanceof BaseEncrypt){
                // 获得加密数据
                BaseEncrypt baseEncrypt = (BaseEncrypt) arg;
                String encryptData = baseEncrypt.getEncryptData();
                // 解密对象
                Object dataToObj = asymmetric.decryptToObj(cryptoModel, encryptData);

                // 根据方法类型转化对象
                Type type = TypeUtil.getParamType(method, i);
                Object obj = Convert.convert(type, dataToObj);
                // 修改缓存中设备数据 空值不覆盖
                Map<String, Object> modelBeanMap = BeanUtil.beanToMap(obj);
                modelBeanMap.entrySet().removeIf(entry -> entry.getValue() == null);

                // 反射赋值
                Field[] fields = ReflectUtil.getFields(arg.getClass());
                for (Field f : fields) {
                    Object val = modelBeanMap.get(f.getName());
                    if(val == null){
                        continue;
                    }

                    //根据需要，将相关属性赋上默认值
                    BeanUtil.setProperty(arg, f.getName(), val);
                }
            }
        }
    }


    /**
     * 出参加密
     * @param returnValue 出参（对象）
     * @param asymmetric 非对称加解密执行器
     * @param cryptoModel 非对称加解密模型
     * @return Object
     */
    private Object resultEncrypt(Object returnValue, CryptoAsymmetricService asymmetric, CryptoAsymmetric cryptoModel) {
        if(returnValue != null){
            try {
                // 执行加密过程
                if(returnValue instanceof ResultVo){
                    // 重新赋值 data
                    ResultVo<Object> ret = (ResultVo<Object>) returnValue;
                    ret.setData(
                            asymmetric.encrypt(cryptoModel, ret.getData())
                    );
                    returnValue = ret;
                }else {
                    returnValue = asymmetric.encrypt(cryptoModel, returnValue);
                    CryptoAsymmetricUtil.encrypt(returnValue);
                }
            }catch (Exception e){
                // 非对称加密失败
                throw new ServiceException(CoreMsg.OTHER_EXCEPTION_CRYPTO_EN);
            }
        }
        return returnValue;
    }

}
