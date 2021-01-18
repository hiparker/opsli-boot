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
package org.opsli.common.api;


/**
 * 用于存放当前线程下 Token
 *
 * @author parker
 * @date 2020-09-15
 */
public class TokenThreadLocal {

    /** 临时线程存储 token 容器 */
    private static final ThreadLocal<String> TOKEN_DATA = new ThreadLocal<>();

    public static void put(String token) {
        if (TOKEN_DATA.get() == null) {
            TOKEN_DATA.set(token);
        }
    }

    public static String get() {
        return TOKEN_DATA.get();
    }

    public static void remove() {
        try {
            TOKEN_DATA.remove();
        }catch (Exception ignored){}
    }
}
