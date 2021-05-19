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
 * Web 条件构造器
 *
 * @author Parker
 * @date 2020-09-21 23:57
 */
public class GenQueryBuilder<T extends BaseEntity>  implements QueryBuilder<T> {


    /**
     * 构造函数 只是生产 查询器
     */
    public GenQueryBuilder(){

    }

    @Override
    public QueryWrapper<T> build() {
        return new QueryWrapper<>();
    }

}
