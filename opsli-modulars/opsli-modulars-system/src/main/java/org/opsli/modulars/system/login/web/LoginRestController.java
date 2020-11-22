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

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.api.TokenThreadLocal;
import org.opsli.common.utils.IPUtil;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.utils.CaptchaUtil;
import org.opsli.core.utils.UserTokenUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.login.entity.LoginForm;
import org.opsli.modulars.system.tenant.entity.SysTenant;
import org.opsli.modulars.system.tenant.service.ITenantService;
import org.opsli.modulars.system.user.service.IUserService;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Date;
import java.util.List;
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

    /** 账号失败次数 */
    private static final String ACCOUNT_SLIP_COUNT_PREFIX = "opsli:account:slip:count:";
    /** 账号失败锁定KEY */
    private static final String ACCOUNT_SLIP_LOCK_PREFIX = "opsli:account:slip:lock:";

    /** 失败阈值 */
    @Value("${opsli.login.slip-count}")
    private int slipCount;
    /** 锁定时间 */
    @Value("${opsli.login.slip-lock-speed}")
    private int slipLockSpeed;

    @Autowired
    private RedisPlugin redisPlugin;
    @Autowired
    private ITenantService iTenantService;
    @Autowired
    private IUserService iUserService;


    /**
     * 登录
     */
    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping("/sys/login")
    public ResultVo<?> login(@RequestBody LoginForm form, HttpServletRequest request){
        boolean captcha = CaptchaUtil.validate(form.getUuid(), form.getCaptcha());
        // 验证码不正确
        if(!captcha){
            return ResultVo.error(TokenMsg.EXCEPTION_LOGIN_CAPTCHA.getCode(),
                    TokenMsg.EXCEPTION_LOGIN_CAPTCHA.getMessage());
        }

        // 判断账号是否临时锁定
        Long loseTimeMillis = (Long) redisPlugin.get(ACCOUNT_SLIP_LOCK_PREFIX + form.getUsername());
        if(loseTimeMillis != null){
            Date currDate = new Date();
            DateTime loseDate = DateUtil.date(loseTimeMillis);
            // 偏移5分钟
            DateTime currLoseDate = DateUtil.offsetSecond(loseDate, slipLockSpeed);

            // 计算失效剩余时间( 分 )
            long betweenM = DateUtil.between(currLoseDate, currDate, DateUnit.MINUTE);
            if(betweenM > 0){
                String msg = StrUtil.format(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCK.getMessage()
                        ,betweenM + "分钟");
                return ResultVo.error(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCK.getCode(),
                        msg);
            }else{
                // 计算失效剩余时间( 秒 )
                long betweenS = DateUtil.between(currLoseDate, currDate, DateUnit.SECOND);
                String msg = StrUtil.format(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCK.getMessage()
                        ,betweenS + "秒");
                return ResultVo.error(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCK.getCode(),
                        msg);
            }
        }

        //用户信息
        UserModel user = UserUtil.getUserByUserName(form.getUsername());

        //账号不存在、密码错误
        if(user == null ||
                !user.getPassword().equals(UserUtil.handlePassword(form.getPassword(), user.getSecretkey()))) {

            // 如果失败次数 超过阈值 则锁定账号
            Long slipNum = redisPlugin.increment(ACCOUNT_SLIP_COUNT_PREFIX + form.getUsername());
            // 设置失效时间为 5分钟
            redisPlugin.expire(ACCOUNT_SLIP_COUNT_PREFIX + form.getUsername(),slipLockSpeed);

            // 如果确认 都失败 则存入临时缓存
            if(slipNum >= slipCount){
                long currentTimeMillis = System.currentTimeMillis();
                // 存入Redis
                redisPlugin.put(ACCOUNT_SLIP_LOCK_PREFIX + form.getUsername(),
                        currentTimeMillis, slipLockSpeed);
            }

            return ResultVo.error(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_NO.getMessage());
        }

        // 删除失败次数记录
        redisPlugin.del(ACCOUNT_SLIP_COUNT_PREFIX + form.getUsername());

        //账号锁定
        if(user.getLocked() == 1){
            return ResultVo.error(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCKED.getMessage());
        }

        // 如果不是超级管理员 需要验证租户是否生效
        if(!UserUtil.SUPER_ADMIN.equals(user.getUsername())){
            QueryBuilder<SysTenant> queryBuilder = new GenQueryBuilder<>();
            QueryWrapper<SysTenant> queryWrapper = queryBuilder.build();
            queryWrapper.eq("id", user.getTenantId())
                    .eq("iz_usable", "1");
            List<SysTenant> tenants = iTenantService.findList(queryWrapper);
            if(tenants == null || tenants.isEmpty()){
                return ResultVo.error(TokenMsg.EXCEPTION_LOGIN_TENANT_NOT_USABLE.getMessage());
            }
        }

        // 删除验证过后验证码
        CaptchaUtil.delCaptcha(form.getUuid());

        //生成token，并保存到Redis
        ResultVo<Map<String, Object>> resultVo = UserTokenUtil.createToken(user);
        if(resultVo.isSuccess()){
            try {
                // 临时设置 token缓存
                TokenThreadLocal.put(String.valueOf(resultVo.getData().get("token")));
                // 保存用户最后登录IP
                String clientIpAddress = IPUtil.getClientIpAddress(request);
                user.setLoginIp(clientIpAddress);
                iUserService.updateLoginIp(user);
            }catch (Exception ignored){}
            finally {
                // 清空 token缓存
                TokenThreadLocal.remove();
            }
        }

        return resultVo;
    }


    /**
     * 登出
     */
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
     * 验证码
     */
    @ApiOperation(value = "验证码", notes = "验证码")
    @GetMapping("captcha.jpg")
    public void captcha(String uuid, HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //获取图片验证码
        BufferedImage image = CaptchaUtil.getCaptcha(uuid);
        if(image != null){
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(image, "jpg", out);
            IOUtils.closeQuietly(out);
        }
    }


    public static void main(String[] args) {
        String passwordStr = "Aa123456";
        String password = UserUtil.handlePassword(passwordStr, "system");
        System.out.println(password);
    }
}
