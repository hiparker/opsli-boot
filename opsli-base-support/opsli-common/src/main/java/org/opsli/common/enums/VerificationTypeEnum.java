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

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * 验证类型
 *
 * @author 周鹏程
 * @date 2022-08-01 12:49 PM
 **/
@AllArgsConstructor
@Getter
public enum VerificationTypeEnum {

    /** 类型 */
    LOGIN("0"),

    AUTH("1")

    ;

    /** 类型 */
    private final String type;

    public static Optional<VerificationTypeEnum> getEnumByType(String type){
        VerificationTypeEnum[] types = values();
        for (VerificationTypeEnum typeEnum : types) {
            if(typeEnum.type.equals(type)){
                return Optional.of(typeEnum);
            }
        }
        return Optional.empty();
    }

}
