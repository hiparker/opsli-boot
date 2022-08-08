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
package org.opsli.core.log.enums;

/**
 * 日志类型
 *
 * @author Parker
 * @date 2021年7月15日20:28:24
 */
public enum LogTypeEnum {

    /** WEB */
    WEB("0"),

    /** 客户端 */
    CLIENT("1"),

    /** 后端 */
    BACKEND("2"),

    /** 程序自动 */
    AUTO("3")
    ;

    private final String value;

    public String getValue() {
        return value;
    }

    LogTypeEnum(String value) {
        this.value = value;
    }
}
