package org.opsli.plugins.redis.scripts;


import org.opsli.plugins.redis.scripts.enums.RedisScriptsEnum;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.redis.scripts
 * @Author: Parker
 * @CreateTime: 2020-09-14 22:01
 * @Description: Redis 锁 Lua脚本
 *
 * Redis 集群下 ，尽量不要使用Redis做为分布式锁
 *
 */
public class RedisUnLockScript implements RedisPluginScript {

    @Override
    public RedisScriptsEnum getEnum() {
        return RedisScriptsEnum.REDIS_UN_LOCK;
    }

    @Override
    public String getScript() {
        return  "local key = KEYS[1]\n" +
                "local identifier = ARGV[1]\n" +
                "\n" +
                "if redis.call(\"GET\", key) == identifier then\n" +
                "    redis.call(\"DEL\", key)\n" +
                "    return 1\n" +
                "end\n" +
                "return 0";
    }
    
}
