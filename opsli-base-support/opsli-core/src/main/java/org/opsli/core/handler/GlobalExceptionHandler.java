package org.opsli.core.handler;

import cn.hutool.core.text.StrFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.opsli.api.base.result.ResultVo;
import org.opsli.common.exception.EmptyException;
import org.opsli.common.exception.JwtException;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.exception.TokenException;
import org.opsli.core.msg.CoreMsg;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import static org.opsli.common.constants.OrderConstants.EXCEPTION_HANDLER_ORDER;

/**
 * @author parker
 * @date 2020-09-13
 */
@Slf4j
@RestControllerAdvice
@Order(EXCEPTION_HANDLER_ORDER)
public class GlobalExceptionHandler {

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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultVo<?> bussinessException(ServiceException e) {
        log.warn("业务异常 - 异常编号：{} - 异常信息：{}",e.getCode(),e.getMessage());
        return ResultVo.error(e.getCode(), e.getMessage());
    }

    /**
     * 拦截 自定义 空异常
     */
    @ExceptionHandler(EmptyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultVo<?> emptyException(EmptyException e) {
        return ResultVo.error(e.getCode(), e.getMessage());
    }

    /**
     * 拦截 自定义 Jwt 认证异常
     */
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultVo<?> jwtException(JwtException e) {
        return ResultVo.error(e.getCode(), e.getMessage());
    }

    /**
     * 拦截 自定义 Shiro 认证异常
     */
    @ExceptionHandler(IncorrectCredentialsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultVo<?> incorrectCredentialsException(IncorrectCredentialsException e) {
        // token失效，请重新登录
        return ResultVo.error(e.getMessage());
    }

    /**
     * 拦截 自定义 Shiro 认证异常
     */
    @ExceptionHandler(LockedAccountException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultVo<?> lockedAccountException(LockedAccountException e) {
        // 账号已被锁定,请联系管理员
        return ResultVo.error(e.getMessage());
    }

    /**
     * 拦截 自定义 Token 认证异常
     */
    @ExceptionHandler(TokenException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultVo<?> tokenException(LockedAccountException e) {
        // Token 异常
        return ResultVo.error(e.getMessage());
    }

    // ============================

    /**
     * 拦截 系统空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultVo<?> nullPointerException(EmptyException e) {
        return ResultVo.error(e.getCode(), e.getMessage());
    }


    /**
     * 拦截 数据库主键冲突
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultVo<?> sqlIntegrityConstraintViolationException(EmptyException e) {
        log.error("数据异常：{}",e.getMessage(),e);
        return ResultVo.error(e.getCode(), CoreMsg.SQL_EXCEPTION_INTEGRITY_CONSTRAINT_VIOLATION.getMessage());
    }

    /**
     * 拦截 数据库异常
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultVo<?> sqlException(SQLException e) {
        //log.error("数据异常：{}",e.getMessage(),e);
        // 默认值异常
        if(StringUtils.contains(e.getMessage(),"doesn't have a default value")){
            String Field = e.getMessage().replaceAll("Field '","")
                    .replaceAll("' doesn't have a default value","");
            String msg = StrFormatter.format(CoreMsg.SQL_EXCEPTION_NOT_HAVE_DEFAULT_VALUE.getMessage(), Field);
            return ResultVo.error(CoreMsg.SQL_EXCEPTION_NOT_HAVE_DEFAULT_VALUE.getCode(), msg);
        }
        String msg = StrFormatter.format(CoreMsg.SQL_EXCEPTION_UNKNOWN.getMessage(), e.getMessage());
        return ResultVo.error(CoreMsg.SQL_EXCEPTION_UNKNOWN.getCode(), msg);
    }

}