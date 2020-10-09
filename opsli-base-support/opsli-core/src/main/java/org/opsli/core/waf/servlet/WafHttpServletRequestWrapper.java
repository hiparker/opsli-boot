package org.opsli.core.waf.servlet;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.constants.TokenConstants;
import org.opsli.common.exception.WafException;
import org.opsli.core.waf.util.XSSFilterKit;
import org.opsli.core.waf.util.SQLFilterKit;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 防火墙过滤处理器
 *
 * @author Parker
 * @date 2020-10-09
 */
@Slf4j
public class WafHttpServletRequestWrapper extends HttpServletRequestWrapper {

	/** 头消息类型 */
	private static final String CONTENT_TYPE = "Content-Type";
	private static final List<String> CONTENT_TYPE_LIST = Lists.newArrayList();

	static {
		CONTENT_TYPE_LIST.add("application/json");
		CONTENT_TYPE_LIST.add("application/json;charset=UTF-8");
		CONTENT_TYPE_LIST.add("application/x-www-form-urlencoded;charset=UTF-8");
	}

	/**
	 * 没被包装过的HttpServletRequest（特殊场景，需要自己过滤）
	 */
	HttpServletRequest orgRequest;


	/** Xss 攻击防护 */
	private final boolean enableXssFilter;
	/** SQL 攻击防护 */
	private final boolean enableSqlFilter;

	public WafHttpServletRequestWrapper(HttpServletRequest request, boolean enableXssFilter, boolean enableSqlFilter) {
		super(request);
		orgRequest = request;
		this.enableXssFilter = enableXssFilter;
		this.enableSqlFilter = enableSqlFilter;
	}

	/**
	 * 过滤json数据
	 * @return
	 * @throws IOException
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {

		//非json类型，直接返回
		if (!CONTENT_TYPE_LIST.contains(super.getHeader(CONTENT_TYPE))) {
			return super.getInputStream();
		}

		//为空，直接返回
		String json = StreamUtils.copyToString(super.getInputStream(), StandardCharsets.UTF_8);
		if (StringUtils.isEmpty(json)) {
			return super.getInputStream();
		}

		ByteArrayInputStream bis;
		try {
			// 防火墙过滤
			JSONObject jsonObject = JSONObject.parseObject(json);
			Set<String> keys = jsonObject.keySet();
			for (String key : keys) {
				jsonObject.put(key,
						filterParamString(String.valueOf(jsonObject.get(key)))
				);
			}
			json = jsonObject.toJSONString();
		}catch (WafException e){
			throw e;
		}catch (Exception e){
			log.error(e.getMessage(),e);
		}finally {
			bis = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
		}

		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return true;
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
			}

			@Override
			public int read() throws IOException {
				return bis.read();
			}
		};
	}


	@Override
	public String getParameter(String name) {
		String value = super.getParameter(filterParamString(name));
		if (!StringUtils.isEmpty(value) && !TokenConstants.ACCESS_TOKEN.equals(name)) {
			// 防火墙过滤
			value = filterParamString(value);
		}
		return value;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] parameters = super.getParameterValues(name);
		if (parameters == null || parameters.length == 0) {
			return null;
		}

		for (int i = 0; i < parameters.length; i++) {
			if (!TokenConstants.ACCESS_TOKEN.equals(name)) {
				if(parameters[i] != null){
					// 防火墙过滤
					parameters[i] = filterParamString(parameters[i]);
				}
			}
		}
		return parameters;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> map = new LinkedHashMap<>();
		Map<String, String[]> parameters = super.getParameterMap();
		for (String key : parameters.keySet()) {
			String[] values = parameters.get(key);
			if (!TokenConstants.ACCESS_TOKEN.equals(key)) {
				for (int i = 0; i < values.length; i++) {
					if(values[i] != null){
						// 防火墙过滤
						values[i] = filterParamString(values[i]);
					}
				}
			}
			map.put(key, values);
		}
		return map;
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(filterParamString(name));
		if (!StringUtils.isEmpty(value) && !TokenConstants.ACCESS_TOKEN.equals(name)) {
			// 防火墙过滤
			value = filterParamString(value);
		}
		return value;
	}


	/**
	 * 获取最原始的request
	 */
	public HttpServletRequest getOrgRequest() {
		return orgRequest;
	}

	/**
	 * 获取最原始的request
	 */
	public static HttpServletRequest getOrgRequest(HttpServletRequest request) {
		if (request instanceof WafHttpServletRequestWrapper) {
			return ((WafHttpServletRequestWrapper) request).getOrgRequest();
		}
		return request;
	}

	/**
	 * @Description 过滤字符串内容
	 * @param rawValue
	 * @return
	 */
	protected String filterParamString(String rawValue) {
		if (StringUtils.isEmpty(rawValue)) {
			return rawValue;
		}
		String tmpStr = rawValue;
		if (this.enableXssFilter) {
			tmpStr = XSSFilterKit.stripXSS(rawValue);
		}
		if (this.enableSqlFilter) {
			tmpStr = XSSFilterKit.stripXSS(
					SQLFilterKit.stripSQL(tmpStr));
		}
		return tmpStr;
	}
}
