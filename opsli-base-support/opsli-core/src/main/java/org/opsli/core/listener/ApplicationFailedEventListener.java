package org.opsli.core.listener;

import lombok.extern.slf4j.Slf4j;
import org.opsli.core.general.StartPrint;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 系统启动失败
 *
 * @author parker
 * @date 2020-03-31 13:56
 */
@Component
@Slf4j
public class ApplicationFailedEventListener implements ApplicationListener<ApplicationFailedEvent> {

    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        StartPrint.getInstance().errorPrint(event.getException().getMessage());
    }

}
