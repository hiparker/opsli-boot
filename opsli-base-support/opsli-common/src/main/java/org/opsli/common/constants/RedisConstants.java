package org.opsli.common.constants;

/**
 * Redis 常量
 * {} 为项目名称
 * @author 周鹏程
 * @date 2021/12/10 19:52
 */
public final class RedisConstants {


	/** 字典名称 */
	public static final String PREFIX_DICT_NAME = "hash#{}:dict:name:";

	/** 字典值 VALUE */
	public static final String PREFIX_DICT_VALUE = "hash#{}:dict:value:";

	/** 菜单编号 */
	public static final String PREFIX_MENU_CODE = "kv#{}:menu:code:";

	/** 参数编号 */
	public static final String PREFIX_OPTIONS_CODE = "kv#{}:options:code";

	/** 用户搜索记录 */
	public static final String PREFIX_HIS_USERNAME = "zset#{}:his:username:";

	private RedisConstants(){}
}
