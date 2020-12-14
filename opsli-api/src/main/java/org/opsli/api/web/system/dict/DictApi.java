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
package org.opsli.api.web.system.dict;

import org.opsli.api.base.result.ResultVo;
import org.opsli.api.wrapper.system.dict.DictModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 数据字典 API
 *
 * 对外 API 直接 暴露 @GetMapping 或者 @PostMapping
 * 对内也推荐 单机版 不需要设置 Mapping 但是调用方法得从Controller写起
 *
 * 这样写法虽然比较绕，但是当单体项目想要改造微服务架构时 时非常容易的
 *
 *
 */
public interface DictApi {

    /** 标题 */
    String TITLE = "数据字典";

    /**
     * 数据字典 查一条
     * @param model 模型
     * @return ResultVo
     */
    @GetMapping("/get")
    ResultVo<DictModel> get(DictModel model);

    /**
     * 数据字典 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultVo
     */
    @GetMapping("/findPage")
    ResultVo<?> findPage(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    );

    /**
     * 数据字典 新增
     * @param model 模型
     * @return ResultVo
     */
    @PostMapping("/insert")
    ResultVo<?> insert(@RequestBody DictModel model);

    /**
     * 数据字典 修改
     * @param model 模型
     * @return ResultVo
     */
    @PostMapping("/update")
    ResultVo<?> update(@RequestBody DictModel model);

    /**
     * 数据字典 删除
     * @param id ID
     * @return ResultVo
     */
    @PostMapping("/del")
    ResultVo<?> del(String id);

    /**
     * 数据字典 批量删除
     * @param ids ID 数组
     * @return ResultVo
     */
    @PostMapping("/delAll")
    ResultVo<?> delAll(String[] ids);

    /**
     * 数据字典 Excel 导出
     * @param request request
     * @param response response
     * @return ResultVo
     */
    @GetMapping("/exportExcel")
    void exportExcel(HttpServletRequest request, HttpServletResponse response);

    /**
     * 数据字典 Excel 导入
     * @param request 文件流 request
     * @return ResultVo
     */
    @PostMapping("/importExcel")
    ResultVo<?> importExcel(MultipartHttpServletRequest request);

    /**
     * 数据字典 Excel 下载导入模版
     * @param response response
     * @return ResultVo
     */
    @GetMapping("/importExcel/template")
    void importTemplate(HttpServletResponse response);


    /**
     * 根据字典类型编号 查询出所有字典
     *
     * @param typeCode 字典类型编号
     * @return
     */
    @GetMapping("/getDictListByCode")
    ResultVo<?> getDictListByCode(String typeCode);
}
