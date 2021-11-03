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

import java.util.function.Function;

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
