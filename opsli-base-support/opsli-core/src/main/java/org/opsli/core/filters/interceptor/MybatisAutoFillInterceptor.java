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
package org.opsli.core.filters.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.core.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * MyBatis 拦截器 注入属性用
 *
 * PS：Plus中的自动注入器太难用了
 *
 * -- 多租户设置 当前方案选择的是按照表加 租户字段
 *      如果租户数量过大 可考虑按照 业务分库 再选择横纵拆表 然后再按照表中租户分区 可以缓解一下数量问题
 *    多租户要考虑数据隔离级别 这里选择的是 按照分页进行隔离，毕竟对于客户来讲，只能看到分页的数据
 *      也就是说 要控制再 findList层
 *      自定义查询SQL的话 一定要注意 ， 如果有租户设置 一定要加上多租户查询
 *
 * 参考地址：https://www.cnblogs.com/qingshan-tang/p/13299701.html
 *
 * 2025年06月01日13:15:17 提供性能优化
 *
 * @author Pace
 * @date 2020-03-01
 */
@Component
@Slf4j
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class MybatisAutoFillInterceptor implements Interceptor {
    /** 使用 Caffeine 缓存提供更好的性能 */
    private static final Cache<Class<?>, FieldProcessor> PROCESSOR_CACHE = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(Duration.ofHours(1))
            .build();

    /** 线程本地缓存，避免重复计算 */
    private static final ThreadLocal<ProcessorContext> CONTEXT_CACHE =
            ThreadLocal.withInitial(ProcessorContext::new);

    @Override
    public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        ProcessorContext context = CONTEXT_CACHE.get();
        try {
            context.reset();
            processInvocation(invocation, context);
            return invocation.proceed();
        } finally {
            context.reset();
        }
    }

    /**
     * 处理调用
     */
    private void processInvocation(Invocation invocation, ProcessorContext context) {
        Object[] args = invocation.getArgs();

        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];

        if (parameter == null) {
            return;
        }

        SqlCommandType commandType = ms.getSqlCommandType();
        if (commandType != SqlCommandType.INSERT && commandType != SqlCommandType.UPDATE) {
            return;
        }

        // 提取真实参数
        Object realParam = extractRealParameter(parameter);
        if (realParam == null) {
            return;
        }

        // 获取或创建字段处理器
        FieldProcessor processor = getFieldProcessor(realParam.getClass());

        // 执行字段处理
        context.setOperationType(commandType);
        processor.process(realParam, context);
    }

    /**
     * 提取真实参数
     */
    private Object extractRealParameter(Object parameter) {
        if (parameter instanceof MapperMethod.ParamMap) {
            MapperMethod.ParamMap<?> paramMap = (MapperMethod.ParamMap<?>) parameter;
            return paramMap.containsKey("et") ? paramMap.get("et") : paramMap.get("param1");
        }
        return parameter;
    }

    /**
     * 获取字段处理器
     */
    private FieldProcessor getFieldProcessor(Class<?> clazz) {
        return PROCESSOR_CACHE.get(clazz, this::createFieldProcessor);
    }

    /**
     * 创建字段处理器
     */
    private FieldProcessor createFieldProcessor(Class<?> clazz) {
        return new ReflectionFieldProcessor(clazz);
    }

    /**
     * 处理器上下文
     */
    private static class ProcessorContext {
        private SqlCommandType operationType;
        private Date currentDate;
        private String userId;
        private String tenantId;
        private UserOrgRefModel userOrgRef;
        private boolean userInfoLoaded = false;

        void reset() {
            operationType = null;
            currentDate = null;
            userId = null;
            tenantId = null;
            userOrgRef = null;
            userInfoLoaded = false;
        }

        void setOperationType(SqlCommandType type) {
            this.operationType = type;
            this.currentDate = DateUtil.date();
        }

        boolean isInsert() {
            return operationType == SqlCommandType.INSERT;
        }

        boolean isUpdate() {
            return operationType == SqlCommandType.UPDATE;
        }

        Date getCurrentDate() {
            return currentDate;
        }

        synchronized void loadUserInfo() {
            if (!userInfoLoaded) {
                try {
                    var user = UserUtil.getUser();
                    userId = user.getId();
                    tenantId = UserUtil.getTenantId();
                    userOrgRef = UserUtil.getUserDefOrgByUserId(userId);
                } catch (Exception e) {
                    userId = "";
                    tenantId = "";
                    userOrgRef = null;
                }
                userInfoLoaded = true;
            }
        }

        String getUserId() {
            loadUserInfo();
            return userId;
        }

        String getTenantId() {
            loadUserInfo();
            return tenantId;
        }

        UserOrgRefModel getUserOrgRef() {
            loadUserInfo();
            return userOrgRef;
        }
    }

    /**
     * 字段处理器接口
     */
    private interface FieldProcessor {
        void process(Object target, ProcessorContext context);
    }

    /**
     * 基于反射的字段处理器
     */
    private static class ReflectionFieldProcessor implements FieldProcessor {
        private final FieldHandler[] handlers;

        public ReflectionFieldProcessor(Class<?> clazz) {
            this.handlers = buildHandlers(clazz);
        }

        @Override
        public void process(Object target, ProcessorContext context) {
            for (FieldHandler handler : handlers) {
                handler.handle(target, context);
            }
        }

        private FieldHandler[] buildHandlers(Class<?> clazz) {
            Field[] fields = ReflectUtil.getFields(clazz);
            List<FieldHandler> handlerList = new ArrayList<>();

            for (Field field : fields) {
                // 跳过排除字段
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null && !tableField.exist()) {
                    continue;
                }

                FieldHandler handler = createFieldHandler(field);
                if (handler != null) {
                    handlerList.add(handler);
                }
            }

            return handlerList.toArray(new FieldHandler[0]);
        }

        private FieldHandler createFieldHandler(Field field) {
            String fieldName = field.getName();
            return switch (fieldName) {
                case MyBatisConstants.FIELD_CREATE_BY -> new CreateByFieldHandler(field);
                case MyBatisConstants.FIELD_UPDATE_BY -> new UpdateByFieldHandler(field);
                case MyBatisConstants.FIELD_CREATE_TIME -> new CreateTimeFieldHandler(field);
                case MyBatisConstants.FIELD_UPDATE_TIME -> new UpdateTimeFieldHandler(field);
                case MyBatisConstants.FIELD_OPTIMISTIC_LOCK -> new OptimisticLockFieldHandler(field);
                case MyBatisConstants.FIELD_DELETE_LOGIC -> new DeleteLogicFieldHandler(field);
                case MyBatisConstants.FIELD_TENANT -> new TenantFieldHandler(field);
                case MyBatisConstants.FIELD_ORG_GROUP -> new OrgGroupFieldHandler(field);
                default -> null;
            };
        }
    }

    /**
     * 字段处理器基类
     */
    private abstract static class FieldHandler {
        protected final Field field;

        protected FieldHandler(Field field) {
            this.field = field;
        }

        abstract void handle(Object target, ProcessorContext context);

        protected void setFieldValue(Object target, Object value) {
            try {
                BeanUtil.setProperty(target, field.getName(), value);
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("设置字段 {} 值失败: {}", field.getName(), e.getMessage());
                }
            }
        }

        protected Object getFieldValue(Object target) {
            return ReflectUtil.getFieldValue(target, field.getName());
        }

        protected boolean isBlank(Object value) {
            return StringUtils.isBlank(Convert.toStr(value));
        }
    }

    // 具体的字段处理器实现
    private static class CreateByFieldHandler extends FieldHandler {
        CreateByFieldHandler(Field field) { super(field); }

        @Override
        void handle(Object target, ProcessorContext context) {
            if (context.isInsert() && isBlank(getFieldValue(target))) {
                String userId = context.getUserId();
                if (StringUtils.isNotBlank(userId)) {
                    setFieldValue(target, userId);
                }
            }
        }
    }

    private static class UpdateByFieldHandler extends FieldHandler {
        UpdateByFieldHandler(Field field) { super(field); }

        @Override
        void handle(Object target, ProcessorContext context) {
            if (isBlank(getFieldValue(target))) {
                String userId = context.getUserId();
                if (StringUtils.isNotBlank(userId)) {
                    setFieldValue(target, userId);
                }
            }
        }
    }

    private static class CreateTimeFieldHandler extends FieldHandler {
        CreateTimeFieldHandler(Field field) { super(field); }

        @Override
        void handle(Object target, ProcessorContext context) {
            if (context.isInsert()) {
                setFieldValue(target, context.getCurrentDate());
            }
        }
    }

    private static class UpdateTimeFieldHandler extends FieldHandler {
        UpdateTimeFieldHandler(Field field) { super(field); }

        @Override
        void handle(Object target, ProcessorContext context) {
            setFieldValue(target, context.getCurrentDate());
        }
    }

    private static class OptimisticLockFieldHandler extends FieldHandler {
        OptimisticLockFieldHandler(Field field) { super(field); }

        @Override
        void handle(Object target, ProcessorContext context) {
            if (context.isInsert()) {
                setFieldValue(target, DictType.NO_YES_NO.getValue());
            }
        }
    }

    private static class DeleteLogicFieldHandler extends FieldHandler {
        DeleteLogicFieldHandler(Field field) { super(field); }

        @Override
        void handle(Object target, ProcessorContext context) {
            if (context.isInsert()) {
                setFieldValue(target, MyBatisConstants.LOGIC_NOT_DELETE_VALUE);
            }
        }
    }

    private static class TenantFieldHandler extends FieldHandler {
        TenantFieldHandler(Field field) { super(field); }

        @Override
        void handle(Object target, ProcessorContext context) {
            if (context.isInsert() && isBlank(getFieldValue(target))) {
                String tenantId = context.getTenantId();
                if (StringUtils.isNotBlank(tenantId)) {
                    setFieldValue(target, tenantId);
                }
            }
        }
    }

    private static class OrgGroupFieldHandler extends FieldHandler {
        OrgGroupFieldHandler(Field field) { super(field); }

        @Override
        void handle(Object target, ProcessorContext context) {
            if (context.isInsert() && isBlank(getFieldValue(target))) {
                UserOrgRefModel userOrgRef = context.getUserOrgRef();
                if (userOrgRef != null && StringUtils.isNotBlank(userOrgRef.getOrgIds())) {
                    setFieldValue(target, userOrgRef.getOrgIds());
                }
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 配置参数
    }
}



