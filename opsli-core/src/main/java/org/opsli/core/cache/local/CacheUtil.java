package org.opsli.core.cache.local;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.constants.CacheConstants;
import org.opsli.core.aspect.CacheDataAop;
import org.opsli.plugins.cache.EhCachePlugin;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.local
 * @Author: Parker
 * @CreateTime: 2020-09-16 16:20
 * @Description: 本地 缓存接口
 */
@Slf4j
@Component
public class CacheUtil {

    /** Redis插件 */
    private static RedisPlugin redisPlugin;
    /** EhCache插件 */
    private static EhCachePlugin ehCachePlugin;

    public static <V> V get(String key, Class<V> vClass){
        return CacheUtil.get(CacheConstants.HOT_DATA,key,vClass,true);
    }

    public static <V> V getByKeyOriginal(String key, Class<V> vClass){
        return CacheUtil.get(CacheConstants.HOT_DATA,key,vClass,false);
    }


    private static <V> V get(String cacheName, String key, Class<V> vClass,boolean keyFlag){
        // 自动处理 key
        if(keyFlag){
            StringBuilder keyBuf = new StringBuilder(CacheDataAop.PREFIX_NAME);
            keyBuf.append(cacheName).append(":");
            keyBuf.append(key);
            key = keyBuf.toString();
        }
        V v = null;
        try {
            JSONObject jsonObject;
            jsonObject = ehCachePlugin.get(cacheName, key, JSONObject.class);
            // 如果本地缓存为空 则去Redis中 再去取一次
            if(jsonObject != null){
                v = jsonObject.toJavaObject(vClass);
            }else{
                jsonObject = (JSONObject) redisPlugin.get(key);
                if(jsonObject != null){
                    v = jsonObject.toJavaObject(vClass);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return v;
    }


    // ================================
    @Autowired
    public  void setRedisPlugin(RedisPlugin redisPlugin) {
        CacheUtil.redisPlugin = redisPlugin;
    }

    @Autowired
    public  void setEhCachePlugin(EhCachePlugin ehCachePlugin) {
        CacheUtil.ehCachePlugin = ehCachePlugin;
    }
}
