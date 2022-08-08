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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.utils.SystemInfoUtil;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * 系统监控
 *
 * @author 薛佳琪
 * @date 2020-09-16 17:33
 */
@Api(tags = "系统监控")
@Slf4j
@ApiRestController("/{ver}/system/monitor")
public class MonitorController {

    /**
     * 查询服务器信息
     * @return ResultWrapper
     */
    @PreAuthorize("hasAuthority('devops_sysmonitor_select')")
    @GetMapping("/getSystemInfo")
    @ApiOperation(value = "当前服务器信息", notes = "当前服务器信息")
    public ResultWrapper<?> getSystemInfo() {
        Map<String,Object> map = Maps.newHashMapWithExpectedSize(5);
        //服务器信息
        map.put("systemInfo", SystemInfoUtil.INSTANCE.getSysInfo());
        //CPU信息
        map.put("cpuInfo", SystemInfoUtil.INSTANCE.getCpuInfo());
        //内存信息
        map.put("memInfo", SystemInfoUtil.INSTANCE.getMemoryInfo());
        //JVM信息
        map.put("JVMInfo", SystemInfoUtil.INSTANCE.getJvmInfo());
        //磁盘信息
        map.put("sysFileInfo", SystemInfoUtil.INSTANCE.getDiskInfo());
        return ResultWrapper.getSuccessResultWrapper(map);
    }

    /**
     * 查询CPU信息
     * @return ResultWrapper
     */
    @PreAuthorize("hasAuthority('devops_sysmonitor_select')")
    @GetMapping("/getCpuInfo")
    @ApiOperation(value = "当前CPU信息", notes = "当前CPU信息")
    public ResultWrapper<?> getCpuInfo() {
        return ResultWrapper
                .getSuccessResultWrapper(SystemInfoUtil.INSTANCE.getCpuInfo());
    }

    /**
     * 查询内存信息
     * @return ResultWrapper
     */
    @PreAuthorize("hasAuthority('devops_sysmonitor_select')")
    @GetMapping("/getMemInfo")
    @ApiOperation(value = "当前内存信息", notes = "当前内存信息")
    public ResultWrapper<?> getMemInfo() {
        return ResultWrapper.getSuccessResultWrapper(
                SystemInfoUtil.INSTANCE.getMemoryInfo());
    }

    /**
     * 查询JVM信息
     * @return ResultWrapper
     */
    @PreAuthorize("hasAuthority('devops_sysmonitor_select')")
    @GetMapping("/getJVMInfo")
    @ApiOperation(value = "当前JVM信息", notes = "当前JVM信息")
    public ResultWrapper<?> getJvmInfo() {
        return ResultWrapper.getSuccessResultWrapper(
                SystemInfoUtil.INSTANCE.getJvmInfo());
    }

}
