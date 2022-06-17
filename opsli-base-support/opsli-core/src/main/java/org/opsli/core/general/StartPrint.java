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

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 打印启动日志
 *
 * @author parker
 * @date 2020-09-16
 */
@Slf4j
@Component
public class StartPrint {

    /** 系统名称 */
    private static String systemName;
    /** 服务端口 */
    private static String serverPort;
    /** 服务根地址 */
    private static String serverContextPath;
    /** 系统启动时间 */
    private static String starterDate;


    /**
     * 成功
     * 打印启动日志
     */
    public void successPrint(){
        // 睡一秒打印
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
        String basePath = getBasePath();
        String printStr =
                "\n----------------------------------------------------------\n" +
                systemName + " 框架启动成功! 相关URLs:\n" +
                "项目地址: \t\thttp://" + basePath + "/\n" +
                "Doc文档: \t\thttp://" + basePath + "/doc.html\n" +
                "----------------------------------------------------------\n";
        Console.log(printStr);
    }

    /**
     * 失败
     * 打印启动日志
     */
    public void errorPrint(){
        this.errorPrint(null);
    }

    /**
     * 失败
     * 打印启动日志
     */
    public void errorPrint(String errorMsg){
        // 睡一秒打印
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
        String printStr =
                "\n----------------------------------------------------------\n" +
                        systemName + " 框架启动失败! 请检查相关配置！\n" +
                        "----------------------------------------------------------\n";
        Console.error(printStr);
        if(StringUtils.isNotEmpty(errorMsg)){
            Console.error(errorMsg);
        }
    }


    /**
     * 获得服务根路径
     * @return String
     */
    public String getBasePath(){
        return NetUtil.getLocalhostStr() + ":" + serverPort
                + StrUtil.addPrefixIfNot(serverContextPath, "/");
    }

    /**
     * 获得当前系统运行时间
     * @return String
     */
    public String getRunTime(){
        //Level.MINUTE表示精确到分
        return DateUtil.formatBetween(DateUtil.parseDateTime(starterDate),
                DateUtil.date(), BetweenFormatter.Level.MINUTE);
    }

    // ======================

    /**
     * 获得实例对象
     * @return StartPrint
     */
    public static StartPrint getInstance(){
        return StartPrintInner.INSTANCE;
    }


    /**
     * 静态内部类
     */
    private static class StartPrintInner{
        /** 实例对象 */
        private static final StartPrint INSTANCE = new StartPrint();
    }



    @Value("${server.port:8080}")
    public void setServerPort(String serverPort) {
        StartPrint.serverPort = serverPort;
    }

    @Value("${server.servlet.context-path:/opsli-boot}")
    public void setServerContextPath(String serverContextPath) {
        StartPrint.serverContextPath = serverContextPath;
    }

    // ===============================

    /***
     * 初始化
     * @param globalProperties 配置类
     */
    @Autowired
    public void init(GlobalProperties globalProperties){
        if(globalProperties != null){
            // 设置系统名称
            this.setSystemName(globalProperties.getSystemName());
            // 设置系统启动时间
            this.setStarterDate(globalProperties.getSystemStarterTime());
        }
    }

    // ==============================

    private void setSystemName(String systemName) {
        StartPrint.systemName = systemName;
    }

    private void setStarterDate(String starterDate) {
        // 非空验证
        if(StringUtils.isEmpty(starterDate)){
            StartPrint.starterDate = DateUtil.date().toString();
            return;
        }

        // 非法时间参数验证
        Date tmp = null;
        try {
            tmp = DateUtil.parseDateTime(starterDate);
        }catch (Exception ignored){}
        if(tmp == null){
            StartPrint.starterDate = DateUtil.date().toString();
            return;
        }

        StartPrint.starterDate = starterDate;
    }

}
