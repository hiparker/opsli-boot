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

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * XSS 过滤
 *
 * @author Parker
 * @date 2020-10-09
 * */
public final class XSSFilterKit {

	private static final Pattern SCRIPT_ALL_PATTERN;
	private static final Pattern SCRIPT_INNER_PATTERN;
	private static final Pattern SCRIPT_PATTERN;
	private static final Pattern EVAL_PATTERN;
	private static final Pattern EXPRESSION_PATTERN;
	private static final Pattern JAVASCRIPT_PATTERN;
	private static final Pattern VBSCRIPT_PATTERN;
	private static final Pattern ONLOAD_PATTERN;
	static {
		SCRIPT_ALL_PATTERN = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
		SCRIPT_INNER_PATTERN = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE
				| Pattern.MULTILINE | Pattern.DOTALL);
		SCRIPT_PATTERN = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);

		EVAL_PATTERN = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE
				| Pattern.MULTILINE | Pattern.DOTALL);

		EXPRESSION_PATTERN = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE
				| Pattern.MULTILINE | Pattern.DOTALL);

		JAVASCRIPT_PATTERN = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);

		VBSCRIPT_PATTERN = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);

		ONLOAD_PATTERN = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE
				| Pattern.MULTILINE | Pattern.DOTALL);
	}

	/**
	 * @Description 过滤XSS脚本内容
	 * @param value
	 * @return
	 */
	public static String stripXSS(String value) {
		String rlt = null;
		if (StringUtils.isNotEmpty(value)) {
			// NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
			// avoid encoded attacks.
			// value = ESAPI.encoder().canonicalize(value);

			// Avoid null characters
			rlt = value.replaceAll("", "");

			// Avoid anything between script tags
			rlt = SCRIPT_ALL_PATTERN.matcher(rlt).replaceAll("");

			// Avoid anything in a src='...' type of expression
			/*scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);
			rlt = scriptPattern.matcher(rlt).replaceAll("");*/

			// Remove any lonesome </script> tag
			rlt = SCRIPT_PATTERN.matcher(rlt).replaceAll("");

			// Remove any lonesome <script ...> tag
			rlt = SCRIPT_INNER_PATTERN.matcher(rlt).replaceAll("");

			// Avoid eval(...) expressions
			rlt = EVAL_PATTERN.matcher(rlt).replaceAll("");

			// Avoid expression(...) expressions
			rlt = EXPRESSION_PATTERN.matcher(rlt).replaceAll("");

			// Avoid javascript:... expressions
			rlt = JAVASCRIPT_PATTERN.matcher(rlt).replaceAll("");

			// Avoid vbscript:... expressions
			rlt = VBSCRIPT_PATTERN.matcher(rlt).replaceAll("");

			// Avoid onload= expressions
			rlt = ONLOAD_PATTERN.matcher(rlt).replaceAll("");
		}

		return rlt;
	}

	// ====================

	private XSSFilterKit(){}
}
