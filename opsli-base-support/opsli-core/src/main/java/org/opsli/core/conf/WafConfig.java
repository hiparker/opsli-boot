package org.opsli.core.conf;

import org.opsli.core.waf.XssProperties;
import org.opsli.core.waf.filter.WafFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * @author 毕子航 951755883@qq.com
 * @date 2018/10/26
 */
@Configuration
@EnableConfigurationProperties({XssProperties.class})
public class WafConfig {

	@Autowired
	XssProperties xssProperties;

	@Bean
	@ConditionalOnProperty(prefix = XssProperties.XSS, name = "enable", havingValue = "true", matchIfMissing = false)
	public FilterRegistrationBean xssFilterRegistration() {
		WafFilter wafFilter = new WafFilter();
		wafFilter.setUrlExclusion(xssProperties.getUrlExclusion());
		wafFilter.setEnableSqlFilter(xssProperties.isSqlFilter());
		wafFilter.setEnableXssFilter(xssProperties.isEnable());

		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		registration.setFilter(wafFilter);
		registration.addUrlPatterns(xssProperties.getUrlPatterns());
		registration.setName(xssProperties.getName());
		registration.setOrder(xssProperties.getOrder());
		return registration;
	}


}
