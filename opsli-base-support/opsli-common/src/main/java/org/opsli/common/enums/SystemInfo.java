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
package org.opsli.common.enums;

import java.util.UUID;

/**
 * 通过单例 模式 生成系统唯一标示
 *
 * @author Parker
 * @date 2020-09-17 23:40
 */
public enum SystemInfo {

    /** 实例 */
    INSTANCE;

    private final String systemID;

    SystemInfo(){
        // 生成系统ID
        systemID = UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 获得系统ID
     * @return String
     */
    public String getSystemID() {
        return systemID;
    }
}
