package org.opsli.common.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 统一返回参数
 *
 * @date 2020年5月15日10:40:54
 * @author Parker
 *
 * 在 Feign 的调用过程中，无法直接序列化数据
 *
 * 所以要加上 @JsonProperty ，否者返回则为一个null
 *
 */
public class ResultDto<T> implements Serializable {

	/** Map 容器 */
	private final Map<String,Object> resultMap = new HashMap<>(3);

	/** 数据 */
	@JsonProperty("data")
	private T data;


	public ResultDto(){
		resultMap.put("success", true);
		resultMap.put("code", HttpStatus.OK.value());
		resultMap.put("msg", "操作成功");
	}

	/**
	 * 获得编号
	 * @return
	 */
	public int getCode() {
		return (int)resultMap.get("code");
	}

	/**
	 * 设置编号
	 * @param code
	 */
	public void setCode(int code) {
		resultMap.put("code", code);
	}

	/**
	 * 获得信息
	 * @return
	 */
	public String getMsg() {
		return (String)resultMap.get("msg");
	}

	/**
	 * 设置信息
	 * @param msg
	 */
	public void setMsg(String msg) {//向json中添加属性，在js中访问，请调用data.msg
		resultMap.put("msg", msg);
	}

	/**
	 * 获得状态
	 * @return
	 */
	public boolean isSuccess() {
		return (boolean)resultMap.get("success");
	}

	/**
	 * 设置状态
	 * @param success
	 */
	public void setSuccess(boolean success) {
		resultMap.put("success", success);
	}
	

	// ---------------------------------

	/**
	 * 设置值
	 * @param value
	 * @return
	 */
	public ResultDto<T> put(T value) {
		data = value;
		return this;
	}

	/**
	 * 获得值
	 * @return
	 */
	public T get(){
		return data;
	}

}