package org.opsli.core.listener;

import lombok.extern.slf4j.Slf4j;
import org.opsli.core.general.StartPrint;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 系统启动成功
 *
 * @author parker
 * @date 2020-03-31 13:56
 */
@Component
@Slf4j
public class ApplicationReadyEventListene implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        StartPrint.INSTANCE.successPrint(applicationContext.getEnvironment());
    }
}