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
package org.opsli.core.persistence.querybuilder.conf;

import java.util.HashMap;
import java.util.Map;

/**
 * Web 请求配置类
 *
 * @author Parker
 * @date 2021年3月3日17:12:08
 */
public class WebQueryConf {

    private final Map<String, String> queryMap;

    public WebQueryConf(){
        queryMap = new HashMap<>();
    }


    /**
     * 判断是否包含 Key
     * @param key key
     * @param value 值
     */
    public void pub(String key, String value){
        queryMap.putIfAbsent(key, value);
    }

    /**
     * 判断是否包含 Key
     * @param key key
     */
    public String get(String key){
        return queryMap.get(key);
    }

    /**
     * 判断是否包含 Key
     * @param key key
     * @return boolean
     */
    public boolean hashKey(String key){
        return queryMap.containsKey(key);
    }

}