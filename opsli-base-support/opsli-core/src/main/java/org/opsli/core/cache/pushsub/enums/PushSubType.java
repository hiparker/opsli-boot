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
 * @Description: 发布订阅类型
 */
public enum PushSubType {

    /** 字典类型 */
    DICT,

    /** 用户数据 */
    USER,

    /** 菜单数据 */
    MENU,

    /** 组织数据 */
    ORG,

    /** 租户 */
    TENANT,

    /** 系统数据 */
    OPTION,

    /** 热点数据 */
    HOT_DATA,

    ;


}
