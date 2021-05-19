package org.opsli.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicationContext.
 * 针对项目 启动后好使， 项目启动前 Bean注册时不好使
 *
 * @author Zaric
 * @date 2016-5-29 下午1:25:40
 */
@Order(-1)
@Component
@Lazy(false)
@Slf4j
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext = null;

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}

	/**
	 * 注册Bean对象
	 * @param beanName beanName
	 * @param bean bean对象
	 * @param <T> 泛型
	 */
	public static <T> boolean registerBean(String beanName, T bean) {
		assertContextInjected();
		boolean ret = false;
		ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
		if(context != null){
			try {
				context.getBeanFactory().registerSingleton(beanName, bean);
				ret = true;
			}catch (Exception e){
				log.error(e.getMessage(), e);
			}
		}
		return ret;
	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clearHolder() {
		if (log.isDebugEnabled()){
			log.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
		}
		applicationContext = null;
	}


	/**
	 * 检查ApplicationContext不为空.
	 */
	private static void assertContextInjected() {
		Validate.validState(applicationContext != null, "applicationContext属性未注入.");
	}

	// ==================

	/**
	 * 实现ApplicationContextAware接口, 注入Context到静态变量中.
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextHolder.applicationContext = applicationContext;
	}

	/**
	 * 实现DisposableBean接口, 在Context关闭时清理静态变量.
	 */
	@Override
	public void destroy(){
		SpringContextHolder.clearHolder();
	}

}

