package org.opsli.common.constants;

/**
 * Redis 常量
 * {} 为项目名称
 * @author Parker
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
	public static final String PREFIX_OPTIONS_CODE = "hash#{}:options";

	/** 用户搜索记录 */
	public static final String PREFIX_HIS_USERNAME = "zset#{}:his:username:";



	/** 用户ID */
	public static final String PREFIX_USER_ID = "kv#{}:user_id:";

	/** 用户ID 和 角色 */
	public static final String PREFIX_USER_ID_AND_ROLES = "kv#{}:user_id:roles:";

	/** 用户ID 和 默认角色 */
	public static final String PREFIX_USER_ID_DEF_ROLE = "kv#{}:user_id:def_role_id:";

	/** 用户ID 和 组织 */
	public static final String PREFIX_USER_ID_ORGS = "kv#{}:user_id:orgs:";

	/** 用户ID 和 默认组织 */
	public static final String PREFIX_USER_ID_DEF_ORG = "kv#{}:user_id:def_org:";

	/** 用户ID 和 权限 */
	public static final String PREFIX_USER_ID_PERMISSIONS = "kv#{}:user_id:permissions:";

	/** 用户ID 和 菜单 */
	public static final String PREFIX_USER_ID_MENUS = "kv#{}:user_id:menus:";

	/** 用户名 + 用户ID */
	public static final String PREFIX_USER_USERNAME = "kv#{}:user:username_id:";
	/** 手机号 + 用户ID */
	public static final String PREFIX_USER_MOBILE = "kv#{}:user:mobile_id:";
	/** 邮箱 + 用户ID */
	public static final String PREFIX_USER_EMAIL = "kv#{}:user:email_id:";


	/** 票据 */
	public static final String PREFIX_TICKET = "set#{}:ticket:";

	/** 账号失败次数 */
	public static final String PREFIX_ACCOUNT_SLIP_COUNT = "kv#{}:account:slip:count:";

	/** 账号失败锁定KEY */
	public static final String PREFIX_ACCOUNT_SLIP_LOCK = "kv#{}:account:slip:lock:";

	/** 临时邮箱验证码 */
	public static final String PREFIX_TMP_EMAIL_CODE_NAME = "kv#{}:verification:email:code:";

	/** 临时手机验证码 */
	public static final String PREFIX_TMP_MOBILE_CODE_NAME = "kv#{}:verification:mobile:code:";

	/** 验证码凭证 用于验证码二次校验(唯一流水号 increment) */
	public static final String PREFIX_TMP_VERIFICATION_CERTIFICATE_NUM_NAME = "kv#{}:verification:certificate-num";
	/** 验证码凭证 用于验证码二次校验 */
	public static final String PREFIX_TMP_VERIFICATION_CERTIFICATE_NAME = "kv#{}:verification:certificate:";

	/** Excel 导出(唯一流水号 increment) */
	public static final String PREFIX_TMP_EXCEL_EXPORT_NUM_NAME = "kv#{}:excel-export-num";
	/** Excel 导出 凭证 */
	public static final String PREFIX_TMP_EXCEL_EXPORT_NAME = "kv#{}:excel-export:";

	private RedisConstants(){}
}
