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
package org.opsli.api.web.system.org;

import org.opsli.api.base.result.ResultVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opsli.api.wrapper.system.org.SysOrgModel;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.api.web.system.org
 * @Author: Parker
 * @CreateTime: 2020-11-28 18:59:59
 * @Description: 组织机构表
 *
 * 对外 API 直接 暴露 @GetMapping 或者 @PostMapping
 * 对内也推荐 单机版 不需要设置 Mapping 但是调用方法得从Controller写起
 *
 * 这样写法虽然比较绕，但是当单体项目想要改造微服务架构时 时非常容易的
 *
 *
 */
public interface SysOrgRestApi {

    /** 标题 */
    String TITLE = "组织机构";


    @GetMapping("/findTreeLazyByUser")
    ResultVo<?> findTreeLazyByUser(String parentId);

    @GetMapping("/findTreeLazy")
    ResultVo<?> findTreeLazy(String parentId);

    @GetMapping("/findGridTree")
    ResultVo<?> findGridTree(String parentId);

    // ================

    /**
    * 组织机构表 查一条
    * @param model 模型
    * @return ResultVo
    */
    @GetMapping("/get")
    ResultVo<SysOrgModel> get(SysOrgModel model);

    /**
    * 组织树
    * @param request request
    * @return ResultVo
    */
    @GetMapping("/findTree")
    ResultVo<?> findTree( HttpServletRequest request );

    /**
    * 组织机构表 新增
    * @param model 模型
    * @return ResultVo
    */
    @PostMapping("/insert")
    ResultVo<?> insert(@RequestBody SysOrgModel model);

    /**
    * 组织机构表 修改
    * @param model 模型
    * @return ResultVo
    */
    @PostMapping("/update")
    ResultVo<?> update(@RequestBody SysOrgModel model);

    /**
    * 组织机构表 删除
    * @param id ID
    * @return ResultVo
    */
    @PostMapping("/del")
    ResultVo<?> del(String id);

    /**
    * 组织机构表 批量删除
    * @param ids ID 数组
    * @return ResultVo
    */
    @PostMapping("/delAll")
    ResultVo<?> delAll(String[] ids);

    /**
    * 组织机构表 Excel 导出
    * @param request request
    * @param response response
    * @return ResultVo
    */
    @GetMapping("/exportExcel")
    ResultVo<?> exportExcel(HttpServletRequest request, HttpServletResponse response);

    /**
    * 组织机构表 Excel 导入
    * @param request 文件流 request
    * @return ResultVo
    */
    @GetMapping("/exportImport")
    ResultVo<?> excelImport(MultipartHttpServletRequest request);

    /**
    * 组织机构表 Excel 下载导入模版
    * @param response response
    * @return ResultVo
    */
    @GetMapping("/exportImport/template")
    ResultVo<?> importTemplate(HttpServletResponse response);

}
