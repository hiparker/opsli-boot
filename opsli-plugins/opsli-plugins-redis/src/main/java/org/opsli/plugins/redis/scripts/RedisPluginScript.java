package org.opsli.plugins.redis.scripts;


import org.opsli.plugins.redis.scripts.enums.RedisScriptsEnum;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.redis.scripts
 * @Author: Parker
 * @CreateTime: 2020-09-14 22:05
 * @Description: Redis Lua 脚本
 *
 * Lua 比 这些框架靠谱的多 ， 单线程保障了Redis的原子性操作
 *
 */
public interface RedisPluginScript {

    /**
     * 获得所属字典
     * @return
     */
    RedisScriptsEnum getEnum();

    /**
     * 获得脚本
     * @return
     */
    String getScript();

}
