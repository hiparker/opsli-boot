package org.opsli.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.msg.CoreMsg;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.opsli.common.constants.OrderConstants.SQL_ORDER;

/**
 * SQL数据 拦截处理
 *
 * @author parker
 * @date 2020-09-16
 */
@Slf4j
@Order(SQL_ORDER)
@Aspect
@Component
public class SQLDataAop {

    @Pointcut("execution(public * org.opsli.core.base.service.impl.CrudServiceImpl.insert(..))")
    public void insert() {
    }

    @Pointcut("execution(public * org.opsli.core.base.service.impl.CrudServiceImpl.update*(..))")
    public void update() {
    }

    @Pointcut("execution(public * org.opsli.core.base.service.impl.CrudServiceImpl.delete*(..))")
    public void delete() {
    }

    @AfterReturning(returning = "ret", pointcut = "insert()")
    public void insertHadnler(Object ret){
        try {
            if(ret == null){
                throw new ServiceException(CoreMsg.SQL_EXCEPTION_UPDATE);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            log.error(e.getMessage(),e);
        }
    }

    @AfterReturning(returning = "ret", pointcut = "update()")
    public void updateHadnler(Object ret){
        try {
            if(ret == null){
                throw new ServiceException(CoreMsg.SQL_EXCEPTION_UPDATE);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            log.error(e.getMessage(),e);
        }
    }

    @AfterReturning(returning = "ret", pointcut = "delete()")
    public void deleteHadnler(Object ret){
        try {
            if(ret != null){
                Integer retCount = (Integer) ret;
                if(retCount == 0){
                    throw new ServiceException(CoreMsg.SQL_EXCEPTION_DELETE);
                }
            }else{
                throw new ServiceException(CoreMsg.SQL_EXCEPTION_DELETE);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            log.error(e.getMessage(),e);
        }
    }

}
