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
package org.opsli.plugins.security.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.plugins.security.exception.errorcode.BaseAuthMsg;

/**
 * 认证异常
 *
 * @author Parker
 * @date 2020-09-13 19:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthException extends RuntimeException{

    private Integer code;

    private String description;

    private String errorMessage;

    public AuthException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public AuthException(Integer code, String errorMessage, Throwable e) {
        super(errorMessage, e);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public AuthException(BaseAuthMsg msg) {
        super(msg.getMessage());
        this.code = msg.getCode();
        this.description = msg.getDescription();
        this.errorMessage = msg.getMessage();
    }

    public AuthException(BaseAuthMsg msg, Throwable e) {
        super(msg.getMessage(), e);
        this.code = msg.getCode();
        this.description = msg.getDescription();
        this.errorMessage = msg.getMessage();
    }

}
