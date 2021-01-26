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
package org.opsli.core.filters.aspect;

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
    public void insertHandler(Object ret){
        try {
            if(ret == null){
                throw new ServiceException(CoreMsg.SQL_EXCEPTION_INSERT);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            log.error(e.getMessage(),e);
        }
    }

    @AfterReturning(returning = "ret", pointcut = "update()")
    public void updateHandler(Object ret){
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
    public void deleteHandler(Object ret){
        try {
            if(ret != null){
                Boolean retFlag = (Boolean) ret;
                if(!retFlag){
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
