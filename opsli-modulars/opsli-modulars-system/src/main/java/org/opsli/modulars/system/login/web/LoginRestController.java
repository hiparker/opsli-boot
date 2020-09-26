package org.opsli.modulars.system.login.web;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.api.TokenThreadLocal;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.utils.CaptchaUtil;
import org.opsli.core.utils.UserTokenUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.login.entity.LoginForm;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

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


    /**
     * 登录
     */
    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping("/sys/login")
    public ResultVo<?> login(@RequestBody LoginForm form){
        boolean captcha = CaptchaUtil.validate(form.getUuid(), form.getCaptcha());
        // 验证码不正确
        if(!captcha){
            return ResultVo.error(TokenMsg.EXCEPTION_LOGIN_CAPTCHA.getMessage());
        }

        //用户信息
        UserModel user = UserUtil.getUserByUserName(form.getUsername());;

        //账号不存在、密码错误
        if(user == null ||
                !user.getPassword().equals(new Md5Hash(form.getPassword(), user.getSecretkey()).toHex())) {
            return ResultVo.error(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_NO.getMessage());
        }

        //账号锁定
        if(user.getLocked() == 1){
            return ResultVo.error(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCKED.getMessage());
        }

        //生成token，并保存到Redis
        return UserTokenUtil.createToken(user);
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

}
