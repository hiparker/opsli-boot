package org.opsli.plugins.redis.scripts;

import org.opsli.plugins.redis.scripts.enums.RedisScriptsEnum;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 脚本缓存
 * @author parker
 */
public class RedisScriptCache {

    /** 脚本存放容器 */
    private final ConcurrentMap<RedisScriptsEnum, RedisPluginScript> scriptCacheMap = new ConcurrentHashMap<>();

    /**
     * 获得缓存脚本
     * @param scriptsEnum 脚本Enum
     * @return 脚本
     */
    public RedisPluginScript getScript(RedisScriptsEnum scriptsEnum){
        if(scriptsEnum == null){
            return null;
        }
        return scriptCacheMap.get(scriptsEnum);
    }

    /**
     * 获得缓存脚本
     * @param redisPluginScript 脚本Enum
     * @return 脚本
     */
    public boolean putScript(RedisPluginScript redisPluginScript){
        boolean ret = true;
        if(redisPluginScript == null || redisPluginScript.getScript() == null || "".equals(redisPluginScript.getScript())
                || redisPluginScript.getEnum() == null
            ){
            return false;
        }
        try {
            scriptCacheMap.put(redisPluginScript.getEnum(),redisPluginScript);
        } catch (Exception e) {
            ret = false;
            e.printStackTrace();
        }
        return ret;
    }

}
