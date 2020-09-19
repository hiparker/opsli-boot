package org.opsli.core.cache.local;

import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSONObject;
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
    private static <V> V get(String cacheName, String key, Class<V> vClass,boolean keyFlag){
        String KeyOriginal = key;
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
                    // 存入EhCache
                    ehCachePlugin.put(CacheConstants.HOT_DATA, KeyOriginal, jsonObject);
                    v = jsonObject.toJavaObject(vClass);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return v;
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

    private static boolean put(String cacheName, String key, Object value,Integer timeout, boolean timeFlag, boolean keyFlag) {
        boolean ret = false;
        try {
            // 自动处理 key
            if(keyFlag){
                StringBuilder keyBuf = new StringBuilder(CacheDataAop.PREFIX_NAME);
                keyBuf.append(cacheName).append(":");
                keyBuf.append(key);
                key = keyBuf.toString();
            }

            // 如果不是 JSONObject 则统一转换为 JSONObject
            if(!(value instanceof JSONObject)){
                String jsonStr = JSONObject.toJSONString(value);
                value = JSONObject.parseObject(jsonStr);
            }

            // 存入EhCache
            ehCachePlugin.put(CacheConstants.HOT_DATA,key, value);

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
                redisPlugin.put(key, value, timeout);
            }else {
                // 存入Redis
                redisPlugin.put(key, value);
            }

            ret = true;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return ret;
    }


    // ========================= DEL - 删除 =========================

    /**
     * 删缓存
     *
     * @param key 键
     * @return boolean
     */
    public static boolean del(String key) {
        boolean ret = false;
        try {
            // 删除 EhCache
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
