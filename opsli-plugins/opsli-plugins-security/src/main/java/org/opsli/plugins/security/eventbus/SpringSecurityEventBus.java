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

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 操作日志事件 总线
 * @author Parker
 * @date 2021年12月7日10:42:39
 */
@Slf4j
@Component
public class SpringSecurityEventBus extends AbstractSpringSecuritySecurityEventBus
        implements SubscriberExceptionHandler {

    private final EventBus eventBus;

    public SpringSecurityEventBus() {
    	// 异步事件配置线程池
        eventBus = new AsyncEventBus(
                new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS,
                        new LinkedBlockingDeque<>(1024),
                        new ThreadFactoryBuilder()
                                .setNameFormat("Spring-Security-Event-Bus").build(),
                        new ThreadPoolExecutor.CallerRunsPolicy())
                , this
        );
    }

    @Override
    public void post(Object event) {
        eventBus.post(event);
    }

    @Override
    public void addConsumer(Object obj) {
        eventBus.register(obj);
    }

    @Override
    public void removeConsumer(Object obj) {
        eventBus.unregister(obj);
    }

    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context) {
        log.error("user event handler exception", exception);
    }

}
