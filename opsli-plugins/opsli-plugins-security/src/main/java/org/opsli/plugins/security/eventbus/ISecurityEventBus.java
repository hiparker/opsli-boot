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
package org.opsli.plugins.security.eventbus;

/**
 * EventBus 接口
 *
 * @author Parker
 * @date 2021年12月7日10:38:24
 */
public interface ISecurityEventBus {
    /**
     * 发布事件
     * @param event 事件实体
     */
    void post(Object event);

    /**
     * 添加消费者
     * @param obj 消费者对象，默认以class为key
     */
    void addConsumer(Object obj);

    /**
     * 移除消费者
     * @param obj 消费者对象，默认以class为key
     */
    void removeConsumer(Object obj);

    /**
     * 扫描消费者
     * @param packageName 扫描包
     */
    void scanConsumer(String packageName);
}
