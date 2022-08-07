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
package org.opsli.plugins.sms;

import lombok.extern.slf4j.Slf4j;
import org.opsli.plugins.sms.service.SmsService;
import org.opsli.plugins.sms.enums.SmsType;
import org.opsli.plugins.sms.exceptions.SmsException;
import org.opsli.plugins.sms.model.SmsModel;
import org.opsli.plugins.sms.msg.SmsMsgCodeEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 短信工厂
 *
 * @author 周鹏程
 * @date 2021/8/27 14:09
 */
@Slf4j
public final class SmsFactory {

	/** 执行器集合 */
	private final static Map<SmsType, SmsService> HANDLE_MAP = new ConcurrentHashMap<>();

	static void put(SmsService smsService){
		if(null == smsService){
			return;
		}
		// 加入集合
		HANDLE_MAP.put(smsService.getType(), smsService);
	}

	/**
	 * 发送消息
	 * 注：需要做好异常处理
	 * @param smsModel 短信model
	 */
	public static void sendSms(SmsType type, SmsModel smsModel){
		SmsService smsService = HANDLE_MAP.get(type);
		if(smsService == null){
			throw new SmsException(SmsMsgCodeEnum.CODE_ERROR_SMS_HANDLER);
		}
		smsService.sendSms(smsModel);
	}

}
