package org.opsli.common.thread;


import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池执行器调用者
 *
 * @author Parker
 * @date 2020-10-08 10:24
 */
@Slf4j
public final class AsyncProcessCoordinator {

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

       public TaskWrapper(final Runnable target, final AsyncProcessExecutorByWait.AsyncWaitLock lock) {
           if(lock == null){
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
          if (gift == null) {
            return;
          }

          try {
             // 执行任务
             gift.run();

             if(count != null){
                 // 标示已执行
                 count.decrementAndGet();
             }
          } catch (Exception e) {
             String errMsg = StrUtil.format("线程池-包装的目标执行异常: {}", e.getMessage());
             log.error(errMsg, e);
          } finally {
             if(latch != null){
                 latch.countDown();
             }
          }
       }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * 此类型无法实例化
     */
    private AsyncProcessCoordinator(){}

    /**
     * 执行指定的任务
     *
     * @param task 任务
     * @return boolean
     */
    protected static boolean execute(final Runnable task) {
        return AsyncProcessor.executeTask(new TaskWrapper(task));
    }

    /**
     * 执行指定的任务
     *
     * @param task 任务
     * @return boolean
     */
    protected static boolean execute(final Runnable task, final AsyncProcessExecutorByWait.AsyncWaitLock lock) {
        boolean execute = AsyncProcessor.executeTask(new TaskWrapper(task, lock));
        // 执行任务被拒绝 门闩减1 计数器不动 End
        if(!execute){
            lock.getLatch().countDown();
        }
        return execute;
    }
}
