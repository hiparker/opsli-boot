package org.opsli.common.thread;

/**
 * 异步进程 执行器
 *
 * @author Parker
 * @date 2021年7月15日13:43:37
 */
public interface AsyncProcessExecutor {


	/**
	 * 存放任务
	 * @param task 任务
	 * @return AsyncProcessExecutor
	 */
	AsyncProcessExecutor put(final Runnable task);

	/**
	 * 执行
	 * @return boolean
	 */
	boolean execute();

}
