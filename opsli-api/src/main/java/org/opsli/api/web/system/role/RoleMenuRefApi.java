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
package org.opsli.api.web.system.role;

import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.wrapper.system.role.RoleMenuRefModel;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 角色菜单 API
 *
 * 对外 API 直接 暴露 @GetMapping 或者 @PostMapping
 * 对内也推荐 单机版 不需要设置 Mapping 但是调用方法得从Controller写起
 *
 * 这样写法虽然比较绕，但是当单体项目想要改造微服务架构时 时非常容易的
 *
 * @author Parker
 * @date 2020-09-13 17:40
 */
public interface RoleMenuRefApi {

    /** 标题 */
    String TITLE = "角色权限管理";
    /** 子标题 */
    String SUB_TITLE = "角色权限";

    /**
     * 获得当前已有权限
     * @param model 角色Id
     * @return ResultWrapper
     */
    @GetMapping("/getPerms")
    ResultWrapper<?> getPerms(RoleMenuRefModel model);

    /**
     * 设置权限
     * @param model roleId 角色Id
     * @param model permsIds 权限Id 数组
     * @return ResultWrapper
     */
    @PostMapping("/setPerms")
    ResultWrapper<?> setPerms(@RequestBody RoleMenuRefModel model);

}
