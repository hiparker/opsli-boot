package org.opsli.core.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.system.*;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.google.common.collect.Lists;
import lombok.Data;
import org.opsli.common.utils.ConvertBytesUtil;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

import java.lang.management.RuntimeMXBean;
import java.util.List;


/**
 * 系统信息工具类
 *
 * @author 薛佳琪
 * @date 2021年4月28日14:14:35
 */
public enum SystemInfoUtil {

    /** 实例对象 */
    INSTANCE;

    /** 监控等待时间 */
    private static final int WAITING_TIME = 1000;

    /**
     * 获得系统信息
     * @return SysInfo
     */
    public SysInfo getSysInfo(){
        OsInfo osInfo = SystemUtil.getOsInfo();
        UserInfo userInfo = SystemUtil.getUserInfo();
        HostInfo hostInfo = SystemUtil.getHostInfo();

        SysInfo sysInfo = new SysInfo();

        sysInfo.setComputerName(hostInfo.getName());
        sysInfo.setComputerIp(hostInfo.getAddress());
        sysInfo.setUserName(userInfo.getName());
        sysInfo.setUserDir(userInfo.getCurrentDir());
        sysInfo.setOsArch(osInfo.getArch());
        sysInfo.setOsName(osInfo.getName());

        return sysInfo;
    }

    /**
     * 获得磁盘信息
     * @return List DiskInfo
     */
    public List<DiskInfo> getDiskInfo(){
        List<DiskInfo> diskInfoList = Lists.newArrayList();
        SystemInfo si = new SystemInfo();

        FileSystem fileSystem = si.getOperatingSystem().getFileSystem();
        List<OSFileStore> fileStores = fileSystem.getFileStores();
        for (OSFileStore fs : fileStores) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;

            DiskInfo diskInfo = new DiskInfo();
            diskInfo.setDiskName(fs.getMount());
            diskInfo.setDiskType(fs.getType());
            diskInfo.setFileName(fs.getName());
            diskInfo.setTotal(ConvertBytesUtil.convertFileSizeToString(total));
            diskInfo.setFree(ConvertBytesUtil.convertFileSizeToString(free));
            diskInfo.setUsed(ConvertBytesUtil.convertFileSizeToString(used));
            diskInfo.setUsage(NumberUtil.mul(NumberUtil.div(used, total, 4), 100));
            diskInfoList.add(diskInfo);
        }

        return diskInfoList;
    }


    /**
     * 获得内存信息
     * @return MemoryInfo
     */
    public MemoryInfo getMemoryInfo(){
        MemoryInfo memoryInfo = new MemoryInfo();
        GlobalMemory memory = OshiUtil.getMemory();
        if(memory != null){
            String total = Convert.toStr(memory.getTotal());
            String used = Convert.toStr(memory.getTotal() - memory.getAvailable());

            memoryInfo.setTotal(
                    ConvertBytesUtil
                            .convertFileSizeToString(memory.getTotal()));
            memoryInfo.setUsed(
                    ConvertBytesUtil
                            .convertFileSizeToString(memory.getTotal() - memory.getAvailable()));
            memoryInfo.setFree(
                    ConvertBytesUtil
                            .convertFileSizeToString(memory.getAvailable()));
            memoryInfo.setUsage(
                    NumberUtil.mul(NumberUtil.div(used, total, 4), 100).doubleValue());
        }
        return memoryInfo;
    }

    /**
     * 获得CPU信息
     * @return CpuInfo
     */
    public CpuInfo getCpuInfo(){
        return OshiUtil.getCpuInfo(WAITING_TIME);
    }

    /**
     * 获得Jvm信息
     * @return JvmInfo
     */
    public JvmInfo getJvmInfo(){
        JavaInfo javaInfo = SystemUtil.getJavaInfo();
        JavaRuntimeInfo javaRuntimeInfo = SystemUtil.getJavaRuntimeInfo();
        RuntimeMXBean runtimeMxBean = SystemUtil.getRuntimeMXBean();


        long total = Runtime.getRuntime().totalMemory();
        long max = Runtime.getRuntime().maxMemory();
        long free = Runtime.getRuntime().freeMemory();
        long used = total - free;

        JvmInfo jvmInfo = new JvmInfo();

        // Jvm 信息
        jvmInfo.setTotal(
                ConvertBytesUtil
                        .convertFileSizeToString(total));
        jvmInfo.setMax(
                ConvertBytesUtil
                        .convertFileSizeToString(max));
        jvmInfo.setFree(
                ConvertBytesUtil
                        .convertFileSizeToString(free));
        jvmInfo.setUsed(
                ConvertBytesUtil
                        .convertFileSizeToString(used));
        jvmInfo.setUsage(
                NumberUtil.mul(
                        NumberUtil.div(
                                Convert.toStr(used), Convert.toStr(total), 4)
                        , 100).doubleValue());

        // Java 供应商
        jvmInfo.setVendor(javaInfo.getVendor());
        jvmInfo.setVendorUrl(javaInfo.getVendorURL());
        // Java 安装目录
        jvmInfo.setHome(javaRuntimeInfo.getHomeDir());
        // Java 版本
        jvmInfo.setVersion(javaInfo.getVersion());

        // JDK 启动时间戳
        long startTime = runtimeMxBean.getStartTime();
        // JDK 运行时间
        String runTimed = DateUtil.formatBetween(
                DateUtil.date(startTime), DateUtil.date(), BetweenFormatter.Level.SECOND);


        // 虚拟机名称
        jvmInfo.setJvmName(runtimeMxBean.getVmName());

        // JDK 启动时间
        jvmInfo.setStartTime(
                DateUtil.formatDateTime(DateUtil.date(startTime)));

        // JDK 运行时间
        jvmInfo.setRunTime(runTimed);

        return jvmInfo;
    }

    // ======================================

    /**
     * 内存信息 静态内部类
     */
    @Data
    private static class MemoryInfo {

        /** 内存总量 */
        private String total;

        /** 已用内存 */
        private String used;

        /** 剩余内存 */
        private String free;

        /** 使用率 */
        private double usage;

    }

    /**
     * Jvm信息 静态内部类
     */
    @Data
    public static class JvmInfo {

        /** 虚拟机名称 */
        private String jvmName;

        /** 当前JVM占用的内存总数(M) */
        private String total;

        /** JVM最大可用内存总数(M) */
        private String max;

        /** JVM空闲内存(M) */
        private String free;

        /** 已使用 */
        private String used;

        /** 使用率 */
        private double usage;

        /** JDK版本 */
        private String version;

        /** JDK路径 */
        private String home;

        /** Java的运行环境供应商 */
        private String vendor;

        /** Java的运行环境供应商 URL */
        private String vendorUrl;

        /** JDK启动时间 */
        private String startTime;

        /** JDK运行时间 */
        private String runTime;

    }

    /**
     * 系统信息
     */
    @Data
    public static class SysInfo {

        /** 服务器名称 */
        private String computerName;

        /** 服务器Ip */
        private String computerIp;

        /** 启动用户名 */
        private String userName;

        /** 项目路径 */
        private String userDir;

        /** 操作系统 */
        private String osName;

        /** 系统架构 */
        private String osArch;

    }

    /**
     * 磁盘监控
     */
    @Data
    public static class DiskInfo {

        /** 盘符路径 */
        private String diskName;

        /** 盘符类型 */
        private String diskType;

        /** 文件系统 */
        private String fileName;

        /** 总大小 */
        private String total;

        /** 剩余大小 */
        private String free;

        /** 已经使用量 */
        private String used;

        /** 资源的使用率 */
        private double usage;
    }

}
