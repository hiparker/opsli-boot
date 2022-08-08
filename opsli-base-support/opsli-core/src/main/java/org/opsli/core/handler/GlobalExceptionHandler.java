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
package org.opsli.core.handler;

import cn.hutool.core.text.StrFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.common.exception.*;
import org.opsli.core.msg.CoreMsg;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import static org.opsli.common.constants.OrderConstants.EXCEPTION_HANDLER_ORDER;

/**
 * 遗产拦截器
 *
 * @author parker
 * @date 2020-09-13
 */
@Slf4j
@RestControllerAdvice
@Order(EXCEPTION_HANDLER_ORDER)
public class GlobalExceptionHandler {

    private static final String SQL_EXCEPTION = "doesn't have a default value";

    /*
    算数异常类：ArithmeticException
    空指针异常类型：NullPointerException
    类型强制转换类型：ClassCastException
    数组负下标异常：NegativeArrayException
    数组下标越界异常：ArrayIndexOutOfBoundsException
    违背安全原则异常：SecturityException
    文件已结束异常：EOFException
    文件未找到异常：FileNotFoundException
    字符串转换为数字异常：NumberFormatException
    操作数据库异常：SQLException
    输入输出异常：IOException
    方法未找到异常：NoSuchMethodException
    下标越界异常：IndexOutOfBoundsExecption
    系统异常：SystemException
    创建一个大小为负数的数组错误异常：NegativeArraySizeException
    数据格式异常：NumberFormatException
    安全异常：SecurityException
    不支持的操作异常：UnsupportedOperationException
    网络操作在主线程异常：NetworkOnMainThreadException
    请求状态异常： IllegalStateException （extends RuntimeException ， 父类：IllegalComponentStateException 在不合理或不正确时间内唤醒一方法时出现的异常信息。换句话说，即 Java 环境或 Java 应用不满足请求操作）
    网络请求异常：HttpHostConnectException
    子线程Thread更新UI view 异常：ViewRootImpl$CalledFromWrongThreadException
    证书不匹配的主机名异常： SSLExceptionero
    反射Method.invoke(obj, args...)方法抛出异常：InvocationTargetException
    EventBus使用异常：EventBusException
    非法参数异常：IllegalArgumentException
    参数不能小于0异常：ZeroException
    */



    /**
     * 拦截 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultWrapper<?> businessException(ServiceException e) {
        log.warn("业务异常 - 异常编号：{} - 异常信息：{}",e.getCode(),e.getMessage());
        return ResultWrapper.getCustomResultWrapper(e.getCode(), e.getMessage());
    }

    /**
     * 拦截 自定义 空异常
     */
    @ExceptionHandler(EmptyException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultWrapper<?> emptyException(EmptyException e) {
        return ResultWrapper.getCustomResultWrapper(e.getCode(), e.getMessage());
    }

    /**
     * 拦截 自定义 Jwt 认证异常
     */
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultWrapper<?> jwtException(JwtException e) {
        return ResultWrapper.getCustomResultWrapper(e.getCode(), e.getMessage());
    }

    /**
     * 拦截 自定义 Token 认证异常
     */
    @ExceptionHandler(TokenException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultWrapper<?> tokenException(TokenException e) {
        // Token 异常
        return ResultWrapper.getCustomResultWrapper(e.getCode(), e.getMessage());
    }

    /**
     * 拦截 自定义 防火墙
     */
    @ExceptionHandler(WafException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResultWrapper<?> wafException(WafException e) {
        return ResultWrapper.getCustomResultWrapper(e.getCode(), e.getMessage());
    }

    // ============================

    /**
     * 拦截 系统空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultWrapper<?> nullPointerException(NullPointerException e) {
        log.error("空指针异常：{}",e.getMessage(),e);
        return ResultWrapper.getErrorResultWrapper();
    }


    /**
     * 拦截 数据库主键冲突
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultWrapper<?> sqlIntegrityConstraintViolationException(EmptyException e) {
        log.error("数据异常：{}",e.getMessage(),e);
        return ResultWrapper.getCustomResultWrapper(e.getCode(), CoreMsg.SQL_EXCEPTION_INTEGRITY_CONSTRAINT_VIOLATION.getMessage());
    }

    /**
     * 拦截 数据库异常
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultWrapper<?> sqlException(SQLException e) {
        //log.error("数据异常：{}",e.getMessage(),e);
        // 默认值异常
        if(StringUtils.contains(e.getMessage(),SQL_EXCEPTION)){
            String field = e.getMessage().replaceAll("Field '","")
                    .replaceAll("' doesn't have a default value","");
            String msg = StrFormatter.format(CoreMsg.SQL_EXCEPTION_NOT_HAVE_DEFAULT_VALUE.getMessage(), field);
            return ResultWrapper.getCustomResultWrapper(CoreMsg.SQL_EXCEPTION_NOT_HAVE_DEFAULT_VALUE.getCode(), msg);
        }
        String msg = StrFormatter.format(CoreMsg.SQL_EXCEPTION_UNKNOWN.getMessage(), e.getMessage());
        return ResultWrapper.getCustomResultWrapper(CoreMsg.SQL_EXCEPTION_UNKNOWN.getCode(), msg);
    }

}
