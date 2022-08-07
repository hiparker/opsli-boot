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

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.constants.RedisConstants;
import org.opsli.common.enums.LoginModelType;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.plugins.redis.RedisPlugin;
import org.opsli.plugins.security.handler.LoginAccessSuccessListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 登陆成功后 清除错误记录 (LoginModel)
 * @author Parker
 * @date 2022-07-17 12:57 PM
 **/
@AllArgsConstructor
@Component
public class LoginClearErrorSuccessHandler implements LoginAccessSuccessListener {

    private final RedisPlugin redisPlugin;

    @Override
    public void handle(
            Object model, Authentication authenticate,
            HttpServletRequest request, HttpServletResponse response) {
        // 1. 获取认证用户
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

        // 2. 获取认证用户其他信息
        UserModel userModel = UserUtil.getUserByUserName(userDetails.getUsername());

        // 3. 清除错误记录
        List<String> delKeyList = Lists.newArrayListWithCapacity(6);

        // 账号
        if(StrUtil.isNotEmpty(userModel.getUsername())){
            // 获得当前失败次数
            String countKey = CacheUtil.formatKey(
                    RedisConstants.PREFIX_ACCOUNT_SLIP_COUNT + LoginModelType.ACCOUNT.name().toLowerCase()
                            + ":" + userModel.getUsername());
            // 获得当前失败锁定状态
            String lockKey = CacheUtil.formatKey(
                    RedisConstants.PREFIX_ACCOUNT_SLIP_LOCK + LoginModelType.ACCOUNT.name().toLowerCase()
                            + ":" + userModel.getUsername());
            delKeyList.add(countKey);
            delKeyList.add(lockKey);
        }

        // 手机
        if(StrUtil.isNotEmpty(userModel.getMobile())){
            // 手机 - 获得当前失败次数
            String countKey = CacheUtil.formatKey(
                    RedisConstants.PREFIX_ACCOUNT_SLIP_COUNT + LoginModelType.MOBILE.name().toLowerCase()
                            + ":" + userModel.getMobile());
            // 手机 - 获得当前失败锁定状态
            String lockKey = CacheUtil.formatKey(
                    RedisConstants.PREFIX_ACCOUNT_SLIP_LOCK + LoginModelType.MOBILE.name().toLowerCase()
                            + ":" + userModel.getMobile());
            delKeyList.add(countKey);
            delKeyList.add(lockKey);
        }

        // 邮箱
        if(StrUtil.isNotEmpty(userModel.getEmail())){
            // 获得当前失败次数
            String countKey = CacheUtil.formatKey(
                    RedisConstants.PREFIX_ACCOUNT_SLIP_COUNT + LoginModelType.EMAIL.name().toLowerCase()
                            + ":" + userModel.getEmail());
            // 获得当前失败锁定状态
            String lockKey = CacheUtil.formatKey(
                    RedisConstants.PREFIX_ACCOUNT_SLIP_LOCK + LoginModelType.EMAIL.name().toLowerCase()
                            + ":" + userModel.getEmail());
            delKeyList.add(countKey);
            delKeyList.add(lockKey);
        }

        // 批量删除
        redisPlugin.del(delKeyList);
    }

}
