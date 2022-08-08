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
package org.opsli.common.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Web统一返回参数
 *
 * @author Parker
 * @date 2020-09-22 17:07
 */
public class ResultVoMap extends HashMap<String,Object> implements Serializable {


	public ResultVoMap(){
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

	/**
	 * 返回对象时忽略此属性
	 */
	@JsonIgnore
	public static ResultVoMap success(String msg) {
		ResultVoMap j = new ResultVoMap();
		j.setMsg(msg);
		return j;
	}

	/**
	 * 返回对象时忽略此属性
	 */
	@JsonIgnore//
	public static ResultVoMap error(String msg) {
		ResultVoMap j = new ResultVoMap();
		j.setSuccess(false);
		j.setMsg(msg);
		return j;
	}

	/**
	 * 返回对象时忽略此属性
	 */
	@JsonIgnore
	public static ResultVoMap success(Map<String, Object> map) {
		ResultVoMap restResponse = new ResultVoMap();
		restResponse.putAll(map);
		return restResponse;
	}

	/**
	 * 返回对象时忽略此属性
	 */
	@JsonIgnore
	public static ResultVoMap success() {
		return new ResultVoMap();
	}


	@Override
	public ResultVoMap put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public ResultVoMap putMap(Map m) {
		super.putAll(m);
		return this;
	}

}
