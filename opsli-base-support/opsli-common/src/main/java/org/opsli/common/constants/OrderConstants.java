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
 * Order
 * @author parker
 * @date 2020-09-16
 */
public interface OrderConstants {

    /** Util 加载顺序 */
    int UTIL_ORDER = 140;

    /** 限流器 */
    int LIMITER_AOP_SORT = 149;

    /** token */
    int TOKEN_AOP_SORT = 150;

    /** 热点数据加载顺序 */
    int HOT_DATA_ORDER = 180;

    /** 参数非法验证顺序 */
    int PARAM_VALIDATE_AOP_SORT = 185;

    /** 搜索历史 */
    int SEARCH_HIS_AOP_SORT = 186;

    /** SQL 切面执行顺序 */
    int SQL_ORDER = 190;

    /** 参数非法验证顺序 */
    int LOG_ORDER = 200;

    /** Controller异常拦截顺序 */
    int EXCEPTION_HANDLER_ORDER = 260;

}
