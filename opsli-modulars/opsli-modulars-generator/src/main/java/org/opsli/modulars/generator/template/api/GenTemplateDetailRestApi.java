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

package org.opsli.modulars.generator.template.api;


import org.opsli.api.base.result.ResultWrapper;
import org.opsli.modulars.generator.template.wrapper.GenTemplateDetailModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 代码模板详情 Api
 *
 * 对外 API 直接 暴露 @GetMapping 或者 @PostMapping
 * 对内也推荐 单机版 不需要设置 Mapping 但是调用方法得从Controller写起
 *
 * 这样写法虽然比较绕，但是当单体项目想要改造微服务架构时 时非常容易的
 *
 * @author Pace
 * @date 2021-05-28 17:12:38
 */
public interface GenTemplateDetailRestApi {

    /** 标题 */
    String TITLE = "代码模板详情";
    /** 子标题 */
    String SUB_TITLE = "代码模板详情";

    /**
    * 代码模板详情 查一条
    * @param model 模型
    * @return ResultWrapper
    */
    @GetMapping("/get")
    ResultWrapper<GenTemplateDetailModel> get(GenTemplateDetailModel model);

    /**
    * 代码模板详情 查询分页
    * @param pageNo 当前页
    * @param pageSize 每页条数
    * @param request request
    * @return ResultWrapper
    */
    @GetMapping("/findPage")
    ResultWrapper<?> findPage(
        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
        HttpServletRequest request
    );

    /**
    * 代码模板详情 新增
    * @param model 模型
    * @return ResultWrapper
    */
    @PostMapping("/insert")
    ResultWrapper<?> insert(@RequestBody GenTemplateDetailModel model);

    /**
    * 代码模板详情 修改
    * @param model 模型
    * @return ResultWrapper
    */
    @PostMapping("/update")
    ResultWrapper<?> update(@RequestBody GenTemplateDetailModel model);

    /**
    * 代码模板详情 删除
    * @param id ID
    * @return ResultWrapper
    */
    @PostMapping("/del")
    ResultWrapper<?> del(String id);

    /**
    * 代码模板详情 批量删除
    * @param ids ID 数组
    * @return ResultWrapper
    */
    @PostMapping("/delAll")
    ResultWrapper<?> delAll(String ids);

    /**
     * 代码模板详情 Excel 导出认证
     *
     * @param type 类型
     * @param request request
     */
    @GetMapping("/excel/auth/{type}")
    ResultWrapper<String> exportExcelAuth(
            @PathVariable("type") String type,
            HttpServletRequest request);

    /**
     * 代码模板详情 Excel 导出
     *
     * @param certificate 凭证
     * @param response response
     */
    @GetMapping("/excel/export/{certificate}")
    void exportExcel(
            @PathVariable("certificate") String certificate,
            HttpServletResponse response);

    /**
    * 代码模板详情 Excel 导入
    * @param request 文件流 request
    * @return ResultWrapper
    */
    @PostMapping("/importExcel")
    ResultWrapper<?> importExcel(MultipartHttpServletRequest request);


    /**
     * 代码模板详情 Excel 下载导入模版
     * @param parentId parentId
     * @return ResultWrapper
     */
    @GetMapping("/findListByParentId")
    ResultWrapper<List<GenTemplateDetailModel>> findListByParentId(String parentId);

}
