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
package org.opsli.plugins.redis.pushsub.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;
import org.opsli.common.enums.SystemInfo;

/**
 * 广播消息时，不会处理 自身的数据，如果要处理请预先处理
 *
 * 防止广播数据处理到自身时出现延迟问题
 *
 * @author Parker
 * @date 2020-09-16 11:47
 */
@Data
@Accessors(chain = true)
public class BaseSubMessage implements RedisPushSubMessage{

    public static final String BASE_TYPE = "TYPE";
    public static final String BASE_ID = "IDENTIFIER";

    /** 发布订阅频道名称 */
    protected String channel;

    protected String json;


    /**
     * 构造函数 转换json
     * @param channel 通道
     * @param type 类型
     * @param jsonObj 数据
     */
    public void build(String channel, String type, JSONObject jsonObj) {
        if(channel == null || type == null || jsonObj == null){
            return;
        }
        jsonObj.put(BASE_TYPE, type);
        jsonObj.put(BASE_ID, SystemInfo.INSTANCE.getSystemID());
        this.json = jsonObj.toString();
        this.channel = channel;
    }

}
