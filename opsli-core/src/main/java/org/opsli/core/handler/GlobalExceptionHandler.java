package org.opsli.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.opsli.common.api.ResultVo;
import org.opsli.common.exception.EmptyException;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.msg.CoreMsg;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLIntegrityConstraintViolationException;

import static org.opsli.common.constants.OrderConstants.EXCEPTION_HANDLER_ORDER;

/**
 * @author parker
 * @date 2020-09-13
 */
@Slf4j
@ControllerAdvice
@Order(EXCEPTION_HANDLER_ORDER)
public class GlobalExceptionHandler {

    /**
     * 拦截 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultVo bussinessException(ServiceException e) {
        log.warn("业务异常 - 异常编号：{} - 异常信息：{}",e.getCode(),e.getMessage());
        ResultVo errorR = ResultVo.error(e.getMessage());
        errorR.setCode(e.getCode());
        return errorR;
    }

    /**
     * 拦截 自定义 空异常
     */
    @ExceptionHandler(EmptyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultVo emptyException(EmptyException e) {
        ResultVo errorR = ResultVo.error(e.getMessage());
        errorR.setCode(e.getCode());
        return errorR;
    }

    /**
     * 拦截 系统空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultVo nullPointerException(EmptyException e) {
        ResultVo errorR = ResultVo.error(e.getMessage());
        errorR.setCode(e.getCode());
        return errorR;
    }

    /**
     * 拦截 数据库主键冲突
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultVo sqlIntegrityConstraintViolationException(EmptyException e) {
        log.error("数据异常：{}",e.getMessage(),e);
        ResultVo errorR = ResultVo.error(CoreMsg.MySQL_EXCEPTION_SQL_INTEGRITY_CONSTRAINT_VIOLATION.getMessage());
        errorR.setCode(e.getCode());
        return errorR;
    }



}