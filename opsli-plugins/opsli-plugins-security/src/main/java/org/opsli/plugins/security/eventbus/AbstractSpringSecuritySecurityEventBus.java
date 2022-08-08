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

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * EventBus 与 Spring 打通桥梁
 *
 * @author Parker
 * @date 2021年12月7日10:39:16
 */
public abstract class AbstractSpringSecuritySecurityEventBus implements ISecurityEventBus, ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        this.scanConsumer(null);
    }

    @Override
    public void scanConsumer(String packageName) {
        context.getBeansOfType(ISecurityEventConsumer.class).forEach((k, v)->{
            this.addConsumer(v);
        });
    }
}
