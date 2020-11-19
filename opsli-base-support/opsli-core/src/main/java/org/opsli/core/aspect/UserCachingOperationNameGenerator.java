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
package org.opsli.core.aspect;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * 替换组件相同名称方法 切面
 */
@Slf4j
@Component
@Aspect
public class UserCachingOperationNameGenerator {


  private final Map<String, Integer> generated = Maps.newHashMap();


  @Pointcut("execution(* springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator.startingWith(String))")
  public void c() {
  }


  @Around("c()")
  public Object a(ProceedingJoinPoint point) {
    Object[] args = point.getArgs();
    return startingWith(String.valueOf(args[0]));
  }

  private String startingWith(String prefix) {
    if (generated.containsKey(prefix)) {
      generated.put(prefix, generated.get(prefix) + 1);
      String nextUniqueOperationName = String.format("%s_%s", prefix, generated.get(prefix));
      //log.warn("组件中存在相同的方法名称，自动生成组件方法唯一名称进行替换: {}", nextUniqueOperationName);
      return nextUniqueOperationName;
    } else {
      generated.put(prefix, 0);
      return prefix;
    }
  }
}


