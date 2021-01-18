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
package org.opsli.modulars.system.monitor.web;

import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
    private IMonitorService iMonitorService;

    /**
     * 查询服务器信息
     * @return ResultVo
     */
    @RequiresPermissions("devops_sysmonitor_select")
    @RequestMapping("/getSystemInfo")
    @ApiOperation(value = "当前服务器信息", notes = "当前服务器信息")
    public ResultVo<?> getSystemInfo() {
        Map<String,Object> map = Maps.newHashMapWithExpectedSize(5);
        //服务器信息
        map.put("systemInfo",iMonitorService.getSysInfo());
        //CPU信息
        map.put("cpuInfo",iMonitorService.getCpuInfo());
        //内存信息
        map.put("memInfo",iMonitorService.getMemInfo());
        //JVM信息
        map.put("JVMInfo",iMonitorService.getJVMInfo());
        //磁盘信息
        map.put("sysFileInfo",iMonitorService.getSysFiles());
        return ResultVo.success(map);
    }

    /**
     * 查询CPU信息
     * @return ResultVo
     */
    @RequiresPermissions("devops_sysmonitor_select")
    @RequestMapping("/getCpuInfo")
    @ApiOperation(value = "当前CPU信息", notes = "当前CPU信息")
    public ResultVo<?> getCpuInfo() {
        return ResultVo.success(
                iMonitorService.getCpuInfo());
    }

    /**
     * 查询内存信息
     * @return ResultVo
     */
    @RequiresPermissions("devops_sysmonitor_select")
    @RequestMapping("/getMemInfo")
    @ApiOperation(value = "当前内存信息", notes = "当前内存信息")
    public ResultVo<?> getMemInfo() {
        return ResultVo.success(
                iMonitorService.getMemInfo());
    }

    /**
     * 查询JVM信息
     * @return ResultVo
     */
    @RequiresPermissions("devops_sysmonitor_select")
    @RequestMapping("/getJVMInfo")
    @ApiOperation(value = "当前JVM信息", notes = "当前JVM信息")
    public ResultVo<?> getJVMInfo() {
        return ResultVo.success(
                iMonitorService.getJVMInfo());
    }

}
