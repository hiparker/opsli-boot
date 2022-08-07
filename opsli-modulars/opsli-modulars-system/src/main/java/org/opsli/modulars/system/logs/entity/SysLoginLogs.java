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
package org.opsli.modulars.system.logs.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * 登录日志信息
 *
 * @author Parker
 * @date 2022年3月18日17:45:18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysLoginLogs extends BaseEntity {

    /** 多租户字段 */
    private String tenantId;
    /**
     * 组织机构ID组 xxx,xxx
     */
    private String orgIds;

    /** 用户名称 */
    private String username;

    /** 真实姓名 */
    private String realName;

    /**
     * 日志类型
     * 1 : 登录
     * 2 : 登出
     */
    private String type;

    /** 操作IP地址 */
    private String remoteAddr;

    /** 用户代理 */
    private String userAgent;

    /** 登陆来源 */
    private String loginFrom;

}
