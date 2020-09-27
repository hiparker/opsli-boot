package org.opsli.core.cache.local;

import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.constants.CacheConstants;
import org.opsli.core.aspect.CacheDataAop;
import org.opsli.plugins.cache.EhCachePlugin;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.local
 * @Author: Parker
 * @CreateTime: 2020-09-16 16:20
 * @Description: 本地 缓存接口
 *
 * 一、控制key的生命周期，Redis不是垃圾桶（缓存不是垃圾桶）
 * 建议使用expire设置过期时间(条件允许可以打散过期时间，防止集中过期)，不过期的数据重点关注idletime。
 *
 * 二、【强制】：拒绝bigkey(防止网卡流量、慢查询)
 * string类型控制在10KB以内，hash、list、set、zset元素个数不要超过5000。
 *
 *
 */
@Slf4j
@Component
public class CacheUtil {

    private static final String JSON_KEY = "data";
    /** 空状态 key 前缀 */
    private static final String NIL_FLAG_PREFIX = "opsli:nil:";

    /** 热点数据缓存时间 秒 */
    private static int ttlHotData;
    /** Redis插件 */
    private static RedisPlugin redisPlugin;
    /** EhCache插件 */
    private static EhCachePlugin ehCachePlugin;

    static {
        // 读取配置信息
        CacheUtil.readPropertyXML();
    }

    /**
     * 取缓存
     * Key 程序自处理
     * 比如：jksahdjh1j1hjk1
     *
     * @param key 键
     * @param vClass 转换类型
     * @return V
     */
    public static <V> V get(String key, Class<V> vClass){
        return CacheUtil.get(CacheConstants.HOT_DATA,key,vClass,true);
    }

    /**
     * 取缓存
     * Key 程序自处理
     * 比如：jksahdjh1j1hjk1
     *
     * @param key 键
     * @return V
     */
    public static Object get(String key){
        return CacheUtil.get(CacheConstants.HOT_DATA,key,true);
    }

    /**
     * 取缓存
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param key 键
     * @param vClass 转换类型
     * @return V
     */
    public static <V> V getByKeyOriginal(String key, Class<V> vClass){
        return CacheUtil.get(CacheConstants.HOT_DATA,key,vClass,false);
    }

    /**
     * 取缓存
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param key 键
     * @return V
     */
    public static Object getByKeyOriginal(String key){
        return CacheUtil.get(CacheConstants.HOT_DATA,key,false);
    }


    /**
     * 取缓存 - 永久
     * Key 程序自处理
     * 比如：jksahdjh1j1hjk1
     *
     * @param key 键
     * @param vClass 转换类型
     * @return V
     */
    public static <V> V getEden(String key, Class<V> vClass){
        return CacheUtil.get(CacheConstants.EDEN_DATA,key,vClass,true);
    }


    /**
     * 取缓存 - 永久
     * Key 程序自处理
     * 比如：jksahdjh1j1hjk1
     *
     * @param key 键
     * @return V
     */
    public static Object getEden(String key){
        return CacheUtil.get(CacheConstants.EDEN_DATA,key,true);
    }

    /**
     * 取缓存 - 永久
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param key 键
     * @param vClass 转换类型
     * @return V
     */
    public static <V> V getEdenByKeyOriginal(String key, Class<V> vClass){
        return CacheUtil.get(CacheConstants.EDEN_DATA,key,vClass,false);
    }

    /**
     * 取缓存 - 永久
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param key 键
     * @return V
     */
    public static Object getEdenByKeyOriginal(String key){
        return CacheUtil.get(CacheConstants.EDEN_DATA,key,false);
    }

    /**
     * 取Hash缓存
     * Key 程序自处理
     * 比如：jksahdjh1j1hjk1
     *
     * @param key 键
     * @param field 字段
     * @param vClass 转换类型
     * @return V
     */
    public static <V> V getHash(String key, String field, Class<V> vClass){
        return CacheUtil.getHash(CacheConstants.EDEN_HASH_DATA,key,field,vClass,true);
    }

    /**
     * 取Hash缓存
     * Key 程序自处理
     * 比如：jksahdjh1j1hjk1
     *
     * @param key 键
     * @param field 字段
     * @return V
     */
    public static Object getHash(String key, String field){
        return CacheUtil.getHash(CacheConstants.EDEN_HASH_DATA,key,field,true);
    }

    /**
     * 取Hash缓存
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param key 键
     * @param field 字段
     * @param vClass 转换类型
     * @return V
     */
    public static <V> V getHashByKeyOriginal(String key, String field, Class<V> vClass){
        return CacheUtil.getHash(CacheConstants.EDEN_HASH_DATA,key,field,vClass,false);
    }

    /**
     * 取Hash缓存
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param key 键
     * @param field 字段
     * @return V
     */
    public static Object getHashByKeyOriginal(String key, String field){
        return CacheUtil.getHash(CacheConstants.EDEN_HASH_DATA,key,field,false);
    }

    /**
     * 取缓存 - 集合
     * Key 程序自处理
     * 比如：jksahdjh1j1hjk1
     *
     * @param keys 键 - 集合
     * @param vClass 转换类型
     * @return V
     */
    public static <V> List<V> getAll(List<String> keys, Class<V> vClass){
        return CacheUtil.getAll(CacheConstants.HOT_DATA,keys,vClass,true);
    }

    /**
     * 取缓存 - 集合
     * Key 程序自处理
     * 比如：jksahdjh1j1hjk1
     *
     * @param keys 键 - 集合
     * @return V
     */
    public static List<Object> getAll(List<String> keys){
        return CacheUtil.getAll(CacheConstants.HOT_DATA,keys,true);
    }

    /**
     * 取缓存 - 集合
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param keys 键 - 集合
     * @param vClass 转换类型
     * @return V
     */
    public static <V> List<V> getAllByKeyOriginal(List<String> keys, Class<V> vClass){
        return CacheUtil.getAll(CacheConstants.HOT_DATA,keys,vClass,false);
    }

    /**
     * 取缓存 - 集合
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param keys 键 - 集合
     * @return V
     */
    public static List<Object> getAllByKeyOriginal(List<String> keys){
        return CacheUtil.getAll(CacheConstants.HOT_DATA,keys,false);
    }

    /**
     * 取缓存 - 永久 - 集合
     * Key 程序自处理
     * 比如：jksahdjh1j1hjk1
     *
     * @param keys 键 - 集合
     * @param vClass 转换类型
     * @return V
     */
    public static <V> List<V> getEdenAll(List<String> keys, Class<V> vClass){
        return CacheUtil.getAll(CacheConstants.EDEN_DATA,keys,vClass,true);
    }

    /**
     * 取缓存 - 永久 - 集合
     * Key 程序自处理
     * 比如：jksahdjh1j1hjk1
     *
     * @param keys 键 - 集合
     * @return V
     */
    public static List<Object> getEdenAll(List<String> keys){
        return CacheUtil.getAll(CacheConstants.EDEN_DATA,keys,true);
    }

    /**
     * 取缓存 - 永久 -集合
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param keys 键 - 集合
     * @param vClass 转换类型
     * @return V
     */
    public static <V> List<V> getEdenAllByKeyOriginal(List<String> keys, Class<V> vClass){
        return CacheUtil.getAll(CacheConstants.EDEN_DATA,keys,vClass,false);
    }

    /**
     * 取缓存 - 永久 -集合
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param keys 键 - 集合
     * @return V
     */
    public static List<Object> getEdenAllByKeyOriginal(List<String> keys){
        return CacheUtil.getAll(CacheConstants.EDEN_DATA,keys,false);
    }

    /**
     * 取Hash缓存
     * Key 程序自处理
     * 比如：jksahdjh1j1hjk1
     *
     * @param key 键
     * @param fields 字段集合
     * @param vClass 转换类型
     * @return V
     */
    public static <V> List<V> getHashAll(String key, List<String> fields, Class<V> vClass){
        return CacheUtil.getHashAll(CacheConstants.EDEN_HASH_DATA,key,fields,vClass,true);
    }

    /**
     * 取Hash缓存
     * Key 程序自处理
     * 比如：jksahdjh1j1hjk1
     *
     * @param key 键
     * @param fields 字段集合
     * @return V
     */
    public static List<Object> getHashAll(String key, List<String> fields){
        return CacheUtil.getHashAll(CacheConstants.EDEN_HASH_DATA,key,fields,true);
    }

    /**
     * 取Hash缓存
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param key 键
     * @param fields 字段集合
     * @param vClass 转换类型
     * @return V
     */
    public static <V> List<V> getHashAllByKeyOriginal(String key, List<String> fields, Class<V> vClass){
        return CacheUtil.getHashAll(CacheConstants.EDEN_HASH_DATA,key,fields,vClass,false);
    }

    /**
     * 取Hash缓存
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param key 键
     * @param fields 字段集合
     * @return V
     */
    public static List<Object> getHashAllByKeyOriginal(String key, List<String> fields){
        return CacheUtil.getHashAll(CacheConstants.EDEN_HASH_DATA,key,fields,false);
    }

    /**
     * 获得 Hash 缓存
     * @param cacheName 主缓存名
     * @param key 键
     * @param field 字段名
     * @param vClass Clazz 反射
     * @param keyFlag 是否处理key
     * @param <V> 泛型
     * @return
     */
    private static <V> V getHash(String cacheName, String key, String field, Class<V> vClass,boolean keyFlag){
        // 自动处理 key
        if(keyFlag){
            key = CacheUtil.handleKey(cacheName, key);
        }
        V v = null;
        try {
            JSONObject jsonObject;
            jsonObject = ehCachePlugin.get(CacheConstants.HOT_DATA, key+":"+field, JSONObject.class);
            // 如果本地缓存为空 则去Redis中 再去取一次
            if(jsonObject != null){
                JSONObject dataJson = jsonObject.getJSONObject(JSON_KEY);
                if(dataJson != null){
                    v = dataJson.toJavaObject(vClass);
                }
            }else{
                jsonObject = (JSONObject) redisPlugin.hGet(key,field);
                if(jsonObject != null){
                    // 存入EhCache
                    ehCachePlugin.put(CacheConstants.HOT_DATA, key+":"+field, jsonObject);
                    JSONObject dataJson = jsonObject.getJSONObject(JSON_KEY);
                    if(dataJson != null){
                        v = dataJson.toJavaObject(vClass);
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return v;
    }



    /**
     * 获得 Hash 缓存
     * @param cacheName 主缓存名
     * @param key 键
     * @param field 字段名
     * @param keyFlag 是否处理key
     * @return
     */
    private static Object getHash(String cacheName, String key, String field, boolean keyFlag){
        // 自动处理 key
        if(keyFlag){
            key = CacheUtil.handleKey(cacheName, key);
        }
        Object v = null;
        try {
            JSONObject jsonObject;
            jsonObject = ehCachePlugin.get(CacheConstants.HOT_DATA, key+":"+field, JSONObject.class);
            // 如果本地缓存为空 则去Redis中 再去取一次
            if(jsonObject != null){
                v = jsonObject.get(JSON_KEY);
            }else{
                jsonObject = (JSONObject) redisPlugin.hGet(key,field);
                if(jsonObject != null){
                    // 存入EhCache
                    ehCachePlugin.put(CacheConstants.HOT_DATA, key+":"+field, jsonObject);
                    v = jsonObject.get(JSON_KEY);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return v;
    }


    /**
     * 获得 Hash 缓存
     * @param cacheName 主缓存名
     * @param key 键
     * @param vClass Clazz 反射
     * @param keyFlag 是否处理key
     * @param <V> 泛型
     * @return
     */
    private static <V> V get(String cacheName, String key, Class<V> vClass,boolean keyFlag){
        // 自动处理 key
        if(keyFlag){
            key = CacheUtil.handleKey(cacheName, key);
        }
        V v = null;
        try {
            JSONObject jsonObject = ehCachePlugin.get(CacheConstants.HOT_DATA, key, JSONObject.class);
            // 如果本地缓存为空 则去Redis中 再去取一次
            if(jsonObject != null){
                JSONObject dataJson = jsonObject.getJSONObject(JSON_KEY);
                if(dataJson != null){
                    v = dataJson.toJavaObject(vClass);
                }
            }else{
                jsonObject = (JSONObject) redisPlugin.get(key);
                if(jsonObject != null){
                    // 存入EhCache
                    ehCachePlugin.put(CacheConstants.HOT_DATA, key, jsonObject);
                    JSONObject dataJson = jsonObject.getJSONObject(JSON_KEY);
                    if(dataJson != null){
                        v = dataJson.toJavaObject(vClass);
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return v;
    }

    /**
     * 获得 Hash 缓存
     * @param cacheName 主缓存名
     * @param key 键
     * @param keyFlag 是否处理key
     * @return
     */
    private static Object get(String cacheName, String key, boolean keyFlag){
        // 自动处理 key
        if(keyFlag){
            key = CacheUtil.handleKey(cacheName, key);
        }
        Object v = null;
        try {
            JSONObject jsonObject = ehCachePlugin.get(CacheConstants.HOT_DATA, key, JSONObject.class);
            // 如果本地缓存为空 则去Redis中 再去取一次
            if(jsonObject != null){
                v = jsonObject.get(JSON_KEY);
            }else{
                jsonObject = (JSONObject) redisPlugin.get(key);
                if(jsonObject != null){
                    // 存入EhCache
                    ehCachePlugin.put(CacheConstants.HOT_DATA, key, jsonObject);
                    v = jsonObject.get(JSON_KEY);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return v;
    }

    /**
     * 获得 缓存 - 集合
     * @param cacheName 主缓存名
     * @param keys 键
     * @param vClass Clazz 反射
     * @param keyFlag 是否处理key
     * @param <V> 泛型
     * @return
     */
    private static <V> List<V> getAll(String cacheName, List<String> keys, Class<V> vClass, boolean keyFlag){
        List<V> vs = Lists.newArrayList();
        try {

            List<String> nokeys = Lists.newArrayList();
            for (String key : keys) {
                // 自动处理 key
                if(keyFlag){
                    key = CacheUtil.handleKey(cacheName, key);
                }
                JSONObject jsonObject = ehCachePlugin.get(CacheConstants.HOT_DATA, key, JSONObject.class);
                if(jsonObject != null){
                    JSONObject dataJson = jsonObject.getJSONObject(JSON_KEY);
                    if(dataJson != null){
                        vs.add(dataJson.toJavaObject(vClass));
                    }
                } else {
                    nokeys.add(key);
                }
            }
            // 如果本地缓存为空 则去Redis中 再去取一次
            if(nokeys.size() > 0){
                List<Object> objs =  redisPlugin.getAll(nokeys);
                for (Object obj : objs) {
                    JSONObject jsonObject = (JSONObject) obj;
                    if(jsonObject != null){
                        JSONObject dataJson = jsonObject.getJSONObject(JSON_KEY);
                        if(dataJson != null){
                            vs.add(dataJson.toJavaObject(vClass));
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return vs;
    }


    /**
     * 获得 缓存 - 集合
     * @param cacheName 主缓存名
     * @param keys 键
     * @param keyFlag 是否处理key
     * @return
     */
    private static List<Object> getAll(String cacheName, List<String> keys, boolean keyFlag){
        List<Object> vs = Lists.newArrayList();
        try {
            List<String> nokeys = Lists.newArrayList();
            for (String key : keys) {
                // 自动处理 key
                if(keyFlag){
                    key = CacheUtil.handleKey(cacheName, key);
                }
                JSONObject jsonObject = ehCachePlugin.get(CacheConstants.HOT_DATA, key, JSONObject.class);
                if(jsonObject != null){
                    vs.add(jsonObject.get(JSON_KEY));
                } else {
                    nokeys.add(key);
                }
            }
            // 如果本地缓存为空 则去Redis中 再去取一次
            if(nokeys.size() > 0){
                List<Object> objs =  redisPlugin.getAll(nokeys);
                for (Object obj : objs) {
                    JSONObject jsonObject = (JSONObject) obj;
                    if(jsonObject != null){
                        vs.add(jsonObject.get(JSON_KEY));
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return vs;
    }

    /**
     * 获得 Hash 缓存 - 集合
     * @param cacheName 主缓存名
     * @param key 键
     * @param fields 字段集合
     * @param vClass Clazz 反射
     * @param keyFlag 是否处理key
     * @param <V> 泛型
     * @return
     */
    private static <V> List<V> getHashAll(String cacheName,String key,List<String> fields, Class<V> vClass, boolean keyFlag){
        // 自动处理 key
        if(keyFlag){
            key = CacheUtil.handleKey(cacheName, key);
        }
        List<V> vs = Lists.newArrayList();
        try {
            List<Object> nofields = Lists.newArrayList();
            for (String field : fields) {
                JSONObject jsonObject = ehCachePlugin.get(CacheConstants.HOT_DATA, key+":"+field, JSONObject.class);
                if(jsonObject != null){
                    JSONObject dataJson = jsonObject.getJSONObject(JSON_KEY);
                    if(dataJson != null){
                        vs.add(dataJson.toJavaObject(vClass));
                    }
                } else {
                    nofields.add(field);
                }
            }
            // 如果本地缓存为空 则去Redis中 再去取一次
            if(nofields.size() > 0){
                List<Object> objs =  redisPlugin.hMultiGet(key, nofields);
                for (Object obj : objs) {
                    JSONObject jsonObject = (JSONObject) obj;
                    if(jsonObject != null){
                        JSONObject dataJson = jsonObject.getJSONObject(JSON_KEY);
                        if(dataJson != null){
                            vs.add(dataJson.toJavaObject(vClass));
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return vs;
    }


    /**
     * 获得 Hash 缓存 - 集合
     * @param cacheName 主缓存名
     * @param key 键
     * @param fields 字段集合
     * @param keyFlag 是否处理key
     * @return
     */
    private static List<Object> getHashAll(String cacheName,String key,List<String> fields, boolean keyFlag){
        // 自动处理 key
        if(keyFlag){
            key = CacheUtil.handleKey(cacheName, key);
        }
        List<Object> vs = Lists.newArrayList();
        try {
            List<Object> nofields = Lists.newArrayList();
            for (String field : fields) {
                JSONObject jsonObject = ehCachePlugin.get(CacheConstants.HOT_DATA, key+":"+field, JSONObject.class);
                if(jsonObject != null){
                    vs.add(jsonObject.get(JSON_KEY));
                } else {
                    nofields.add(field);
                }
            }
            // 如果本地缓存为空 则去Redis中 再去取一次
            if(nofields.size() > 0){
                List<Object> objs =  redisPlugin.hMultiGet(key, nofields);
                for (Object obj : objs) {
                    JSONObject jsonObject = (JSONObject) obj;
                    if(jsonObject != null){
                        vs.add(jsonObject.get(JSON_KEY));
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return vs;
    }

    // ========================= PUT - 热点区 =========================

    /**
     *  存缓存 - 热点区
     *  默认取配置文件中缓存失效时间
     *  注意：不是永久缓存
     *
     * @param key 键
     * @param value 值
     * @return boolean
     */
    public static boolean put(String key, Object value) {
        return CacheUtil.put(CacheConstants.HOT_DATA, key, value, null, true, true);
    }


    /**
     * 存缓存 - 热点区
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param key 键
     * @param value 值
     * @return boolean
     */
    public static boolean putByKeyOriginal(String key, Object value){
        return CacheUtil.put(CacheConstants.HOT_DATA, key, value, null, true, false);
    }

    // ========================= PUT - 永久代 =========================

    /**
     * 存缓存 - 永久代
     * 注：这里是存入Redis永久代，本地EhCache做临时缓存
     * Redis不是垃圾桶 谨用 永久缓存
     *
     * @param key 键
     * @param value 值
     * @return boolean
     */
    @Deprecated
    public static boolean putEden(String key, Object value) {
        return CacheUtil.put(CacheConstants.EDEN_DATA, key, value, null, false, true);
    }

    /**
     * 存缓存 - 永久代
     * 注：这里是存入Redis永久代，本地EhCache做临时缓存
     * Redis不是垃圾桶 谨用 永久缓存
     *
     * @param key 键
     * @param value 值
     * @return boolean
     */
    @Deprecated
    public static boolean putEdenByKeyOriginal(String key, Object value) {
        return CacheUtil.put(CacheConstants.EDEN_DATA, key, value, null, false, false);
    }

    // ========================= PUT - 永久代 =========================

    /**
     * 存Hash缓存 - 永久代
     * 注：这里是存入Redis永久代，本地EhCache做临时缓存
     * Redis不是垃圾桶 谨用 永久缓存
     *
     * @param key 键
     * @param value 值
     * @return boolean
     */
    @Deprecated
    public static boolean putEdenHash(String key, String field, Object value) {
        return CacheUtil.putEdenHash(CacheConstants.EDEN_HASH_DATA, key, field, value, true);
    }

    /**
     * 存Hash缓存 - 永久代
     * 注：这里是存入Redis永久代，本地EhCache做临时缓存
     * Redis不是垃圾桶 谨用 永久缓存
     *
     * @param key 键
     * @param value 值
     * @return boolean
     */
    @Deprecated
    public static boolean putEdenHashByKeyOriginal(String key, String field, Object value) {
        return CacheUtil.putEdenHash(CacheConstants.EDEN_HASH_DATA, key, field, value, false);
    }



    /**
     * 存 Hash 缓存
     * @param cacheName 主缓存名
     * @param key 键
     * @param field 字段名
     * @param value 值
     * @param keyFlag 处理key
     * @return
     */
    private static boolean putEdenHash(String cacheName, String key, String field, Object value, boolean keyFlag) {
        boolean ret = false;
        try {
            // 自动处理 key
            if(keyFlag){
                key = CacheUtil.handleKey(cacheName, key);
            }

            // 则统一转换为 JSONObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(JSON_KEY, value);

            // 存入EhCache
            ehCachePlugin.put(CacheConstants.HOT_DATA, key+":"+field, jsonObject);

            // 存入Redis
            redisPlugin.hPut(key, field, jsonObject);

            ret = true;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return ret;
    }

    /**
     * 存普通缓存
     * @param cacheName 主缓存名
     * @param key 键
     * @param value 值
     * @param timeout 有效时间
     * @param timeFlag 是否开启时间
     * @param keyFlag 处理key
     * @return
     */
    private static boolean put(String cacheName, String key, Object value,Integer timeout, boolean timeFlag, boolean keyFlag) {
        boolean ret = false;
        try {
            // 自动处理 key
            if(keyFlag){
                key = CacheUtil.handleKey(cacheName, key);
            }

            // 则统一转换为 JSONObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(JSON_KEY, value);

            // 存入EhCache
            ehCachePlugin.put(CacheConstants.HOT_DATA,key, jsonObject);

            if(timeFlag){

                // 如果缓存失效时间设置为空 则默认使用系统配置时间
                // 对于Redis缓存定位，为远程缓存同步库 当EhCache缓存失效时，Redis可以起到抗一波的作用
                // 所以，为了防止缓存雪崩 让Redis的失效时间 = EhCache热数据失效时间*1.2 ~ 2 倍之间随机
                if(timeout == null){
                    timeout = RandomUtils.nextInt(
                            Double.valueOf(String.valueOf(ttlHotData * 1.2)).intValue(),
                            Double.valueOf(String.valueOf(ttlHotData * 2)).intValue());
                }
                // 存入Redis
                redisPlugin.put(key, jsonObject, timeout);
            }else {
                // 存入Redis
                redisPlugin.put(key, jsonObject);
            }

            ret = true;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return ret;
    }


    // ========================= DEL - 删除 =========================

    /**
     * 删缓存 热点数据
     *
     * Key 程序自处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param key 键
     * @return boolean
     */
    public static boolean del(String key) {
        return CacheUtil.del(CacheConstants.HOT_DATA, key, true);
    }

    /**
     * 删缓存 热点数据
     *
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param key 键
     * @return boolean
     */
    public static boolean delByKeyOriginal(String key) {
        return CacheUtil.del(CacheConstants.HOT_DATA, key, false);
    }

    /**
     * 删 永久Hash 缓存
     *
     * Key 程序自处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param key 键
     * @param field 字段名
     * @return boolean
     */
    public static boolean delEdenHash(String key, String field) {
        return CacheUtil.delEdenHash(CacheConstants.EDEN_HASH_DATA, key, field, true);
    }

    /**
     * 删 永久Hash 缓存
     *
     * Key 程序不处理
     * 比如：源Key opsli:hotData:ahdjksahjkd1
     *
     * @param key 键
     * @param field 字段名
     * @return boolean
     */
    public static boolean delEdenHashByKeyOriginal(String key, String field) {
        return CacheUtil.delEdenHash(CacheConstants.EDEN_HASH_DATA, key, field, false);
    }

    /**
     * 删 永久 Hash 缓存
     * @param cacheName 主缓存名
     * @param key 键
     * @param field 字段名
     * @param keyFlag 是否处理Key
     * @return boolean
     */
    private static boolean delEdenHash(String cacheName, String key, String field, boolean keyFlag) {
        boolean ret = false;
        try {
            // 自动处理 key
            if(keyFlag){
                key = CacheUtil.handleKey(cacheName, key);
            }
            // 删除 EhCache 不论是什么 本地都按照热数据处理
            ehCachePlugin.delete(CacheConstants.HOT_DATA,key+":"+field);
            // 删除 Redis
            redisPlugin.hDelete(key, field);
            ret = true;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return ret;
    }

    /**
     * 删缓存
     * @param cacheName 主缓存名
     * @param key 键
     * @param keyFlag 是否处理Key
     * @return boolean
     */
    private static boolean del(String cacheName, String key, boolean keyFlag) {
        boolean ret = false;
        try {
            // 自动处理 key
            if(keyFlag){
                key = CacheUtil.handleKey(cacheName, key);
            }
            // 删除 EhCache 不论是什么 本地都按照热数据处理
            ehCachePlugin.delete(CacheConstants.HOT_DATA,key);
            // 删除 Redis
            redisPlugin.del(key);
            ret = true;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return ret;
    }

    // ====================================================================

    /**
     *  放一个空属性 有效时间为 5分钟
     *  用于 防止穿透判断 弥补布隆过滤器
     *
     * @param key 键
     * @return boolean
     */
    public static boolean putNilFlag(String key) {
        boolean ret = false;
        try {
            // 存入Redis
            redisPlugin.put(NIL_FLAG_PREFIX + key, 1, 300);
            ret = true;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return ret;
    }

    /**
     *  删除空属性
     *  用于 防止穿透判断 弥补布隆过滤器
     *
     * @param key 键
     * @return boolean
     */
    public static boolean delNilFlag(String key) {
        boolean ret = false;
        try {
            // 存入Redis
            redisPlugin.del(NIL_FLAG_PREFIX + key);
            ret = true;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return ret;
    }


    /**
     *  获得一个空属性 有效时间为 5分钟
     *  用于 防止穿透判断 弥补布隆过滤器
     *
     * @param key 键
     * @return boolean
     */
    public static boolean hasNilFlag(String key) {
        try {
            // 存入Redis
            Object o = redisPlugin.get(NIL_FLAG_PREFIX + key);
            if(o != null){
                return true;
            }
            return false;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return false;
    }


    // ====================================================================

    /**
     * 处理 key
     * @param cacheName
     * @param key
     * @return
     */
    public static String handleKey(String cacheName, String key){
        StringBuilder keyBuf = new StringBuilder(CacheDataAop.PREFIX_NAME);
        keyBuf.append(cacheName).append(":");
        keyBuf.append(key);
        return keyBuf.toString();
    }

    /**
     * 读配置文件
     */
    private static void readPropertyXML(){
        Document document = XmlUtil.readXML("config/ehcache-opsli.xml");
        NodeList nodeList = document.getElementsByTagName("cache");
        if(nodeList != null){
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node item = nodeList.item(i);
                NamedNodeMap attributes = item.getAttributes();
                if(attributes == null){
                    continue;
                }
                Node alias = attributes.getNamedItem("alias");
                if("hotData".equals(alias.getNodeValue())){
                    NodeList childNodes = item.getChildNodes();
                    if(childNodes != null){
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            if("expiry".equals(childNodes.item(j).getNodeName())){
                                NodeList expiryNodes = childNodes.item(j).getChildNodes();
                                if(expiryNodes != null){
                                    for (int k = 0; k < expiryNodes.getLength(); k++) {
                                        if("ttl".equals(expiryNodes.item(k).getNodeName())){
                                            Node ttlNode = expiryNodes.item(k);
                                            Node ttlValue = ttlNode.getFirstChild();
                                            // 默认 60000秒 6小时
                                            String ttl = "60000";
                                            if(StringUtils.isNotEmpty(ttlValue.getNodeValue())){
                                                ttl = ttlValue.getNodeValue();
                                            }
                                            ttlHotData = Integer.parseInt(ttl);
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    @Autowired
    public  void setRedisPlugin(RedisPlugin redisPlugin) {
        CacheUtil.redisPlugin = redisPlugin;
    }

    @Autowired
    public  void setEhCachePlugin(EhCachePlugin ehCachePlugin) {
        CacheUtil.ehCachePlugin = ehCachePlugin;
    }

}
