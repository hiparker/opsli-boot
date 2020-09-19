package org.opsli.api.base.result;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
		description="视图层返回Api对象  success:成功状态  code:编号  msg:信息  datatime:时间戳")
public class ResultVo<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 成功状态 */
	@ApiModelProperty(value = "成功状态")
	private boolean success;

	/** 消息 */
	@ApiModelProperty(value = "消息")
	private String msg;

	/** 状态码 */
	@ApiModelProperty(value = "状态码")
	private Integer code;

	/** 时间戳 */
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	@ApiModelProperty(value = "时间戳")
	private long timestamp;

	/** 数据对象 */
	@ApiModelProperty(value = "数据")
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	@JsonProperty("data")
	private T data;


	/**
	 * 设置值
	 * @param data 数据
	 * @return ResultVo<T>
	 */
	public ResultVo<T> put(T data) {
		this.data = data;
		return this;
	}

	/**
	 * 获得值
	 * @return T
	 */
	public T get(){
		return this.data;
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
		this.msg = "操作成功！";
		this.code = HttpStatus.OK.value();
		this.timestamp = System.currentTimeMillis();
	}

	// ================================== 静态方法 ===================================

	/**
	 * 返回成功状态
	 * @return ResultVo<Object>
	 */
	@JsonIgnore//返回对象时忽略此属性
	public static ResultVo<Object> success() {
		return new ResultVo<>();
	}

	/**
	 * 返回成功状态
	 * @param msg 返回信息
	 * @return ResultVo<Object>
	 */
	@JsonIgnore//返回对象时忽略此属性
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
	@JsonIgnore//返回对象时忽略此属性
	public static <T> ResultVo<T> success(T data) {
		ResultVo<T> ret = new ResultVo<>();
		ret.put(data);
		return ret;
	}

	/**
	 * 返回成功状态
	 * @param msg 返回信息
	 * @param data 返回数据
	 * @param <T> 泛型
	 * @return ResultVo<T>
	 */
	@JsonIgnore//返回对象时忽略此属性
	public static <T> ResultVo<T> success(String msg, T data) {
		ResultVo<T> ret = new ResultVo<>();
		ret.put(data);
		return ret;
	}


	/**
	 * 返回错误状态
	 * @param msg 返回信息
	 * @return ResultVo<Object>
	 */
	@JsonIgnore//返回对象时忽略此属性
	public static ResultVo<Object> error(String msg) {
		ResultVo<Object> ret = new ResultVo<>();
		ret.setMsg(msg);
		ret.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return ret;
	}

	/**
	 * 返回错误状态
	 * @param code 错误编号
	 * @param msg 返回信息
	 * @return ResultVo<T>
	 */
	@JsonIgnore//返回对象时忽略此属性
	public static ResultVo<Object> error(int code, String msg) {
		ResultVo<Object> ret = new ResultVo<>();
		ret.setMsg(msg);
		ret.setCode(code);
		return ret;
	}

	/**
	 * 返回成功状态
	 * @param code 错误编号
	 * @param data 返回数据
	 * @param <T> 泛型
	 * @return ResultVo<T>
	 */
	@JsonIgnore//返回对象时忽略此属性
	public static <T> ResultVo<T> error(int code, String msg, T data) {
		ResultVo<T> ret = new ResultVo<>();
		ret.setMsg(msg);
		ret.setCode(code);
		ret.put(data);
		return ret;
	}

}