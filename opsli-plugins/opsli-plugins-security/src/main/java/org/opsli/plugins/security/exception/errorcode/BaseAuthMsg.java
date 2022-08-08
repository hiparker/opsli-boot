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
package org.opsli.plugins.security.exception.errorcode;

/**
 * 总消息类 用来存放消息
 * 将消息全部提取出至一个总文件
 *
 * @author Parker
 * @date 2020-09-22 17:07
 */
public interface BaseAuthMsg {

    /**
     * 获取消息的状态码
     *
     * @return Integer
     */
    int getCode();

    /**
     * 获取消息的状态码
     *
     * @return Integer
     */
    String getDescription();

    /**
     * 获取消息提示信息
     *
     * @return String
     */
    String getMessage();

}
