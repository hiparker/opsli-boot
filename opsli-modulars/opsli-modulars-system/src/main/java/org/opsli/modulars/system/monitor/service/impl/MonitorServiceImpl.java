/**
 * @作者 薛佳琪
 * @创建时间 2020/11/27 14:54
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
