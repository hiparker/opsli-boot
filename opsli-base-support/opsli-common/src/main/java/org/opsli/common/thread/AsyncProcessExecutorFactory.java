package org.opsli.common.thread;

/**
 * 异步进程 执行器 工厂
 *
 * @author Parker
 * @date 2021年7月15日13:43:37
 */
public final class AsyncProcessExecutorFactory {

	/**
	 * 创建等待执行器
	 * @return AsyncProcessExecutor
	 */
	public static AsyncProcessExecutor createWaitExecutor(){
		return new AsyncProcessExecutorByWait();
	}

	/**
	 * 创建等待执行器
	 * @param key KEY
	 * @return AsyncProcessExecutor
	 */
	public static AsyncProcessExecutor createWaitExecutor(String key){
		return new AsyncProcessExecutorByWait(key);
	}

	/**
	 * 创建正常执行器
	 * @return AsyncProcessExecutor
	 */
	public static AsyncProcessExecutor createNormalExecutor(){
		return new AsyncProcessExecutorByNormal();
	}

	/**
	 * 创建正常执行器
	 * @param key KEY
	 * @return AsyncProcessExecutor
	 */
	public static AsyncProcessExecutor createNormalExecutor(String key){
		return new AsyncProcessExecutorByNormal(key);
	}

	// =====================

	private AsyncProcessExecutorFactory(){}

}
