package org.opsli.core.waf.util;

import org.opsli.common.exception.WafException;
import org.opsli.core.msg.CoreMsg;
import org.springframework.util.StringUtils;

/**
 * SQL过滤
 *
 * @author Parker
 * @date 2020-10-09
 */
public final class SQLFilterKit {

	/**
	 * SQL注入过滤
	 *
	 * @param str 待验证的字符串
	 */
	public static String stripSQL(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		//去掉'|"|;|\字符
		str = StringUtils.replace(str, "'", "");
		str = StringUtils.replace(str, "\"", "");
		str = StringUtils.replace(str, ";", "");
		str = StringUtils.replace(str, "\\", "");

		//转换成小写
		str = str.toLowerCase();

		//非法字符
		String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};

		//判断是否包含非法字符
		for (String keyword : keywords) {
			if (str.contains(keyword)) {
				throw new WafException(CoreMsg.WAF_EXCEPTION_SQL);
			}
		}
		return str;
	}

	// ====================
	private SQLFilterKit(){}
}
