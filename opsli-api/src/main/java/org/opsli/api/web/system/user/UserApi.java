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

import org.opsli.api.base.encrypt.EncryptModel;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.api.wrapper.system.user.*;
import org.springframework.web.bind.annotation.*;
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
     * @return ResultWrapper
     */
    @GetMapping("/getInfo")
    ResultWrapper<UserInfo> getInfo(HttpServletRequest request);

    /**
     * 当前登陆用户信息
     *
     * @param userId 用户ID
     * @return ResultWrapper
     */
    @GetMapping("/getInfoById")
    ResultWrapper<UserInfo> getInfoById(String userId);

    /**
     * 当前登陆用户信息
     * @return ResultWrapper
     */
    @GetMapping("/getOrg")
    ResultWrapper<?> getOrg();

    /**
     * 当前登陆用户信息
     *
     * @param userId 用户ID
     * @return ResultWrapper
     */
    @GetMapping("/getOrgByUserId")
    ResultWrapper<?> getOrgByUserId(String userId);

    /**
     * 根据 userId 获得用户角色Id集合
     * @param userId 用户Id
     * @return ResultWrapper
     */
    @GetMapping("/getRoleIdsByUserId")
    ResultWrapper<List<String>> getRoleIdsByUserId(String userId);

    /**
     * 修改密码 ID
     *
     * @param encryptModel 账号密码
     * @return ResultVo
     */
    @PostMapping("/updatePasswordById")
    ResultWrapper<?> updatePasswordById(@RequestBody EncryptModel encryptModel);


    /**
     * 修改密码
     * @param encryptModel 账号密码
     * @return ResultWrapper
     */
    @PostMapping("/updatePassword")
    ResultWrapper<?> updatePassword(@RequestBody EncryptModel encryptModel);

    /**
     * 修改邮箱
     * @param encryptModel 加密数据
     * @return ResultWrapper
     */
    @PostMapping("/updateEmail")
    ResultWrapper<?> updateEmail(@RequestBody EncryptModel encryptModel);

    /**
     * 修改手机
     * @param encryptModel 加密数据
     * @return ResultWrapper
     */
    @PostMapping("/updateMobile")
    ResultWrapper<?> updateMobile(@RequestBody EncryptModel encryptModel);


    /**
     * 重置密码 ID
     *
     * @param encryptModel 加密数据（用户ID）
     * @return ResultWrapper
     */
    @PostMapping("/resetPasswordById")
    ResultWrapper<?> resetPasswordById(@RequestBody EncryptModel encryptModel);

    /**
     * 变更账户状态
     *
     * @param encryptModel 加密数据（EnableUserModel）
     * @return ResultWrapper
     */
    @PostMapping("/enableAccount")
    ResultWrapper<?> enableAccount(@RequestBody EncryptModel encryptModel);

    /**
     * 修改密码（忘记密码）
     *
     * @param encryptModel 加密数据
     * @return ResultWrapper
     */
    @PostMapping("/updatePasswordByForget")
    ResultWrapper<?> updatePasswordByForget(@RequestBody EncryptModel encryptModel);


    /**
     * 上传头像
     * @param userAvatarModel 图片地址
     * @return ResultWrapper
     */
    @PostMapping("/updateAvatar")
    ResultWrapper<?> updateAvatar(@RequestBody UserAvatarModel userAvatarModel);


    /**
     * 用户信息 查一条
     * @param model 模型
     * @return ResultWrapper
     */
    @GetMapping("/get")
    ResultWrapper<UserModel> get(UserModel model);

    /**
     * 用户信息 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param orgIdGroup 组织ID组
     * @param request request
     * @return ResultWrapper
     */
    @GetMapping("/findPage")
    ResultWrapper<?> findPage(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "orgIdGroup") String orgIdGroup,
            HttpServletRequest request
    );

    /**
     * 用户信息 根据租户Id 查询 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultWrapper
     */
    @GetMapping("/findPageByTenant")
    ResultWrapper<?> findPageByTenant(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    );



    /**
     * 用户信息 新增
     * @param model 模型
     * @return ResultWrapper
     */
    @PostMapping("/insert")
    ResultWrapper<?> insert(@RequestBody UserModel model);

    /**
     * 用户信息 修改
     * @param model 模型
     * @return ResultWrapper
     */
    @PostMapping("/update")
    ResultWrapper<?> update(@RequestBody UserModel model);

    /**
     * 用户自身信息 修改
     * @param model 模型
     * @return ResultWrapper
     */
    @PostMapping("/updateSelf")
    ResultWrapper<?> updateSelf(@RequestBody UserModel model);


    /**
     * 用户信息 删除
     * @param encryptModel 加密（id ID）
     * @return ResultWrapper
     */
    @PostMapping("/del")
    ResultWrapper<?> del(@RequestBody EncryptModel encryptModel);

    /**
     * 用户信息 批量删除
     * @param encryptModel 加密（ids ID 数组）
     * @return ResultWrapper
     */
    @PostMapping("/delAll")
    ResultWrapper<?> delAll(@RequestBody EncryptModel encryptModel);


    /**
     * 用户信息 Excel 导出认证
     *
     * @param type 类型
     * @param request request
     */
    @GetMapping("/excel/auth/{type}")
    ResultWrapper<String> exportExcelAuth(
            @PathVariable("type") String type,
            HttpServletRequest request);

    /**
     * 用户信息 Excel 导出
     *
     * @param certificate 凭证
     * @param response response
     */
    @GetMapping("/excel/export/{certificate}")
    void exportExcel(
            @PathVariable("certificate") String certificate,
            HttpServletResponse response);

    /**
     * 用户信息 Excel 导入
     * @param request 文件流 request
     * @return ResultWrapper
     */
    @PostMapping("/importExcel")
    ResultWrapper<?> importExcel(MultipartHttpServletRequest request);




    /**
     * 切换租户
     * @param tenantId 租户ID
     * @return ResultWrapper
     */
    @PostMapping("/switchTenant")
    ResultWrapper<?> switchTenant(String tenantId);

    /**
     * 切换回自己账户
     * @return ResultWrapper
     */
    @PostMapping("/switchOneself")
    ResultWrapper<?> switchOneself();

    /**
     * 根据 username 获得用户
     * @param username 用户名
     * @return ResultWrapper
     */
    //@GetMapping("/getUserByUsername")
    ResultWrapper<UserModel> getUserByUsername(String username);

    /**
     * 根据 手机号 获得用户
     * @param mobile 手机号
     * @return ResultWrapper
     */
    ResultWrapper<UserModel> getUserByMobile(String mobile);

    /**
     * 根据 邮箱 获得用户
     * @param email 邮箱
     * @return ResultWrapper
     */
    ResultWrapper<UserModel> getUserByEmail(String email);



}
