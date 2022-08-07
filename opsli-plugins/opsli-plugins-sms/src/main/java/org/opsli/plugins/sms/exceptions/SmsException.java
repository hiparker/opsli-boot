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
package org.opsli.plugins.sms.exceptions;


import org.opsli.common.base.msg.BaseMsg;
import org.opsli.common.exception.ServiceException;

/**
 * 短信异常
 *
 * @author 周鹏程
 * @date 2021年8月27日14:27:35
 */
public class SmsException extends ServiceException {

    public SmsException(Integer code, String errorMessage) {
        super(code, errorMessage);
    }

    public SmsException(BaseMsg msg) {
        super(msg);
    }
}
