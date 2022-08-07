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
import org.opsli.api.wrapper.system.org.SysOrgModel;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.api.wrapper.system.user.UserOrgRefWebModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * 用户组织 API
 *
 * 对外 API 直接 暴露 @GetMapping 或者 @PostMapping
 * 对内也推荐 单机版 不需要设置 Mapping 但是调用方法得从Controller写起
 *
 * 这样写法虽然比较绕，但是当单体项目想要改造微服务架构时 时非常容易的
 *
 * @author Parker
 * @date 2020-09-13 17:40
 */
public interface UserOrgRefApi {

    /** 标题 */
    String TITLE = "用户组织管理";
    /** 子标题 */
    String SUB_TITLE = "用户组织";


    /**
     * 设置角色
     * @param model 用户组织关联对象
     * @return ResultWrapper
     */
    @PostMapping("/setOrg")
    ResultWrapper<?> setOrg(@RequestBody UserOrgRefWebModel model);

    /**
     * 根据用户ID 获得组织列表
     * @param userId 用户ID
     * @return List
     */
    ResultWrapper<List<UserOrgRefModel>> findListByUserId(String userId);


    /**
     * 根据 userId 获得用户默认组织
     * @param userId 用户Id
     * @return ResultWrapper
     */
    //@GetMapping("/getRolesByUserId")
    ResultWrapper<UserOrgRefModel> getDefOrgByUserId(String userId);

}
