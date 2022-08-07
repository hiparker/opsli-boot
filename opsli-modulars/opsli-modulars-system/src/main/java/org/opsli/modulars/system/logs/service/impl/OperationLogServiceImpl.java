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
package org.opsli.modulars.system.logs.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.opsli.core.base.service.impl.CrudServiceImpl;

import org.opsli.modulars.system.logs.entity.OperationLog;
import org.opsli.api.wrapper.system.logs.OperationLogModel;
import org.opsli.modulars.system.logs.service.IOperationLogService;
import org.opsli.modulars.system.logs.mapper.OperationLogMapper;


/**
 * 行为日志 Service Impl
 *
 * @author Parker
 * @date 2022-07-26 19:21:57
 */
@Service
public class OperationLogServiceImpl extends CrudServiceImpl<OperationLogMapper, OperationLog, OperationLogModel>
    implements IOperationLogService {

    @Autowired(required = false)
    private OperationLogMapper mapper;

}