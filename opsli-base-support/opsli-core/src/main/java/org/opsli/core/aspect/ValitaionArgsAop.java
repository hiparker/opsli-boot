package org.opsli.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.api.utils.ValidationUtil;
import org.opsli.common.annotation.HotDataPut;
import org.opsli.common.exception.ServiceException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static org.opsli.common.constants.OrderConstants.PARAM_VALIDATE_AOP_SORT;

/**
 * 参数校验 拦截处理
 *
 * @author parker
 * @date 2020-09-16
 */
@Slf4j
@Order(PARAM_VALIDATE_AOP_SORT)
@Aspect
@Component
public class ValitaionArgsAop {


    @Pointcut("execution(public * org.opsli.modulars*..*.*Controller*.*(..))")
    public void requestMapping() {
    }

    /**
     * 切如 post 请求
     * @param point
     */
    @Before("requestMapping()")
    public void validation(JoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        try {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletRequest request = sra.getRequest();
            String method = request.getMethod();

            // 只有 post 请求 才会去验证数据
            if("POST".equals(method)){
                for (Object arg : args) {
                    // 参数校验
                    if(arg instanceof ApiWrapper){
                        ValidationUtil.verify(arg);
                    }
                }
            }
        }catch (ServiceException e){
            throw e;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

}
