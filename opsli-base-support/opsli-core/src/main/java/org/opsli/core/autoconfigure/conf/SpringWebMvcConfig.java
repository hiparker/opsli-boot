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

import lombok.extern.slf4j.Slf4j;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.api.ApiRequestMappingHandlerMapping;
import org.opsli.core.autoconfigure.properties.ApiPathProperties;
import org.opsli.core.filters.interceptor.UserAuthInterceptor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;

/**
 * 配置统一的后台接口访问路径的前缀
 *
 * @author Parker
 * @date 2020-09-15
 */
@Slf4j
@Configuration
public class SpringWebMvcConfig implements WebMvcConfigurer, WebMvcRegistrations {

	@Resource
	private ApiPathProperties apiPathProperties;

	/**
	 * 重写RequestMappingHandlerMapping，自定义匹配的处理器
	 * @return RequestMappingHandlerMapping
	 */
	@Override
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return new ApiRequestMappingHandlerMapping();
	}

	/**
	 * 配置 ApiRestController 生效
	 * @param configurer 配置
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer
				.addPathPrefix(apiPathProperties.getGlobalPrefix(),c -> c.isAnnotationPresent(ApiRestController.class));
	}

	/**
	 * 解决跨域问题
	 *
	 * @param registry registry
	 */
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**")
//				.allowedOriginPatterns("*")
//				.allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
//				.allowedHeaders("*")
//				.allowCredentials(true)
//				.maxAge(7200);
//	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 加载特定拦截器
		registry.addInterceptor(new UserAuthInterceptor());
		WebMvcConfigurer.super.addInterceptors(registry);
	}
}
