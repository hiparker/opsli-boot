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
package org.opsli.core.filters.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.opsli.common.annotation.Limiter;
import org.opsli.common.enums.AlertType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.OutputStreamUtil;
import org.opsli.common.utils.RateLimiterUtil;
import org.opsli.core.msg.CoreMsg;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static org.opsli.common.constants.OrderConstants.LIMITER_AOP_SORT;

/**
 * 限流器
 *
 * @author Parker
 * @date 2020-09-16
 */
@Slf4j
@Order(LIMITER_AOP_SORT)
@Aspect
@Component
public class LimiterAop {


    @Pointcut("@annotation(org.opsli.common.annotation.Limiter)")
    public void requestMapping() {
    }

    /**
     * 限流
     * @param point point
     */
    @Before("requestMapping()")
    public void limiterHandle(JoinPoint point){
        try {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            if(sra != null) {
                HttpServletRequest request = sra.getRequest();
                HttpServletResponse response = sra.getResponse();
                Limiter limiter = method.getAnnotation(Limiter.class);
                if(limiter != null){
                    AlertType alertType = limiter.alertType();
                    double qps = limiter.qps();

                    // 限流
                    boolean enterFlag = RateLimiterUtil.enter(request, qps);
                    if(!enterFlag){
                        // alert 弹出
                        if(AlertType.ALERT == alertType){
                            OutputStreamUtil.exceptionResponse(
                                    CoreMsg.OTHER_EXCEPTION_LIMITER.getMessage(),
                                    response
                            );
                        }else {
                            // 异常返回
                            throw new ServiceException(CoreMsg.OTHER_EXCEPTION_LIMITER);
                        }
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
