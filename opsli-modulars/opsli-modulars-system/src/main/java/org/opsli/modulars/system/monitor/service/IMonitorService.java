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
package org.opsli.modulars.system.monitor.service;

import org.opsli.core.monitor.utils.*;

import java.util.List;


/**
 * 系统监控
 * @author 薛佳琪
 */
public interface IMonitorService {

    /**
     * 获得系统信息
     * @return
     */
    Sys getSysInfo();


    /**
     * 获得CPU信息
     * @return
     */
    CPU getCpuInfo();


    /**
     * 获得内存信息
     * @return
     */
    Mem getMemInfo();

    /**
     * 获得JVM信息
     * @return
     */
    JVM getJVMInfo();

    /**
     * 获得系统磁盘信息
     * @return
     */
    List<SysFile> getSysFiles();

}
