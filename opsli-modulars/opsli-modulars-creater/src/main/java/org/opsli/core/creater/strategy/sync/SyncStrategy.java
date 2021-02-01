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
package org.opsli.core.creater.strategy.sync;

import org.opsli.core.creater.enums.DataBaseType;
import org.opsli.modulars.creater.table.wrapper.CreaterTableAndColumnModel;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.creater.strategy.sync
 * @Author: Parker
 * @CreateTime: 2020-11-18 11:47
 * @Description: 同步策略
 */
public interface SyncStrategy {

    /**
     * 获得分类
     * @return
     */
    DataBaseType getType();

    /**
     * 执行 同步操作
     */
    void execute(CreaterTableAndColumnModel model);

}
