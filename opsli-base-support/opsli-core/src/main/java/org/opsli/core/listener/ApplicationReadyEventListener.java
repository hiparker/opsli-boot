package org.opsli.core.listener;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.opsli.core.general.StartPrint;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 系统启动成功
 *
 * @author Pace
 * @date 2020-03-31 13:56
 */
@Component
@Slf4j
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
        // 输出启动日志
        StartPrint.getInstance().successPrint();
    }

}
