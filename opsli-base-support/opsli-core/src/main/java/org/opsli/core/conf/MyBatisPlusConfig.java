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
