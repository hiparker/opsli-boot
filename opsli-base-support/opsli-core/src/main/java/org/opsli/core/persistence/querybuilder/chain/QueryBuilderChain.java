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
package org.opsli.core.persistence.querybuilder.chain;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.persistence.querybuilder.conf.WebQueryConf;

/**
 * 查询构建器责任链
 *
 * @author Parker
 * @date 2020-09-13 19:36
 */
public interface QueryBuilderChain {

    /**
     * 执行
     * @param entityClazz entity class
     * @param wrapper 包装类
     * @param <T> 泛型
     * @return <T>
     */
    <T extends BaseEntity> QueryWrapper<T> handler(Class<T> entityClazz, QueryWrapper<T> wrapper);

    /**
     * 执行
     * @param entityClazz entity class
     * @param wrapper 包装类
     * @param webQueryConf 字段（如果是关联查询 出现字段冲突可指定字段）
     * @param <T> 泛型
     * @return <T>
     */
    <T extends BaseEntity> QueryWrapper<T> handler(Class<T> entityClazz, WebQueryConf webQueryConf,
                                                   QueryWrapper<T> wrapper);

}
