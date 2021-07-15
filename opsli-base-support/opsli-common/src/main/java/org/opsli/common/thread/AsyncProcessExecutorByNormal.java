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
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 多线程锁执行器 正常处理
 *
 * @author Parker
 * @date 2020-12-10 10:36
 */
@Slf4j
public class AsyncProcessExecutorByNormal implements AsyncProcessExecutor{

    /** 任务队列 */
    private final List<Runnable> taskList;

    public AsyncProcessExecutorByNormal(){
        taskList = new ArrayList<>();
    }

    /**
     * 执行
     * @param task 任务
     */
    @Override
    public AsyncProcessExecutorByNormal put(final Runnable task){
        taskList.add(task);
        return this;
    }

    /**
     * 执行 线程锁 等待查询结果 结果完成后继续执行
     *
     * @return boolean 最终直接结果
     */
    @Override
    public boolean execute(){
        if(CollUtil.isEmpty(this.taskList)){
            return true;
        }

        for (Runnable task : this.taskList) {
            // 多线程执行任务
            AsyncProcessCoordinator.execute(task);
        }

        // 返回执行结果
        return true;
    }

}
