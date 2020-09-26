package org.opsli.core.filter;

import org.apache.commons.lang3.StringUtils;
import org.opsli.common.api.TokenThreadLocal;
import org.opsli.core.utils.UserTokenUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Token 拦截器 用于存放 token
 */
public class TokenFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 获得请求中 Token
        String requestToken = UserTokenUtil.getRequestToken(request);
        if(StringUtils.isNotEmpty(requestToken)){
            // 放入当前线程缓存中
            TokenThreadLocal.put(requestToken);
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        // 线程销毁时 删除 token
        TokenThreadLocal.remove();
    }
}