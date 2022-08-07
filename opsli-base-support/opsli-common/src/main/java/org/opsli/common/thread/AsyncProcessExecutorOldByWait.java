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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程锁执行器
 * 用于当前方法中复杂业务多线程处理，等待线程执行完毕后 获得统一结果
 *
 * @author Parker
 * @date 2020-12-10 10:36
 */
@Slf4j
@Deprecated
public class AsyncProcessExecutorOldByWait implements AsyncProcessExecutor {

    /** 线程池字典 */
    private static final Map<String, AsyncProcessor> EXECUTOR_MAP = Maps.newConcurrentMap();

    /** 线程Key */
    private final String key;

    /** 任务队列 */
    private final List<Runnable> taskList;

    /** 执行器 */
    private final AsyncProcessor processor;

    /**
     * 构造函数
     */
    public AsyncProcessExecutorOldByWait(){
        this.key = "def";
        taskList = new ArrayList<>();
        processor = getProcessor(this.key);
    }

    /**
     * 构造函数
     * @param key 线程池唯一Key
     */
    public AsyncProcessExecutorOldByWait(String key){
        this.key = key;
        taskList = new ArrayList<>();
        processor = getProcessor(this.key);
    }


    /**
     * 执行
     * @param task 任务
     */
    @Override
    public AsyncProcessExecutor put(final Runnable task){
        taskList.add(task);
        return this;
    }

    /**
     * 执行 线程锁 等待查询结果 结果完成后继续执行
     */
    @Override
    public boolean execute(){
        if(CollUtil.isEmpty(this.taskList)){
            return true;
        }

        // 锁
        AsyncWaitLock lock = new AsyncWaitLock(this.taskList.size());

        for (Runnable task : this.taskList) {
            // 多线程执行任务
            boolean execute = this.execute(task, lock);
            // 执行任务被拒绝 门闩减1 计数器不动 End
            if(!execute){
                lock.getLatch().countDown();
            }
        }

        // 线程锁 等待查询结果 结果完成后继续执行
        try {
            lock.getLatch().await();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }finally {
            this.taskList.clear();
        }

        // 返回执行结果
        return lock.getCount().get() == 0;
    }

    /**
     * 执行指定的任务
     *
     * @param task 任务
     * @return boolean
     */
    private boolean execute(final Runnable task, final AsyncWaitLock lock) {
        return processor.executeTask(new TaskWrapper(task, lock));
    }

    /**
     * 获得执行器
     * @param key Key
     * @return AsyncProcessor
     */
    private synchronized static AsyncProcessor getProcessor(String key){
        AsyncProcessor asyncProcessor = EXECUTOR_MAP.get(key);
        if(null == asyncProcessor){
            asyncProcessor = new AsyncProcessor();
            asyncProcessor.init(key);
            EXECUTOR_MAP.put(key, asyncProcessor);
        }
        return asyncProcessor;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * 线程锁对象
     *
     * @author Parker
     * @date 2020-10-08 10:24
     */
    @Getter
    public static class AsyncWaitLock {

        /** 门闩 */
        private final CountDownLatch latch;

        /** 计数器 */
        private final AtomicInteger count;

        public AsyncWaitLock(int count){
            // 初始化锁参数
            this.count = new AtomicInteger(count);
            // 门闩 线程锁
            this.latch = new CountDownLatch(count);
        }
    }

    /**
     * Task 包装类<br>
     * 此类型的意义是记录可能会被 Executor 吃掉的异常<br>
     */
    public static class TaskWrapper implements Runnable {

        private final Runnable gift;
        private final CountDownLatch latch;
        private final AtomicInteger count;

        public TaskWrapper(final Runnable target) {
            this.gift = target;
            this.count = null;
            this.latch = null;
        }

        public TaskWrapper(final Runnable target, final AsyncWaitLock lock) {
            if (lock == null) {
                this.gift = null;
                this.count = null;
                this.latch = null;
                return;
            }

            this.gift = target;
            this.count = lock.getCount();
            this.latch = lock.getLatch();
        }

        @Override
        public void run() {
            // 捕获异常，避免在 Executor 里面被吞掉了
            if (gift != null) {
                try {
                    gift.run();
                    // 标示已执行
                    count.decrementAndGet();
                } catch (Exception e) {
                    String errMsg = StrUtil.format("线程池-包装的目标执行异常: {}", e.getMessage());
                    log.error(errMsg, e);
                } finally {
                    latch.countDown();
                }
            }
        }
    }

}
