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
package org.opsli.plugins.cache;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.cache
 * @Author: Parker
 * @CreateTime: 2020-09-16 19:19
 * @Description: EhCache 缓存接口
 */
public interface EhCachePlugin {

    /**
     *  添加缓存数据
      * @param cacheName
     *  @param key
     *  @param value
     *  @return
     */
    boolean put(String cacheName, String key, Object value);

    /**
     * 获取缓存数据
     * @param cacheName
     * @param key
     * @return
     */
    <V> V get(String cacheName, String key ,Class<V> vClass);


    /**
     * 删除缓存数据
     * @param cacheName
     * @param key
     */
    boolean delete(String cacheName, String key);

}
