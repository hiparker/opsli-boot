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
package org.opsli.plugins.waf.util;

import org.opsli.common.exception.WafException;
import org.opsli.plugins.waf.msg.WafMsg;
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
		//str = StringUtils.replace(str, "\"", "");
		str = StringUtils.replace(str, ";", "");
		str = StringUtils.replace(str, "\\", "");

		//转换成小写
		str = str.toLowerCase();

		//非法字符
		String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};

		//判断是否包含非法字符
		for (String keyword : keywords) {
			if (str.contains(keyword)) {
				throw new WafException(WafMsg.WAF_EXCEPTION_SQL);
			}
		}
		return str;
	}


	/**
	 * SQL注入过滤
	 *
	 * @param str 待验证的字符串
	 */
	public static String replaceSQL(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		//去掉'|"|;|\字符
		str = StringUtils.replace(str, "'", "");
		str = StringUtils.replace(str, "\"", "");
		str = StringUtils.replace(str, ";", "");
		str = StringUtils.replace(str, "\\", "");
		str = StringUtils.replace(str, "|", "");

		//转换成小写
		//str = str.toLowerCase();

		// 非法字符
		//String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};
		// 非法字符（为了代码生成器 放开 delete update 扫描）
		String[] keywords = {"master", "truncate", "insert", "select", "declare", "alter", "drop"};

		// 替换非法字符
		for (String keyword : keywords) {
			str = StringUtils.replace(str, keyword, "");
		}

		return str;
	}

	// ====================
	private SQLFilterKit(){}
}
