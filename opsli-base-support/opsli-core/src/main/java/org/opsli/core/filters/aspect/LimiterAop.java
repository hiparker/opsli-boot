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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.atomic.LongAdder;

import static org.opsli.common.constants.OrderConstants.LIMITER_AOP_SORT;

/**
 * 限流器 - 极致性能优化版本
 *
 * 优化要点：
 * 1. 缓存注解信息避免重复解析
 * 2. 快速路径优化
 * 3. 减少对象创建
 * 4. 优化异常处理
 * 5. 添加性能监控
 *
 * @author Pace
 * @date 2020-09-16
 */
@Slf4j
@Order(LIMITER_AOP_SORT)
@Aspect
@Component
public class LimiterAop {

    /** 限流器配置缓存 - 避免重复解析注解 */
    private static final Cache<Method, LimiterConfig> LIMITER_CONFIG_CACHE = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofHours(1))
            .build();

    /** 性能统计 */
    private static final LongAdder PROCESS_COUNT = new LongAdder();
    private static final LongAdder BLOCKED_COUNT = new LongAdder();
    private static final LongAdder ERROR_COUNT = new LongAdder();

    /** 预定义的异常实例，避免重复创建 */
    private static final ServiceException LIMITER_EXCEPTION = new ServiceException(CoreMsg.OTHER_EXCEPTION_LIMITER);

    @Pointcut("@annotation(org.opsli.common.annotation.Limiter)")
    public void requestMapping() {
    }

    /**
     * 限流处理 - 优化版本
     */
    @Before("requestMapping()")
    public void limiterHandle(JoinPoint point) {
        PROCESS_COUNT.increment();

        try {
            // 快速获取请求上下文
            ServletRequestAttributes sra = getServletRequestAttributes();
            if (sra == null) {
                return;
            }

            // 从缓存获取限流配置
            LimiterConfig config = getLimiterConfig(point);
            if (config == null) {
                return;
            }

            // 执行限流检查
            if (!checkRateLimit(sra.getRequest(), config.qps)) {
                BLOCKED_COUNT.increment();
                handleLimitExceeded(sra.getResponse(), config.alertType);
            }

        } catch (ServiceException e) {
            ERROR_COUNT.increment();
            throw e;
        } catch (Exception e) {
            ERROR_COUNT.increment();
            if (log.isDebugEnabled()) {
                log.debug("限流器处理异常: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * 快速获取 ServletRequestAttributes
     */
    private ServletRequestAttributes getServletRequestAttributes() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        return (ra instanceof ServletRequestAttributes) ? (ServletRequestAttributes) ra : null;
    }

    /**
     * 获取限流配置 - 带缓存优化
     */
    private LimiterConfig getLimiterConfig(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        return LIMITER_CONFIG_CACHE.get(method, this::parseLimiterConfig);
    }

    /**
     * 解析限流配置
     */
    private LimiterConfig parseLimiterConfig(Method method) {
        Limiter limiter = method.getAnnotation(Limiter.class);
        if (limiter == null) {
            return null;
        }

        return new LimiterConfig(limiter.qps(), limiter.alertType());
    }

    /**
     * 执行限流检查
     */
    private boolean checkRateLimit(HttpServletRequest request, double qps) {
        return RateLimiterUtil.enter(request, qps);
    }

    /**
     * 处理限流超出
     */
    private void handleLimitExceeded(HttpServletResponse response, AlertType alertType) {
        if (AlertType.ALERT == alertType) {
            // 直接输出响应，避免异常开销
            OutputStreamUtil.exceptionResponse(
                    CoreMsg.OTHER_EXCEPTION_LIMITER.getMessage(),
                    response
            );
        } else {
            // 抛出预定义异常，避免重复创建
            throw LIMITER_EXCEPTION;
        }
    }

    /**
     * 限流配置缓存类 - 不可变对象
     */
    private static final class LimiterConfig {
        final double qps;
        final AlertType alertType;

        LimiterConfig(double qps, AlertType alertType) {
            this.qps = qps;
            this.alertType = alertType;
        }
    }

    /**
     * 获取性能统计信息
     */
    public static LimiterStats getStats() {
        return new LimiterStats(
                PROCESS_COUNT.sum(),
                BLOCKED_COUNT.sum(),
                ERROR_COUNT.sum(),
                LIMITER_CONFIG_CACHE.estimatedSize()
        );
    }

    /**
     * 性能统计数据类
     */
    public static final class LimiterStats {
        public final long processCount;
        public final long blockedCount;
        public final long errorCount;
        public final long cacheSize;
        public final double blockRate;

        LimiterStats(long processCount, long blockedCount, long errorCount, long cacheSize) {
            this.processCount = processCount;
            this.blockedCount = blockedCount;
            this.errorCount = errorCount;
            this.cacheSize = cacheSize;
            this.blockRate = processCount > 0 ? (double) blockedCount / processCount : 0.0;
        }

        @Override
        public String toString() {
            return String.format(
                    "LimiterStats{processCount=%d, blockedCount=%d, errorCount=%d, blockRate=%.2f%%, cacheSize=%d}",
                    processCount, blockedCount, errorCount, blockRate * 100, cacheSize
            );
        }
    }

    /**
     * 清理缓存
     */
    public static void clearCache() {
        LIMITER_CONFIG_CACHE.invalidateAll();
        log.info("限流器缓存已清理");
    }
}
