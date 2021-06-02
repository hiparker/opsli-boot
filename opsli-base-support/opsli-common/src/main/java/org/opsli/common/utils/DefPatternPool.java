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
package org.opsli.common.utils;

import cn.hutool.core.lang.SimpleCache;

import java.util.regex.Pattern;

/**
 * 自定义正则池，更多正则见:https://any86.github.io/any-rule/
 *
 * @author Looly
 */
public final class DefPatternPool {

	/**
	 * 密码强度校验，最少6位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符
	 * 密码至少包含大小写字母、数字、字符，且不少于6位
	 */
	public final static Pattern SECURITY_PASSWORD = Pattern.compile("^\\S*(?=\\S{6,})(?=\\S*\\d)(?=\\S*[A-Z])(?=\\S*[a-z])(?=\\S*[!@#$%^&*?.])\\S*$");

	/**
	 * 占位符  /\$\{(\w+)\}/g
	 * ${xxxx}
	 */
	public final static Pattern PLACEHOLDER = Pattern.compile("\\$\\{([\\w\\.\\-\\/\\+\\$\\#\\@\\!\\^\\&\\(\\)]+)\\}");


	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Pattern池
	 */
	private static final SimpleCache<RegexWithFlag, Pattern> POOL = new SimpleCache<>();

	/**
	 * 先从Pattern池中查找正则对应的{@link Pattern}，找不到则编译正则表达式并入池。
	 *
	 * @param regex 正则表达式
	 * @return {@link Pattern}
	 */
	public static Pattern get(String regex) {
		return get(regex, 0);
	}

	/**
	 * 先从Pattern池中查找正则对应的{@link Pattern}，找不到则编译正则表达式并入池。
	 *
	 * @param regex 正则表达式
	 * @param flags 正则标识位集合 {@link Pattern}
	 * @return {@link Pattern}
	 */
	public static Pattern get(String regex, int flags) {
		final RegexWithFlag regexWithFlag = new RegexWithFlag(regex, flags);

		Pattern pattern = POOL.get(regexWithFlag);
		if (null == pattern) {
			pattern = Pattern.compile(regex, flags);
			POOL.put(regexWithFlag, pattern);
		}
		return pattern;
	}

	/**
	 * 移除缓存
	 *
	 * @param regex 正则
	 * @param flags 标识
	 * @return 移除的{@link Pattern}，可能为{@code null}
	 */
	public static Pattern remove(String regex, int flags) {
		return POOL.remove(new RegexWithFlag(regex, flags));
	}

	/**
	 * 清空缓存池
	 */
	public static void clear() {
		POOL.clear();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 正则表达式和正则标识位的包装
	 *
	 * @author Looly
	 */
	private static class RegexWithFlag {
		private final String regex;
		private final int flag;

		/**
		 * 构造
		 *
		 * @param regex 正则
		 * @param flag  标识
		 */
		public RegexWithFlag(String regex, int flag) {
			this.regex = regex;
			this.flag = flag;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + flag;
			result = prime * result + ((regex == null) ? 0 : regex.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			RegexWithFlag other = (RegexWithFlag) obj;
			if (flag != other.flag) {
				return false;
			}
			if (regex == null) {
				return other.regex == null;
			} else {
				return regex.equals(other.regex);
			}
		}

	}

	// ==================

	private DefPatternPool(){}
}