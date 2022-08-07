/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.core.cache;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.Striped;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

/**
 * 安全缓存类
 * 为了提高 缓存查询效率，放弃之前采用的分布式锁
 * 本地锁要优于分布式锁的速度，当然在秒杀系统还是要使用分布式锁的
 *
 * 目前只支持 Redis 的 String 和 Hash
 * 实际业务的话 这两种一般也是足够了
 * 依赖于 RedisTemplate 和 LRU cache，多套业务部署 最大穿透次数为 业务服务N次
 *
 * @author Parker
 * @date 2021/12/10 12:39
 */
@Slf4j
public final class SecurityCache {

	/** 热点数据缓存时间 秒 (6小时)*/
	private static final int TTL_HOT_DATA_TIME = 21600;
	/** 默认缓存时效 超出后自动清理 (分钟) */
	private static final int DEFAULT_CACHE_TIME = 5;
	/** 创建缓存对象 用于存储 空缓存 */
	private static final Cache<String, CacheStatus> LFU_NULL_CACHE;
	/** 缓存前缀 KV */
	private static final String CACHE_PREFIX_KV = "kv#";
	/** 缓存前缀 HASH */
	private static final String CACHE_PREFIX_HASH = "hash#";

	/** 默认锁时间 (秒) */
	private static final int DEFAULT_LOCK_TIME = 10;
	/** 锁 */
	@SuppressWarnings("UnstableApiUsage")
	private static final Striped<Lock> STRIPED = Striped.lock(1024);


	static {
		// 超时自动清理
		LFU_NULL_CACHE = CacheBuilder
				.newBuilder()
				.expireAfterWrite(DEFAULT_CACHE_TIME, TimeUnit.MINUTES).build();
	}

	/**
	 * 获得缓存
	 * @param redisTemplate redisTemplate
	 * @param key 主键
	 * @param callbackSource 原数据回调
	 * @return Object
	 */
	public static Object get(
			final RedisTemplate<String, Object> redisTemplate,
			final String key, final Function<String, Object> callbackSource) {
		return get(redisTemplate, key, callbackSource, false);
	}
	/**
	 * 获得缓存
	 * @param redisTemplate redisTemplate
	 * @param key 主键
	 * @param callbackSource 原数据回调
	 * @return Object
	 */
	public static Object get(
			final RedisTemplate<String, Object> redisTemplate,
			final String key, final Function<String, Object> callbackSource, final boolean isEden){
		if(null == redisTemplate || null == key || null == callbackSource){
			throw new RuntimeException("入参[redisTemplate,key,callbackSource]必填");
		}

		// 先判断本地缓存是否存在 默认为存在（类似于伪布隆过滤）
		if(isNonExist(key)){
			return null;
		}

		// 缓存 Object 对象
		Object cache = getCacheObject(redisTemplate, key);
		// 如果缓存不为空 则直接返回
		if(null != cache){
			return cache;
		}

		// 如果还没查到缓存 则需要 穿透到 源数据查询
		// 开启本地锁
		@SuppressWarnings("UnstableApiUsage")
		Lock lock = STRIPED.get(key);
		try {
			// 尝试获得锁
			if(lock.tryLock(DEFAULT_LOCK_TIME, TimeUnit.SECONDS)){
				// 先判断本地缓存是否存在 默认为存在（类似于伪布隆过滤）
				if(isNonExist(key)){
					return null;
				}

				// 梅开二度 如果查到后 直接返回
				cache = getCacheObject(redisTemplate, key);
				// 如果缓存不为空 则直接返回
				if(null != cache){
					return cache;
				}

				// 如果这时候还没有 则查询源数据
				cache = callbackSource.apply(key);
				if(null == cache){
					// 存储缓存状态
					LFU_NULL_CACHE.put(key, CacheStatus.NOT_EXIST);
					return null;
				}

				// 存入 Redis缓存
				put(redisTemplate, key, cache, isEden);
			}
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}finally {
			lock.unlock();
		}
		return cache;
	}

	/**
	 * 获得缓存 （自定义 TTL）
	 * @param redisTemplate redisTemplate
	 * @param key 主键
	 * @param callbackSource 原数据回调
	 * @return Object
	 */
	public static Object getByTtl(
			final RedisTemplate<String, Object> redisTemplate,
			final String key, final Function<String, Object> callbackSource, final int ttl){
		if(null == redisTemplate || null == key || null == callbackSource){
			throw new RuntimeException("入参[redisTemplate,key,callbackSource]必填");
		}

		// 先判断本地缓存是否存在 默认为存在（类似于伪布隆过滤）
		if(isNonExist(key)){
			return null;
		}
		// 缓存 Object 对象
		Object cache = getCacheObject(redisTemplate, key);
		// 如果缓存不为空 则直接返回
		if(null != cache){
			return cache;
		}

		// 如果还没查到缓存 则需要 穿透到 源数据查询
		// 开启本地锁
		@SuppressWarnings("UnstableApiUsage")
		Lock lock = STRIPED.get(key);
		try {
			// 尝试获得锁
			if(lock.tryLock(DEFAULT_LOCK_TIME, TimeUnit.SECONDS)){
				// 先判断本地缓存是否存在 默认为存在（类似于伪布隆过滤）
				if(isNonExist(key)){
					return null;
				}
				// 梅开二度 如果查到后 直接返回
				cache = getCacheObject(redisTemplate, key);
				// 如果缓存不为空 则直接返回
				if(null != cache){
					return cache;
				}

				// 如果这时候还没有 则查询源数据
				cache = callbackSource.apply(key);
				if(null == cache){
					// 存储缓存状态
					LFU_NULL_CACHE.put(key, CacheStatus.NOT_EXIST);
					return null;
				}

				// 存入 Redis缓存
				put(redisTemplate, key, cache, ttl);
			}
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}finally {
			lock.unlock();
		}
		return cache;
	}

	/**
	 * 存储缓存
	 * @param redisTemplate redisTemplate
	 * @param key 主键
	 * @param val 值
	 */
	public static void put(
			final RedisTemplate<String, Object> redisTemplate,
			final String key, final Object val) {
		put(redisTemplate, key, val, false);
	}
	/**
	 * 存储缓存
	 * @param redisTemplate redisTemplate
	 * @param key 主键
	 * @param val 值
	 * @param isEden 是否永久存储
	 */
	public static void put(
			final RedisTemplate<String, Object> redisTemplate,
			final String key, final Object val, final boolean isEden) {
		if (null == redisTemplate || null == key || null == val) {
			throw new RuntimeException("入参[redisTemplate,key,val]必填");
		}

		String cacheKey = StrUtil.addPrefixIfNot(key, CACHE_PREFIX_KV);

		// 判断是否为永久存储
		if(isEden) {
			redisTemplate.opsForValue()
					.set(cacheKey, val);
		}else{
			// 随机缓存失效时间 防止缓存雪崩
			// 范围在当前时效的 1.2 - 2倍

			// 生成随机失效时间
			int timeout = RandomUtil.randomInt(
					Convert.toInt(TTL_HOT_DATA_TIME * 1.2),
					Convert.toInt(TTL_HOT_DATA_TIME * 2)
			);

			redisTemplate.opsForValue()
					.set(
							cacheKey,
							val,
							timeout,
							TimeUnit.SECONDS
					);
		}

		// 清除本地记录
		LFU_NULL_CACHE.invalidate(key);
	}

	/**
	 * 存储缓存
	 * @param redisTemplate redisTemplate
	 * @param key 主键
	 * @param val 值
	 * @param seconds 过期秒数
	 */
	public static void put(
			final RedisTemplate<String, Object> redisTemplate,
			final String key, final Object val, final Integer seconds) {
		if (null == redisTemplate || null == key || null == val) {
			throw new RuntimeException("入参[redisTemplate,key,val]必填");
		}

		String cacheKey = StrUtil.addPrefixIfNot(key, CACHE_PREFIX_KV);

		redisTemplate.opsForValue()
				.set(
						cacheKey,
						val,
						seconds,
						TimeUnit.SECONDS
				);

		// 清除本地记录
		LFU_NULL_CACHE.invalidate(key);
	}

	/**
	 * 获得缓存 Hash
	 * @param redisTemplate redisTemplate
	 * @param key 主键
	 * @param callbackSource 原数据回调
	 * @return Object
	 */
	public static Object hGet(
			final RedisTemplate<String, Object> redisTemplate,
			final String key, final String field, final Function<String, Object> callbackSource){
		if(null == redisTemplate || null == key
				|| null == field || null == callbackSource){
			throw new RuntimeException("入参[redisTemplate,key,field,callbackSource]必填");
		}

		final String tempKey = key + "_" + field;

		// 先判断本地缓存是否存在 默认为存在（类似于伪布隆过滤）
		if(isNonExist(tempKey)){
			return null;
		}
		// 缓存 Object 对象
		Object cache = getHashCacheObject(redisTemplate, key, field);
		// 如果缓存不为空 则直接返回
		if(null != cache){
			return cache;
		}

		// 如果还没查到缓存 则需要 穿透到 源数据查询
		// 开启本地锁
		@SuppressWarnings("UnstableApiUsage")
		Lock lock = STRIPED.get(tempKey);
		try {
			// 尝试获得锁
			if(lock.tryLock(DEFAULT_LOCK_TIME, TimeUnit.SECONDS)){
				// 先判断本地缓存是否存在 默认为存在（类似于伪布隆过滤）
				if(isNonExist(tempKey)){
					return null;
				}

				// 梅开二度 如果查到后 直接返回
				cache = getHashCacheObject(redisTemplate, key, field);
				// 如果缓存不为空 则直接返回
				if(null != cache){
					return cache;
				}

				// 如果这时候还没有 则查询源数据
				cache = callbackSource.apply(null);
				if(null == cache){
					// 存储缓存状态
					LFU_NULL_CACHE.put(tempKey, CacheStatus.NOT_EXIST);
					return null;
				}

				// 存入 Redis缓存
				hPut(redisTemplate, key, field, cache);
			}
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}finally {
			lock.unlock();
		}
		return cache;
	}

	/**
	 * 获得全部缓存 Hash
	 * 注：没有补偿机制 如果当前hash缓存 不是通过程序被删除
	 * 	  直接从redis 中将缓存干掉，人为的造成 redis与数据库缓存不一致则没救
	 *
	 * @param redisTemplate redisTemplate
	 * @param key 主键
	 * @param callbackSource 原数据回调
	 * @return Object
	 */
	public static Map<String, Object> hGetAll(
			final RedisTemplate<String, Object> redisTemplate, final String key,
			final Function<String, Map<String, Object>> callbackSource){
		if(null == redisTemplate || null == key
				|| null == callbackSource){
			throw new RuntimeException("入参[redisTemplate,key,callbackSource]必填");
		}

		// 先判断本地缓存是否存在 默认为存在（类似于伪布隆过滤）
		if(isNonExist(key)){
			return null;
		}
		// 缓存 Object 对象
		Map<String, Object> cache = getAllHashCacheObject(redisTemplate, key, null);
		// 如果缓存不为空 则直接返回
		if(null != cache){
			return cache;
		}

		// 如果还没查到缓存 则需要 穿透到 源数据查询
		// 开启本地锁
		@SuppressWarnings("UnstableApiUsage")
		Lock lock = STRIPED.get(key);
		try {
			// 尝试获得锁
			if(lock.tryLock(DEFAULT_LOCK_TIME, TimeUnit.SECONDS)){
				// 先判断本地缓存是否存在 默认为存在（类似于伪布隆过滤）
				if(isNonExist(key)){
					return null;
				}

				// 梅开二度 如果查到后 直接返回
				cache = getAllHashCacheObject(redisTemplate, key, null);
				// 如果缓存不为空 则直接返回
				if(null != cache){
					return cache;
				}

				// 如果这时候还没有 则查询源数据
				cache = callbackSource.apply(null);
				if(null == cache){
					// 存储缓存状态
					LFU_NULL_CACHE.put(key, CacheStatus.NOT_EXIST);
					return null;
				}

				// 存入 Redis缓存
				hAllPut(redisTemplate, key, cache);
			}
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}finally {
			lock.unlock();
		}
		return cache;
	}


	/**
	 * 获得全部缓存 Hash
	 *
	 * @param redisTemplate redisTemplate
	 * @param key 主键
	 * @param callbackSourceCount 原数据数量回调
	 * @param callbackSource 原数据回调
	 * @return Object
	 */
	public static Map<String, Object> hGetAll(
			final RedisTemplate<String, Object> redisTemplate, final String key,
			final Function<String, Integer> callbackSourceCount, final Function<String, Map<String, Object>> callbackSource){
		if(null == redisTemplate || null == key
				|| null == callbackSourceCount || null == callbackSource){
			throw new RuntimeException("入参[redisTemplate,key,callbackSourceCount,callbackSource]必填");
		}

		// 先判断本地缓存是否存在 默认为存在（类似于伪布隆过滤）
		if(isNonExist(key)){
			return null;
		}
		// 缓存 Object 对象
		Map<String, Object> cache = getAllHashCacheObject(redisTemplate, key, callbackSourceCount);
		// 如果缓存不为空 则直接返回
		if(null != cache){
			return cache;
		}

		// 如果还没查到缓存 则需要 穿透到 源数据查询
		// 开启本地锁
		@SuppressWarnings("UnstableApiUsage")
		Lock lock = STRIPED.get(key);
		try {
			// 尝试获得锁
			if(lock.tryLock(DEFAULT_LOCK_TIME, TimeUnit.SECONDS)){
				// 先判断本地缓存是否存在 默认为存在（类似于伪布隆过滤）
				if(isNonExist(key)){
					return null;
				}

				// 梅开二度 如果查到后 直接返回
				cache = getAllHashCacheObject(redisTemplate, key, callbackSourceCount);
				// 如果缓存不为空 则直接返回
				if(null != cache){
					return cache;
				}

				// 如果这时候还没有 则查询源数据
				cache = callbackSource.apply(null);
				if(null == cache){
					// 存储缓存状态
					LFU_NULL_CACHE.put(key, CacheStatus.NOT_EXIST);
					return null;
				}

				// 存入 Redis缓存
				hAllPut(redisTemplate, key, cache);
			}
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}finally {
			lock.unlock();
		}
		return cache;
	}


	/**
	 * Hash 存储缓存
	 * @param redisTemplate redisTemplate
	 * @param key 主键
	 * @param cacheMap 缓存Map
	 */
	public static void hAllPut(
			final RedisTemplate<String, Object> redisTemplate,
			final String key, final Map<String, Object> cacheMap) {
		if (null == redisTemplate || null == key
				|| null == cacheMap) {
			throw new RuntimeException("入参[redisTemplate,key,cacheMap]必填");
		}

		String cacheKeyByHash = StrUtil.addPrefixIfNot(key, CACHE_PREFIX_HASH);

		redisTemplate.opsForHash()
				.putAll(cacheKeyByHash, cacheMap);

		// 清除本地记录
		LFU_NULL_CACHE.invalidate(key);
	}

	/**
	 * Hash 存储缓存
	 * @param redisTemplate redisTemplate
	 * @param key 主键
	 * @param field 字段
	 * @param val 值
	 */
	public static void hPut(
			final RedisTemplate<String, Object> redisTemplate,
			final String key, final String field, final Object val) {
		if (null == redisTemplate || null == key
				|| null == field || null == val) {
			throw new RuntimeException("入参[redisTemplate,key,field,val]必填");
		}

		String cacheKeyByHash = StrUtil.addPrefixIfNot(key, CACHE_PREFIX_HASH);

		redisTemplate.opsForHash()
				.put(cacheKeyByHash, field, val);

		final String tempKey = key + "_" + field;
		// 清除本地记录
		LFU_NULL_CACHE.invalidate(tempKey);
	}


	/**
	 * 批量删除 Hash缓存
	 * @param redisTemplate redisTemplate
	 * @param key 主键
	 * @param fields 字段
	 * @return boolean
	 */
	public static boolean hDelMore(
			final RedisTemplate<String, Object> redisTemplate,
			final String key, final String... fields) {
		if (null == redisTemplate || null == key
				|| null == fields ) {
			throw new RuntimeException("入参[redisTemplate,key,fields]必填");
		}

		int count = fields.length;
		for (String field : fields) {
			boolean isDel = hDel(redisTemplate, key, field);
			if(isDel){
				count--;
			}
		}
		return 0 == count;
	}

	/**
	 * 删除 Hash缓存
	 * @param redisTemplate redisTemplate
	 * @param key 主键
	 * @param field 字段
	 * @return boolean
	 */
	public static boolean hDel(
			final RedisTemplate<String, Object> redisTemplate,
			final String key, final String field) {
		if (null == redisTemplate || null == key
				|| null == field ) {
			throw new RuntimeException("入参[redisTemplate,key,field]必填");
		}

		final String tempKey = key + "_" + field;

		// 清除本地记录
		LFU_NULL_CACHE.invalidate(tempKey);

		// 判断是否存在
		boolean isExist = Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey(key, field));
		if(!isExist){
			// 如果不存在 直接返回删除成功
			return true;
		}

		// 清除缓存
		return 0 != redisTemplate.opsForHash().delete(key, field);
	}


	/**
	 * 删除缓存
	 * @param redisTemplate redisTemplate
	 * @param keys 主键
	 */
	public static boolean remove(
			final RedisTemplate<String, Object> redisTemplate,
			final String... keys) {
		if (null == redisTemplate || null == keys) {
			throw new RuntimeException("入参[redisTemplate,key]必填");
		}

		List<String> removeKeyList = new ArrayList<>();
		for (String key : keys) {
			// 清除本地记录
			LFU_NULL_CACHE.invalidate(key);

			removeKeyList.add(StrUtil.addPrefixIfNot(key, CACHE_PREFIX_KV));
			removeKeyList.add(StrUtil.addPrefixIfNot(key, CACHE_PREFIX_HASH));
		}

		// 清除缓存
		redisTemplate.delete(removeKeyList);
		return true;
	}


	// =================================================================================================================

	/**
	 * 从Redis 直接获得缓存
	 * @param redisTemplate redisTemplate
	 * @param key Key
	 * @return Object
	 */
	private static Object getCacheObject(RedisTemplate<String, Object> redisTemplate, String key) {
		Object cache = null;
		try {
			String cacheKey = StrUtil.addPrefixIfNot(key, CACHE_PREFIX_KV);

			// 从 缓存回调查询数据
			cache = redisTemplate.opsForValue().get(cacheKey);
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}
		return cache;
	}

	/**
	 * 从Redis 直接获得缓存 Hash
	 * @param redisTemplate redisTemplate
	 * @param key Key
	 * @return Object
	 */
	private static Object getHashCacheObject(RedisTemplate<String, Object> redisTemplate, String key, String field) {
		Object cache = null;
		try {
			String cacheKeyByHash = StrUtil.addPrefixIfNot(key, CACHE_PREFIX_HASH);

			// 从 缓存回调查询数据
			cache = redisTemplate.opsForHash().get(cacheKeyByHash, field);
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}
		return cache;
	}


	/**
	 * 从Redis 直接获得全部缓存 Hash
	 * @param redisTemplate redisTemplate
	 * @param key key
	 * @param callbackSourceCount callbackSourceCount
	 * @return Object
	 */
	private static Map<String, Object> getAllHashCacheObject(RedisTemplate<String, Object> redisTemplate, String key,
												final Function<String, Integer> callbackSourceCount) {
		Map<String, Object> cache = null;
		try {
			String cacheKeyByHash = StrUtil.addPrefixIfNot(key, CACHE_PREFIX_HASH);

			// 从 缓存回调查询数据
			Map<Object, Object> entries = redisTemplate.opsForHash().entries(cacheKeyByHash);

			// 如果补偿器不为空 则进行补偿判断
			if(null != callbackSourceCount){
				// 判断数量不相等 则返回一个空 重新查询处理缓存
				Integer count = callbackSourceCount.apply(key);
				if(count != entries.size()){
					return null;
				}
			}

			if(MapUtil.isNotEmpty(entries)){
				cache = new HashMap<>();
				for (Map.Entry<Object, Object> entry : entries.entrySet()) {
					cache.put(String.valueOf(entry.getKey()), entry.getValue());
				}
			}
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}
		return cache;
	}

	/**
	 * 判断是否不存在
	 * @param key  key
	 * @return boolean
	 */
	private static boolean isNonExist(String key){
		try {
			// 先判断本地缓存是否存在 默认为存在（类似于伪布隆过滤）
			CacheStatus cacheStatus = LFU_NULL_CACHE.get(key, () -> CacheStatus.EXIST);
			// 如果不存在 则直接返回空
			return CacheStatus.NOT_EXIST.equals(cacheStatus);
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 状态
	 */
	private enum CacheStatus{

		/** 存在(默认) */
		EXIST,

		/** 不存在 */
		NOT_EXIST

	}

	/**
	 * 私有化构造函数
	 */
	private SecurityCache(){}

}
