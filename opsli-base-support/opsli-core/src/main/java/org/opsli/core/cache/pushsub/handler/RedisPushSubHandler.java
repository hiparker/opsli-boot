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
package org.opsli.core.cache.pushsub.handler;

import com.alibaba.fastjson.JSONObject;
import org.opsli.core.cache.pushsub.enums.PushSubType;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.receiver
 * @Author: Parker
 * @CreateTime: 2020-09-15 15:11
 * @Description: 标示类 用于获得 消息未知
 */
public interface RedisPushSubHandler {


    PushSubType getType();

    /**
     * 消息处理
     * @param msgJson
     */
    void handler(JSONObject msgJson);

}
