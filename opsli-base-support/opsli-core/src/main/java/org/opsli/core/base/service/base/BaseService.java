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
package org.opsli.core.base.service.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.base.service.interfaces.BaseServiceInterface;

/**
 * 基础Service
 *
 * @author Parker
 * @date 2020-09-15
 */
public abstract class BaseService <M extends BaseMapper<T>, T extends BaseEntity>
        extends ServiceImpl<M, T> implements BaseServiceInterface<T> {

    @Override
    public Class<?> getServiceClazz() {
        return this.getClass();
    }
}
