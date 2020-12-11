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
package org.opsli.common.thread.wait;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Parker
 * @CreateTime: 2020-12-10 10:36
 * @Description: 多线程锁执行器
 *
 * 用于当前方法中复杂业务多线程处理，等待线程执行完毕后 获得统一结果
 *
 */
@Slf4j
public class AsyncProcessWaitExecutor {

    /** 线程初始值 */
    private final int initVal;
    /** 任务执行计数器 */
    private AtomicInteger count;
    /** 门闩 线程锁 */
    private CountDownLatch latch;

    public AsyncProcessWaitExecutor(final int initVal){
        this.initVal = initVal;
        if(this.initVal > 0){
            // 计数器
            count = new AtomicInteger(this.initVal);
            latch = new CountDownLatch(this.initVal);
        }
    }

    /**
     * 执行
     * @param task 任务
     */
    public void execute(final Runnable task){
        if(this.initVal > 0){
            // 多线程执行任务
            boolean execute = AsyncProcessQueueWait.execute(task, count, latch);
            // 执行任务被拒绝 门闩减1 计数器不动 End
            if(!execute){
                latch.countDown();
            }
        }
    }


    /**
     * 线程锁 等待查询结果 结果完成后继续执行
     */
    public void await(){
        if(this.initVal > 0){
            // 线程锁 等待查询结果 结果完成后继续执行
            try {
                latch.await();
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 线程锁 等待查询结果 结果完成后继续执行
     */
    public boolean isSuccess(){
        if(this.initVal > 0){
            return count.get() == 0;
        }
        return true;
    }


}
