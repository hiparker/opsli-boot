/**
 * @作者 薛佳琪
 * @创建时间 2020/11/27 14:54
 */
package org.opsli.modulars.system.monitor.service;

import org.opsli.core.monitor.utils.*;

import java.util.List;


public interface IMonitorService {

    public Sys getSysInfo() throws Exception;


    public CPU getCpuInfo() throws Exception;


    public Mem getMemInfo() throws Exception;

    public JVM getJVMInfo() throws Exception;

    public List<SysFile> getSysFiles() throws Exception;

}
