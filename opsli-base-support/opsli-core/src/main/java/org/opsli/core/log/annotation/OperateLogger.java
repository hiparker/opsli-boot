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
package org.opsli.core.log.annotation;


import org.opsli.core.log.enums.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 日志注解
 *
 * @author Parker
 * @date 2021年7月15日20:28:24
 */
@Target({ElementType.METHOD}) //注解作用于方法级别
@Retention(RetentionPolicy.RUNTIME) //运行时起作用
public @interface OperateLogger {

    /**
     * 是否输出日志
     */
    boolean loggable() default true;

    /**
     * 日志描述
     * 可使用占位符获取参数: ${tel}
     */
    String description() default "";

    /**
     * 模块名称
     */
    ModuleEnum module() default ModuleEnum.MODULE_COMMON;

    /**
     * 操作类型(enum):主要是 select,insert,update,delete
     */
    OperationTypeEnum operationType() default OperationTypeEnum.UNKNOWN;

    /**
     * 日志请求类型
     */
    LogTypeEnum type() default LogTypeEnum.BACKEND;

    /**
     * 日志级别
     */
    LogLevelEnum level() default LogLevelEnum.INFO;

    /**
     * 日志输出范围, 用于标记需要记录的日志信息范围，包含入参、返回值等
     * ALL-入参和出参, BEFORE-入参, AFTER-出参
     */
    LogScopeEnum scope() default LogScopeEnum.ALL;

    /**
     * 是否存入数据库
     */
    boolean db() default false;

    /**
     * 是否输出到控制台
     */
    boolean console() default true;

}
