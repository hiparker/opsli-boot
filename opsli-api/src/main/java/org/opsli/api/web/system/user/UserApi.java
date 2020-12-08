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
package org.opsli.api.web.system.user;

import org.opsli.api.base.result.ResultVo;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserInfo;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.api.wrapper.system.user.UserPassword;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 用户信息 API
 *
 * 对外 API 直接 暴露 @GetMapping 或者 @PostMapping
 * 对内也推荐 单机版 不需要设置 Mapping 但是调用方法得从Controller写起
 *
 * 这样写法虽然比较绕，但是当单体项目想要改造微服务架构时 时非常容易的
 *
 *
 */
public interface UserApi {

    /** 标题 */
    String TITLE = "用户信息";

    /**
     * 当前登陆用户信息
     * @return ResultVo
     */
    @GetMapping("/getInfo")
    ResultVo<UserInfo> getInfo(HttpServletRequest request);

    /**
     * 当前登陆用户信息
     * @return ResultVo
     */
    @GetMapping("/getInfoById")
    ResultVo<UserInfo> getInfoById(@RequestParam(name = "userId") String userId);

    /**
     * 当前登陆用户信息
     * @return ResultVo
     */
    @GetMapping("/getOrg")
    ResultVo<UserOrgRefModel> getOrg();

    /**
     * 当前登陆用户信息
     * @return ResultVo
     */
    @GetMapping("/getOrgByUserId")
    ResultVo<UserOrgRefModel> getOrgByUserId(@RequestParam(name = "userId") String userId);

    /**
     * 根据 userId 获得用户角色Id集合
     * @param userId 用户Id
     * @return ResultVo
     */
    @GetMapping("/getRoleIdsByUserId")
    ResultVo<List<String>> getRoleIdsByUserId(String userId);


    /**
     * 修改密码
     * @return ResultVo
     */
    @PostMapping("/updatePassword")
    ResultVo<?> updatePassword(@RequestBody UserPassword userPassword);

    /**
     * 修改密码 ID
     * @return ResultVo
     */
    @PostMapping("/updatePasswordById")
    ResultVo<?> updatePasswordById(@RequestBody UserPassword userPassword);


    /**
     * 上传头像
     * @param request 文件流 request
     * @return ResultVo
     */
    @PostMapping("/updateAvatar")
    ResultVo<?> updateAvatar(MultipartHttpServletRequest request);


    /**
     * 用户信息 查一条
     * @param model 模型
     * @return ResultVo
     */
    @GetMapping("/get")
    ResultVo<UserModel> get(UserModel model);

    /**
     * 用户信息 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultVo
     */
    @GetMapping("/findPage")
    ResultVo<?> findPage(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            UserOrgRefModel org,
            HttpServletRequest request
    );

    /**
     * 用户信息 新增
     * @param model 模型
     * @return ResultVo
     */
    @PostMapping("/insert")
    ResultVo<?> insert(@RequestBody UserModel model);

    /**
     * 用户信息 修改
     * @param model 模型
     * @return ResultVo
     */
    @PostMapping("/update")
    ResultVo<?> update(@RequestBody UserModel model);

    /**
     * 用户信息 删除
     * @param id ID
     * @return ResultVo
     */
    @PostMapping("/del")
    ResultVo<?> del(String id);

    /**
     * 用户信息 批量删除
     * @param ids ID 数组
     * @return ResultVo
     */
    @PostMapping("/delAll")
    ResultVo<?> delAll(String[] ids);

    /**
     * 用户信息 Excel 导出
     * @param request request
     * @param response response
     * @return ResultVo
     */
    @GetMapping("/exportExcel")
    ResultVo<?> exportExcel(HttpServletRequest request, HttpServletResponse response);

    /**
     * 用户信息 Excel 导入
     * @param request 文件流 request
     * @return ResultVo
     */
    @GetMapping("/exportImport")
    ResultVo<?> excelImport(MultipartHttpServletRequest request);


    /**
     * 用户信息 Excel 下载导入模版
     * @param response response
     * @return ResultVo
     */
    @GetMapping("/exportImport/template")
    ResultVo<?> importTemplate(HttpServletResponse response);


    /**
     * 根据 username 获得用户
     * @param username 用户名
     * @return ResultVo
     */
    //@GetMapping("/getUserByUsername")
    ResultVo<UserModel> getUserByUsername(String username);

    /**
     * 根据 userId 获得用户角色
     * @param userId 用户Id
     * @return ResultVo
     */
    //@GetMapping("/getRolesByUserId")
    ResultVo<List<String>> getRolesByUserId(String userId);

    /**
     * 根据 userId 获得用户权限
     * @param userId 用户Id
     * @return ResultVo
     */
    //@GetMapping("/queryAllPerms")
    ResultVo<List<String>> getAllPerms(String userId);

    /**
     * 根据 userId 获得用户菜单
     * @param userId 用户Id
     * @return ResultVo
     */
    //@GetMapping("/queryAllPerms")
    ResultVo<List<MenuModel>> getMenuListByUserId(String userId);


    /**
     * 当前登陆用户信息
     * @return ResultVo
     */
    ResultVo<UserOrgRefModel> getOrgInfoByUserId(String userId);


}
