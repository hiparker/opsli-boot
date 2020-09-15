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

        return  "-- 解锁脚本\n"+
                "-- 判断是当前线程持有锁，避免解了其他线程加的锁\n"+
                "if redis.call('hexists',KEYS[1],ARGV[1]) == 1 then\n"+
                "   -- 重入次数大于1，扣减次数\n"+
                "   --if tonumber(redis.call('hget',KEYS[1],ARGV[1])) > 1 then\n"+
                "   --    return redis.call('hincrby', KEYS[1], ARGV[1], -1);\n"+
                "   -- 重入次数等于1，删除该锁\n"+
                "   --else\n"+
                "       redis.call('del', KEYS[1]);\n"+
                "       return 1;\n"+
                "   --end\n"+
                "-- 判断不是当前线程持有锁，返回解锁失败\n"+
                "else\n"+
                "   return 0;\n"+
                "end\n";
    }
    
}
