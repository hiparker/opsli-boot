package org.opsli.common.thread.refuse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * @Author: 一枝花算不算浪漫
 * @CreateTime: 2020-10-08 10:24
 * @Description: 自定义线程执行器 - 线程超时自动拒绝
 */
@Slf4j
public class AsyncProcessorReFuse {

    /**
     * 默认最大并发数<br>
     */
    private static final int DEFAULT_MAX_CONCURRENT = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 线程池名称格式
     */
    private static final String THREAD_POOL_NAME = "ExternalConvertProcessPool-Refuse-%d";

    /**
     * 线程工厂名称
     */
    private static final ThreadFactory FACTORY = new BasicThreadFactory.Builder()
            .namingPattern(THREAD_POOL_NAME)
            .daemon(true).build();

    /**
     * 默认队列大小
     */
    private static final int DEFAULT_SIZE = 500;

    /**
     * 默认线程等待时间 秒
     */
    private static final int DEFAULT_WAIT_TIME = 10;

    /**
     * 默认线程存活时间
     */
    private static final long DEFAULT_KEEP_ALIVE = 60L;

    /**NewEntryServiceImpl.java:689
     * Executor
     */
    private static final ExecutorService EXECUTOR;

    /**
     * 执行队列
     */
    private static final BlockingQueue<Runnable> EXECUTOR_QUEUE = new ArrayBlockingQueue<>(DEFAULT_SIZE);

    static {
        // 创建 Executor
        // 此处默认最大值改为处理器数量的 4 倍
        try {
           EXECUTOR = new ThreadPoolExecutor(DEFAULT_MAX_CONCURRENT, DEFAULT_MAX_CONCURRENT * 4, DEFAULT_KEEP_ALIVE,
               TimeUnit.SECONDS, EXECUTOR_QUEUE, FACTORY);

           // 关闭事件的挂钩
           Runtime.getRuntime().addShutdownHook(new Thread(() -> {
              log.info("AsyncProcessor shutting down.");

              EXECUTOR.shutdown();

              try {
                 // 等待1秒执行关闭
                 if (!EXECUTOR.awaitTermination(DEFAULT_WAIT_TIME, TimeUnit.SECONDS)) {
                     log.error("AsyncProcessor shutdown immediately due to wait timeout.");
                     EXECUTOR.shutdownNow();
                 }
              } catch (InterruptedException e) {
                 log.error("AsyncProcessor shutdown interrupted.");
                 EXECUTOR.shutdownNow();
              }

              log.info("AsyncProcessor shutdown complete.");
           }));
        } catch (Exception e) {
            log.error("AsyncProcessor init error.", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 此类型无法实例化
     */
    private AsyncProcessorReFuse() {
    }

    /**
     * 执行任务，不管是否成功<br>
     * 其实也就是包装以后的 {@link } 方法
     *
     * @param task
     * @return
     */
    public static boolean executeTask(Runnable task) {
        try {
            EXECUTOR.execute(task);
        } catch (RejectedExecutionException e) {
            log.error("Task executing was rejected.", e);
            return false;
        }
        return true;
    }

    /**
     * 提交任务，并可以在稍后获取其执行情况<br>
     * 当提交失败时，会抛出 {@link }
     *
     * @param task
     * @return
     */
    public static <T> Future<T> submitTask(Callable<T> task) {
        try {
            return EXECUTOR.submit(task);
        } catch (RejectedExecutionException e) {
            log.error("Task executing was rejected.", e);
            throw new UnsupportedOperationException("Unable to submit the task, rejected.", e);
        }
    }
}
