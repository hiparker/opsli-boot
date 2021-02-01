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
package org.opsli.core.autoconfigure.conf;

import cn.hutool.core.convert.Convert;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.core.waf.filter.WafFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.DispatcherType;

/**
 * 软件防火墙
 * 防止XSS SQL 攻击
 *
 * @author Parker
 * @date 2020-10-09
 */
@Configuration
public class WafConfig {

	@Resource
	private GlobalProperties globalProperties;

	@Bean
	@ConditionalOnProperty(prefix = GlobalProperties.PREFIX +".waf", name = "enable", havingValue = "true", matchIfMissing = false)
	public FilterRegistrationBean<WafFilter> wafFilterRegistration() {
		WafFilter wafFilter = new WafFilter();
		wafFilter.setUrlExclusion(globalProperties.getWaf().getUrlExclusion());
		wafFilter.setEnableSqlFilter(globalProperties.getWaf().isSqlFilter());
		wafFilter.setEnableXssFilter(globalProperties.getWaf().isXssFilter());

		FilterRegistrationBean<WafFilter> registration = new FilterRegistrationBean<>();
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		registration.setFilter(wafFilter);
		registration.addUrlPatterns(Convert.toStrArray(globalProperties.getWaf().getUrlPatterns()));
		registration.setName(WafFilter.class.getSimpleName());
		registration.setOrder(globalProperties.getWaf().getOrder());
		return registration;
	}


}
