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

import cn.hutool.core.date.DateUtil;
import org.opsli.api.wrapper.system.logs.LogsModel;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.modulars.system.logs.entity.SysLogs;
import org.opsli.modulars.system.logs.mapper.LogsMapper;
import org.opsli.modulars.system.logs.service.ILogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * 日志 Service Impl
 *
 * @author Parker
 * @date 2020-11-28 18:59:59
 */
@Service
public class LogsServiceImpl extends CrudServiceImpl<LogsMapper, SysLogs, LogsModel> implements ILogsService {

    /** Log 存储量阈值 */
    private static final int LOG_BIG_COUNT = 10_0000;

    @Autowired(required = false)
    private LogsMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LogsModel insert(LogsModel model) {
        long count = super.count();
        // 如果日志存储量为10万 则自动清空上月前数据
        if(count > LOG_BIG_COUNT){
            this.emptyByOneMonth();
        }
        return super.insert(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean emptyByOneMonth() {
        Date newDate = DateUtil.lastMonth();
        return mapper.emptyByOneMonth(newDate);
    }
}


