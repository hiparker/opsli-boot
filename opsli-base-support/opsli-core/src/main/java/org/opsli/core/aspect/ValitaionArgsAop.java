package org.opsli.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.api.utils.ValidationUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
    public void doBefore(JoinPoint point) {
        Object[] args = point.getArgs();
        for (Object arg : args) {
           // 参数校验
           if(arg instanceof ApiWrapper){
               ValidationUtil.verify(arg);
           }
        }
    }

}
