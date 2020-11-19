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
package org.opsli.core.conf;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.opsli.core.conf.mybatis.AutoFillInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @Author parker
 *
 * MyBatis - Plus 配置
 *
 */
@Slf4j
@EnableTransactionManagement  //开启事务
@Configuration
public class MyBatisPlusConfig {

	/***
	 * 乐观锁
	 * @return
	 */
	@Bean
	public OptimisticLockerInterceptor optimisticLockerInterceptor(){
		return new OptimisticLockerInterceptor();
	}

	/**
	 * Mybatis 拦截器
	 * @param sqlSessionFactory
	 * @return
	 */
	@Bean
	public String myInterceptor(SqlSessionFactory sqlSessionFactory) {
		sqlSessionFactory.getConfiguration().addInterceptor(new AutoFillInterceptor());
		return "interceptor";
	}

}
