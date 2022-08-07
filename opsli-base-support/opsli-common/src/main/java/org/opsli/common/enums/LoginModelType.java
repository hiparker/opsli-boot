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

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;

/**
 * 登陆类型
 *
 * @author Parker
 * @date 2022-07-21 2:07 PM
 **/
public enum LoginModelType {

    /** 未知 */
    UNKNOWN,

    /** 账号 */
    ACCOUNT,

    /** 手机 */
    MOBILE,

    /** 邮箱 */
    EMAIL

    ;

    public static LoginModelType getTypeByStr(String principal){
        if(StrUtil.isBlank(principal)){
            return UNKNOWN;
        }

        // 手机号
        if(Validator.isMobile(principal)){
            return MOBILE;
        }
        // 邮箱
        else if(Validator.isEmail(principal)){
            return EMAIL;
        }

        // 默认账号
        return ACCOUNT;
    }

}
