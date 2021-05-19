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
 * EhCache 缓存接口
 *
 * @author Parker
 * @date 2020-09-16 11:47
 */
public interface EhCachePlugin {

    /**
     *  添加缓存数据
      * @param cacheName 缓存名称
     *  @param key Key
     *  @param value 值
     *  @return boolean
     */
    boolean put(String cacheName, String key, Object value);

    /**
     * 获取缓存数据
     * @param cacheName 缓存名称
     * @param key Key
     * @param vClass Class
     * @return V
     */
    <V> V get(String cacheName, String key, Class<V> vClass);


    /**
     * 删除缓存数据
     * @param cacheName 缓存名
     * @param key Key
     * @return boolean
     */
    boolean delete(String cacheName, String key);

}
