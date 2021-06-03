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
package org.opsli.plugins.oss.enums;

/**
 * 存储服务类型
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
public enum OssStorageType {

    /** 存储服务类型 */
    LOCAL("local", "本地"),
    UP_YUN("upYun", "又拍云"),


    ;

    private final String code;
    private final String desc;

    public static OssStorageType getType(String cacheType) {
        OssStorageType[] types = values();
        for (OssStorageType type : types) {
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

    OssStorageType(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }
}
