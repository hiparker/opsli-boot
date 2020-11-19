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
package org.opsli.core.persistence.querybuilder;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.opsli.core.base.entity.BaseEntity;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.persistence.querybuilder
 * @Author: Parker
 * @CreateTime: 2020-09-21 23:53
 * @Description: 查询构造器
 */
public interface QueryBuilder<T extends BaseEntity> {

    /**
     * 构造器
     * @return
     */
    QueryWrapper<T> build();

}
