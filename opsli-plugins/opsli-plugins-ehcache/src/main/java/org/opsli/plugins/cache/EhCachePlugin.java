package org.opsli.plugins.cache;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.cache
 * @Author: Parker
 * @CreateTime: 2020-09-16 19:19
 * @Description: EhCache 缓存接口
 */
public interface EhCachePlugin {

    /**
     *  添加缓存数据
      * @param cacheName
     *  @param key
     *  @param value
     *  @return
     */
    boolean put(String cacheName, String key, Object value);

    /**
     * 获取缓存数据
     * @param cacheName
     * @param key
     * @return
     */
    Object get(String cacheName, String key);

    /**
     * 获取缓存数据
     * @param cacheName
     * @param key
     * @return
     */
    <V> V get(String cacheName, String key ,Class<V> vClass);


    /**
     * 删除缓存数据
     * @param cacheName
     * @param key
     */
    boolean delete(String cacheName, String key);

}
