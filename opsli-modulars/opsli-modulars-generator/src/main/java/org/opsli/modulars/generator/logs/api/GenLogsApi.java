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
package org.opsli.modulars.generator.logs.api;

import org.opsli.api.base.result.ResultWrapper;
import org.opsli.modulars.generator.logs.wrapper.GenLogsModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 代码生成器 - 生成记录 API
 *
 * 对外 API 直接 暴露 @GetMapping 或者 @PostMapping
 * 对内也推荐 单机版 不需要设置 Mapping 但是调用方法得从Controller写起
 *
 * 这样写法虽然比较绕，但是当单体项目想要改造微服务架构时 时非常容易的
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
public interface GenLogsApi {

    /** 标题 */
    String TITLE = "代码生成器-日志";
    /** 子标题 */
    String SUB_TITLE = "代码生成器-日志";

    /**
     * 生成记录 查一条
     * @param tableId 模型
     * @return ResultWrapper
     */
    @GetMapping("/getByTableId")
    ResultWrapper<GenLogsModel> getByTableId(String tableId);

    /**
     * 代码生成 修改
     */
    @GetMapping("/create")
    void create(HttpServletRequest request, HttpServletResponse response);

    /**
     * 生成菜单
     *
     * @param menuParentId 上级菜单ID
     * @param tableId 表ID
     * @return ResultWrapper
     */
    @PostMapping("/createMenu")
    ResultWrapper<?> createMenu(String menuParentId, String tableId);


}
