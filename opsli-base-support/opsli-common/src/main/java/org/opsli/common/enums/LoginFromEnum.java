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


import cn.hutool.core.bean.BeanUtil;

import java.util.Arrays;

/**
 * 登录类型
 *
 * @author 周鹏程
 * @date 2020-09-17 23:40
 */
public enum LoginFromEnum {

    /** 账号 */
    PC("0"),
    /** APP - 安卓 */
    APP_ANDROID("1"),
    /** APP - 苹果 */
    APP_IOS("2"),
    /** 微信小程序 */
    WX_APPLET("3"),
    /** h5 */
    H5("4"),
    /** 未知 */
    UNKNOWN("-1"),

    ;

    private final static String FILED_NAME = "loginFrom";
    /***/
    private final String type;

    LoginFromEnum(String type){
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static LoginFromEnum getByCode(String code){
        return Arrays.stream(LoginFromEnum.values())
                .filter(value -> value.getType().equals(code))
                .findFirst().orElse(LoginFromEnum.UNKNOWN);
    }

    public static LoginFromEnum getByBean(Object bean){
        if(null == bean || !BeanUtil.isBean(bean.getClass())){
            return LoginFromEnum.UNKNOWN;
        }

        Object fieldValue = BeanUtil.getFieldValue(bean, FILED_NAME);
        if(null == fieldValue){
            return LoginFromEnum.UNKNOWN;
        }

        return getByCode((String) fieldValue);
    }

}
