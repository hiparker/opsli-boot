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
package org.opsli.core.log.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 操作日志
 *
 * @author Parker
 * @date 2021年7月15日20:28:24
 */
@Data
@ToString
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private String id;

    /** 创建时间 */
    private long createTime;

    /** 日志等级 */
    private String level;

    /** 被操作的系统模块 */
    private String moduleId;

    /** 方法名 */
    private String method;

    /** 参数 */
    private String args;

    /** 操作人id */
    private String userId;

    /** 操作人用户名 */
    private String username;

    /** 真实姓名 */
    private String realName;

    /** 日志描述 */
    private String description;

    /** 操作类型 */
    private String operationType;

    /** 方法运行时间 */
    private Long runTime;

    /** 方法返回值 */
    private String returnValue;

    /** 租户ID */
    private String tenantId;

    /**
     * 日志请求类型
     */
    private String logType;

}
