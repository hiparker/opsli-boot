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
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.opsli.api.base.encrypt.BaseEncrypt;
import org.opsli.api.base.result.ResultVo;
import org.opsli.common.annotation.InterfaceCrypto;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.Props;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.EncryptAndDecryptByRsaUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import static org.opsli.common.constants.OrderConstants.ENCRYPT_ADN_DECRYPT_AOP_SORT;

/**
 * 接口请求加解密 拦截处理
 *
 * @author parker
 * @date 2021-01-23
 */
@Slf4j
@Order(ENCRYPT_ADN_DECRYPT_AOP_SORT)
@Aspect
@Component
public class InterfaceEncryptAndDecryptAop {

    /** RSA 公钥 */
    private static String RSA_PUBLIC_KEY;
    /** RSA 私钥 */
    private static String RSA_PRIVATE_KEY;
    /** RSA */
    private static RSA ASSIGN_RSA;
    static {
        // 缓存前缀
        Props props = new Props("application.yaml");
        RSA_PUBLIC_KEY = props.getStr("opsli.encrypt-decrypt.rsa.public-key");
        RSA_PRIVATE_KEY = props.getStr("opsli.encrypt-decrypt.rsa.private-key");
        try {
            ASSIGN_RSA = EncryptAndDecryptByRsaUtil.INSTANCE.createRsa(RSA_PUBLIC_KEY, RSA_PRIVATE_KEY);
        }catch (Exception e){
            ASSIGN_RSA = EncryptAndDecryptByRsaUtil.INSTANCE.createRsa();
            RSA_PUBLIC_KEY = ASSIGN_RSA.getPublicKeyBase64();
            RSA_PRIVATE_KEY = ASSIGN_RSA.getPrivateKeyBase64();
            String errorMsg = StrUtil.format(CoreMsg.OTHER_EXCEPTION_RSA_CREATE.getMessage(),
                    RSA_PUBLIC_KEY, RSA_PRIVATE_KEY
                    );
            log.error(errorMsg);
        }
    }

    /**
     * 获得公钥
     * @return
     */
    public static String getRsaPublicKey() {
        return RSA_PUBLIC_KEY;
    }

    /**
     * 获得私钥
     * @return
     */
    public static String getRsaPrivateKey() {
        return RSA_PRIVATE_KEY;
    }

    @Pointcut("@annotation(org.opsli.common.annotation.InterfaceCrypto)")
    public void encryptAndDecrypt() {
    }

    /**
     * 切如 post 请求
     * @param point
     */
    @Around("encryptAndDecrypt()")
    public Object encryptAndDecryptHandle(ProceedingJoinPoint point) throws Throwable {
        // 获得请求参数
        Object[] args = point.getArgs();
        // 返回结果
        Object returnValue = null;

        MethodSignature signature = (MethodSignature) point.getSignature();
        // 获得 方法
        Method  method = signature.getMethod();
        // 获得方法注解
        InterfaceCrypto annotation =
                method.getAnnotation(InterfaceCrypto.class);
        if(annotation != null){

            // 1. 拆解请求数据
            // request 解密
            if (annotation.enable() && annotation.requestDecrypt()){
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    // 参数校验
                    if(arg instanceof BaseEncrypt){
                        // 获得加密数据
                        BaseEncrypt baseEncrypt = (BaseEncrypt) arg;
                        String encryptData = baseEncrypt.getEncryptData();
                        // 解密对象
                        Object dataToObj = EncryptAndDecryptByRsaUtil.INSTANCE.decryptedDataToObj(ASSIGN_RSA, encryptData);

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

            // 2. 执行方法
            returnValue = point.proceed(args);

            // 3. 返回响应数据
            // response 加密
            if (annotation.enable() && annotation.responseEncrypt()){
                try {
                    // 执行加密过程
                    if(returnValue instanceof ResultVo){
                        ResultVo<Object> ret = (ResultVo<Object>) returnValue;
                        ret.setData(
                                EncryptAndDecryptByRsaUtil.INSTANCE.encryptedData(
                                        ASSIGN_RSA, ret.getData()
                                )
                        );
                        returnValue = ret;
                    }else {
                        returnValue = EncryptAndDecryptByRsaUtil.INSTANCE.encryptedData(
                                ASSIGN_RSA, returnValue
                        );
                    }
                }catch (Exception e){
                    // RSA非对称加密失败
                    throw new ServiceException(CoreMsg.OTHER_EXCEPTION_RSA_EN);
                }
            }
        }
        return returnValue;
    }

}
