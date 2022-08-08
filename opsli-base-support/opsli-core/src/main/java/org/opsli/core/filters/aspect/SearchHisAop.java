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

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.opsli.common.annotation.SearchHis;
import org.opsli.core.utils.SearchHisUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static org.opsli.common.constants.OrderConstants.SEARCH_HIS_AOP_SORT;

/**
 * 搜索历史 AOP
 *
 * @author Parker
 * @date 2020-09-16
 */
@Slf4j
@Order(SEARCH_HIS_AOP_SORT)
@Aspect
@Component
public class SearchHisAop {


    @Pointcut("@annotation(org.opsli.common.annotation.SearchHis)")
    public void requestMapping() {
    }

    /**
     * 限流
     * @param point point
     */
    @Before("requestMapping()")
    public void limiterHandle(JoinPoint point){
        try {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            if(sra != null) {
                HttpServletRequest request = sra.getRequest();
                SearchHis searchHis = method.getAnnotation(SearchHis.class);
                if(searchHis != null){
                    String[] keys = searchHis.keys();

                    // 存入缓存
                    SearchHisUtil.putSearchHis(request, Convert.toList(String.class, keys));
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

}
