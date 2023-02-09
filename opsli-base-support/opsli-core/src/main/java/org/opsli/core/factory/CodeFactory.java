package org.opsli.core.factory;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.constants.RedisConstants;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.utils.EnvPrefixTool;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;


/**
 * 编号生成 工厂
 *
 * @author parker
 * @date 2022/1/13 18:28
 */
@Slf4j
@AllArgsConstructor
public class CodeFactory {

	/** 压缩秒格式 */
	private final static String FORMAT_COMPRESS_SECOND = "yyyyMMddHHmmss";

	/** 默认1分钟不会超过 10000个订单 */
	private final static int TTL = 60;
	/** 自增ID 前缀 */
	private final static String INCR_PREFIX_BIZ = "biz_";


	/**
	 * 生成业务流水号
	 * 规则是：业务编码+年的后2位+月+日+秒+订单数  固定长度为 14 同一秒不会存在10000个订单
	 *
	 * @param redisTemplate redisTemplate
	 * @param bizTag 业务标识
	 * @return String
	 */
	public String generateSoleNo(RedisTemplate<String, String> redisTemplate, String bizTag){
		if(null == redisTemplate){
			throw new RuntimeException("RedisTemplate 为空");
		}

		// 格式化 业务号
		final String formatBizTag = EnvPrefixTool.getEnvBizTag(bizTag);

		LocalDateTime localDateTime = LocalDateTime.now();

		// 当前时间 到日
		String formatDate = DateUtil.format(DateUtil.date(localDateTime), "yyyyMMdd");

		// 日期前缀
		String prefixDate = StrUtil.sub(formatDate, 2, formatDate.length());

		// 已走过秒
		int secondCount = (int) (getTimeMillsSecond(localDateTime)/1000 - getTimeMillsDay(localDateTime)/1000);
		String prefixSecond = StrUtil.fillBefore(String.valueOf(secondCount), '0', 5);

		// 获得自增ID
		String incrementId = getIncrementId(redisTemplate, INCR_PREFIX_BIZ, formatBizTag);

		return formatBizTag + prefixDate + prefixSecond
				+ incrementId;
	}



	/**
	 * 生成UUID
	 *
	 * @return String
	 */
	public String generateUUID(){
		return StrUtil.uuid();
	}


	/**
	 * 获得 自增ID
	 * @param redisTemplate redisTemplate
	 * @param prefix 前缀
	 * @param tag 标识
	 * @return long
	 */
	private String getIncrementId(RedisTemplate<String, String> redisTemplate, String prefix, String tag) {
		// 自增ID
		Long incrementId;
		// 缓存Key
		String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_ID_INCR)
				+ prefix + tag;
		// 判断是否存在
		incrementId = redisTemplate.opsForValue().increment(cacheKey);
		if(null == incrementId){
			throw new RuntimeException("Redis 生成自增ID 失败");
		}
		Long expire = redisTemplate.getExpire(cacheKey, TimeUnit.SECONDS);
		if(null == expire || expire < 0){
			// 设置 60 秒失效
			redisTemplate.expire(cacheKey, TTL, TimeUnit.SECONDS);
		}

		// 不足4位补0, 超过4位返回原始值
		return StrUtil.fillBefore(String.valueOf(incrementId), '0', 4);
	}

	/**
	 * 获取分钟的时间戳
	 *
	 * @return long
	 */
	private static long getTimeMillsDay(LocalDateTime localDateTime) {
		LocalDate localDate = localDateTime.toLocalDate();
		return LocalDateTime.of(
						localDate.getYear(), localDate.getMonth(),
						localDate.getDayOfMonth(), 0,
						0, 0)
				.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	/**
	 * 获取精确到秒的时间戳
	 *
	 * @return long
	 */
	private static long getTimeMillsSecond(LocalDateTime localDateTime) {
		LocalDate localDate = localDateTime.toLocalDate();
		LocalTime localTime =  localDateTime.toLocalTime();
		return LocalDateTime.of(
				localDate.getYear(), localDate.getMonth(),
				localDate.getDayOfMonth(), localTime.getHour(),
				localTime.getMinute(), localTime.getSecond())
				.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

}
