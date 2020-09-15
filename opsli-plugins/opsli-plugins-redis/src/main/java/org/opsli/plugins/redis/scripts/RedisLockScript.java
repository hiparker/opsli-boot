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
public class RedisLockScript implements RedisPluginScript {

    @Override
    public RedisScriptsEnum getEnum() {
        return RedisScriptsEnum.REDIS_LOCK;
    }

    @Override
    public String getScript() {

        return  "-- 加锁脚本\n"+
                "-- key1：要加锁的名称 argv1:当前线程或主机的地址 argv2：锁存活的时间ms \n"+
                "local expire_time = tonumber(ARGV[2])\n"+
                "if redis.call('exists', KEYS[1]) == 0 then\n"+
                "   -- 锁不存在，创建一把锁，存入hash类型的值\n"+
                "   redis.call('hset', KEYS[1], ARGV[1], 1)\n"+
                "   -- 设置锁的存活时间，防止死锁\n"+
                "   redis.call('pexpire', KEYS[1], expire_time)\n"+
                "   return 1\n"+
                "end\n"+
                "if redis.call('hexists', KEYS[1], ARGV[1]) == 1 then\n"+
                "   -- 表示是同一线程重入\n"+
                "   redis.call('hincrby', KEYS[1], ARGV[1], 1)\n"+
                "   -- 重新设置锁的过期时间\n"+
                "   redis.call('pexpire', KEYS[1], expire_time)\n"+
                "   return 1\n"+
                "end\n"+
                "-- 没抢到锁，返回锁的剩余有效时间ms\n"+
                "return 0\n";
    }
    
}
