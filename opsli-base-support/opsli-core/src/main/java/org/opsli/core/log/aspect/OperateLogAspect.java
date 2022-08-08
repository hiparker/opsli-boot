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
package org.opsli.core.log.aspect;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.opsli.common.utils.MessUtil;
import org.opsli.core.base.dto.LoginUserDto;
import org.opsli.core.holder.UserContextHolder;
import org.opsli.core.log.annotation.OperateLogger;
import org.opsli.core.log.bean.OperationLog;
import org.opsli.core.log.bus.OperationLogEventBus;
import org.opsli.core.log.enums.LogLevelEnum;
import org.opsli.core.log.enums.LogScopeEnum;
import org.opsli.core.utils.UserTokenUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * 操作日志拦截器
 *
 * @author Parker
 * @date 2021年7月15日20:28:24
 */
@Slf4j
@Aspect
@Component
@Order(50)
public class OperateLogAspect {

    /** 替换条件 */
    private static final String RE = "\\$\\{([\\w\\.\\-\\/\\+\\$\\#\\@\\!\\^\\&\\(\\)]+)\\}";

    @Resource
    private OperationLogEventBus operationLogEventBus;

    @Pointcut("@annotation(org.opsli.core.log.annotation.OperateLogger)")
    public void operationLog(){}

    /**
     * 环绕增强，相当于MethodInterceptor
     */
    @Around("operationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        long time = System.currentTimeMillis();
        try {
            result =  joinPoint.proceed();
            return result;
        } finally {
            time = System.currentTimeMillis() - time;
            try {
                // 方法执行完成后增加日志
                addOperationLog(joinPoint, result, time);
            }catch (Exception e){
                log.error("LogAspect 操作失败：{}" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加日志
     * @param joinPoint point
     * @param result 结果
     * @param time 时间
     */
    private void addOperationLog(JoinPoint joinPoint, Object result, long time){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        OperateLogger annotation = signature.getMethod()
                .getAnnotation(OperateLogger.class);
        if(annotation == null || !annotation.loggable()){
            return;
        }

        OperationLog operationLog = new OperationLog();

        // 参数
        String argsStr = null;
        try {
            argsStr = JSONUtil.toJsonStr(joinPoint.getArgs());
        }catch (Exception ignored){}
        // 结果
        String resultStr = null;
        try {
            resultStr = JSONUtil.toJsonStr(result);
        }catch (Exception ignored){}

        if(LogScopeEnum.REQUEST.equals(annotation.scope())){
            operationLog.setArgs(argsStr);
        } else if(LogScopeEnum.RESPONSE.equals(annotation.scope())){
            operationLog.setReturnValue(resultStr);
        } else if(LogScopeEnum.ALL.equals(annotation.scope())){
            operationLog.setArgs(argsStr);
            operationLog.setReturnValue(resultStr);
        }

        operationLog.setId(UUID.randomUUID().toString());
        operationLog.setRunTime(time);
        operationLog.setCreateTime(System.currentTimeMillis());
        operationLog.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());

        operationLog.setLevel(annotation.level().getValue());
        operationLog.setDescription(
                // 对当前登录用户和占位符处理
                getDetail(
                        ((MethodSignature) joinPoint.getSignature()).getParameterNames(),
                        joinPoint.getArgs(),
                        annotation)
        );
        operationLog.setOperationType(annotation.operationType().getValue());
        operationLog.setLogType(annotation.type().getValue());
        operationLog.setModuleId(annotation.module().getId());

        // 当前Token
        Optional<String> tokenOptional = UserContextHolder.getToken();
        if(tokenOptional.isPresent()){
            LoginUserDto loginUserDto = UserTokenUtil.getLoginUserDto(tokenOptional.get()).orElse(null);
            if(null != loginUserDto){
                operationLog.setUserId(String.valueOf(loginUserDto.getUid()));
                operationLog.setUsername(loginUserDto.getUsername());
                operationLog.setRealName(loginUserDto.getNickname());
                operationLog.setTenantId(loginUserDto.getTenantId());
            }
        }

        // 输出控制台
        if(annotation.console()){
            printToConsole(annotation.level(), operationLog);
        }
        // 存入数据库
        if(annotation.db()){
            operationLogEventBus.post(operationLog);
        }
    }

    /**
     * 输出到控制台
     * @param level 等级
     * @param operationLog 日志
     */
    private void printToConsole(LogLevelEnum level, OperationLog operationLog){
        switch (level){
            case INFO:
                log.info("记录日志:{}" , operationLog);
                break;
            case DEBUG:
                log.debug("记录日志:{}" , operationLog);
                break;
            case WARN:
                log.warn("记录日志:{}" , operationLog);
                break;
            case ERROR:
                log.error("记录日志:{}" , operationLog);
                break;
            case TRACE:
                log.trace("记录日志:{}" , operationLog);
                break;
            default:
                break;
        }
    }

    /**
     * 对当前登录用户和占位符处理
     * @param argNames 方法参数名称数组
     * @param args 方法参数数组
     * @param annotation 注解信息
     * @return 返回处理后的描述
     */
    private String getDetail(String[] argNames, Object[] args, OperateLogger annotation){
        Map<String, Object> map = Maps.newHashMap();
        for(int i = 0;i < argNames.length;i++){
            map.put(argNames[i], args[i]);
        }

        String description = annotation.description();
        try {
            // 当前用户信息
            Optional<String> tokenOptional = UserContextHolder.getToken();
            if(tokenOptional.isPresent()){
                LoginUserDto loginUserDto = UserTokenUtil.getLoginUserDto(tokenOptional.get()).orElse(null);
                if(null != loginUserDto){
                    description = "'" + loginUserDto.getNickname() + "'=> " + annotation.description();
                }
            }
            description = MessUtil.getMes(description, RE, "${", "}", map);
        }catch (Exception e){
            e.printStackTrace();
        }
        return description;
    }

}
