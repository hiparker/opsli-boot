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
package org.opsli.plugins.waf.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * 防火墙 消息
 *
 * @author Pace
 * @date 2020-09-16
 */
public enum WafMsg implements BaseMsg {

    /**
     * 防火墙
     */
    WAF_EXCEPTION_XSS(10500, "包含非法字符！"),
    WAF_EXCEPTION_SQL(10501, "包含非法字符！"),

    ;


    private final int code;
    private final String message;

    WafMsg(int code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
