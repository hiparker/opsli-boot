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

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 单线程池
 *
 * @author Parker
 * @date 2021/8/27 17:00
 */
@Slf4j
public final class SyncProcessSingleExecutor {

	private static final Map<String, ExecutorService> EXECUTOR_MAP = Maps.newConcurrentMap();

	private static final String KEY = "def";

	/**
	 * 执行器
	 * @param r 任务
	 */
	public static synchronized void execute(Runnable r){
		execute(KEY, r);
	}


	/**
	 * 执行器
	 * @param key 唯一Key
	 * @param r 任务
	 */
	public static synchronized void execute(String key, Runnable r){
		if(null == r){
			return;
		}

		ExecutorService executorService = EXECUTOR_MAP.get(key);
		if(null == executorService){
			executorService = ThreadUtil.newSingleExecutor();
			EXECUTOR_MAP.put(key, executorService);
		}

		executorService.execute(new TaskWrapper(r));
	}


	/**
	 * Task 包装类<br>
	 * 此类型的意义是记录可能会被 Executor 吃掉的异常<br>
	 */
	private static class TaskWrapper implements Runnable {

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

	private SyncProcessSingleExecutor(){}
}
