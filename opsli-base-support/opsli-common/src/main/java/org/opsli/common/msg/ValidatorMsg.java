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
package org.opsli.common.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * 参数验证 - 消息
 *
 * @author Parker
 * @date 2020-09-13 19:36
 */
public enum ValidatorMsg implements BaseMsg {

    /** 不能为空 */
    EXCEPTION_IS_NOT_NULL(9800,"不能为空! "),
    /** 字母，数字和下划线 */
    EXCEPTION_IS_GENERAL(9801,"不是字母，数字或下划线! "),
    /** 数字 */
    EXCEPTION_IS_INTEGER(9802,"不是整数! "),
    /** 小数浮点 */
    EXCEPTION_IS_DECIMAL(9803,"不是小数浮点! "),
    /** 质数 */
    EXCEPTION_IS_PRIMES(9804,"不是质数! "),
    /** 纯字母 */
    EXCEPTION_IS_LETTER(9805,"不是纯字母! "),
    /** 大写 */
    EXCEPTION_IS_UPPER_CASE(9806,"不是大写字母! "),
    /** 小写 */
    EXCEPTION_IS_LOWER_CASE(9807,"不是小写字母! "),
    /** IP */
    EXCEPTION_IS_IP(9808,"不是IP地址! "),
    /** IP4 */
    EXCEPTION_IS_IPV4(9809,"不是IPV4地址! "),
    /** IP6 */
    EXCEPTION_IS_IPV6(98010,"不是IPV6地址! "),
    /** 金额 */
    EXCEPTION_IS_MONEY(9811,"不是金额! "),
    /** 邮箱 */
    EXCEPTION_IS_EMAIL(9812,"不是邮箱! "),
    /** 手机号 */
    EXCEPTION_IS_MOBILE(9813,"不是手机号! "),
    /** 18位身份证 */
    EXCEPTION_IS_CITIZENID(9814,"不是18位身份证! "),
    /** 汉字 */
    EXCEPTION_IS_CHINESE(9815,"不是汉字! "),
    /** 汉字，字母，数字和下划线 */
    EXCEPTION_IS_GENERAL_WITH_CHINESE(9816,"不是汉字，字母，数字和下划线! "),
    /** 邮编 */
    EXCEPTION_IS_ZIPCODE(9817,"不是邮编! "),
    /** URL */
    EXCEPTION_IS_URL(9818,"不是URL! "),
    /** MAC地址 */
    EXCEPTION_IS_MAC(9819,"不是MAC地址! "),
    /** 中国车牌 */
    EXCEPTION_IS_PLATE_NUMBER(9820,"不是中国车牌! "),
    /** 安全密码 */
    EXCEPTION_IS_SECURITY_PASSWORD(9821,"至少包含大小写字母、数字、特殊字符，且不少于6位！"),


    /** 超出最大长度 */
    EXCEPTION_IS_MAX(9830,"超出最大长度! "),
    /** 小于最小长度 */
    EXCEPTION_IS_MIN(9831,"小于最小长度! "),


    ;

    private static final String PREFIX = "参数验证错误: ";
    private final int code;
    private final String message;
    private String fieldName;

    ValidatorMsg(int code, String message){
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
