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
package org.opsli.plugins.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;

/**
 * 加载用户信息
 *
 * @author Parker
 * @date 2022-07-16 3:44 PM
 **/
public interface ILoadUserDetailService {

    /**
     * 获得Class 类型
     *  该方法 标识该Service 能够支持 哪儿些 AuthenticationToken
     * 注：重复的 AuthenticationToken.class 会被覆盖
     * @return Class
     */
    Collection<Class<? extends Authentication>> getClassTypes();

    /**
     * 加载用户信息
     * 实现方式: 只需要负责 三步处理
     *  1. 如何获得用户？
     *  2. 如果用户为空怎么办？
     *  3. 如何将系统的用户属性封装成 UserDetails 返回出去？
     *      (推荐使用 UserDetailModel.builder()来完成此操作 )
     *
     * @param principal 主键
     * @return Optional<UserDetails>
     */
    Optional<UserDetails> loadUserByPrincipal(Object principal);

}
