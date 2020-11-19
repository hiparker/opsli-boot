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
package org.opsli.plugins.cache.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.mail.msg
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:54
 * @Description: 邮件消息
 */
public enum EhCacheMsg implements BaseMsg {

    /** 缓存未开启 */
    EXCEPTION_ENABLE(90001,"本地缓存未开启！"),
    EXCEPTION_PUT(90001,"添加缓存失败"),
    EXCEPTION_GET(90001,"获取缓存数据失败"),
    EXCEPTION_DEL(90001,"删除缓存数据失败"),


    ;


    private int code;
    private String message;

    EhCacheMsg(int code, String message){
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
