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

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.opsli.core.filters.interceptor.MybatisAutoFillInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * MyBatis - Plus 配置
 *
 * @author Parker
 * @date 2020-09-15
 */
@Slf4j
@EnableTransactionManagement
@Configuration
public class MyBatisPlusConfig {

	/**
	 * 相关拦截器
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();

		// 乐观锁
		mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

		// 防止全表更新与删除插件
		//mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

		return mybatisPlusInterceptor;
	}

	/**
	 * Mybatis 拦截器
	 * @param sqlSessionFactory sqlSessionFactory
	 * @return String
	 */
	@Bean
	public String myInterceptor(SqlSessionFactory sqlSessionFactory) {
		sqlSessionFactory.getConfiguration().addInterceptor(new MybatisAutoFillInterceptor());
		return "interceptor";
	}

}
