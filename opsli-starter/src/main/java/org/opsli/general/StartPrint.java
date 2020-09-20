package org.opsli.general;

import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

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
        // 睡一秒打印
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
        String ip = "localhost";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        }catch (UnknownHostException e){
            log.error("网卡IP 获取失败");
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        StringBuilder printStr = new StringBuilder();
        printStr.append("\n----------------------------------------------------------\n")
                .append("Opsli-Boot 框架已启动! 相关URLs:\n")
                .append("项目地址: \t\thttp://" + ip + ":" + serverPort + contextPath + "/\n")
                .append("Doc文档: \t\thttp://" + ip + ":" + serverPort + contextPath + "/doc.html\n")
                .append("----------------------------------------------------------\n");
        Console.log(printStr.toString());
    }

}
