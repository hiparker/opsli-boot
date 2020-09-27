package org.opsli.api.conf;

import lombok.extern.slf4j.Slf4j;
import org.opsli.api.conf.prop.ApiPathProperties;
import org.opsli.common.annotation.ApiRestController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 配置统一的后台接口访问路径的前缀
 * @author parker
 */
@Slf4j
@Configuration
public class SpringWebMvcConfig implements WebMvcConfigurer {

	@Resource
	private ApiPathProperties apiPathProperties;

	/**
	 * 配置 ApiRestController 生效
	 * @param configurer
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer
				.addPathPrefix(apiPathProperties.getGlobalPrefix(),c -> c.isAnnotationPresent(ApiRestController.class));
	}

 
}
