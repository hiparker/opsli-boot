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
package org.opsli.core.cache.pushsub.enums;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.enums
 * @Author: Parker
 * @CreateTime: 2020-09-15 16:15
 * @Description: 消息具体类型
 */
public enum MsgArgsType {

    /** 字典模型 */
    DICT_MODEL,
    /** 字典模型-集合 */
    DICT_MODELS,
    /** 字典模型-传输类型 */
    DICT_MODEL_TYPE,
    /** 字典操作类型 */
    DICT_TYPE,

    /** 用户ID */
    USER_ID,
    /** 用户名 */
    USER_USERNAME,
    /** 用户数据类型 */
    USER_MODEL_TYPE,
    /** 用户数据*/
    USER_MODEL_DATA,

    /** 菜单编号 */
    MENU_CODE,
    /** 菜单数据*/
    MENU_MODEL_DATA,

    /** 组织 用户ID */
    ORG_USER_ID,
    /** 组织 用户数据 */
    ORG_USER_DATA,

    /** 租户ID */
    TENANT_ID,
    /** 租户数据 */
    TENANT_DATA,

    /** 缓存数据Key */
    CACHE_DATA_KEY,
    /** 缓存数据Key */
    CACHE_DATA_NAME,
    /** 缓存数据Value */
    CACHE_DATA_VALUE,
    /** 缓存数据Type */
    CACHE_DATA_TYPE,
    ;

}
