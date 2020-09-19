package org.opsli.api.base.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Web统一返回参数
 *
 * @date 2020年5月15日10:40:54
 * @author Parker
 * 
 */
@ApiModel(value="视图层返回Api对象",
		description="视图层返回Api对象  success:成功状态  code:编号  msg:信息  datatime:时间戳")
public class ResultVo extends HashMap<String,Object> implements Serializable {
 
 
	public ResultVo(){
		this.put("success", true);
		this.put("code", HttpStatus.OK.value());
		this.put("msg", "操作成功");
		this.put("datatime", System.currentTimeMillis());
	}

	/** get/set
	 * 	msg : 信息
	 * 	code : 编码
	 * 	success : 是否成功状态
	 * */
	public String getMsg() {
		return (String)this.get("msg");
	}
 
	public void setMsg(String msg) {//向json中添加属性，在js中访问，请调用data.msg
		this.put("msg", msg);
	}

	public int getCode() {
		return (int)this.get("code");
	}

	public void setCode(int code) {
		this.put("code", code);
	}

	public boolean isSuccess() {
		return (boolean)this.get("success");
	}
 
	public void setSuccess(boolean success) {
		this.put("success", success);
	}

	// -------------------------------------------

	@JsonIgnore//返回对象时忽略此属性
	public static ResultVo success(String msg) {
		ResultVo j = new ResultVo();
		j.setMsg(msg);
		return j;
	}
	@JsonIgnore//返回对象时忽略此属性
	public static ResultVo error(String msg) {
		ResultVo j = new ResultVo();
		j.setSuccess(false);
		j.setMsg(msg);
		return j;
	}

	@JsonIgnore//返回对象时忽略此属性
	public static ResultVo success(Map<String, Object> map) {
		ResultVo restResponse = new ResultVo();
		restResponse.putAll(map);
		return restResponse;
	}

	@JsonIgnore//返回对象时忽略此属性
	public static ResultVo success() {
		return new ResultVo();
	}
 
 
	@Override
	public ResultVo put(String key, Object value) {
		super.put(key, value);
		return this;
	}
 
	public ResultVo putMap(Map m) {
		super.putAll(m);
		return this;
	}

}