package org.opsli;

import org.opsli.general.StartPrint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@SpringBootApplication()
public class OpsliApplication {

    public static void main(String[] args){
        ConfigurableApplicationContext application = SpringApplication.run(OpsliApplication.class, args);
        // 打印启动日志
        StartPrint.INSTANCE.print(application.getEnvironment());
    }

}