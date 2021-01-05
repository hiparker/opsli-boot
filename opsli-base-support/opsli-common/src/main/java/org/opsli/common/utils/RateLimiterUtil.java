package org.opsli.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
    /** 默认缓存个数 超出后流量自动清理 */
    private static final int DEFAULT_CACHE_COUNT = 10_0000;
    /** 默认缓存时效 超出后自动清理 */
    private static final int DEFAULT_CACHE_TIME = 5;
    /** 默认等待时长 */
    private static final int DEFAULT_WAIT = 5000;
    /** 限流器单机缓存 */
    private static final Cache<String, Map<String, RateLimiterInner> > LFU_CACHE;

    static{
        LFU_CACHE = CacheBuilder
                        .newBuilder().maximumSize(DEFAULT_CACHE_COUNT)
                        .expireAfterWrite(DEFAULT_CACHE_TIME, TimeUnit.MINUTES).build();
    }


    /**
     * 删除IP
     * @param ip
     */
    public static void removeIp(String ip) {
        LFU_CACHE.invalidate(ip);
    }

    /**
     * 方法进入
     * @param request
     * @return
     */
    public static boolean enter(HttpServletRequest request) {
        // 获得IP
        String clientIpAddress = IPUtil.getClientIpAddress(request);
        // 获得URI
        String clientURI = request.getRequestURI();
        return RateLimiterUtil.enter(clientIpAddress, clientURI);
    }

    /**
     * 方法进入
     * @param request
     * @return
     */
    public static boolean enter(HttpServletRequest request, Double dfQps) {
        // 获得IP
        String clientIpAddress = IPUtil.getClientIpAddress(request);
        // 获得URI
        String clientURI = request.getRequestURI();
        return RateLimiterUtil.enter(clientIpAddress, clientURI, dfQps);
    }

    /**
     * 方法进入
     * @param clientIpAddress IP
     * @return
     */
    public static boolean enter(String clientIpAddress, String resource) {
        return RateLimiterUtil.enter(clientIpAddress, resource, null);
    }

    /**
     * 方法进入
     * @param clientIpAddress IP
     * @param dfQps 手动指派QPS
     * @return
     */
    public static boolean enter(String clientIpAddress, String resource, Double dfQps) {
        // 计时器
        TimeInterval timer = DateUtil.timer();

        Map<String, RateLimiterInner> rateLimiterInnerMap;
        try {
            rateLimiterInnerMap = LFU_CACHE.get(clientIpAddress, ()->{
                // 当缓存取不到时 重新加载缓存
                Map<String, RateLimiterInner> tmpMap = Maps.newConcurrentMap();
                // 设置限流器
                RateLimiterInner rateLimiterInner = new RateLimiterInner();
                rateLimiterInner.setQps(dfQps);
                rateLimiterInner.setRateLimiter(RateLimiter.create(dfQps));
                tmpMap.put(resource, rateLimiterInner);
                return tmpMap;
            });
        }catch (ExecutionException e){
            log.error(e.getMessage(), e);
            return false;
        }

        RateLimiterInner rateLimiterObj;

        Double qps = dfQps;
        // 初始化过程
        RateLimiterInner rateLimiterInner = rateLimiterInnerMap.get(resource);
        // 如果为空 则创建一个新的限流器
        if(rateLimiterInner == null){
            System.out.println(456);
            rateLimiterInner = new RateLimiterInner();
            rateLimiterInner.setQps(dfQps);
            rateLimiterInner.setRateLimiter(RateLimiter.create(dfQps));
            rateLimiterInnerMap.put(resource, rateLimiterInner);

        }else{
            qps = rateLimiterInner.getQps();
        }
        rateLimiterObj = rateLimiterInner;

        //不限流
        if (qps == null || qps == 0.0) {
            return true;
        }

        RateLimiter rateLimiter = rateLimiterObj.getRateLimiter();

        //非阻塞
        if (!rateLimiter.tryAcquire(Duration.ofMillis(DEFAULT_WAIT))) {
            // 花费毫秒数
            long timerCount = timer.interval();
            //限速中，提示用户
            log.error("限流器 - 访问频繁 耗时: "+ timerCount + "ms, IP地址: " + clientIpAddress + ", URI: " + resource);
            return false;
        } else {
            // 正常访问
            // 花费毫秒数
            long timerCount = timer.interval();
            return true;
        }
    }



    // ==============



    public static void main(String[] args) {
        RateLimiterUtil.removeIp("127.0.0.1");
        for (int i = 0; i < 1000; i++) {
            int j = i;
            new Thread(()->{
                boolean enter = RateLimiterUtil.enter("127.0.0.1","/api/v1", RateLimiterUtil.DEFAULT_QPS);
                System.out.println(enter);
            }).start();
        }
    }

}

/**
 * 限流器
 */
@Data
class RateLimiterInner {

    /** qps */
    private Double qps;

    /** 限流器 */
    private RateLimiter rateLimiter;

}



