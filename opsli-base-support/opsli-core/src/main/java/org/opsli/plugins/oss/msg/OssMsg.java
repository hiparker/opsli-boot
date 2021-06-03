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
package org.opsli.plugins.oss.msg;


import org.opsli.common.base.msg.BaseMsg;

/**
 * OSS服务异常 - 消息
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
public enum OssMsg implements BaseMsg {

    /**
     * OSS
     */
    EXCEPTION_UPLOAD_ERROR(90500, "上传文件失败，请检查配置信息"),
    EXCEPTION_UPLOAD_AUTH_ERROR(90501, "权限认证异常")

    ;

    private final int code;
    private final String message;

    OssMsg(int code, String message){
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
