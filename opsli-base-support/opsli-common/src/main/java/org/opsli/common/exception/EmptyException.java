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


/**
 * 空异常
 *
 * @author Pace
 * @date 2020-09-17 23:40
 */
public class EmptyException extends ServiceException{

    private Integer code;

    private String errorMessage;

    public EmptyException() {
        super(400, "请求数据不完整或格式错误！");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
