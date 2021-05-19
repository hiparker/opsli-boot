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
package org.opsli.plugins.redis.scripts;

import org.opsli.plugins.redis.scripts.enums.RedisScriptsEnum;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 脚本缓存
 *
 * @author Parker
 * @date 2020-09-16 11:47
 */
public class RedisScriptCache {

    /** 脚本存放容器 */
    private final ConcurrentMap<RedisScriptsEnum, String> scriptCacheMap = new ConcurrentHashMap<>();

    /**
     * 获得缓存脚本
     * @param scriptsEnum 脚本Enum
     * @return 脚本
     */
    public String getScript(RedisScriptsEnum scriptsEnum){
        if(scriptsEnum == null){
            return null;
        }
        return scriptCacheMap.get(scriptsEnum);
    }

    /**
     * 获得缓存脚本
     * @param scriptsEnum 脚本Enum
     * @param script 脚本
     * @return 脚本
     */
    public boolean putScript(RedisScriptsEnum scriptsEnum, String script){
        boolean ret = true;
        if(scriptsEnum == null || script == null || "".equals(script)
            ){
            return false;
        }
        try {
            scriptCacheMap.put(scriptsEnum,script);
        } catch (Exception e) {
            ret = false;
            e.printStackTrace();
        }
        return ret;
    }

}
