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
package org.opsli.common.constants;

/**
 * 缓存 常量
 *
 * @author Parker
 * @date 2020-09-22 17:07
 */
public final class CacheConstants {

    public static final String PREFIX_NAME = "opsli";

    /** Ehcache 缓存存放空间 */
    public static final String EHCACHE_SPACE = "timed";

    /** 热数据前缀 */
    public static final String HOT_DATA_PREFIX = "hot_data";


    private CacheConstants(){}
}
