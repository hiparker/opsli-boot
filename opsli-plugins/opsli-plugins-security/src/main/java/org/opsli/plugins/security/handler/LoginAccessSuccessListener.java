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
package org.opsli.plugins.security.handler;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录后 监听器
 *
 * @author Parker
 * @date 2022-07-16 10:51 PM
 **/
public interface LoginAccessSuccessListener {

    /**
     * 获得 监听器 类型
     *  注：如果指定了Type类型 则 如果当前消息类型与Listener的类型不符 则不会发器调用
     * @return Class
     */
    default Class<?> getModelType(){
        return Object.class;
    }

    /**
     * 执行
     *
     * @param model 登录参数（认证信息）
     * @param authenticate 登录参数（认证信息）
     * @param request request
     * @param response response
     */
    void handle(Object model, Authentication authenticate, HttpServletRequest request, HttpServletResponse response);

}
