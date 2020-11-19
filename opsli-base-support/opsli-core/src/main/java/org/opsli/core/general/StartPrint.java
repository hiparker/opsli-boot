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
package org.opsli.core.general;

import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 周鹏程
 * @CreateTime: 2020-11-04 17:34
 * @Description: 打印启动日志
 */
@Slf4j
@Component
public class StartPrint {

    /** 实例对象 */
    public static StartPrint INSTANCE = new StartPrint();

    /** 服务端口 */
    private static String serverPort;
    /** 服务根地址 */
    private static String serverContextPath;

    @Value("${server.port:8080}")
    public void setServerPort(String serverPort) {
        StartPrint.serverPort = serverPort;
    }

    @Value("${server.servlet.context-path:/opsli-boot}")
    public void setServerContextPath(String serverContextPath) {
        StartPrint.serverContextPath = serverContextPath;
    }

    /**
     * 成功
     * 打印启动日志
     */
    public void successPrint(){
        // 睡一秒打印
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
        String basePath = getBasePath();
        StringBuilder printStr = new StringBuilder();
        printStr.append("\n----------------------------------------------------------\n")
                .append("OPSLI 快速开发平台 框架启动成功! 相关URLs:\n")
                .append("项目地址: \t\thttp://" + basePath + "/\n")
                .append("Doc文档: \t\thttp://" + basePath + "/doc.html\n")
                .append("----------------------------------------------------------\n");
        Console.log(printStr.toString());
    }

    /**
     * 势必啊
     * 打印启动日志
     */
    public void errorPrint(){
        // 睡一秒打印
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
        StringBuilder printStr = new StringBuilder();
        printStr.append("\n----------------------------------------------------------\n")
                .append("OPSLI 快速开发平台 框架启动失败! 请检查相关配置！\n")
                .append("----------------------------------------------------------\n");
        Console.log(printStr.toString());
    }


    /**
     * 获得服务根路径
     * @return
     */
    public String getBasePath(){
        String ip = "127.0.0.1";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        }catch (UnknownHostException ignored){}
        return ip + ":" + serverPort + serverContextPath;
    }
}
