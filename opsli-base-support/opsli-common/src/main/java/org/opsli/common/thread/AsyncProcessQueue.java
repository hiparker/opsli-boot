package org.opsli.common.thread;


import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: 一枝花算不算浪漫
 * @CreateTime: 2020-10-08 10:24
 * @Description: 自定义线程有界队列
 */
@Slf4j
public class AsyncProcessQueue {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Task 包装类<br>
     * 此类型的意义是记录可能会被 Executor 吃掉的异常<br>
     */
    public static class TaskWrapper implements Runnable {

       private final Runnable gift;

       public TaskWrapper(final Runnable target) {
       this.gift = target;
      }

       @Override
       public void run() {
          // 捕获异常，避免在 Executor 里面被吞掉了
          if (gift != null) {
             try {
                gift.run();
             } catch (Exception e) {
                String errMsg = StrUtil.format("线程池-包装的目标执行异常: {}", e.getMessage());
                log.error(errMsg, e);
             }
          }
       }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * 执行指定的任务
     *
     * @param task
     * @return
     */
    public static boolean execute(final Runnable task) {
        return AsyncProcessor.executeTask(new TaskWrapper(task));
    }
}
