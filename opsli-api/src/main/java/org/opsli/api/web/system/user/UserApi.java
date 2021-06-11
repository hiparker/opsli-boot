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
import org.opsli.api.wrapper.system.user.UserOrgRefWebModel;
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
 * 用户信息 API
 *
 * 对外 API 直接 暴露 @GetMapping 或者 @PostMapping
 * 对内也推荐 单机版 不需要设置 Mapping 但是调用方法得从Controller写起
 *
 * 这样写法虽然比较绕，但是当单体项目想要改造微服务架构时 时非常容易的
 *
 * @author Parker
 * @date 2020-09-13 17:40
 */
public interface UserApi {

    /** 标题 */
    String TITLE = "用户管理";
    /** 子标题 */
    String SUB_TITLE = "用户";

    /**
     * 当前登陆用户信息
     * @param request request
     * @return ResultVo
     */
    @GetMapping("/getInfo")
    ResultVo<UserInfo> getInfo(HttpServletRequest request);

    /**
     * 当前登陆用户信息
     *
     * @param userId 用户ID
     * @return ResultVo
     */
    @GetMapping("/getInfoById")
    ResultVo<UserInfo> getInfoById(String userId);

    /**
     * 当前登陆用户信息
     * @return ResultVo
     */
    @GetMapping("/getOrg")
    ResultVo<?> getOrg();

    /**
     * 当前登陆用户信息
     *
     * @param userId 用户ID
     * @return ResultVo
     */
    @GetMapping("/getOrgByUserId")
    ResultVo<?> getOrgByUserId(String userId);

    /**
     * 根据 userId 获得用户角色Id集合
     * @param userId 用户Id
     * @return ResultVo
     */
    @GetMapping("/getRoleIdsByUserId")
    ResultVo<List<String>> getRoleIdsByUserId(String userId);


    /**
     * 修改密码
     * @param userPassword 账号密码
     * @return ResultVo
     */
    @PostMapping("/updatePassword")
    ResultVo<?> updatePassword(@RequestBody UserPassword userPassword);

    /**
     * 修改密码 ID
     *
     * @param userPassword 密码类
     * @return ResultVo
     */
    @PostMapping("/updatePasswordById")
    ResultVo<?> updatePasswordById(@RequestBody UserPassword userPassword);

    /**
     * 重置密码 ID
     *
     * @param userId 用户ID
     * @return ResultVo
     */
    @PostMapping("/resetPasswordById")
    ResultVo<?> resetPasswordById(String userId);

    /**
     * 变更账户状态
     *
     * @param userId 用户ID
     * @param enable 启用状态
     * @return ResultVo
     */
    @PostMapping("/enableAccount")
    ResultVo<?> enableAccount(String userId, String enable);

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
     * @param orgIdGroup 组织ID组
     * @param request request
     * @return ResultVo
     */
    @GetMapping("/findPage")
    ResultVo<?> findPage(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "orgIdGroup") String orgIdGroup,
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
     * 用户自身信息 修改
     * @param model 模型
     * @return ResultVo
     */
    @PostMapping("/updateSelf")
    ResultVo<?> updateSelf(@RequestBody UserModel model);


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
    ResultVo<?> delAll(String ids);

    /**
     * 用户信息 Excel 导出
     * @param request request
     * @param response response
     */
    @GetMapping("/exportExcel")
    void exportExcel(HttpServletRequest request, HttpServletResponse response);

    /**
     * 用户信息 Excel 导入
     * @param request 文件流 request
     * @return ResultVo
     */
    @PostMapping("/importExcel")
    ResultVo<?> importExcel(MultipartHttpServletRequest request);


    /**
     * 用户信息 Excel 下载导入模版
     * @param response response
     */
    @GetMapping("/importExcel/template")
    void importTemplate(HttpServletResponse response);


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
     *
     * @param userId 用户ID
     * @return ResultVo
     */
    ResultVo<UserOrgRefWebModel> getOrgInfoByUserId(String userId);


}
