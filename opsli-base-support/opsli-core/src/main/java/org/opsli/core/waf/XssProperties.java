package org.opsli.core.waf;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Xss过滤器配置属性
 *
 * @author 毕子航 951755883@qq.com
 * @date 2018/10/26
 */
@ConfigurationProperties(prefix = XssProperties.XSS)
public class XssProperties {
	public static final String XSS = "xss";

	/**
	 * xss 是否生效
	 */
	boolean enable = false;
	/**
	 * sql 过滤
	 */
	boolean sqlFilter = false;

	/**
	 * xss过滤器的名字
	 */
	String name = "xssFilter";
	/**
	 * xss过滤器需要过滤的路径
	 */
	String[] urlPatterns = {"/*"};

	List<String> urlExclusion;

	/**
	 * 过滤器的优先级，值越小优先级越高
	 */
	int order = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getUrlPatterns() {
		return urlPatterns;
	}

	public void setUrlPatterns(String[] urlPatterns) {
		this.urlPatterns = urlPatterns;
	}

	public List<String> getUrlExclusion() {
		return urlExclusion;
	}

	public void setUrlExclusion(List<String> urlExclusion) {
		this.urlExclusion = urlExclusion;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public boolean isSqlFilter() {
		return sqlFilter;
	}

	public void setSqlFilter(boolean sqlFilter) {
		this.sqlFilter = sqlFilter;
	}
}
