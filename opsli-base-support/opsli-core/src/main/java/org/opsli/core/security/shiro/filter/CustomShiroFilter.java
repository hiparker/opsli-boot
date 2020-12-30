package org.opsli.core.security.shiro.filter;


import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.opsli.api.base.result.ResultVo;
import org.opsli.common.constants.SignConstants;
import org.opsli.common.constants.TokenTypeConstants;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.security.shiro.token.ExternalToken;
import org.opsli.core.security.shiro.token.OAuth2Token;
import org.opsli.core.utils.JwtUtil;
import org.opsli.core.utils.UserTokenUtil;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * oauth2过滤器
 *
 * 就是因为，以前单机，页面的挑转自己能控制，失败的时候定一个错误码：401，
 *
 * 集中登录，集中授权，
 *
 * @author 孙志强

 * @date 2017-05-20 13:00
 */
public class CustomShiroFilter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token
        String token = UserTokenUtil.getRequestToken((HttpServletRequest) request);

        if(StringUtils.isBlank(token)){
            return null;
        }

        // 分析token
        String claim = JwtUtil.getClaim(token, SignConstants.TYPE);

        // 第三方登录
        if(TokenTypeConstants.TYPE_EXTERNAL.equals(claim)){
            return new ExternalToken(token);
        }
        // .... 追加登录方式

        return new OAuth2Token(token);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())){
            return true;
        }
        // remeberMe  ,remeberMe特殊页面，需要授权，

        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //获取请求token，如果token不存在，直接返回401
        String token = UserTokenUtil.getRequestToken((HttpServletRequest) request);
        if(StringUtils.isBlank(token)){
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
            httpResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
            httpResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
            httpResponse.setContentType("application/json; charset=utf-8");
            // 401 Token失效，请重新登录
            ResultVo<Object> error = ResultVo.error(TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY.getCode(),
                    TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY.getMessage());
            httpResponse.getWriter().print(error.toJsonStr());
            return false;
        }

        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        httpResponse.setContentType("application/json; charset=utf-8");

        try {
            //处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            ResultVo<Object> error = ResultVo.error(401, throwable.getMessage());
            httpResponse.getWriter().print(error.toJsonStr());
        } catch (IOException ignored) {}
        return false;
    }

}
