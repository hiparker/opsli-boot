package org.opsli.api.base.result;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * API 统一返回参数
 *
 * @date 2020年5月15日10:40:54
 * @author Parker
 *
 * 在 Feign 的调用过程中，无法直接序列化数据
 *
 * 所以要加上 泛型对象 @JsonProperty ，否者返回则为一个null
 *
 */
@Data
@ApiModel(value="视图层返回Api对象",
		description="视图层返回Api对象  success:成功状态  code:编号  msg:信息  timestamp:时间戳  data:数据")
public class ResultVo<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 默认成功信息 */
	public static final String DEF_SUCCESS_MSG = "操作成功！";
	/** 默认失败信息 */
	public static final String DEF_ERROR_MSG = "操作失败！";

	/** 成功状态 */
	@ApiModelProperty(value = "成功状态")
	@JsonProperty("success")
	private boolean success;

	/** 消息 */
	@ApiModelProperty(value = "消息")
	@JsonProperty("msg")
	private String msg;

	/** 状态码 */
	@ApiModelProperty(value = "状态码")
	@JsonProperty("code")
	private Integer code;

	/** 时间戳 */
	@ApiModelProperty(value = "时间戳")
	@JsonProperty("timestamp")
	private Long timestamp;

	/** 数据对象 */
	@ApiModelProperty(value = "数据")
	@JsonProperty("data")
	private T data;

	public T getData() {
		return data;
	}

	public ResultVo<T> setData(T data) {
		this.data = data;
		return this;
	}

	/**
	 * 转化成Json字符串
	 * @return String
	 */
	public String toJsonStr(){
		return JSONObject.toJSONString(this);
	}

	// ===========================================

	/**
	 * 构造函数
	 */
	public ResultVo() {
		// 初始化值
		this.success = true;
		this.msg = DEF_SUCCESS_MSG;
		this.code = HttpStatus.OK.value();
		this.timestamp = System.currentTimeMillis();
	}

	// ================================== 静态方法 ===================================

	/**
	 * 返回成功状态
	 * @return ResultVo<Object>
	 */
	@JsonIgnore
	public static ResultVo<Object> success() {
		return new ResultVo<>();
	}

	/**
	 * 返回成功状态
	 * @param msg 返回信息
	 * @return ResultVo<Object>
	 */
	@JsonIgnore
	public static ResultVo<Object> success(String msg) {
		ResultVo<Object> ret = new ResultVo<>();
		ret.setMsg(msg);
		return ret;
	}

	/**
	 * 返回成功状态
	 * @param data 返回数据
	 * @param <T> 泛型
	 * @return ResultVo<T>
	 */
	@JsonIgnore
	public static <T> ResultVo<T> success(T data) {
		ResultVo<T> ret = new ResultVo<>();
		ret.setData(data);
		return ret;
	}

	/**
	 * 返回成功状态
	 * @param msg 返回信息
	 * @param data 返回数据
	 * @param <T> 泛型
	 * @return ResultVo<T>
	 */
	@JsonIgnore
	public static <T> ResultVo<T> success(String msg, T data) {
		ResultVo<T> ret = new ResultVo<>();
		ret.setMsg(msg);
		ret.setData(data);
		return ret;
	}

	/**
	 * 返回成功状态
	 * @param msg 返回信息
	 * @param data 返回数据
	 * @param <T> 泛型
	 * @return ResultVo<T>
	 */
	@JsonIgnore
	public static <T> ResultVo<T> success(Integer code, String msg, T data) {
		ResultVo<T> ret = new ResultVo<>();
		ret.setCode(code);
		ret.setMsg(msg);
		ret.setData(data);
		return ret;
	}


	/**
	 * 返回错误状态
	 * @return ResultVo<Object>
	 */
	@JsonIgnore
	public static ResultVo<Object> error() {
		return ResultVo.error(
				HttpStatus.INTERNAL_SERVER_ERROR.value()
				, DEF_ERROR_MSG, null);
	}

	/**
	 * 返回错误状态
	 * @param msg 返回信息
	 * @return ResultVo<Object>
	 */
	@JsonIgnore
	public static ResultVo<Object> error(String msg) {
		return ResultVo.error(
				HttpStatus.INTERNAL_SERVER_ERROR.value()
				, msg, null);
	}

	/**
	 * 返回错误状态
	 * @param code 错误编号
	 * @param msg 返回信息
	 * @return ResultVo<T>
	 */
	@JsonIgnore
	public static ResultVo<Object> error(Integer code, String msg) {
		return ResultVo.error(code, msg, null);
	}

	/**
	 * 返回成功状态
	 * @param code 错误编号
	 * @param data 返回数据
	 * @param <T> 泛型
	 * @return ResultVo<T>
	 */
	@JsonIgnore
	public static <T> ResultVo<T> error(Integer code, String msg, T data) {
		ResultVo<T> ret = new ResultVo<>();
		ret.setMsg(msg);
		ret.setCode(code);
		ret.setData(data);
		ret.setSuccess(false);
		return ret;
	}

}
