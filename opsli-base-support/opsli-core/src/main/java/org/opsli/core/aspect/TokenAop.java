package org.opsli.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.opsli.common.api.TokenThreadLocal;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.utils.UserTokenUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.opsli.common.constants.OrderConstants.TOKEN_AOP_SORT;

/**
 * 参数校验 拦截处理
 *
 * @author parker
 * @date 2020-09-16
 */
@Slf4j
@Order(TOKEN_AOP_SORT)
@Aspect
@Component
public class TokenAop {


    @Pointcut("execution(public * org.opsli.modulars*..*.*Controller*.*(..))")
    public void requestMapping() {
    }

    /**
     * 切如 post 请求
     * @param point
     */
    @Around("requestMapping()")
    public Object tokenAop(ProceedingJoinPoint point) throws Throwable {

        // 将 Token放入 线程缓存
        try {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletRequest request = sra.getRequest();
            String requestToken = UserTokenUtil.getRequestToken(request);
            if(StringUtils.isNotEmpty(requestToken)){
                // 放入当前线程缓存中
                TokenThreadLocal.put(requestToken);
            }
        }catch (ServiceException e){
            throw e;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        // 防止线程抛异常 线程变量不回收 导致oom
        Object returnValue = null;
        try {
            // 执行正常操作
            Object[] args= point.getArgs();
            returnValue = point.proceed(args);
        } finally {
            // 线程销毁时 删除 token
            TokenThreadLocal.remove();
        }

        return returnValue;
    }

}
