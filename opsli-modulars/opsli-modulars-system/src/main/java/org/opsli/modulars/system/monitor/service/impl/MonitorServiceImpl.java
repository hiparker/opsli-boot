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

@Service
public class MonitorServiceImpl implements IMonitorService {

    /**
     * 获取服务器信息
     * @return
     * @throws Exception
     */
    @Override
    public Sys getSysInfo() throws Exception {
        SystemHardwareInfo systemHardwareInfo = new SystemHardwareInfo();
        systemHardwareInfo.copyToSysInof();
        return systemHardwareInfo.getSys();
    }


    /**
     * 获取CPU信息
     * @return
     * @throws Exception
     */

    @Override
    public CPU getCpuInfo() throws Exception {
        SystemHardwareInfo systemHardwareInfo = new SystemHardwareInfo();
        systemHardwareInfo.copyToCupInof();
        return systemHardwareInfo.getCpu();
    }

    @Override
    public Mem getMemInfo() throws Exception {
        SystemHardwareInfo systemHardwareInfo = new SystemHardwareInfo();
        systemHardwareInfo.copyToMemInof();
        return systemHardwareInfo.getMem();
    }

    @Override
    public JVM getJVMInfo() throws Exception {
        SystemHardwareInfo systemHardwareInfo = new SystemHardwareInfo();
        systemHardwareInfo.copyToJvmInof();
        return systemHardwareInfo.getJvm();
    }

    @Override
    public List<SysFile> getSysFiles() throws Exception {
        SystemHardwareInfo systemHardwareInfo = new SystemHardwareInfo();
        systemHardwareInfo.copyToSysFilesInof();
        return systemHardwareInfo.getSysFiles();
    }


}
