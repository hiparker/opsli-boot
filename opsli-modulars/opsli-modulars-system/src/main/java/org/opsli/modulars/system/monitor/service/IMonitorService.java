/**
 * @作者 薛佳琪
 * @创建时间 2020/11/27 14:54
 */
package org.opsli.modulars.system.monitor.service;

import org.opsli.core.monitor.utils.*;

import java.util.List;


/**
 * 系统监控
 * @author 薛佳琪
 */
public interface IMonitorService {

    Sys getSysInfo() throws Exception;


    CPU getCpuInfo() throws Exception;


    Mem getMemInfo() throws Exception;

    JVM getJVMInfo() throws Exception;

    List<SysFile> getSysFiles() throws Exception;

}
