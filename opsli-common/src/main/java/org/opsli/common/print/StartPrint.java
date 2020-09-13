package org.opsli.common.print;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.print
 * @Author: Parker
 * @CreateTime: 2020-09-12 17:54
 * @Description: 打印启动日志
 */
@Slf4j
public enum StartPrint {

    /** 实例对象 */
    INSTANCE;

    /**
     * 打印启动日志
     */
    public void print(Environment env){
        String ip = "localhost";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        }catch (UnknownHostException e){
            log.error("网卡IP 获得失败");
        }
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application opsli-boot is running! Access URLs:\n\t" +
                "Local: \t\thttp://" + ip + ":" + port + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "swagger-ui: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
                "Doc: \t\thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "----------------------------------------------------------");
    }

}
