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
package org.opsli.common.thread;

import cn.hutool.core.util.StrUtil;
import com.google.common.util.concurrent.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 自定义线程执行器 - 等待线程执行完毕不拒绝
 *
 * @author Parker
 * @date 2020-10-08 10:24
 */
@Slf4j
public class AsyncProcessor {

    /**
     * 线程池名称格式
     */
    private static final String THREAD_POOL_NAME = "AsyncProcessorWaitPool-{}-%d";

    /**
     * 默认线程池关闭等待时间 秒
     */
    private static final int DEFAULT_WAIT_TIME = 10;

    /**
     * 线程池监听执行器
     */
    private ListeningExecutorService execute;

    /**
     * 初始化
     * @param key 线程池标识
     */
    public void init(String key){
        if(StringUtils.isBlank(key)){
            return;
        }

        // 线程工厂名称
        String formatThreadPoolName = StrUtil.format(THREAD_POOL_NAME, key);

        // 创建 Executor
        // 此处默认最大值改为处理器数量的 4 倍
        try {
            // 监听执行器
            execute = MoreExecutors.listeningDecorator(
                    ThreadPoolFactory.createDefThreadPool(formatThreadPoolName));

            // 这里不会自动关闭线程， 当线程超过阈值时 抛异常
            // 关闭事件的挂钩
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("ProcessorWait 异步处理器关闭");

                execute.shutdown();

                try {
                    // 等待1秒执行关闭
                    if (!execute.awaitTermination(DEFAULT_WAIT_TIME, TimeUnit.SECONDS)) {
                        log.error("ProcessorWait 由于等待超时，异步处理器立即关闭");
                        execute.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    log.error("ProcessorWait 异步处理器关闭中断");
                    execute.shutdownNow();
                }

                log.info("ProcessorWait 异步处理器关闭完成");
            }));
        } catch (Exception e) {
            log.error("ProcessorWait 异步处理器初始化错误", e);
            throw new ExceptionInInitializerError(e);
        }
    }


    /**
     * 执行任务，不管是否成功<br>
     * 其实也就是包装以后的 {@link } 方法
     *
     * @param task 任务
     * @return boolean
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
     */
    public <T> void executeTaskAndCallback(Callable<T> task, Function<CallbackResult<T>, Void> callback) {
        ListenableFuture<T> future = execute.submit(task);
        Futures.addCallback(future, new FutureCallback<T>() {
            @Override
            public void onSuccess(T result) {
                CallbackResult<T> callbackResult = new CallbackResult<>();
                callbackResult.setSuccess(true);
                callbackResult.setResult(result);
                // 线程池失败后 返回该 Runnable
                callback.apply(callbackResult);
            }

            @Override
            public void onFailure(Throwable t) {
                log.error("线程名称：{} - 执行异常信息：{}", Thread.currentThread().getName(), t.getMessage());
                CallbackResult<T> callbackResult = new CallbackResult<>();
                callbackResult.setSuccess(false);
                callback.apply(callbackResult);
            }
        }, execute);
    }

    // =================

    /**
     * 回调结果
     * @param <T>
     */
    @Data
    public static class CallbackResult<T>{

        /** 状态 */
        private Boolean success;

        /** 结果 */
        private T result;

    }
}
