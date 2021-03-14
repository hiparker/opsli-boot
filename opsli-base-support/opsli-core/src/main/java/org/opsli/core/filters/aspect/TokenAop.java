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

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.opsli.common.api.TokenThreadLocal;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.utils.LogUtil;
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


    @Pointcut("execution(public * org.opsli..*.*Controller*.*(..))")
    public void requestMapping() {
    }

    /**
     * 切如 post 请求
     * @param point
     */
    @Around("requestMapping()")
    public Object tokenAop(ProceedingJoinPoint point) throws Throwable {

        // Token
        String requestToken = TokenThreadLocal.get();

        // 如果 ThreadLocal为空 则去当前request中获取
        if(StringUtils.isEmpty(requestToken)){
            // 将 Token放入 线程缓存
            try {
                RequestAttributes ra = RequestContextHolder.getRequestAttributes();
                ServletRequestAttributes sra = (ServletRequestAttributes) ra;
                if(sra != null) {
                    HttpServletRequest request = sra.getRequest();
                    requestToken = UserTokenUtil.getRequestToken(request);
                    if(StringUtils.isNotEmpty(requestToken)){
                        // 放入当前线程缓存中
                        TokenThreadLocal.put(requestToken);
                    }
                }
            }catch (ServiceException e){
                throw e;
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }


        // 计时器
        TimeInterval timer = DateUtil.timer();
        // 执行
        Exception exception = null;
        // 防止线程抛异常 线程变量不回收 导致oom
        Object returnValue;
        try {
            // 执行正常操作
            returnValue = point.proceed();
        }catch (Exception e){
            exception = e;
            throw e;
        } finally {
            // 花费毫秒数
            long timerCount = timer.interval();
            //保存日志
            LogUtil.saveLog(point, exception, timerCount);

            // 线程销毁时 删除 token
            if(StringUtils.isNotEmpty(requestToken)){
                TokenThreadLocal.remove();
            }
        }

        return returnValue;
    }

}
