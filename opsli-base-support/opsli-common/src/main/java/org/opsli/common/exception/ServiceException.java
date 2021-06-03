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
package org.opsli.common.exception;

import org.opsli.common.base.msg.BaseMsg;

/**
 * 框架服务异常
 *
 * @author Parker
 * @date 2020-09-13 19:41
 */
public class ServiceException extends RuntimeException{

    private Integer code;

    private String errorMessage;

    public ServiceException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public ServiceException(Integer code, String errorMessage, Throwable e) {
        super(errorMessage, e);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public ServiceException(BaseMsg msg) {
        super(msg.getMessage());
        this.code = msg.getCode();
        this.errorMessage = msg.getMessage();
    }

    public ServiceException(BaseMsg msg, Throwable e) {
        super(msg.getMessage(), e);
        this.code = msg.getCode();
        this.errorMessage = msg.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
