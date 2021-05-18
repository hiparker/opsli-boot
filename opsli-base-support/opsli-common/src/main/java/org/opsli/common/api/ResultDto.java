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

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 统一返回参数
 *
 * 在 Feign 的调用过程中，无法直接序列化数据
 *
 * 所以要加上 @JsonProperty ，否者返回则为一个null
 *
 * @author Parker
 * @date 2020-09-22 17:07
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
