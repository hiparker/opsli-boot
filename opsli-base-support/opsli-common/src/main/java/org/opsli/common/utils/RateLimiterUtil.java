package org.opsli.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.concurrent.ConcurrentMap;

/**
 * @BelongsProject: think-bboss-parent
 * @BelongsPackage: com.think.bboss.common.utils
 * @Author: Parker
 * @CreateTime: 2021-01-05 16:06
 * @Description: 单机限流
 */
@Slf4j
public final class RateLimiterUtil {

    /** 默认QPS */
    public static final double DEFAULT_QPS = 10d;

    /** 默认等待时长 */
    private static final int DEFAULT_WAIT = 5000;
    /** key-value (service,Qps) ，IP的限制速率 */
    private static final ConcurrentMap<String,Double> IP_MAP;
    /** userkey-service,limiter ,限制用户对接口的访问速率 */
    private static final ConcurrentMap<String, RateLimiter> IP_LIMITER_MAP;

    static{
        IP_MAP = Maps.newConcurrentMap();
        IP_LIMITER_MAP = Maps.newConcurrentMap();
    }

    /**
     * 修改IP QPS
     * @param ip
     * @param qps
     */
    public static void updateIpQps(String ip, double qps) {
        IP_MAP.put(ip,qps);
    }

    /**
     * 删除IP
     * @param ip
     */
    public static void removeIp(String ip) {
        IP_MAP.remove(ip);
        IP_LIMITER_MAP.remove(ip);
    }

    /**
     * 方法进入
     * @param request
     * @return
     */
    public static boolean enter(HttpServletRequest request) {
        // 获得IP
        String clientIpAddress = IPUtil.getClientIpAddress(request);
        return RateLimiterUtil.enter(clientIpAddress);
    }

    /**
     * 方法进入
     * @param request
     * @return
     */
    public static boolean enter(HttpServletRequest request, Double dfQps) {
        // 获得IP
        String clientIpAddress = IPUtil.getClientIpAddress(request);
        return RateLimiterUtil.enter(clientIpAddress, dfQps);
    }

    /**
     * 方法进入
     * @param clientIpAddress IP
     * @return
     */
    public static boolean enter(String clientIpAddress) {
        return RateLimiterUtil.enter(clientIpAddress, null);
    }

    /**
     * 方法进入
     * @param clientIpAddress IP
     * @param dfQps 手动指派QPS
     * @return
     */
    public static boolean enter(String clientIpAddress, Double dfQps) {
        // 计时器
        TimeInterval timer = DateUtil.timer();

        Double qps = IP_MAP.get(clientIpAddress);
        // 如果当前MAP IP为空， 且指派IP不为空 则自动赋值
        if(qps == null && dfQps != null){
            RateLimiterUtil.updateIpQps(clientIpAddress, dfQps);
            qps = dfQps;
        }

        //不限流
        if (qps == null || qps == 0.0) {
            return true;
        }

        RateLimiter rateLimiter = IP_LIMITER_MAP.get(clientIpAddress);
        // 如果为空 则创建一个新的限流器
        if (rateLimiter == null) {
            rateLimiter = RateLimiter.create(qps);

            RateLimiter putByOtherThread = IP_LIMITER_MAP.putIfAbsent(clientIpAddress, rateLimiter);
            if (putByOtherThread != null) {
                rateLimiter = putByOtherThread;
            }
            rateLimiter.setRate(qps);
        }

        //非阻塞
        if (!rateLimiter.tryAcquire(Duration.ofMillis(DEFAULT_WAIT))) {
            // 花费毫秒数
            long timerCount = timer.interval();
            //限速中，提示用户
            log.info("限流器 耗时: "+ timerCount + "ms, 访问过频繁: " + clientIpAddress);
            return false;
        } else {
            // 花费毫秒数
            long timerCount = timer.interval();
            //正常访问
            log.info("限流器 耗时: "+ timerCount + "ms");
            return true;
        }
    }


    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                boolean enter = RateLimiterUtil.enter("127.0.0.1", RateLimiterUtil.DEFAULT_QPS);
                System.out.println(enter);
            }).start();
        }
    }

}
