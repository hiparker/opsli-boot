package org.opsli.common.thread;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * 自定义线程执行器 - 等待线程执行完毕不拒绝
 *
 * @author 周鹏程
 * @date 2020-10-08 10:24
 */
@Slf4j
public class AsyncProcessor {

    /**
     * 默认最大并发数<br>
     */
    private static final int DEFAULT_MAX_CONCURRENT = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 线程池名称格式
     */
    private static final String THREAD_POOL_NAME = "AsyncProcessPool-{}-%d";

    /**
     * 默认队列大小
     */
    private static final int DEFAULT_SIZE = 1024;

    /**
     * 默认线程池关闭等待时间 秒
     */
    private static final int DEFAULT_WAIT_TIME = 10;

    /**
     * 默认线程存活时间
     */
    private static final long DEFAULT_KEEP_ALIVE = 60L;

    /**
     * 执行器
     */
    private ExecutorService execute;


    /**
     * 初始化
     */
    public AsyncProcessor(String key){
        if(StringUtils.isBlank(key)){
            return;
        }

        // 线程工厂名称
        String formatThreadPoolName = StrUtil.format(THREAD_POOL_NAME, key);
        BasicThreadFactory basicThreadFactory = new BasicThreadFactory.Builder()
                .namingPattern(formatThreadPoolName)
                .daemon(true).build();

        // 创建 Executor
        // 此处默认最大值改为处理器数量的 4 倍
        try {
            // 执行队列
            BlockingQueue<Runnable> executorQueue = new ArrayBlockingQueue<>(DEFAULT_SIZE);
            execute = new ThreadPoolExecutor(DEFAULT_MAX_CONCURRENT, DEFAULT_MAX_CONCURRENT * 4, DEFAULT_KEEP_ALIVE,
                    TimeUnit.SECONDS, executorQueue, basicThreadFactory);
            // 这里不会自动关闭线程， 当线程超过阈值时 抛异常
            // 关闭事件的挂钩
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("AsyncProcessorWait 异步处理器关闭");

                execute.shutdown();

                try {
                    // 等待1秒执行关闭
                    if (!execute.awaitTermination(DEFAULT_WAIT_TIME, TimeUnit.SECONDS)) {
                        log.error("AsyncProcessorWait 由于等待超时，异步处理器立即关闭");
                        execute.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    log.error("AsyncProcessorWait 异步处理器关闭中断");
                    execute.shutdownNow();
                }

                log.info("AsyncProcessorWait 异步处理器关闭完成");
            }));
        } catch (Exception e) {
            log.error("AsyncProcessorWait 异步处理器初始化错误", e);
            throw new ExceptionInInitializerError(e);
        }
    }


    /**
     * 执行任务，不管是否成功<br>
     * 其实也就是包装以后的 {@link } 方法
     *
     * @param task
     * @return
     */
    public boolean executeTask(Runnable task) {
        try {
            execute.execute(task);
        } catch (RejectedExecutionException e) {
            log.error("AsyncProcessorWait 执行任务被拒绝", e);
            return false;
        }
        return true;
    }

    /**
     * 提交任务，并可以在稍后获取其执行情况<br>
     * 当提交失败时，会抛出 {@link }
     *
     * @param task 任务
     * @return Future<T>
     */
    public <T> Future<T> submitTask(Callable<T> task) {
        try {
            return execute.submit(task);
        } catch (RejectedExecutionException e) {
            log.error("AsyncProcessorWait 执行任务被拒绝", e);
            throw new UnsupportedOperationException("AsyncProcessorWait 无法提交任务，已被拒绝", e);
        }
    }
}
