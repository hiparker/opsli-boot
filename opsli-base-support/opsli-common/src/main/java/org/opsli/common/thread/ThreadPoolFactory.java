package org.opsli.common.thread;


import com.alibaba.ttl.threadpool.TtlExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 线程池工厂
 *
 * @author Parker
 * @date 2021/11/2 10:48
 */
public final class ThreadPoolFactory {

	/**
	 * 默认最大并发数<br>
	 */
	private static final int DEFAULT_MAX_CONCURRENT = Runtime.getRuntime().availableProcessors() * 2;

	/**
	 * 默认线程存活时间
	 */
	private static final long DEFAULT_KEEP_ALIVE = 60L;

	/**
	 * 默认队列大小
	 */
	private static final int DEFAULT_SIZE = 1024;

	/**
	 * 线程池名称格式
	 */
	private static final String DEFAULT_THREAD_POOL_NAME = "ProcessPool-{}-%d";


	/**
	 * 创建默认的线程池
	 * @return ThreadPoolExecutor
	 */
	public static ExecutorService createDefThreadPool(){
		return createInitThreadPool(DEFAULT_MAX_CONCURRENT, DEFAULT_MAX_CONCURRENT * 4, DEFAULT_KEEP_ALIVE,
				TimeUnit.SECONDS, DEFAULT_SIZE, DEFAULT_THREAD_POOL_NAME, new ThreadPoolExecutor.CallerRunsPolicy());
	}


	/**
	 * 创建默认的线程池
	 *
	 * @param poolName 线程池名称
	 * @return ThreadPoolExecutor
	 */
	public static ExecutorService createDefThreadPool(String poolName){
		return createInitThreadPool(DEFAULT_MAX_CONCURRENT, DEFAULT_MAX_CONCURRENT * 4, DEFAULT_KEEP_ALIVE,
				TimeUnit.SECONDS, DEFAULT_SIZE, poolName, new ThreadPoolExecutor.CallerRunsPolicy());
	}

	/**
	 * 创建默认的线程池
	 *
	 * @param maxConcurrent 最大线程数
	 * @param poolName 线程池名称
	 * @return ThreadPoolExecutor
	 */
	public static ExecutorService createDefThreadPool(int maxConcurrent, String poolName){
		return createInitThreadPool(maxConcurrent, maxConcurrent * 4, DEFAULT_KEEP_ALIVE,
				TimeUnit.SECONDS, DEFAULT_SIZE, poolName, new ThreadPoolExecutor.CallerRunsPolicy());
	}

	/**
	 * 创建线程池
	 * @param coreConcurrent 核心线程数
	 * @param maxConcurrent 最大线程数
	 * @param keepAlive 线程存活时效
	 * @param timeUnit 线程存活单位
	 * @param queueSize 队列大小
	 * @param poolName 线程池名称
	 * @param handler 拒绝处理策略
	 * @return ThreadPoolExecutor
	 */
	public static ExecutorService createInitThreadPool(final int coreConcurrent,
														  final int maxConcurrent,
														  final long keepAlive,
														  final TimeUnit timeUnit,
														  final int queueSize,
														  final String poolName,
														  final RejectedExecutionHandler handler
														  ){
		return TtlExecutors.getTtlExecutorService(new ThreadPoolExecutor(coreConcurrent, maxConcurrent, keepAlive, timeUnit,
				new LinkedBlockingDeque<>(queueSize),
				new ThreadFactoryBuilder().setNameFormat(poolName).build(),
				handler
		));
	}

	private ThreadPoolFactory(){}

}
