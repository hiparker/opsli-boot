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
package org.opsli.modulars.system.monitor.service.impl;

import org.opsli.core.monitor.SystemHardwareInfo;
import org.opsli.core.monitor.utils.*;
import org.opsli.modulars.system.monitor.service.IMonitorService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统监控
 *
 * @author 薛佳琪
 */
@Service
public class MonitorServiceImpl implements IMonitorService {


    @Override
    public Sys getSysInfo() {
        SystemHardwareInfo systemHardwareInfo = new SystemHardwareInfo();
        systemHardwareInfo.copyToSysInfo();
        return systemHardwareInfo.getSys();
    }


    @Override
    public CPU getCpuInfo() {
        SystemHardwareInfo systemHardwareInfo = new SystemHardwareInfo();
        systemHardwareInfo.copyToCupInfo();
        return systemHardwareInfo.getCpu();
    }

    @Override
    public Mem getMemInfo() {
        SystemHardwareInfo systemHardwareInfo = new SystemHardwareInfo();
        systemHardwareInfo.copyToMemInfo();
        return systemHardwareInfo.getMem();
    }

    @Override
    public JVM getJVMInfo() {
        SystemHardwareInfo systemHardwareInfo = new SystemHardwareInfo();
        systemHardwareInfo.copyToJvmInfo();
        return systemHardwareInfo.getJvm();
    }

    @Override
    public List<SysFile> getSysFiles() {
        SystemHardwareInfo systemHardwareInfo = new SystemHardwareInfo();
        systemHardwareInfo.copyToSysFilesInfo();
        return systemHardwareInfo.getSysFiles();
    }


}
