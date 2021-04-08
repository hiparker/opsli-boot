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
package org.opsli.modulars.creater.createrlogs.api;

import org.opsli.api.base.result.ResultVo;
import org.opsli.modulars.creater.createrlogs.wrapper.CreaterLogsModel;
import org.opsli.modulars.creater.table.wrapper.CreaterTableAndColumnModel;
import org.opsli.modulars.creater.table.wrapper.CreaterTableModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 代码生成器 - 生成记录 API
 *
 * 对外 API 直接 暴露 @GetMapping 或者 @PostMapping
 * 对内也推荐 单机版 不需要设置 Mapping 但是调用方法得从Controller写起
 *
 * 这样写法虽然比较绕，但是当单体项目想要改造微服务架构时 时非常容易的
 *
 *
 */
public interface CreaterLogsApi {

    /** 标题 */
    String TITLE = "代码生成器-日志";
    /** 子标题 */
    String SUB_TITLE = "代码生成器-日志";

    /**
     * 生成记录 查一条
     * @param tableId 模型
     * @return ResultVo
     */
    @GetMapping("/getByTableId")
    ResultVo<CreaterLogsModel> getByTableId(String tableId);

    /**
     * 生成记录 修改
     * @param model 模型
     * @return ResultVo
     */
    @GetMapping("/create")
    void create(CreaterLogsModel model, HttpServletResponse response);


}
