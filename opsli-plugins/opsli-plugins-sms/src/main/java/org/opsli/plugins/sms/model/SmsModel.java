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
package org.opsli.plugins.sms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 短信消息
 *
 * @author 周鹏程
 * @date 2021/8/25 14:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/** access_key */
	private String accessKey;

	/** access_key_secret */
	private String accessKeySecret;

	/** 手机号集合 */
	List<String> tels;

	/** 模板编号 */
	String templateCode;

	/** 模板参数 */
	Map<String, String> templateParam;

	/** 签名 可空默认为 公司名 */
	String signName;

}
