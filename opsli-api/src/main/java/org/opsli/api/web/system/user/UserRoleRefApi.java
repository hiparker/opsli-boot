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

import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.role.RoleMenuRefModel;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.api.wrapper.system.user.UserRoleRefModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * 用户角色 API
 *
 * 对外 API 直接 暴露 @GetMapping 或者 @PostMapping
 * 对内也推荐 单机版 不需要设置 Mapping 但是调用方法得从Controller写起
 *
 * 这样写法虽然比较绕，但是当单体项目想要改造微服务架构时 时非常容易的
 *
 * @author Parker
 * @date 2020-09-13 17:40
 */
public interface UserRoleRefApi {

    /** 标题 */
    String TITLE = "用户角色管理";
    /** 子标题 */
    String SUB_TITLE = "用户角色";

    /**
     * 设置角色
     * @param userId 用户ID
     * @return ResultWrapper
     */
    @GetMapping("/getRoles")
    ResultWrapper<?> getRoles(String userId);

    /**
     * 设置角色
     * @param model 用户角色关联对象
     * @return ResultWrapper
     */
    @PostMapping("/setRoles")
    ResultWrapper<?> setRoles(@RequestBody UserRoleRefModel model);


    /**
     * 根据 userId 获得用户角色
     * @param userId 用户Id
     * @return ResultWrapper
     */
    //@GetMapping("/getRolesByUserId")
    ResultWrapper<List<String>> getRolesByUserId(String userId);

    /**
     * 根据 userId 获得用户默认角色
     * @param userId 用户Id
     * @return ResultWrapper
     */
    //@GetMapping("/getRolesByUserId")
    ResultWrapper<RoleModel> getDefRoleByUserId(String userId);

    /**
     * 根据 userId 获得用户权限
     * @param userId 用户Id
     * @return ResultWrapper
     */
    //@GetMapping("/queryAllPerms")
    ResultWrapper<List<String>> getAllPerms(String userId);

    /**
     * 根据 userId 获得用户菜单
     * @param userId 用户Id
     * @return ResultWrapper
     */
    //@GetMapping("/queryAllPerms")
    ResultWrapper<List<MenuModel>> getMenuListByUserId(String userId);

}
