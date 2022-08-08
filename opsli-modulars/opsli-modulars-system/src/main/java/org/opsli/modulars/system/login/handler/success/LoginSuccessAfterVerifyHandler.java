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
package org.opsli.modulars.system.login.handler.success;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.tenant.TenantModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.TokenException;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.utils.TenantUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.plugins.security.handler.LoginAccessSuccessListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 登陆成功后 验证器
 * @author Parker
 * @date 2022-07-17 12:57 PM
 **/
@AllArgsConstructor
@Component
public class LoginSuccessAfterVerifyHandler implements LoginAccessSuccessListener {

    @Override
    public void handle(
            Object model, Authentication authenticate,
            HttpServletRequest request, HttpServletResponse response) {
        // 1. 获取认证用户
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

        // 2. 获取认证用户其他信息
        UserModel user = UserUtil.getUserByUserName(userDetails.getUsername());

        // 如果不是超级管理员 则要进行安全验证
        if(!StringUtils.equals(UserUtil.SUPER_ADMIN, user.getUsername())){

            // 如果不是 系统用户， 也就是租户用户 需要验证租户启用情况
            if(!TenantUtil.SUPER_ADMIN_TENANT_ID.equals(user.getTenantId())){
                // 验证租户是否生效
                TenantModel tenant = TenantUtil.getTenant(user.getTenantId());
                if(tenant == null){
                    throw new TokenException(TokenMsg.EXCEPTION_LOGIN_TENANT_NOT_USABLE);
                }
            }

            // 账号锁定验证
            if(StringUtils.isEmpty(user.getEnable()) ||
                    DictType.NO_YES_NO.getValue().equals(user.getEnable())){
                // 账号已被锁定,请联系管理员
                throw new TokenException(TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCKED);
            }

            // 检测用户是否有角色
            List<String> roleModelList = UserUtil.getUserRolesByUserId(user.getId());
            if(CollUtil.isEmpty(roleModelList)){
                // 用户暂无角色，请设置后登录
                throw new TokenException(TokenMsg.EXCEPTION_USER_ROLE_NOT_NULL);
            }

            // 检测用户是否有角色菜单
            List<MenuModel> menuModelList = UserUtil.getMenuListByUserId(user.getId());
            if(CollUtil.isEmpty(menuModelList)){
                // 用户暂无角色菜单，请设置后登录
                throw new TokenException(TokenMsg.EXCEPTION_USER_MENU_NOT_NULL);
            }

            // 检测用户是否有角色权限
            List<String> userAllPermsList = UserUtil.getUserAllPermsByUserId(user.getId());
            if(CollUtil.isEmpty(userAllPermsList)){
                // 用户暂无角色菜单，请设置后登录
                throw new TokenException(TokenMsg.EXCEPTION_USER_PERMS_NOT_NULL);
            }
        }
    }

}
