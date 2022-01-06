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
package org.opsli.common.constants;

/**
 * Order 排序
 *
 * @author Parker
 * @date 2020-09-16 17:42
 */
public final class OrderConstants {

    /** Util 加载顺序 */
    public static final int UTIL_ORDER = 140;

    /** 限流器 */
    public static final int LIMITER_AOP_SORT = 149;

    /** token */
    public static final int TOKEN_AOP_SORT = 150;

    /** 请求加解密 */
    public static final int ENCRYPT_ADN_DECRYPT_AOP_SORT = 160;

    /** 热点数据加载顺序 */
    public static final int HOT_DATA_ORDER = 180;

    /** 参数非法验证顺序 */
    public static final int VERIFY_ARGS_AOP_SORT = 185;

    /** 搜索历史 */
    public static final int SEARCH_HIS_AOP_SORT = 186;

    /** SQL 切面执行顺序 */
    public static final int SQL_ORDER = 190;

    /** 参数非法验证顺序 */
    public static final int LOG_ORDER = 200;

    /** Controller异常拦截顺序 */
    public static final int EXCEPTION_HANDLER_ORDER = 260;

    private OrderConstants(){}

}
