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
package org.opsli.modulars.generator.table.api;

import org.opsli.api.base.result.ResultWrapper;
import org.opsli.modulars.generator.table.wrapper.GenTableAndColumnModel;
import org.opsli.modulars.generator.table.wrapper.GenTableModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器 - 表 API
 *
 * 对外 API 直接 暴露 @GetMapping 或者 @PostMapping
 * 对内也推荐 单机版 不需要设置 Mapping 但是调用方法得从Controller写起
 *
 * 这样写法虽然比较绕，但是当单体项目想要改造微服务架构时 时非常容易的
 *
 * @author Pace
 * @date 2020-09-16 17:34
 */
public interface TableApi {

    /** 标题 */
    String TITLE = "代码生成器-表";
    /** 子标题 */
    String SUB_TITLE = "代码生成器-表";

    /**
     * 表 查一条
     * @param model 模型
     * @return ResultWrapper
     */
    @GetMapping("/get")
    ResultWrapper<GenTableAndColumnModel> get(GenTableModel model);

    /**
     * 表 查询分页
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
     * 表 新增
     * @param model 模型
     * @return ResultWrapper
     */
    @PostMapping("/insert")
    ResultWrapper<?> insert(@RequestBody GenTableAndColumnModel model);

    /**
     * 表 修改
     * @param model 模型
     * @return ResultWrapper
     */
    @PostMapping("/update")
    ResultWrapper<?> update(@RequestBody GenTableAndColumnModel model);

    /**
     * 表 删除
     * @param id ID
     * @return ResultWrapper
     */
    @PostMapping("/del")
    ResultWrapper<?> del(String id);

    /**
     * 表 批量删除
     * @param ids ID 数组
     * @return ResultWrapper
     */
    @PostMapping("/delAll")
    ResultWrapper<?> delAll(String ids);

    /**
     * 同步到数据库
     * @param id ID
     * @return ResultWrapper
     */
    @PostMapping("/sync")
    ResultWrapper<?> sync(String id);

    /**
     * 获得当前数据库
     * @return ResultWrapper
     */
    @GetMapping("/getTables")
    ResultWrapper<?> getTables();

    /**
     * 导入选中表
     *
     * @param tableNames 表名集合
     * @return ResultWrapper
     */
    @PostMapping("/importTables")
    ResultWrapper<?> importTables(String tableNames);

    /**
     * 获得数据库类型下 字段类型
     * @return List
     */
    @GetMapping("/getFieldTypes")
    ResultWrapper<List<String>> getFieldTypes();

    /**
     * 获得数据库类型下 全部类型对应Java类型
     * @return List
     */
    @GetMapping("/getJavaFieldTypes")
    ResultWrapper<Map<String, String> > getJavaFieldTypes();

    /**
     * 获得全部类型对应Java类型集合（兜底String 类型）
     * @return List
     */
    @GetMapping("/getJavaFieldTypesBySafety")
    ResultWrapper<Map<String, List<String>>> getJavaFieldTypesBySafety();


}
