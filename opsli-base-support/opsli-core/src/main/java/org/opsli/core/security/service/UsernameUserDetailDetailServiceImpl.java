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
package org.opsli.core.security.service;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.enums.DictType;
import org.opsli.core.utils.UserUtil;
import org.opsli.plugins.security.UserDetailModel;
import org.opsli.plugins.security.properties.AuthProperties;
import org.opsli.plugins.security.service.ILoadUserDetailService;
import org.opsli.plugins.security.utils.PasswordUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户名+密码 获取用户信息Service
 * 实际只需要用到 用户名唯一主键即可
 *
 * @author Parker
 * @date 2022-07-14 4:44 PM
 **/
@AllArgsConstructor
@Service("usernameUserDetailDetailService")
public class UsernameUserDetailDetailServiceImpl implements ILoadUserDetailService {

    private static final String DEFAULT_ROLE_PREFIX = "ROLE_";
    private final AuthProperties authProperties;

    @Override
    public Collection<Class<? extends Authentication>> getClassTypes() {
        return Collections.singleton(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public Optional<UserDetails> loadUserByPrincipal(Object principal) {
        UserModel userModel = UserUtil.getUserByUserName((String) principal);
        if(null == userModel){
            return Optional.empty();
        }

        // 处理权限数据
        List<String> authorities = new ArrayList<>();
        List<String> userRoles = UserUtil.getUserRolesByUserId(userModel.getId());
        List<String> userAllPerms = UserUtil.getUserAllPermsByUserId(userModel.getId());
        for (String userRole : userRoles) {
            authorities.add(DEFAULT_ROLE_PREFIX+userRole);
        }
        authorities.addAll(userAllPerms);

        // 清除空的 授权
        authorities.removeIf(StrUtil::isEmpty);

        List<GrantedAuthority> grantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetailModel userDetailModel = UserDetailModel.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                // 账户启动
                .enabled(
                        DictType.NO_YES_YES.getValue().equals(userModel.getEnable()))
                // 账户未过期（如果需要 请自行扩展字段）
                .accountNonExpired(
                        DictType.NO_YES_YES.getValue().equals(userModel.getEnable()))
                // 账户未锁定（如果需要 请自行扩展字段）
                .accountNonLocked(
                        DictType.NO_YES_YES.getValue().equals(userModel.getEnable()))
                // 判断凭证是否过期（默认不判断 如果需要 请自行扩展过期后修改密码操作）
                .credentialsNonExpired(
                        PasswordUtil.isCredentialsNonExpired(
                                userModel.getPassword(), authProperties.getCredentialsExpired()))
                // 授权信息
                .authorities(grantedAuthorities)
                .build();

        return Optional.ofNullable(userDetailModel);
    }

}
