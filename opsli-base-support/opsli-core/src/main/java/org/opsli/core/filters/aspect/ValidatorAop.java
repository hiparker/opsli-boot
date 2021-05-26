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
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.core.utils.ValidatorUtil;
import org.opsli.common.exception.ServiceException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.opsli.common.constants.OrderConstants.VERIFY_ARGS_AOP_SORT;

/**
 * 参数校验 拦截处理
 *
 * @author parker
 * @date 2020-09-16
 */
@Slf4j
@Order(VERIFY_ARGS_AOP_SORT)
@Aspect
@Component
public class ValidatorAop {

    /** post请求 */
    private static final String POST_TYPE = "POST";

    @Pointcut("execution(public * org.opsli.modulars*..*.*Controller*.*(..))")
    public void requestMapping() {
    }

    /**
     * 切如 post 请求
     * @param point point
     */
    @Before("requestMapping()")
    public void validation(JoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        try {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            if(sra == null){
                return;
            }
            HttpServletRequest request = sra.getRequest();
            String method = request.getMethod();

            // 只有 post 请求 才会去验证数据
            if(POST_TYPE.equals(method)){
                for (Object arg : args) {
                    // 参数校验
                    if(arg instanceof ApiWrapper){
                        ApiWrapper apiWrapper = (ApiWrapper) arg;
                        // 如果是内部调用 则不判断参数非法情况
                        if(apiWrapper.getIzApi() != null && !apiWrapper.getIzApi()){
                            ValidatorUtil.verify(arg);
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
