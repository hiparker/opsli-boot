package org.opsli.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.opsli.common.api.ResultVo;
import org.opsli.common.exception.EmptyException;
import org.opsli.common.exception.ServiceException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author parker
 * @date 2020-09-13
 */
@Slf4j
@ControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {

    /**
     * 拦截业务异常
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
     * 拦截空异常
     */
    @ExceptionHandler(EmptyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResultVo bussinessException(EmptyException e) {
        ResultVo errorR = ResultVo.error(e.getMessage());
        errorR.setCode(e.getCode());
        return errorR;
    }

}