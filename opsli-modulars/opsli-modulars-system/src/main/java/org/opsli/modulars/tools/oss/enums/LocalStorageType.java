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
package org.opsli.modulars.tools.oss.enums;

/**
 * 本地存储 参数类型
 *
 * @author Parker
 */
public enum LocalStorageType {

    /** 本地存储服务 */
    DOMAIN("storage_local_domain", "域名"),
    PATH_PREFIX("storage_local_path_prefix", "路径前缀"),

    ;

    private final String code;
    private final String desc;

    public static LocalStorageType getType(String cacheType) {
        LocalStorageType[] var1 = values();
        for (LocalStorageType type : var1) {
            if (type.code.equalsIgnoreCase(cacheType)) {
                return type;
            }
        }
        return null;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    // =================

    LocalStorageType(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }
}
