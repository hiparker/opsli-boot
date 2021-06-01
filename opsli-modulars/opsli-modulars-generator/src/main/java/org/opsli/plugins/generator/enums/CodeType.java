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
package org.opsli.plugins.generator.enums;

/**
 * 代码类型
 *
 * @author Mybatis-plus
 * @date 2020-09-22 11:17
 */
public enum CodeType {

    /** 代码类型 */
    BACKEND("0", "后端"),
    FRONTEND("1", "前端"),
    ;

    private final String type;
    private final String desc;

    public static CodeType getCodeType(String type) {
        CodeType[] var = values();
        for (CodeType codeType : var) {
            if (codeType.type.equalsIgnoreCase(type)) {
                return codeType;
            }
        }
        return BACKEND;
    }

    public String getType() {
        return this.type;
    }

    public String getDesc() {
        return this.desc;
    }

    // ================

    CodeType(final String type, final String desc) {
        this.type = type;
        this.desc = desc;
    }
}
