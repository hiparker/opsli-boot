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
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.opsli.core.utils.LogUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.opsli.common.constants.OrderConstants.TOKEN_AOP_SORT;

/**
 * 日志 拦截处理
 *
 * @author parker
 * @date 2020-09-16
 */
@Slf4j
@Order(TOKEN_AOP_SORT)
@Aspect
@Component
public class LogAop {


    @Pointcut("execution(public * org.opsli..*.*Controller*.*(..))")
    public void requestMapping() {
    }

    /**
     * 切如 post 请求
     * @param point point
     */
    @Around("requestMapping()")
    public Object tokenAop(ProceedingJoinPoint point) throws Throwable {
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
        }
        return returnValue;
    }

}
