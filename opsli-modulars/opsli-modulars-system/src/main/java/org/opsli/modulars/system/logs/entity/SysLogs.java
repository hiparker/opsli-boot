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

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 日志表
 *
 * @author Parker
 * @date 2020-11-28 18:59:59
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysLogs extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 多租户字段
     */
    private String tenantId;
    /**
     * 组织机构ID组 xxx,xxx
     */
    private String orgIds;

    /** 日志类型（1：接入日志；2：错误日志） */
    private String type;

    /** 日志标题 */
    private String title;

    /** 操作用户的IP地址 */
    private String remoteAddr;

    /** 操作用户代理信息 */
    private String userAgent;

    /** 执行时间 */
    private Long timeout;

    /** 操作的URI */
    private String requestUri;

    /** 操作的方式 */
    private String method;

    /** 操作提交的数据 */
    private String params;

    /** 异常信息 */
    private String exception;

}
