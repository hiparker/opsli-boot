package org.opsli.plugins.redis;

import lombok.extern.slf4j.Slf4j;
import org.opsli.plugins.redis.exception.RedisPluginException;
import org.opsli.plugins.redis.msg.RedisMsg;
import org.opsli.plugins.redis.pushsub.entity.BaseSubMessage;
import org.opsli.plugins.redis.scripts.RedisPluginScript;
import org.opsli.plugins.redis.scripts.RedisScriptCache;
import org.opsli.plugins.redis.scripts.enums.RedisScriptsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 插件类
 * @author parker
 * @date 2020-09-14
 */
@Slf4j
@Component
public class RedisPlugin {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private RedisScriptCache redisScriptCache;

	// ===================== 基础相关 =====================

	/**
	 * 判断key是否存在
	 *
	 * @param key Redis键
	 * @return boolean
	 */
	public boolean hasKey(String key) {
		Boolean ret = null;
		try {
			ret = redisTemplate.hasKey(key);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return false;
		}
		return ret != null && ret;
	}

	/**
	 * 删除1个key
	 *
	 * @param key Redis键
	 * @return 成功删除的个数
	 */
	public boolean del(String key) {
		Boolean ret = redisTemplate.delete(key);
		return ret != null && ret;
	}
	/**
	 * 删除多个key
	 *
	 * @param keys Redis键集合
	 * @return 成功删除的个数
	 */
	public Long del(Collection<String> keys) {
		Long ret = null;
		if (keys != null && keys.size() > 0) {
			ret = redisTemplate.delete(keys);
		}
		return ret;
	}

	/**
	 * 使用脚本
	 * @param scriptsEnum 脚本枚举
	 * @param keys 多值
	 * @param argv 多参数
	 * @return object
	 */
	public Object callScript(RedisScriptsEnum scriptsEnum, List<String> keys, List<String> argv) {
		// 获得Script脚本
		RedisPluginScript script = redisScriptCache.getScript(scriptsEnum);
		if(script == null){
			return false;
		}
		RedisScript<Object> of = RedisScript.of(script.getScript());
		return redisTemplate.execute(of,keys,argv);
	}

	// ===================== Redis 锁相关 =====================
	/*
	* 分布式锁需要考虑的问题
	* 1、这把锁没有失效时间，一旦解锁操作失败，就会导致锁记录一直在tair中，其他线程无法再获得到锁。
	* 2、这把锁只能是非阻塞的，无论成功还是失败都直接返回。
	* 3、这把锁是非重入的，一个线程获得锁之后，在释放锁之前，无法再次获得该锁，因为使用到的key在tair中已经存在。无法再执行put操作。
	* */

	/**
	 * Redis 加分布式锁
	 * @param lockName 锁名称
	 * @param acquireTimeOut 尝试获取锁等待时间
	 * @param lockTimeOut 锁有效时间
	 * @return identifier 很重要，解锁全靠他 唯一凭证
	 */
	public String lockWithTimeOut(String lockName, long acquireTimeOut, int lockTimeOut) {
		String identifier = UUID.randomUUID().toString();
		List<String> keys = Collections.singletonList("lock:" + lockName);
		List<String> argv = Arrays.asList(identifier,
				String.valueOf(lockTimeOut));

		long acquireTimeEnd = System.currentTimeMillis() + acquireTimeOut;
		boolean acquired = false;
		// 尝试获得锁
		while (!acquired && (System.currentTimeMillis() < acquireTimeEnd)) {
			Long ret = (Long) this.callScript(RedisScriptsEnum.REDIS_LOCK, keys, argv);
			if(ret == null){
				break;
			}
			if (1 == ret){
				acquired = true;
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException ignored) {
					log.error(ignored.getMessage(),ignored);
				}
			}
		}
		return acquired ? identifier : null;
	}

	/**
	 * Redis 释放分布式锁
	 * @param lockName 锁名称
	 * @param identifier 唯一凭证
	 * @return boolean
	 */
	public boolean unLock(String lockName, String identifier) {
		List<String> keys = Collections.singletonList("lock:" + lockName);
		List<String> argv = Collections.singletonList(identifier);
		Long ret = (Long) this.callScript(RedisScriptsEnum.REDIS_UN_LOCK, keys, argv);
		if(ret == null){
			return false;
		}
		return 1 == ret;
	}


	// ===================== 缓存有效时间相关 =====================

	/**
	 * 指定缓存有效时间
	 *
	 * @param key Redis键
	 * @param timeout 超时时间(秒)
	 * @return boolean
	 */
	public boolean expire(String key, long timeout) {
		return this.expire(key, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 设置缓存有效时间
	 *
	 * @param key Redis键
	 * @param timeout 超时时间
	 * @param unit 时间单位
	 * @return boolean
	 */
	public boolean expire(String key, long timeout, TimeUnit unit) {
		Boolean ret = null;
		try {
			if(timeout > 0){
				ret = redisTemplate.expire(key, timeout, unit);
			}
		}catch (Exception e){
			log.error(e.getMessage(),e);
		}
		return ret != null && ret;
	}

	/**
	 * 获得缓存有效时间
	 *
	 * @param key Redis键
	 * @return 超时时间(秒) 0 则表示永久有效
	 */
	public Long getExpire(String key) {
		return this.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * 获得缓存有效时间
	 *
	 * @param key Redis键
	 * @param unit 时间单位
	 * @return 超时时间 0 则表示永久有效
	 */
	public Long getExpire(String key, TimeUnit unit) {
		if(key == null || "".equals(key)){
			throw new RedisPluginException(RedisMsg.EXCEPTION_KEY_NULL);
		}
		return redisTemplate.getExpire(key, unit);
	}



	// ===================== 普通对象 =====================

	/**
	 * 获取普通对象
	 *
	 * @param key Redis键
	 * @return 对象
	 */
	public Object get(String key) {
		if(key == null){
			return null;
		}
		return redisTemplate.opsForValue().get(key);
	}



	/**
	 * 存入普通对象
	 * 	无时间限制
	 *
	 * @param key   Redis键
	 * @param value 值
	 * @return boolean
	 */
	public boolean put(String key, Object value) {
		boolean ret = false;
		try {
			redisTemplate.opsForValue().set(key, value);
			ret = true;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return ret;
	}

	/**
	 * 存入普通对象
	 *	有时间限制
	 *
	 * @param key Redis键
	 * @param value 值
	 * @param timeout 有效期，单位秒
	 * @return boolean
	 */
	public boolean put(String key, Object value, long timeout) {
		return this.put(key, value, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 存入普通对象
	 *	有时间限制
	 *
	 * @param key Redis键
	 * @param value 值
	 * @param timeout 有效期，单位秒
	 * @param unit 时间单位
	 * @return boolean
	 */
	public boolean put(String key, Object value, long timeout, TimeUnit unit) {
		boolean ret = false;
		try {
			if (timeout > 0) {
				redisTemplate.opsForValue().set(key, value, timeout, unit);
			} else {
				put(key, value);
			}
			ret = true;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return ret;
	}


	/**
	 * 递增 原子性++ increment
	 *
	 * @param key Redis键
	 * @return
	 */
	public Long increment(String key) {
		return this.increment(key,1);
	}

	/**
	 * 递增 原子性++ increment
	 * 
	 * @param key Redis键
	 * @param by  增加 i
	 * @return
	 */
	public Long increment(String key, long by) {
		if(key == null || "".equals(key)){
			throw new RedisPluginException(RedisMsg.EXCEPTION_KEY_NULL);
		}
		if (by < 0) {
			throw new RedisPluginException(RedisMsg.EXCEPTION_INCREMENT);
		}
		return redisTemplate.opsForValue().increment(key, by);
	}

	/**
	 * 递减 原子性 -- decrement
	 *
	 * @param key Redis键
	 * @return
	 */
	public Long decrement(String key) {
		return this.decrement(key,1);
	}

	/**
	 * 递减 原子性 -- decrement
	 * 
	 * @param key Redis键
	 * @param by  减少 i
	 * @return
	 */
	public Long decrement(String key, long by) {
		if(key == null || "".equals(key)){
			throw new RedisPluginException(RedisMsg.EXCEPTION_KEY_NULL);
		}
		if (by < 0) {
			throw new RedisPluginException(RedisMsg.EXCEPTION_DECREMENT);
		}
		return redisTemplate.opsForValue().decrement(key, by);
	}

	// ===================== 二进制位图 =====================
	/*
	* 比如 用在用户 登陆统计等
	* */

	/**
	 * 设置二进制位图
	 * @param key Redis键
	 * @param offset 偏移量
	 * @param value 0：false  1：true
	 * @return boolean
	 */
	public boolean bPut(String key, long offset, boolean value) {
		Boolean ret = redisTemplate.opsForValue().setBit(key, offset, value);
		return ret != null && ret;
	}

	/**
	 * 获得二进制位图 对应偏移量的值
	 * @param key Redis键
	 * @param offset 偏移量
	 * @return boolean
	 */
	public boolean bGet(String key, long offset) {
		Boolean ret = redisTemplate.opsForValue().getBit(key, offset);
		return ret != null && ret;
	}


	/**
	 * 统计对应的bitmap上value为1的数量
	 *
	 * @param key bitmap的key
	 * @return value等于1的数量
	 */
	public Long bGetCount(String key) {
		return redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes()));
	}

	/**
	 * 统计指定范围中value为1的数量
	 *
	 * @param key   bitMap中的key
	 * @param start 该参数的单位是byte（1byte=8bit），{@code setBit(key,7,true);}进行存储时，单位是bit。那么只需要统计[0,1]便可以统计到上述set的值。
	 * @param end   该参数的单位是byte。
	 * @return 在指定范围[start*8,end*8]内所有value=1的数量
	 */
	public Long bGetCount(String key, int start, int end) {
		return redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes(), start, end));
	}


	/**
	 * 对一个或多个保存二进制的字符串key进行元操作，并将结果保存到saveKey上。
	 * <p>
	 * bitop and saveKey key [key...]，对一个或多个key逻辑并，结果保存到saveKey。
	 * bitop or saveKey key [key...]，对一个或多个key逻辑或，结果保存到saveKey。
	 * bitop xor saveKey key [key...]，对一个或多个key逻辑异或，结果保存到saveKey。
	 * bitop xor saveKey key，对一个或多个key逻辑非，结果保存到saveKey。
	 * <p>
	 *
	 * @param op      元操作类型；
	 * @param saveKey 元操作后将结果保存到saveKey所在的结构中。
	 * @param desKey  需要进行元操作的类型。
	 * @return 1：返回元操作值。
	 */
	public Long bGetOp(RedisStringCommands.BitOperation op, String saveKey, String... desKey) {
		byte[][] bytes = new byte[desKey.length][];
		for (int i = 0; i < desKey.length; i++) {
			bytes[i] = desKey[i].getBytes();
		}
		return redisTemplate.execute((RedisCallback<Long>) con -> con.bitOp(op, saveKey.getBytes(), bytes));
	}

	/**
	 * 对一个或多个保存二进制的字符串key进行元操作，并将结果保存到saveKey上，并返回统计之后的结果。
	 *
	 * @param op      元操作类型；
	 * @param saveKey 元操作后将结果保存到saveKey所在的结构中。
	 * @param desKey  需要进行元操作的类型。
	 * @return 返回saveKey结构上value=1的所有数量值。
	 */
	public Long bGetOpResult(RedisStringCommands.BitOperation op, String saveKey, String... desKey) {
		this.bGetOp(op, saveKey, desKey);
		return this.bGetCount(saveKey);
	}

	// ===================== Hash/Map =====================

	/**
	 * 获取存储在哈希表中指定字段的值
	 *
	 * @param key
	 * @param field
	 * @return
	 */
	public Object hGet(String key, String field) {
		return redisTemplate.opsForHash().get(key, field);
	}

	/**
	 * 获取所有给定字段的值
	 *
	 * @param key
	 * @return
	 */
	public Map<Object, Object> hGetAll(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	/**
	 * 获取所有给定字段的值
	 *
	 * @param key
	 * @param fields
	 * @return
	 */
	public List<Object> hMultiGet(String key, Collection<Object> fields) {
		return redisTemplate.opsForHash().multiGet(key, fields);
	}

	/**
	 * 添加一个Hash 数据
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public boolean hPut(String key, String field, Object value) {
		boolean ret = false;
		try {
			redisTemplate.opsForHash().put(key, field, value);
			ret = true;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return ret;
	}


	/**
	 * 添加一组Hash数据
	 * @param key
	 * @param maps
	 * @return
	 */
	public boolean hPutAll(String key, Map<String, Object> maps) {
		boolean ret = false;
		try {
			redisTemplate.opsForHash().putAll(key, maps);
			ret = true;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return ret;
	}


	/**
	 * 仅当hashKey不存在时才设置
	 *
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public boolean hPutIfAbsent(String key, String field, Object value) {
		Boolean ret = null;
		try {
			ret = redisTemplate.opsForHash().putIfAbsent(key, field, value);
		}catch (Exception e){
			log.error(e.getMessage(),e);
		}
		return ret != null && ret;
	}

	/**
	 * 删除一个或多个哈希表字段
	 *
	 * @param key
	 * @param fields
	 * @return
	 */
	public Long hDelete(String key, Object... fields) {
		return redisTemplate.opsForHash().delete(key, fields);
	}


	/**
	 * 删除一个或多个哈希表字段
	 *
	 * @param key
	 * @param fields
	 * @return
	 */
	public Long hDelete(String key, Collection<Object> fields) {
		return redisTemplate.opsForHash().delete(key, fields);
	}

	/**
	 * 查看哈希表 key 中，指定的字段是否存在
	 *
	 * @param key
	 * @param field
	 * @return
	 */
	public boolean hHashKey(String key, String field) {
		Boolean ret = null;
		try {
			ret = redisTemplate.opsForHash().hasKey(key, field);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return ret != null && ret;
	}

	/**
	 * 为哈希表 key 中的指定字段的整数值加上增量 increment
	 *
	 * @param key
	 * @param field
	 * @param increment
	 * @return
	 */
	public Long hIncrBy(String key, Object field, long increment) {
		return redisTemplate.opsForHash().increment(key, field, increment);
	}

	/**
	 * 为哈希表 key 中的指定字段的整数值加上增量 increment
	 *
	 * @param key
	 * @param field
	 * @param delta
	 * @return
	 */
	public Double hIncrByFloat(String key, Object field, double delta) {
		return redisTemplate.opsForHash().increment(key, field, delta);
	}

	/**
	 * 获取所有哈希表中的字段
	 *
	 * @param key
	 * @return
	 */
	public Set<Object> hKeys(String key) {
		return redisTemplate.opsForHash().keys(key);
	}

	/**
	 * 获取哈希表中字段的数量
	 *
	 * @param key
	 * @return
	 */
	public Long hSize(String key) {
		return redisTemplate.opsForHash().size(key);
	}

	/**
	 * 获取哈希表中所有值
	 *
	 * @param key
	 * @return
	 */
	public List<Object> hValues(String key) {
		return redisTemplate.opsForHash().values(key);
	}

	// ===================== List =====================

	/**
	 * 通过索引获取列表中的元素
	 *
	 * @param key
	 * @param index
	 * @return
	 */
	public Object lIndex(String key, long index) {
		return redisTemplate.opsForList().index(key, index);
	}

	/**
	 * 获取列表指定范围内的元素
	 *
	 * @param key
	 * @param start
	 *            开始位置, 0是开始位置
	 * @param end
	 *            结束位置, -1返回所有
	 * @return
	 */
	public List<Object> lRange(String key, long start, long end) {
		return redisTemplate.opsForList().range(key, start, end);
	}

	/**
	 * 存储在list头部
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lLeftPush(String key, String value) {
		return redisTemplate.opsForList().leftPush(key, value);
	}

	/**
	 * 存储在list头部
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lLeftPushAll(String key, String... value) {
		return redisTemplate.opsForList().leftPushAll(key, value);
	}

	/**
	 * 存储在list头部
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lLeftPushAll(String key, Collection<String> value) {
		return redisTemplate.opsForList().leftPushAll(key, value);
	}

	/**
	 * 存储在list头部
	 * 当list存在的时候才加入
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lLeftPushIfPresent(String key, String value) {
		return redisTemplate.opsForList().leftPushIfPresent(key, value);
	}

	/**
	 * 存储在list头部
	 * 如果pivot存在,再pivot前面添加
	 *
	 * @param key
	 * @param pivot
	 * @param value
	 * @return
	 */
	public Long lLeftPush(String key, String pivot, String value) {
		return redisTemplate.opsForList().leftPush(key, pivot, value);
	}

	/**
	 * 存储在list尾部
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lRightPush(String key, String value) {
		return redisTemplate.opsForList().rightPush(key, value);
	}

	/**
	 * 存储在list尾部
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lRightPushAll(String key, String... value) {
		return redisTemplate.opsForList().rightPushAll(key, value);
	}

	/**
	 * 存储在list尾部
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lRightPushAll(String key, Collection<String> value) {
		return redisTemplate.opsForList().rightPushAll(key, value);
	}

	/**
	 * 存储在list尾部
	 * 为已存在的列表添加值
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lRightPushIfPresent(String key, String value) {
		return redisTemplate.opsForList().rightPushIfPresent(key, value);
	}

	/**
	 * 存储在list尾部
	 * 在pivot元素的右边添加值
	 *
	 * @param key
	 * @param pivot
	 * @param value
	 * @return
	 */
	public Long lRightPush(String key, String pivot, String value) {
		return redisTemplate.opsForList().rightPush(key, pivot, value);
	}

	/**
	 * 通过索引设置列表元素的值
	 *
	 * @param key
	 * @param index
	 *            位置
	 * @param value
	 */
	public boolean lSet(String key, long index, String value) {
		boolean ret = false;
		try {
			redisTemplate.opsForList().set(key, index, value);
			ret = true;
		}catch (Exception e){
			log.error(e.getMessage(),e);
		}
		return ret;
	}

	/**
	 * 移出并获取列表的第一个元素
	 *
	 * @param key
	 * @return 删除的元素
	 */
	public Object lLeftPop(String key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
	 *
	 * @param key
	 * @param timeout
	 *            等待时间
	 * @param unit
	 *            时间单位
	 * @return
	 */
	public Object lBLeftPop(String key, long timeout, TimeUnit unit) {
		return redisTemplate.opsForList().leftPop(key, timeout, unit);
	}

	/**
	 * 移除并获取列表最后一个元素
	 *
	 * @param key
	 * @return 删除的元素
	 */
	public Object lRightPop(String key) {
		return redisTemplate.opsForList().rightPop(key);
	}

	/**
	 * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
	 *
	 * @param key
	 * @param timeout
	 *            等待时间
	 * @param unit
	 *            时间单位
	 * @return
	 */
	public Object lBRightPop(String key, long timeout, TimeUnit unit) {
		return redisTemplate.opsForList().rightPop(key, timeout, unit);
	}

	/**
	 * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
	 *
	 * @param sourceKey
	 * @param destinationKey
	 * @return
	 */
	public Object lRightPopAndLeftPush(String sourceKey, String destinationKey) {
		return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey,
				destinationKey);
	}

	/**
	 * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
	 *
	 * @param sourceKey
	 * @param destinationKey
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public Object lBRightPopAndLeftPush(String sourceKey, String destinationKey,
										long timeout, TimeUnit unit) {
		return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey,
				destinationKey, timeout, unit);
	}

	/**
	 * 删除集合中值等于value得元素
	 *
	 * @param key
	 * @param index
	 *            index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素;
	 *            index<0, 从尾部开始删除第一个值等于value的元素;
	 * @param value
	 * @return
	 */
	public Long lRemove(String key, long index, String value) {
		return redisTemplate.opsForList().remove(key, index, value);
	}

	/**
	 * 裁剪list
	 *
	 * @param key
	 * @param start
	 * @param end
	 */
	public boolean lTrim(String key, long start, long end) {
		boolean ret = false;
		try {
			redisTemplate.opsForList().trim(key, start, end);
			ret = true;
		}catch (Exception e){
			log.error(e.getMessage(),e);
		}
		return ret;
	}

	/**
	 * 获取列表长度
	 *
	 * @param key
	 * @return
	 */
	public Long lLen(String key) {
		return redisTemplate.opsForList().size(key);
	}

	// ===================== Set =====================

	/**
	 * 判断集合是否包含value
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean sHashKey(String key, Object value) {
		Boolean ret = null;
		try {
			ret = redisTemplate.opsForSet().isMember(key, value);
		}catch (Exception e){
			log.error(e.getMessage(),e);
		}
		return ret != null && ret;
	}

	/**
	 * set添加元素
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public Long sPut(String key, String... values) {
		return redisTemplate.opsForSet().add(key, values);
	}

	/**
	 * set移除元素
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public Long sRemove(String key, Object... values) {
		return redisTemplate.opsForSet().remove(key, values);
	}

	/**
	 * set移除元素
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public Long sRemove(String key, Collection<Object> values) {
		return redisTemplate.opsForSet().remove(key, values);
	}


	/**
	 * 移除并返回集合的一个随机元素
	 *
	 * @param key
	 * @return
	 */
	public Object sPop(String key) {
		return redisTemplate.opsForSet().pop(key);
	}

	/**
	 * 将元素value从一个集合移到另一个集合
	 *
	 * @param key
	 * @param value
	 * @param destKey
	 * @return
	 */
	public boolean sMove(String key, String value, String destKey) {
		Boolean ret = null;
		try {
			ret = redisTemplate.opsForSet().move(key, value, destKey);
		}catch (Exception e){
			log.error(e.getMessage(),e);
		}
		return ret != null && ret;
	}

	/**
	 * 获取集合的大小
	 *
	 * @param key
	 * @return
	 */
	public Long sSize(String key) {
		return redisTemplate.opsForSet().size(key);
	}

	/**
	 * 获取两个集合的交集
	 *
	 * @param key
	 * @param otherKey
	 * @return
	 */
	public Set<Object> sIntersect(String key, String otherKey) {
		return redisTemplate.opsForSet().intersect(key, otherKey);
	}

	/**
	 * 获取key集合与多个集合的交集
	 *
	 * @param key
	 * @param otherKeys
	 * @return
	 */
	public Set<Object> sIntersect(String key, Collection<String> otherKeys) {
		return redisTemplate.opsForSet().intersect(key, otherKeys);
	}

	/**
	 * key集合与otherKey集合的交集存储到destKey集合中
	 *
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public Long sIntersectAndStore(String key, String otherKey, String destKey) {
		return redisTemplate.opsForSet().intersectAndStore(key, otherKey,
				destKey);
	}

	/**
	 * key集合与多个集合的交集存储到destKey集合中
	 *
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public Long sIntersectAndStore(String key, Collection<String> otherKeys,
								   String destKey) {
		return redisTemplate.opsForSet().intersectAndStore(key, otherKeys,
				destKey);
	}

	/**
	 * 获取两个集合的并集
	 *
	 * @param key
	 * @param otherKeys
	 * @return
	 */
	public Set<Object> sUnion(String key, String otherKeys) {
		return redisTemplate.opsForSet().union(key, otherKeys);
	}

	/**
	 * 获取key集合与多个集合的并集
	 *
	 * @param key
	 * @param otherKeys
	 * @return
	 */
	public Set<Object> sUnion(String key, Collection<String> otherKeys) {
		return redisTemplate.opsForSet().union(key, otherKeys);
	}

	/**
	 * key集合与otherKey集合的并集存储到destKey中
	 *
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public Long sUnionAndStore(String key, String otherKey, String destKey) {
		return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
	}

	/**
	 * key集合与多个集合的并集存储到destKey中
	 *
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public Long sUnionAndStore(String key, Collection<String> otherKeys,
							   String destKey) {
		return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
	}

	/**
	 * 获取两个集合的差集
	 *
	 * @param key
	 * @param otherKey
	 * @return
	 */
	public Set<Object> sDifference(String key, String otherKey) {
		return redisTemplate.opsForSet().difference(key, otherKey);
	}

	/**
	 * 获取key集合与多个集合的差集
	 *
	 * @param key
	 * @param otherKeys
	 * @return
	 */
	public Set<Object> sDifference(String key, Collection<String> otherKeys) {
		return redisTemplate.opsForSet().difference(key, otherKeys);
	}

	/**
	 * key集合与otherKey集合的差集存储到destKey中
	 *
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public Long sDifference(String key, String otherKey, String destKey) {
		return redisTemplate.opsForSet().differenceAndStore(key, otherKey,
				destKey);
	}

	/**
	 * key集合与多个集合的差集存储到destKey中
	 *
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public Long sDifference(String key, Collection<String> otherKeys,
							String destKey) {
		return redisTemplate.opsForSet().differenceAndStore(key, otherKeys,
				destKey);
	}

	/**
	 * 获取集合所有元素
	 *
	 * @param key
	 * @return
	 */
	public Set<Object> setMembers(String key) {
		return redisTemplate.opsForSet().members(key);
	}

	/**
	 * 随机获取集合中的一个元素
	 *
	 * @param key
	 * @return
	 */
	public Object sRandomMember(String key) {
		return redisTemplate.opsForSet().randomMember(key);
	}

	/**
	 * 随机获取集合中count个元素
	 *
	 * @param key
	 * @param count
	 * @return
	 */
	public List<Object> sRandomMembers(String key, long count) {
		return redisTemplate.opsForSet().randomMembers(key, count);
	}

	/**
	 * 随机获取集合中count个元素并且去除重复的
	 *
	 * @param key
	 * @param count
	 * @return
	 */
	public Set<Object> sDistinctRandomMembers(String key, long count) {
		return redisTemplate.opsForSet().distinctRandomMembers(key, count);
	}


	// ===================== zSet =====================

	/**
	 * 添加元素,有序集合是按照元素的score值由小到大排列
	 *
	 * @param key
	 * @param value
	 * @param score
	 * @return
	 */
	public Boolean zAdd(String key, String value, double score) {
		return redisTemplate.opsForZSet().add(key, value, score);
	}

	/**
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public Long zAdd(String key, Set<ZSetOperations.TypedTuple<Object>> values) {
		return redisTemplate.opsForZSet().add(key, values);
	}

	/**
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public Long zRemove(String key, Object... values) {
		return redisTemplate.opsForZSet().remove(key, values);
	}

	/**
	 * 增加元素的score值，并返回增加后的值
	 *
	 * @param key
	 * @param value
	 * @param delta
	 * @return
	 */
	public Double zIncrementScore(String key, String value, double delta) {
		return redisTemplate.opsForZSet().incrementScore(key, value, delta);
	}

	/**
	 * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
	 *
	 * @param key
	 * @param value
	 * @return 0表示第一位
	 */
	public Long zRank(String key, Object value) {
		return redisTemplate.opsForZSet().rank(key, value);
	}

	/**
	 * 返回元素在集合的排名,按元素的score值由大到小排列
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public Long zReverseRank(String key, Object value) {
		return redisTemplate.opsForZSet().reverseRank(key, value);
	}

	/**
	 * 获取集合的元素, 从小到大排序
	 *
	 * @param key
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置, -1查询所有
	 * @return
	 */
	public Set<Object> zRange(String key, long start, long end) {
		return redisTemplate.opsForZSet().range(key, start, end);
	}

	/**
	 * 获取集合元素, 并且把score值也获取
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<ZSetOperations.TypedTuple<Object>> zRangeWithScores(String key, long start,
																   long end) {
		return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
	}

	/**
	 * 根据Score值查询集合元素
	 *
	 * @param key
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 * @return
	 */
	public Set<Object> zRangeByScore(String key, double min, double max) {
		return redisTemplate.opsForZSet().rangeByScore(key, min, max);
	}

	/**
	 * 根据Score值查询集合元素, 从小到大排序
	 *
	 * @param key
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 * @return
	 */
	public Set<ZSetOperations.TypedTuple<Object>> zRangeByScoreWithScores(String key,
																		  double min, double max) {
		return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
	}

	/**
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<ZSetOperations.TypedTuple<Object>> zRangeByScoreWithScores(String key,
																		  double min, double max, long start, long end) {
		return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max,
				start, end);
	}

	/**
	 * 获取集合的元素, 从大到小排序
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<Object> zReverseRange(String key, long start, long end) {
		return redisTemplate.opsForZSet().reverseRange(key, start, end);
	}

	/**
	 * 获取集合的元素, 从大到小排序, 并返回score值
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<ZSetOperations.TypedTuple<Object>> zReverseRangeWithScores(String key,
																		  long start, long end) {
		return redisTemplate.opsForZSet().reverseRangeWithScores(key, start,
				end);
	}

	/**
	 * 根据Score值查询集合元素, 从大到小排序
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<Object> zReverseRangeByScore(String key, double min,
											double max) {
		return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
	}

	/**
	 * 根据Score值查询集合元素, 从大到小排序
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<ZSetOperations.TypedTuple<Object>> zReverseRangeByScoreWithScores(
			String key, double min, double max) {
		return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key,
				min, max);
	}

	/**
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<Object> zReverseRangeByScore(String key, double min,
											double max, long start, long end) {
		return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max,
				start, end);
	}

	/**
	 * 根据score值获取集合元素数量
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Long zCount(String key, double min, double max) {
		return redisTemplate.opsForZSet().count(key, min, max);
	}

	/**
	 * 获取集合大小
	 *
	 * @param key
	 * @return
	 */
	public Long zSize(String key) {
		return redisTemplate.opsForZSet().size(key);
	}

	/**
	 * 获取集合大小
	 *
	 * @param key
	 * @return
	 */
	public Long zZCard(String key) {
		return redisTemplate.opsForZSet().zCard(key);
	}

	/**
	 * 获取集合中value元素的score值
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public Double zScore(String key, Object value) {
		return redisTemplate.opsForZSet().score(key, value);
	}

	/**
	 * 移除指定索引位置的成员
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Long zRemoveRange(String key, long start, long end) {
		return redisTemplate.opsForZSet().removeRange(key, start, end);
	}

	/**
	 * 根据指定的score值的范围来移除成员
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Long zRemoveRangeByScore(String key, double min, double max) {
		return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
	}

	/**
	 * 获取key和otherKey的并集并存储在destKey中
	 *
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public Long zUnionAndStore(String key, String otherKey, String destKey) {
		return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
	}

	/**
	 *
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public Long zUnionAndStore(String key, Collection<String> otherKeys,
							   String destKey) {
		return redisTemplate.opsForZSet()
				.unionAndStore(key, otherKeys, destKey);
	}

	/**
	 * 交集
	 *
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public Long zIntersectAndStore(String key, String otherKey,
								   String destKey) {
		return redisTemplate.opsForZSet().intersectAndStore(key, otherKey,
				destKey);
	}

	/**
	 * 交集
	 *
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public Long zIntersectAndStore(String key, Collection<String> otherKeys,
								   String destKey) {
		return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys,
				destKey);
	}

	// ===================== 消息发布 =====================

	/**
	 * Redis 消息发布 Push
	 * @param basePubMessage
	 */
	public boolean sendMessage(BaseSubMessage basePubMessage) {
		if(basePubMessage == null){
			throw new RedisPluginException(RedisMsg.EXCEPTION_PUSH_SUB_NULL);
		}
		boolean ret = false;
		try {
			redisTemplate.convertAndSend(basePubMessage.getChannel(), basePubMessage.getJson());
			ret = true;
		}catch (Exception e){
			log.error(e.getMessage(),e);
		}
		return ret;
	}

}
