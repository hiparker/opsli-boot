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
package org.opsli.core.cache.pushsub.entity;

import lombok.Data;
import org.opsli.core.cache.pushsub.enums.PushSubType;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.entity
 * @Author: Parker
 * @CreateTime: 2020-09-18 00:01
 * @Description: 热点数据处理 Entity
 */
@Data
public class CacheDataEntity {

    /** key */
    private String key;

    /** 数据类型 */
    private PushSubType type;

    /** 缓存名称 */
    private String cacheName;

}
