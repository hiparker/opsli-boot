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
