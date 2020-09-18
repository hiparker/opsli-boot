package org.opsli.core.conf;

import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.prop.ApiPathProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 配置统一的后台接口访问路径的前缀
 * @author C西
 */
@Configuration
public class SpringWebMvcConfig implements WebMvcConfigurer {

	@Resource
	private ApiPathProperties apiPathProperties;

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer
				.addPathPrefix(apiPathProperties.getGlobalPrefix(),c -> c.isAnnotationPresent(ApiRestController.class));
	}
 
}
