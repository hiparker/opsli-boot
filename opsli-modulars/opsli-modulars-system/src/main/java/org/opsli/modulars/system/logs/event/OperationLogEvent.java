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
package org.opsli.modulars.system.logs.event;

import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.wrapper.system.logs.OperationLogModel;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.eventbus.IEventConsumer;
import org.opsli.core.log.bean.OperationLog;
import org.opsli.modulars.system.logs.service.IOperationLogService;
import org.springframework.stereotype.Component;

/**
 * 操作日志 事件
 *
 * @author Parker
 * @date 2021年7月15日20:28:24
 */
@Slf4j
@AllArgsConstructor
@Component
public class OperationLogEvent implements IEventConsumer<OperationLog> {

	private final IOperationLogService operationLogService;

	@Subscribe
	@Override
	public void consumer(OperationLog event) {
		//log.info("存储日志：{}", event);
		OperationLogModel operationLogModel =
				WrapperUtil.transformInstance(event, OperationLogModel.class);
		operationLogModel.setId(null);
		operationLogService.save(operationLogModel);
	}

}
