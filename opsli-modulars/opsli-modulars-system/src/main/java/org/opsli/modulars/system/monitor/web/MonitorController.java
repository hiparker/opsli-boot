/**
 * @作者 薛佳琪
 * @创建时间 2020/11/27 14:39
 */
package org.opsli.modulars.system.monitor.web;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultVo;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.modulars.system.monitor.service.IMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统监控
 *
 * @author 薛佳琪
 */
@Slf4j
@ApiRestController("/sys/monitor")
public class MonitorController {

    @Autowired
    private IMonitorService monitorService;

    /**
     * 查询服务器信息
     * @return ResultVo
     */
    @RequestMapping("/getSystemInfo")
    @ApiOperation(value = "当前服务器信息", notes = "当前服务器信息")
    public ResultVo<?> getSystemInfo(HttpServletRequest request) throws Exception {
        Map<String,Object> map = new HashMap<>();
        //服务器信息
        map.put("systemInfo",monitorService.getSysInfo());
        //CPU信息
        map.put("cpuInfo",monitorService.getCpuInfo());
        //内存信息
        map.put("memInfo",monitorService.getMemInfo());
        //JVM信息
        map.put("JVMInfo",monitorService.getJVMInfo());
        //磁盘信息
        map.put("sysFileInfo",monitorService.getSysFiles());
        return ResultVo.success(map);
    }
    /**
     * 查询CPU信息
     * @return ResultVo
     */
    @RequestMapping("/getCpuInfo")
    @ApiOperation(value = "当前CPU信息", notes = "当前CPU信息")
    public ResultVo<?> getCpuInfo(HttpServletRequest request) throws Exception {
        return ResultVo.success(monitorService.getCpuInfo());
    }
    /**
     * 查询内存信息
     * @return ResultVo
     */
    @RequestMapping("/getMemInfo")
    @ApiOperation(value = "当前内存信息", notes = "当前内存信息")
    public ResultVo<?> getMemInfo(HttpServletRequest request) throws Exception {
        return ResultVo.success(monitorService.getMemInfo());
    }
    /**
     * 查询JVM信息
     * @return ResultVo
     */
    @RequestMapping("/getJVMInfo")
    @ApiOperation(value = "当前JVM信息", notes = "当前JVM信息")
    public ResultVo<?> getJVMInfo(HttpServletRequest request) throws Exception {
        return ResultVo.success(monitorService.getJVMInfo());
    }

}
