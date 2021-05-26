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
package org.opsli.common.enums;


/**
 * 通过单例 模式 生成系统唯一标示
 *
 * @author Parker
 * @date 2020-09-17 23:40
 */
public enum ValidatorType {

    /** 不能为空 */
    IS_NOT_NULL,
    /** 整数 */
    IS_INTEGER,
    /** 小数浮点 */
    IS_DECIMAL,
    /** 质数（素数） */
    IS_PRIMES,
    /** 纯字母 */
    IS_LETTER,
    /** 大写 */
    IS_UPPER_CASE,
    /** 小写 */
    IS_LOWER_CASE,
    /** IP (兼容IPV4 + IPV6) */
    IS_IP,
    /** IP4 */
    IS_IPV4,
    /** IP6 */
    IS_IPV6,
    /** 金额 */
    IS_MONEY,
    /** 邮箱 */
    IS_EMAIL,
    /** 手机号 */
    IS_MOBILE,
    /** 18位身份证 */
    IS_CITIZENID,
    /** 汉字 */
    IS_CHINESE,
    /** 字母，数字和下划线 */
    IS_GENERAL,
    /** 汉字，字母，数字和下划线 */
    IS_GENERAL_WITH_CHINESE,
    /** 邮编 */
    IS_ZIPCODE,
    /** URL */
    IS_URL,
    /** MAC地址 */
    IS_MAC,
    /** 中国车牌 */
    IS_PLATE_NUMBER,
    /** 安全密码 */
    IS_SECURITY_PASSWORD,

    ;
    public static void main(String[] args) {
        /*
        为空判断
            isNull
        字母，数字和下划线
            isGeneral
        数字
            isNumber
        纯字母
            isLetter
        大小写
            isUpperCase
            isLowerCase
        ip4
            isIpv4
        金额
            isMoney
        邮件
            isEmail
        手机号码
            isMobile
        18位身份证
            isCitizenId
        邮编
            isZipCode
        URL
            isUrl
        汉字
            isChinese
        汉字，字母，数字和下划线
            isGeneralWithChinese
        mac地址
            isMac
        中国车牌
            isPlateNumber
        uuid
            isUUID
        *
        *
        *
        *
        *
        * */
    }
}
