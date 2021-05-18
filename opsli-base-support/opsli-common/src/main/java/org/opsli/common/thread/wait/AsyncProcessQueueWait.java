package org.opsli.common.thread.wait;


import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程有界队列 - 等待线程执行完毕不拒绝
 *
 * @author 一枝花算不算浪漫
 * @date 2020-10-08 10:24
 */
@Slf4j
public class AsyncProcessQueueWait {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Task 包装类<br>
     * 此类型的意义是记录可能会被 Executor 吃掉的异常<br>
     */
    public static class TaskWrapper implements Runnable {

       private final Runnable gift;
       private final CountDownLatch latch;
       private final AtomicInteger count;

       public TaskWrapper(final Runnable target, final AtomicInteger count, final CountDownLatch latch) {
            this.gift = target;
            this.count = count;
            this.latch = latch;
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

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * 执行指定的任务
     *
     * @param task 任务
     * @return boolean
     */
    public static boolean execute(final Runnable task, final AtomicInteger count, final CountDownLatch latch) {
        return AsyncProcessorWait.executeTask(new TaskWrapper(task, count, latch));
    }
}
