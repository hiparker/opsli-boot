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
package org.opsli.modulars.tools.common;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.encrypt.EncryptModel;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.role.RoleMenuRefApi;
import org.opsli.api.wrapper.system.options.OptionsModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.Limiter;
import org.opsli.common.enums.OptionsType;
import org.opsli.common.enums.VerificationTypeEnum;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.utils.CryptoUtil;
import org.opsli.core.utils.OptionsUtil;
import org.opsli.core.utils.ValidatorUtil;
import org.opsli.modulars.system.login.dto.EmailCodeModel;
import org.opsli.modulars.system.login.dto.MobileCodeModel;
import org.opsli.modulars.tools.common.bean.VerificationCodeBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 公共 Controller
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Api(tags = RoleMenuRefApi.TITLE)
@Slf4j
@AllArgsConstructor
@ApiRestController("/{ver}/common")
public class CommonRestController {

    private final VerificationCodeBean verificationCodeBean;

    /**
     * 获得公钥
     */
    @Limiter
    @ApiOperation(value = "获得公钥", notes = "获得公钥")
    @GetMapping("/public-key")
    public ResultWrapper<?> getPublicKey(){
        // 获得公钥
        OptionsModel option = OptionsUtil.getOptionByCode(OptionsType.CRYPTO_ASYMMETRIC_PUBLIC_KEY);
        if(option != null){
            return ResultWrapper.getSuccessResultWrapper(option.getOptionValue());
        }

        // 失败
        return ResultWrapper.getErrorResultWrapper();
    }



    /**
     * 生成Email 验证码
     *
     * @param encryptModel 加密参数
     */
    @Limiter(qps = 1)
    @ApiOperation(value = "生成Email 验证码", notes = "生成Email 验证码")
    @PostMapping("/email/create-code")
    public ResultWrapper<?> createEmailCode(@RequestBody EncryptModel encryptModel) {
        // 解密对象
        Object dataToObj = CryptoUtil
                .asymmetricDecryptToObj(encryptModel.getEncryptData());

        // 转换模型
        EmailCodeModel emailCodeModel = WrapperUtil.transformInstance(dataToObj, EmailCodeModel.class);

        // 验证对象
        ValidatorUtil.verify(emailCodeModel);

        // 验证type
        VerificationTypeEnum.getEnumByType(emailCodeModel.getType())
                .orElseThrow(() -> new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_ARGS_NULL));

        // 发送短信
        try {
            verificationCodeBean.sendEmailCode(emailCodeModel.getEmail(), emailCodeModel.getType());
        }catch (ServiceException e){
            return ResultWrapper.getCustomResultWrapper(
                    e.getCode(), e.getErrorMessage());
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return ResultWrapper.getErrorResultWrapper();
        }
        return ResultWrapper.getSuccessResultWrapperByMsg("发送邮件成功");
    }



    /**
     * 生成Mobile 验证码
     *
     * @param encryptModel 加密参数
     */
    @Limiter(qps = 1)
    @ApiOperation(value = "生成Mobile 验证码", notes = "生成Mobile 验证码")
    @PostMapping("/mobile/create-code")
    public ResultWrapper<?> createMobileCode(@RequestBody EncryptModel encryptModel) {
        // 解密对象
        Object dataToObj = CryptoUtil
                .asymmetricDecryptToObj(encryptModel.getEncryptData());

        // 转换模型
        MobileCodeModel mobileCodeModel = WrapperUtil.transformInstance(dataToObj, MobileCodeModel.class);

        // 验证对象
        ValidatorUtil.verify(mobileCodeModel);

        // 验证type
        VerificationTypeEnum.getEnumByType(mobileCodeModel.getType())
                .orElseThrow(() -> new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_ARGS_NULL));

        // 发送短信
        try {
            verificationCodeBean.sendMobileCode(mobileCodeModel.getMobile(), mobileCodeModel.getType());
        }catch (ServiceException e){
            return ResultWrapper.getCustomResultWrapper(
                    e.getCode(), e.getErrorMessage());
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return ResultWrapper.getErrorResultWrapper();
        }
        return ResultWrapper.getSuccessResultWrapperByMsg("发送短信成功");
    }


}
