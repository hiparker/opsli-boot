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
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程锁执行器
 * 用于当前方法中复杂业务多线程处理，等待线程执行完毕后 获得统一结果
 * 2021年11月2日14:07:54 重构 多线程异步等待执行器
 *
 * @author Parker
 * @date 2020-12-10 10:36
 */
@Slf4j
public class AsyncProcessExecutorByWait implements AsyncProcessExecutor {

    /** 线程池字典 */
    private static final Map<String, AsyncProcessor> EXECUTOR_MAP = Maps.newConcurrentMap();

    /** 线程Key */
    private final String key;

    /** 任务执行计数器 */
    private AtomicInteger count;

    /** 任务队列 */
    private final List<Callable<Object>> taskList;

    /** 执行器 */
    private final AsyncProcessor processor;


    /**
     * 构造函数
     */
    public AsyncProcessExecutorByWait(){
        this.key = "def";
        taskList = new ArrayList<>();
        processor = getProcessor(this.key);
    }

    /**
     * 构造函数
     * @param key 线程池唯一Key
     */
    public AsyncProcessExecutorByWait(String key){
        this.key = key;
        taskList = new ArrayList<>();
        processor = getProcessor(this.key);
    }


    /**
     * 放入执行任务
     * 特殊处理 Runnable 转换为 Callable
     * @param task 任务
     */
    @Override
    public AsyncProcessExecutor put(final Runnable task){
        taskList.add(Executors.callable(task));
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

        // 初始化锁参数
        count = new AtomicInteger(this.taskList.size());
        // 门闩 线程锁
        CountDownLatch latch = new CountDownLatch(this.taskList.size());

        for (Callable<Object> task : this.taskList) {
            // 回调减 门闩
            processor.executeTaskAndCallback(task, (result)->{
                if(result.getSuccess()){
                    count.decrementAndGet();
                }
                latch.countDown();
                return null;
            });
        }

        // 线程锁 等待查询结果 结果完成后继续执行
        try {
            latch.await();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }finally {
            this.taskList.clear();
        }

        // 返回执行结果
        return count.get() == 0;
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

}
