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
package org.opsli.core.holder;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.opsli.core.utils.UserTokenUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 用户认证信息上下文
 *
 * @author Parker
 * @date 2021年12月22日16:22:59
 */
public final class UserContextHolder {

    /**
     * 存储用户上下文信息
     * 采用父子线程共享缓存
     */
    public static final ThreadLocal<String> THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 获取当前上下文用户
     * 推荐用法,避免空指针:
     * LoginUserInfo loginUserInfo = UserContextHolder.get().orElseThrow(() -> new BusinessException("提示未登录即可"));
     *
     * @return Optional<LoginUserInfo>
     */
    public static Optional<String> getToken() {

        String token = THREAD_LOCAL.get();

        // 2021-03-10
        // 这里纠正 Token 在被多聚合项目 aop切面 remove后 无法获得Token bug
        // 如果 token 为空 则尝试去 request 获取
        if(StringUtils.isEmpty(token)){
            try {
                RequestAttributes ra = RequestContextHolder.getRequestAttributes();
                ServletRequestAttributes sra = (ServletRequestAttributes) ra;
                if (sra != null) {
                    HttpServletRequest request = sra.getRequest();
                    token = UserTokenUtil.getRequestToken(request);
                }
            }catch (Exception ignored){}
        }

        return Optional.ofNullable(token);
    }


    public static void setToken(String token) {
        if (THREAD_LOCAL.get() == null) {
            THREAD_LOCAL.set(token);
        }
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }


    /**
     * 私有化构造函数
     */
    private UserContextHolder(){}

}
