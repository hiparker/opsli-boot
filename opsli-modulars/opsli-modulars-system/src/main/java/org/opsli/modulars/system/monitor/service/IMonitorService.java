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
