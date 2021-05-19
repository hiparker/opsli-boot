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
package org.opsli.core.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * 参数验证 - 消息
 *
 * @author Parker
 * @date 2020-09-13 19:36
 */
public enum ValidationMsg implements BaseMsg {

    /** 不能为空 */
    EXCEPTION_IS_NOT_NULL(9800,"不能为空! "),
    /** 字母，数字和下划线 */
    EXCEPTION_IS_GENERAL(9801,"不是字母，数字或下划线! "),
    /** 数字 */
    EXCEPTION_IS_NUMBER(9802,"不是数字! "),
    /** 小数浮点 */
    EXCEPTION_IS_DECIMAL(9803,"不是小数浮点! "),
    /** 纯字母 */
    EXCEPTION_IS_LETTER(9804,"不是纯字母! "),
    /** 大写 */
    EXCEPTION_IS_UPPER_CASE(9805,"不是大写字母! "),
    /** 小写 */
    EXCEPTION_IS_LOWER_CASE(9806,"不是小写字母! "),
    /** ip4 */
    EXCEPTION_IS_IPV4(9807,"不是IPV4地址! "),
    /** 金额 */
    EXCEPTION_IS_MONEY(9808,"不是金额! "),
    /** 邮箱 */
    EXCEPTION_IS_EMAIL(9809,"不是邮箱! "),
    /** 手机号 */
    EXCEPTION_IS_MOBILE(98010,"不是手机号! "),
    /** 18位身份证 */
    EXCEPTION_IS_CITIZENID(9811,"不是18位身份证! "),
    /** 邮编 */
    EXCEPTION_IS_ZIPCODE(9812,"不是邮编! "),
    /** URL */
    EXCEPTION_IS_URL(9813,"不是URL! "),
    /** 汉字 */
    EXCEPTION_IS_CHINESE(9814,"不是汉字! "),
    /** 汉字，字母，数字和下划线 */
    EXCEPTION_IS_GENERAL_WITH_CHINESE(9815,"不是汉字，字母，数字和下划线! "),
    /** MAC地址 */
    EXCEPTION_IS_MAC(9816,"不是MAC地址! "),
    /** 中国车牌 */
    EXCEPTION_IS_PLATE_NUMBER(9817,"不是中国车牌! "),

    /** 超出最大长度 */
    EXCEPTION_IS_MAX(9818,"超出最大长度! "),
    /** 小于最小长度 */
    EXCEPTION_IS_MIN(9819,"小于最小长度! "),


    ;

    private static final String PREFIX = "参数验证错误: ";
    private final int code;
    private final String message;
    private String fieldName;

    ValidationMsg(int code, String message){
        this.code = code;
        this.message = message;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return PREFIX + this.fieldName + "，" + this.message;
    }
}
