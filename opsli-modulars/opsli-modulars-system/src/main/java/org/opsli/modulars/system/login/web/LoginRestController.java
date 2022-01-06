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

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.options.OptionsModel;
import org.opsli.api.wrapper.system.tenant.TenantModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.annotation.LoginCrypto;
import org.opsli.common.annotation.Limiter;
import org.opsli.common.enums.DictType;
import org.opsli.common.thread.AsyncProcessExecutor;
import org.opsli.common.thread.AsyncProcessExecutorFactory;
import org.opsli.core.utils.ValidatorUtil;
import org.opsli.core.api.TokenThreadLocal;
import org.opsli.common.enums.AlertType;
import org.opsli.common.enums.OptionsType;
import org.opsli.common.exception.TokenException;
import org.opsli.common.utils.IPUtil;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.utils.*;
import org.opsli.modulars.system.login.entity.LoginForm;
import org.opsli.modulars.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 登陆 / 登出 / 验证码
 * 不需要继承 api 接口
 *
 * @author parker
 * @date 2020-05-23 13:30
 */
@Api(tags = "登录相关")
@Slf4j
@RestController
public class LoginRestController {

    @Autowired
    private IUserService iUserService;

    /**
     * 登录 登录数据加密
     */
    @Limiter
    @LoginCrypto
    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping("/system/login")
    public ResultVo<UserTokenUtil.TokenRet> login(@RequestBody LoginForm form, HttpServletRequest request){

        // 非空验证
        if(form == null){
            throw new TokenException(TokenMsg.EXCEPTION_LOGIN_NULL);
        }

        // 验证登录对象
        ValidatorUtil.verify(form);

        // 判断账号是否临时锁定
        UserTokenUtil.verifyLockAccount(form.getUsername());

        // 获得当前失败次数
        long slipCount = UserTokenUtil.getSlipCount(form.getUsername());

        // 失败次数超过 验证次数阈值 开启验证码验证
        if(slipCount >= UserTokenUtil.LOGIN_PROPERTIES.getSlipVerifyCount()){
            CaptchaUtil.validate(form.getUuid(), form.getCaptcha());
        }

        // 用户信息
        UserModel user = UserUtil.getUserByUserName(form.getUsername());

        // 账号不存在、密码错误
        if(user == null ||
                !user.getPassword().equals(UserUtil.handlePassword(form.getPassword(), user.getSecretKey()))) {
            // 判断是否需要锁定账号 这里没有直接抛异常 而是返回错误信息， 其中包含 是否开启验证码状态
            TokenMsg lockAccountMsg = UserTokenUtil.lockAccount(form.getUsername());
            throw new TokenException(lockAccountMsg);
        }

        // 如果验证成功， 则清除锁定信息
        UserTokenUtil.clearLockAccount(form.getUsername());

        // 如果不是超级管理员 并且不是系统系统用户 则进行验证
        if(!StringUtils.equals(UserUtil.SUPER_ADMIN, user.getUsername()) &&
                !TenantUtil.SUPER_ADMIN_TENANT_ID.equals(user.getTenantId())
            ){

            // 账号锁定验证
            if(StringUtils.isEmpty(user.getEnable()) ||
                    DictType.NO_YES_NO.getValue().equals(user.getEnable())){
                // 账号已被锁定,请联系管理员
                throw new TokenException(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCKED);
            }

            // 验证租户是否生效
            TenantModel tenant = TenantUtil.getTenant(user.getTenantId());
            if(tenant == null){
                throw new TokenException(TokenMsg.EXCEPTION_LOGIN_TENANT_NOT_USABLE);
            }

            // 检测用户是否有角色
            List<String> roleModelList = UserUtil.getUserRolesByUserId(user.getId());
            if(CollUtil.isEmpty(roleModelList)){
                // 用户暂无角色，请设置后登录
                throw new TokenException(TokenMsg.EXCEPTION_USER_ROLE_NOT_NULL);
            }

            // 检测用户是否有角色菜单
            List<MenuModel> menuModelList = UserUtil.getMenuListByUserId(user.getId());
            if(CollUtil.isEmpty(menuModelList)){
                // 用户暂无角色菜单，请设置后登录
                throw new TokenException(TokenMsg.EXCEPTION_USER_MENU_NOT_NULL);
            }

            // 检测用户是否有角色权限
            List<String> userAllPermsList = UserUtil.getUserAllPermsByUserId(user.getId());
            if(CollUtil.isEmpty(userAllPermsList)){
                // 用户暂无角色菜单，请设置后登录
                throw new TokenException(TokenMsg.EXCEPTION_USER_PERMS_NOT_NULL);
            }
        }

        // 失败次数超过 验证次数阈值 开启验证码验证
        if(slipCount >= UserTokenUtil.LOGIN_PROPERTIES.getSlipVerifyCount()){
            // 删除验证过后验证码
            CaptchaUtil.delCaptcha(form.getUuid());
        }

        //生成token，并保存到Redis
        ResultVo<UserTokenUtil.TokenRet> resultVo = UserTokenUtil.createToken(user);
        if(resultVo.isSuccess()){
            AsyncProcessExecutor normalExecutor = AsyncProcessExecutorFactory.createNormalExecutor();
            // 异步保存IP
            normalExecutor.put(()->{
                // 保存用户最后登录IP
                String clientIpAddress = IPUtil.getClientIdBySingle(request);
                user.setLoginIp(clientIpAddress);
                iUserService.updateLoginIp(user);
            });
            normalExecutor.execute();
        }
        return resultVo;
    }


    /**
     * 登出
     */
    @Limiter
    @ApiOperation(value = "登出", notes = "登出")
    @PostMapping("/system/logout")
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
    @GetMapping("/system/slipCount")
    public ResultVo<?> slipCount(String username){
        // 获得当前失败次数
        long slipCount = UserTokenUtil.getSlipCount(username);
        Map<String, Object> ret = Maps.newHashMap();
        ret.put("base", UserTokenUtil.LOGIN_PROPERTIES.getSlipVerifyCount());
        ret.put("curr", slipCount);
        return ResultVo.success(ret);
    }


    /**
     * 验证码
     */
    @Limiter(alertType = AlertType.ALERT)
    @ApiOperation(value = "验证码", notes = "验证码")
    @GetMapping("captcha")
    public void captcha(String uuid, HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        if(out != null){
            response.setHeader("Cache-Control", "no-store, no-cache");
            //生成图片验证码
            CaptchaUtil.createCaptcha(uuid, out);
        }
    }

    /**
     * 获得公钥
     */
    @Limiter
    @ApiOperation(value = "获得公钥", notes = "获得公钥")
    @GetMapping("/system/publicKey")
    public ResultVo<?> getPublicKey(){

        // 获得公钥
        OptionsModel option = OptionsUtil.getOptionByCode(OptionsType.CRYPTO_ASYMMETRIC_PUBLIC_KEY);
        if(option != null){
            return ResultVo.success(
                    "操作成功!",
                    option.getOptionValue()
            );
        }

        // 失败
        return ResultVo.error();
    }

    // =================

    public static void main(String[] args) {
        String passwordStr = "Bb123456";
        String password = UserUtil.handlePassword(passwordStr, "z25fk1otoj45ref83shq");
        System.out.println(password);
    }
}
