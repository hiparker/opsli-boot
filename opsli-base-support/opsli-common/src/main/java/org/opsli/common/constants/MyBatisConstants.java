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
 * MyBatis 常量
 *
 * @author Parker
 * @date 2020-09-16 17:42
 */
public final class MyBatisConstants {


    /** 逻辑删除值 */
    public static final String  LOGIC_DELETE_VALUE = "1";
    /** 逻辑不删除值 */
    public static final String  LOGIC_NOT_DELETE_VALUE = "0";


    /** ID */
    public static final String FIELD_ID = "id";
    /** PID */
    public static final String FIELD_PARENT_ID = "parentId";
    /** PIDs */
    public static final String FIELD_PARENT_IDS = "parentIds";
    /** 创建人 */
    public static final String FIELD_CREATE_BY = "createBy";
    /** 更新时间 */
    public static final String FIELD_CREATE_TIME = "createTime";
    /** 更新人 */
    public static final String FIELD_UPDATE_BY = "updateBy";
    /** 更新时间 */
    public static final String FIELD_UPDATE_TIME = "updateTime";
    /** 逻辑删除 */
    public static final String FIELD_DELETE_LOGIC = "deleted";
    /** 乐观锁 */
    public static final String FIELD_OPTIMISTIC_LOCK = "version";
    /** 多租户字段 */
    public static final String FIELD_TENANT = "tenantId";
    /** 组织字段 */
    public static final String FIELD_ORG_GROUP = "orgIds";

    private MyBatisConstants(){}
}
