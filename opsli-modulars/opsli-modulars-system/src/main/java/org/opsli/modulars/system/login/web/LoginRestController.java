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
package org.opsli.modulars.system.login.web;

import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.utils.ValidationUtil;
import org.opsli.api.wrapper.system.tenant.TenantModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.annotation.InterfaceEncryptAndDecrypt;
import org.opsli.common.annotation.Limiter;
import org.opsli.common.api.TokenThreadLocal;
import org.opsli.common.enums.AlertType;
import org.opsli.common.exception.TokenException;
import org.opsli.common.thread.refuse.AsyncProcessQueueReFuse;
import org.opsli.common.utils.IPUtil;
import org.opsli.common.utils.OutputStreamUtil;
import org.opsli.core.aspect.InterfaceEncryptAndDecryptAop;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.security.shiro.realm.JwtRealm;
import org.opsli.core.utils.*;
import org.opsli.modulars.system.login.entity.LoginForm;
import org.opsli.modulars.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * 登陆 / 登出 / 验证码
 *
 * @author parker
 * @date 2020-05-23 13:30
 *
 * 不需要继承 api 接口
 *
 */
@Slf4j
@RestController
public class LoginRestController {

    @Autowired
    private IUserService iUserService;


    /**
     * 登录 登录数据加密
     */
    @Limiter
    @InterfaceEncryptAndDecrypt(responseEncrypt = false)
    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping("/sys/login")
    public ResultVo<?> login(@RequestBody LoginForm form, HttpServletRequest request){
        // 非空验证
        if(form == null){
            throw new TokenException(TokenMsg.EXCEPTION_LOGIN_NULL);
        }

        // 验证登录对象
        ValidationUtil.verify(form);

        // 判断账号是否临时锁定
        UserTokenUtil.verifyLockAccount(form.getUsername());

        // 获得当前失败次数
        long slipCount = UserTokenUtil.getSlipCount(form.getUsername());

        // 失败次数超过 验证次数阈值 开启验证码验证
        if(slipCount >= UserTokenUtil.ACCOUNT_SLIP_VERIFY_COUNT){
            CaptchaUtil.validate(form.getUuid(), form.getCaptcha());
        }

        // 用户信息
        UserModel user = UserUtil.getUserByUserName(form.getUsername());

        // 账号不存在、密码错误
        if(user == null ||
                !user.getPassword().equals(UserUtil.handlePassword(form.getPassword(), user.getSecretkey()))) {
            // 判断是否需要锁定账号 这里没有直接抛异常 而是返回错误信息， 其中包含 是否开启验证码状态
            TokenMsg lockAccountMsg = UserTokenUtil.lockAccount(form.getUsername());
            throw new TokenException(lockAccountMsg);
        }

        // 如果验证成功， 则清除锁定信息
        UserTokenUtil.clearLockAccount(form.getUsername());

        // 账号锁定
        if(JwtRealm.LOCK_VAL.equals(user.getLocked())){
            throw new TokenException(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCKED);
        }

        // 如果不是超级管理员 需要验证租户是否生效
        if(!UserUtil.SUPER_ADMIN.equals(user.getUsername())){
            TenantModel tenant = TenantUtil.getTenant(user.getTenantId());
            if(tenant == null){
                throw new TokenException(TokenMsg.EXCEPTION_LOGIN_TENANT_NOT_USABLE);
            }
        }

        // 失败次数超过 验证次数阈值 开启验证码验证
        if(slipCount >= UserTokenUtil.ACCOUNT_SLIP_VERIFY_COUNT){
            // 删除验证过后验证码
            CaptchaUtil.delCaptcha(form.getUuid());
        }

        //生成token，并保存到Redis
        ResultVo<Map<String, Object>> resultVo = UserTokenUtil.createToken(user);
        if(resultVo.isSuccess()){
            // 异步保存IP
            AsyncProcessQueueReFuse.execute(()->{
                // 保存用户最后登录IP
                String clientIpAddress = IPUtil.getClientIpAddress(request);
                user.setLoginIp(clientIpAddress);
                iUserService.updateLoginIp(user);
            });
        }
        return resultVo;
    }


    /**
     * 登出
     */
    @Limiter
    @ApiOperation(value = "登出", notes = "登出")
    @PostMapping("/sys/logout")
    public ResultVo<?> logout() {
        String token = TokenThreadLocal.get();
        // 登出失败，没有授权Token
        if(StringUtils.isEmpty(token)){
            return ResultVo.error(TokenMsg.EXCEPTION_LOGOUT_ERROR.getMessage());
        }
        UserTokenUtil.logout(token);
        return ResultVo.success(TokenMsg.EXCEPTION_LOGOUT_SUCCESS.getMessage());
    }

    /**
     * 获得当前登录失败次数
     */
    @Limiter
    @ApiOperation(value = "获得当前登录失败次数", notes = "获得当前登录失败次数")
    @GetMapping("/sys/slipCount")
    public ResultVo<?> slipCount(String username){
        // 获得当前失败次数
        long slipCount = UserTokenUtil.getSlipCount(username);
        Map<String, Object> ret = Maps.newHashMap();
        ret.put("base", UserTokenUtil.ACCOUNT_SLIP_VERIFY_COUNT);
        ret.put("curr", slipCount);
        return ResultVo.success(ret);
    }


    /**
     * 验证码
     */
    @Limiter(alertType = AlertType.ALERT)
    @ApiOperation(value = "验证码", notes = "验证码")
    @GetMapping("captcha.jpg")
    public void captcha(String uuid, HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        try {
            //获取图片验证码
            BufferedImage image = CaptchaUtil.getCaptcha(uuid);
            if(image != null){
                ServletOutputStream out = response.getOutputStream();
                ImageIO.write(image, "jpg", out);
                IOUtils.closeQuietly(out);
            }
        }catch (RuntimeException e){
            OutputStreamUtil.exceptionResponse(e.getMessage(), response);
        }
    }

    /**
     * 获得公钥
     */
    @Limiter
    @ApiOperation(value = "获得公钥", notes = "获得公钥")
    @GetMapping("/sys/publicKey")
    public ResultVo<?> getPublicKey(){
        return ResultVo.success(
                "操作成功!",
                InterfaceEncryptAndDecryptAop.getRsaPublicKey()
        );
    }



    public static void main(String[] args) {
        String passwordStr = "Aa123456";
        String password = UserUtil.handlePassword(passwordStr, "system");
        System.out.println(password);
    }
}
