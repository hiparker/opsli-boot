/**
 * @作者 薛佳琪
 * @创建时间 2020/11/23 11:24
 */
package org.opsli.core.monitor;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Lists;
import lombok.Data;
import org.opsli.core.monitor.utils.*;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;
/**
 * 系统监控
 *
 * @author 薛佳琪
 */
@Data
public class SystemHardwareInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 监控等待时间 */
    private static final int OSHI_WAIT_SECOND = 1000;

    /**
     * CPU相关信息
     */
    private CPU cpu = new CPU();

    /**
     * 內存相关信息
     */
    private Mem mem = new Mem();

    /**
     * JVM相关信息
     */
    private JVM jvm = new JVM();

    /**
     * 服务器相关信息
     */
    private Sys sys = new Sys();

    /**
     * 磁盘相关信息
     */
    private List<SysFile> sysFiles = Lists.newLinkedList();


    public void copyTo() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem operatingSystem = si.getOperatingSystem();
        setCpuInfo(hal.getProcessor());

        setMemInfo(hal.getMemory());

        setSysInfo(operatingSystem);

        setJvmInfo();

        setSysFiles(operatingSystem);
    }


    public void copyToCupInfo() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        setCpuInfo(hal.getProcessor());
    }
    public void copyToMemInfo() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        setMemInfo(hal.getMemory());
    }
    public void copyToSysInfo() {
        SystemInfo si = new SystemInfo();
        setSysInfo(si.getOperatingSystem());
    }
    public void copyToJvmInfo() {
        setJvmInfo();
    }
    public void copyToSysFilesInfo() {
        SystemInfo si = new SystemInfo();
        setSysFiles(si.getOperatingSystem());
    }



    /**
     * 设置CPU信息
     */
    private void setCpuInfo(CentralProcessor processor) {
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();

        // OSHI 等待睡眠
        Util.sleep(OSHI_WAIT_SECOND);

        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + ioWait + irq + softIrq + steal;
        cpu.setCpuNum(processor.getLogicalProcessorCount());
        cpu.setCpuName(processor.getName());
        cpu.setTotal(totalCpu);
        cpu.setSys(cSys);
        cpu.setUsed(user);
        cpu.setWait(ioWait);
        cpu.setFree(idle);
    }

    /**
     * 设置内存信息
     */
    private void setMemInfo(GlobalMemory memory) {
        mem.setTotal(memory.getTotal());
        mem.setUsed(memory.getTotal() - memory.getAvailable());
        mem.setFree(memory.getAvailable());
    }

    /**
     * 设置服务器信息
     */
    private void setSysInfo(OperatingSystem operatingSystem) {
        Properties props = System.getProperties();
        sys.setComputerName(operatingSystem.getNetworkParams().getHostName());
        sys.setComputerIp(NetUtil.getLocalhostStr());
        sys.setOsName(props.getProperty("os.name"));
        sys.setOsArch(props.getProperty("os.arch"));
        sys.setUserDir(props.getProperty("user.dir"));
    }

    /**
     * 设置Java虚拟机
     */
    private void setJvmInfo() {
        Properties props = System.getProperties();
        jvm.setTotal(Runtime.getRuntime().totalMemory());
        jvm.setMax(Runtime.getRuntime().maxMemory());
        jvm.setFree(Runtime.getRuntime().freeMemory());
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setHome(props.getProperty("java.home"));
        jvm.setVendor(props.getProperty("java.vendor"));
        jvm.setVendorUrl(props.getProperty("java.vendor.url"));
    }

    /**
     * 设置磁盘信息
     */
    private void setSysFiles(OperatingSystem os) {
        FileSystem fileSystem = os.getFileSystem();
        OSFileStore[] fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(convertFileSize(total));
            sysFile.setFree(convertFileSize(free));
            sysFile.setUsed(convertFileSize(used));
            sysFile.setUsage(NumberUtil.mul(NumberUtil.div(used, total, 4), 100));
            sysFiles.add(sysFile);
        }
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB" , (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB" , f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB" , f);
        } else {
            return String.format("%d B" , size);
        }
    }

}
