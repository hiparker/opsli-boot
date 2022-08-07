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
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.role.RoleMenuRefApi;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.Limiter;
import org.opsli.common.enums.VerificationTypeEnum;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.utils.UserUtil;
import org.opsli.core.utils.VerificationCodeUtil;
import org.opsli.modulars.tools.common.bean.VerificationCodeBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * 操作身份认证 Controller
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Api(tags = RoleMenuRefApi.TITLE)
@Slf4j
@AllArgsConstructor
@ApiRestController("/{ver}/operation/auth")
public class OperationAuthRestController {


    private final VerificationCodeBean verificationCodeBean;


    /**
     * 生成Email 验证码
     * 直接获取当前用户
     */
    @Limiter(qps = 1)
    @ApiOperation(value = "生成Email 验证码(自身)", notes = "生成Email 验证码(自身)")
    @PostMapping("/email/create-code/{type}")
    public ResultWrapper<?> createEmailCode(@PathVariable String type) {
        UserModel user = UserUtil.getUser();

        // 验证type
        VerificationTypeEnum.getEnumByType(type)
                .orElseThrow(() -> new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_ARGS_NULL));

        // 发送邮件
        try {
            verificationCodeBean.sendEmailCode(user.getEmail(), type);
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
     * 验证 Email 验证码
     * 直接获取当前用户
     *
     * @return ResultWrapper<String> 授权码
     */
    @Limiter(qps = 1)
    @ApiOperation(value = "验证Email 验证码(自身)", notes = "验证Email 验证码(自身)")
    @PostMapping("/email/check-code/{type}/{code}")
    public ResultWrapper<String> checkEmailCode(@PathVariable String type, @PathVariable String code) {
        UserModel user = UserUtil.getUserBySource();

        // 验证type
        VerificationTypeEnum.getEnumByType(type)
                .orElseThrow(() -> new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_ARGS_NULL));

        String certificate;

        // 验证 邮箱验证码，同时生成一个验证后的唯一凭证
        try {
            certificate = VerificationCodeUtil.checkEmailCodeToCreateCertificate(user.getEmail(), code, type);
        }catch (ServiceException e){
            return ResultWrapper.getCustomResultWrapper(
                    e.getCode(), e.getErrorMessage());
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return ResultWrapper.getErrorResultWrapper();
        }
        return ResultWrapper.getSuccessResultWrapper(certificate);
    }


    /**
     * 生成Mobile 验证码
     * 直接获取当前用户
     */
    @Limiter(qps = 1)
    @ApiOperation(value = "生成Mobile 验证码(自身)", notes = "生成Mobile 验证码(自身)")
    @PostMapping("/mobile/create-code/{type}")
    public ResultWrapper<?> createMobileCode(@PathVariable String type) {
        UserModel user = UserUtil.getUser();

        // 验证type
        VerificationTypeEnum.getEnumByType(type)
                .orElseThrow(() -> new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_ARGS_NULL));

        try {
            verificationCodeBean.sendMobileCode(user.getMobile(), type);
        }catch (ServiceException e){
            return ResultWrapper.getCustomResultWrapper(
                    e.getCode(), e.getErrorMessage());
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return ResultWrapper.getErrorResultWrapper();
        }
        return ResultWrapper.getSuccessResultWrapperByMsg("发送短信成功");
    }

    /**
     * 验证 Mobile 验证码
     * 直接获取当前用户
     *
     * @return ResultWrapper<String> 授权码
     */
    @Limiter(qps = 1)
    @ApiOperation(value = "验证 Mobile 验证码(自身)", notes = "验证 Mobile 验证码(自身)")
    @PostMapping("/mobile/check-code/{type}/{code}")
    public ResultWrapper<String> checkMobileCode(@PathVariable String type, @PathVariable String code) {
        UserModel user = UserUtil.getUserBySource();

        // 验证type
        VerificationTypeEnum.getEnumByType(type)
                .orElseThrow(() -> new ServiceException(TokenMsg.EXCEPTION_CAPTCHA_ARGS_NULL));

        String certificate;
        // 验证 手机验证码，同时生成一个验证后的唯一凭证
        try {
            certificate =
                    VerificationCodeUtil
                            .checkMobileCodeToCreateCertificate(user.getMobile(), code, type);
        }catch (ServiceException e){
            return ResultWrapper.getCustomResultWrapper(
                    e.getCode(), e.getErrorMessage());
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return ResultWrapper.getErrorResultWrapper();
        }
        return ResultWrapper.getSuccessResultWrapper(certificate);
    }


}
