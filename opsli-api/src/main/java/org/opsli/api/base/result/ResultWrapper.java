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
package org.opsli.api.base.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.opsli.common.base.msg.BaseMsg;

import java.io.Serializable;

/**
 * 返回包装类
 *
 * @author Parker
 * @date 2021年12月30日15:31:41
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "基础返回实体",
		description="视图层返回Api对象  code:编号  msg:信息  timestamp:时间戳  data:数据")
public class ResultWrapper<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 响应提示信息
	 */
	@ApiModelProperty(name = "msg", dataType = "string", value = "响应信息")
	private String msg;

	/**
	 * 返回码：0正常，-1以上为错误信息
	 */
	@ApiModelProperty(name = "code", dataType = "int", value = "响应码")
	private int code;

	/**
	 * 返回
	 */
	@ApiModelProperty(name = "data", dataType = "object", value = "数据内容")
	private T data;

	/**
	 * 时间戳
	 */
	@ApiModelProperty(name = "timestamp", dataType = "long", value = "时间戳")
	private long timestamp;



	/**
	 * 获取默认的成功信息,
	 * 默认的成功状态码 {@link StateCodeEnum#SUCCESS}
	 * @param <T> 泛型
	 * @return ResultWrapper<T>
	 */
	public static <T> ResultWrapper<T> getSuccessResultWrapperByMsg(String msg) {
		return ResultWrapper.<T>builder()
				.code(StateCodeEnum.SUCCESS.getCode())
				.msg(msg)
				.timestamp(System.currentTimeMillis())
				.build();
	}

	/**
	 * 获取默认的成功信息,
	 * 默认的成功状态码 {@link StateCodeEnum#SUCCESS}
	 * @param <T> 泛型
	 * @return ResultWrapper<T>
	 */
	public static <T> ResultWrapper<T> getSuccessResultWrapper() {
		return getSuccessResultWrapper(null);
	}


	/**
	 * 获取默认的成功信息,
	 * 默认的成功状态码 {@link StateCodeEnum#SUCCESS}
	 * @param data 数据
	 * @param <T> 泛型
	 * @return ResultWrapper<T>
	 */
	public static <T> ResultWrapper<T> getSuccessResultWrapper(T data) {
		return ResultWrapper.<T>builder()
				.code(StateCodeEnum.SUCCESS.getCode())
				.msg(StateCodeEnum.SUCCESS.getMsg())
				.data(data)
				.timestamp(System.currentTimeMillis())
				.build();
	}

	/**
	 * 获取默认的错误的信息
	 * 默认的失败状态码 {@link StateCodeEnum#ERROR}
	 * @param <T> 泛型
	 * @return ResultWrapper<T>
	 */
	public static <T> ResultWrapper<T> getErrorResultWrapper() {
		return getErrorResultWrapper(null);
	}

	/**
	 * 获取默认的错误的信息
	 * 默认的失败状态码 {@link StateCodeEnum#ERROR}
	 * @param data 数据
	 * @param <T> 泛型
	 * @return ResultWrapper<T>
	 */
	public static <T> ResultWrapper<T> getErrorResultWrapper(T data) {
		return ResultWrapper.<T>builder()
				.code(StateCodeEnum.ERROR.getCode())
				.msg(StateCodeEnum.ERROR.getMsg())
				.data(data)
				.timestamp(System.currentTimeMillis())
				.build();
	}


	/**
	 * 获取约定好的自定义的成功信息，方便前端业务特殊处理
	 * @param stateCode 状态码信息
	 * @param <T> 泛型
	 * @return ResultWrapper<T>
	 */
	public static <T> ResultWrapper<T> getCustomResultWrapper(StateCodeEnum stateCode) {
		return getCustomResultWrapper(null, stateCode);
	}

	/**
	 * 获取约定好的自定义的成功信息，方便前端业务特殊处理
	 * @param data 业务数据
	 * @param stateCode 状态码信息
	 * @param <T> 泛型
	 * @return ResultWrapper<T>
	 */
	public static <T> ResultWrapper<T> getCustomResultWrapper(T data, StateCodeEnum stateCode) {
		return ResultWrapper.<T>builder()
				.code(stateCode.getCode())
				.msg(stateCode.getMsg())
				.data(data)
				.timestamp(System.currentTimeMillis())
				.build();
	}


	/**
	 * 获取约定好的自定义的成功信息，方便前端业务特殊处理
	 * @param baseMsg 状态码信息
	 * @param <T> 泛型
	 * @return ResultWrapper<T>
	 */
	public static <T> ResultWrapper<T> getCustomResultWrapper(BaseMsg baseMsg) {
		return getCustomResultWrapper(null, baseMsg);
	}

	/**
	 * 获取约定好的自定义的成功信息，方便前端业务特殊处理
	 * @param data 业务数据
	 * @param baseMsg 状态码信息
	 * @param <T> 泛型
	 * @return ResultWrapper<T>
	 */
	public static <T> ResultWrapper<T> getCustomResultWrapper(T data, BaseMsg baseMsg) {
		return ResultWrapper.<T>builder()
				.code(baseMsg.getCode())
				.msg(baseMsg.getMessage())
				.data(data)
				.timestamp(System.currentTimeMillis())
				.build();
	}


	/**
	 * 获取约定好的自定义的成功信息，方便前端业务特殊处理
	 * 默认的成功状态码 {@link StateCodeEnum#SUCCESS}
	 * @param data 业务数据
	 * @param code 状态码
	 * @param msg 提示信息
	 * @param <T> 泛型
	 * @return ResultWrapper<T>
	 */
	public static <T> ResultWrapper<T> getCustomResultWrapper(T data, int code, String msg) {
		return ResultWrapper.<T>builder()
				.code(code)
				.msg(msg)
				.data(data)
				.timestamp(System.currentTimeMillis())
				.build();
	}

	/**
	 * 获取约定好的自定义的成功信息，方便前端业务特殊处理
	 * 默认的成功状态码 {@link StateCodeEnum#SUCCESS}
	 * @param code 状态码
	 * @param msg 提示信息
	 * @param <T> 泛型
	 * @return ResultWrapper<T>
	 */
	public static <T> ResultWrapper<T> getCustomResultWrapper(int code, String msg) {
		return ResultWrapper.<T>builder()
				.code(code)
				.msg(msg)
				.data(null)
				.timestamp(System.currentTimeMillis())
				.build();
	}

	/**
	 * 验证返回结果是否成功,
	 * @param resultWrapper 返回包装类
	 * @param <T> 泛型
	 * @return boolean
	 */
	public static <T> boolean isSuccess(ResultWrapper<T> resultWrapper) {
		if(null == resultWrapper){
			return false;
		}

		return StateCodeEnum.SUCCESS.getCode() == resultWrapper.getCode();
	}


	/**
	 * 请求状态枚举
	 *
	 * @author Parker
	 * @date 2021年12月30日15:29:29
	 */
	@Getter
	@AllArgsConstructor
	public enum StateCodeEnum {

		/** 请求状态枚举 */

		SUCCESS(0, "请求成功"),

		FAILURE(-1, "操作失败"),

		ERROR(-1, "服务器异常");

		private final int code;
		private final String msg;
	}

}
