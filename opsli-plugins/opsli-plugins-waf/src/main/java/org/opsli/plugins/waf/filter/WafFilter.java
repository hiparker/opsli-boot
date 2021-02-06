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
package org.opsli.plugins.waf.filter;



import org.opsli.plugins.waf.servlet.WafHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Set;

/**
 * 防火墙
 *
 * @author Parker
 * @date 2020-10-09
 */
public class WafFilter implements Filter {

	private boolean enableXssFilter = false;
	private boolean enableSqlFilter = false;

	private Set<String> urlExclusion;


	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String servletPath = httpServletRequest.getServletPath();

		// 如果是排除url 则放行
		if (urlExclusion != null && urlExclusion.contains(servletPath)) {
			chain.doFilter(request, response);
		} else {
			// 执行过滤
			chain.doFilter(
					new WafHttpServletRequestWrapper((HttpServletRequest) request, enableXssFilter, enableSqlFilter),
					response);
		}
	}

	@Override
	public void destroy() {
	}

	// ============================


	public void setEnableXssFilter(boolean enableXssFilter) {
		this.enableXssFilter = enableXssFilter;
	}

	public void setEnableSqlFilter(boolean enableSqlFilter) {
		this.enableSqlFilter = enableSqlFilter;
	}

	public void setUrlExclusion(Set<String> urlExclusion) {
		this.urlExclusion = urlExclusion;
	}
}
