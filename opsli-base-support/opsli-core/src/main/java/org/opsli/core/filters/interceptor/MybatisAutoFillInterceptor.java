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
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.core.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
 * @author Parker
 * @date 2020-03-01
 */
@Component
@Slf4j
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class MybatisAutoFillInterceptor implements Interceptor {

    private static final String ET = "et";

    /** 实体类字段 */
    static private final Map<Class<?>, Field[]> ENTITY_FIELD_MAP = new HashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws IllegalAccessException, InvocationTargetException {
        fillField(invocation);
        return invocation.proceed();
    }

    /**
     * 注入字段
     * @param invocation 源
     */
    private void fillField(Invocation invocation) {
        Object[] args = invocation.getArgs();
        SqlCommandType sqlCommandType = null;
        for (Object arg : args) {
            //第一个参数处理。根据它判断是否给“操作属性”赋值。
            //如果是第一个参数 MappedStatement
            if (arg instanceof MappedStatement) {
                MappedStatement ms = (MappedStatement) arg;
                sqlCommandType = ms.getSqlCommandType();
                //如果是“增加”或“更新”操作，则继续进行默认操作信息赋值。否则，则退出
                if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
                    continue;
                } else {
                    break;
                }
            }

            if (sqlCommandType == SqlCommandType.INSERT) {
                // 新增
                this.insertFill(arg);

            } else if (sqlCommandType == SqlCommandType.UPDATE) {
                // 修改
                this.updateFill(arg);
            }
        }
    }

    /**
     * 新增数据
     * @param arg 参数
     */
    public void insertFill(Object arg) {
        if(arg == null ){
            return;
        }

        // 排除字段
        List<String> existField = Lists.newArrayList();

        // 当前时间
        Date currDate = DateUtil.date();

        // 字段缓存 减少每次更新 反射
        Field[] fields = ENTITY_FIELD_MAP.get(arg.getClass());
        if(fields == null){
            fields = ReflectUtil.getFields(arg.getClass());
            ENTITY_FIELD_MAP.put(arg.getClass(), fields);
        }

        for (Field f : fields) {
            // 判断是否是排除字段
            if(existField.contains(f.getName())){
                continue;
            }

            // 如果设置为忽略字段 则直接跳过不处理
            TableField tableField = f.getAnnotation(TableField.class);
            if(tableField != null){
                boolean exist = tableField.exist();
                if(!exist){
                    existField.add(f.getName());
                    continue;
                }
            }

            switch (f.getName()) {
                // 创建人
                case MyBatisConstants.FIELD_CREATE_BY:
                    // 如果创建人 为空则进行默认赋值
                    Object createValue = ReflectUtil.getFieldValue(arg, f.getName());
                    if(StringUtils.isBlank(Convert.toStr(createValue))){
                        BeanUtil.setProperty(arg, MyBatisConstants.FIELD_CREATE_BY, UserUtil.getUser().getId());
                    }
                    break;
                // 更新人
                case MyBatisConstants.FIELD_UPDATE_BY:
                    // 如果更新人 为空则进行默认赋值
                    Object updateValue = ReflectUtil.getFieldValue(arg, f.getName());
                    if(StringUtils.isBlank(Convert.toStr(updateValue))){
                        BeanUtil.setProperty(arg, MyBatisConstants.FIELD_UPDATE_BY, UserUtil.getUser().getId());
                    }
                    break;
                // 创建日期
                case MyBatisConstants.FIELD_CREATE_TIME:
                    BeanUtil.setProperty(arg, MyBatisConstants.FIELD_CREATE_TIME, currDate);
                    break;
                // 更新日期
                case MyBatisConstants.FIELD_UPDATE_TIME:
                    BeanUtil.setProperty(arg, MyBatisConstants.FIELD_UPDATE_TIME, currDate);
                    break;
                // 乐观锁
                case MyBatisConstants.FIELD_OPTIMISTIC_LOCK:
                    BeanUtil.setProperty(arg, MyBatisConstants.FIELD_OPTIMISTIC_LOCK, 0);
                    break;
                // 逻辑删除
                case MyBatisConstants.FIELD_DELETE_LOGIC:
                    BeanUtil.setProperty(arg, MyBatisConstants.FIELD_DELETE_LOGIC,  MyBatisConstants.LOGIC_NOT_DELETE_VALUE);
                    break;
                // 多租户设置
                case MyBatisConstants.FIELD_TENANT:
                    // 2020-12-05 修复当前租户可能为空字符串报错问题
                    // 如果租户ID 为空则进行默认赋值
                    Object tenantValue = ReflectUtil.getFieldValue(arg, f.getName());
                    if(StringUtils.isBlank(Convert.toStr(tenantValue))){
                        BeanUtil.setProperty(arg, MyBatisConstants.FIELD_TENANT,  UserUtil.getTenantId());
                    }
                    break;
                // 组织机构设置
                case MyBatisConstants.FIELD_ORG_GROUP:
                    // 如果组织IDs 为空则进行默认赋值
                    Object orgValue = ReflectUtil.getFieldValue(arg, f.getName());
                    if(StringUtils.isBlank(Convert.toStr(orgValue))){
                        UserOrgRefModel userOrgRefModel =
                                UserUtil.getUserDefOrgByUserId(UserUtil.getUser().getId());
                        if(null != userOrgRefModel){
                            String orgIds = userOrgRefModel.getOrgIds();
                            BeanUtil.setProperty(arg, MyBatisConstants.FIELD_ORG_GROUP, orgIds);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 修改数据
     * @param arg 参数
     */
    public void updateFill(Object arg) {
        if(arg == null ){
            return;
        }

        // 排除字段
        List<String> existField = Lists.newArrayList();

        // 2020-09-19
        // 修改这儿 有可能会拿到一个 MapperMethod，需要特殊处理
        if (arg instanceof MapperMethod.ParamMap) {
            MapperMethod.ParamMap<?> paramMap = (MapperMethod.ParamMap<?>) arg;
            if (paramMap.containsKey(ET)) {
                arg = paramMap.get(ET);
            } else {
                arg = paramMap.get("param1");
            }
            if (arg == null) {
                return;
            }
        }

        // 字段缓存 减少每次更新 反射
        Field[] fields = ENTITY_FIELD_MAP.get(arg.getClass());
        if(fields == null){
            fields = ReflectUtil.getFields(arg.getClass());
            ENTITY_FIELD_MAP.put(arg.getClass(), fields);
        }

        for (Field f : fields) {
            // 判断是否是排除字段
            if(existField.contains(f.getName())){
                continue;
            }

            // 如果设置为忽略字段 则直接跳过不处理
            TableField tableField = f.getAnnotation(TableField.class);
            if(tableField != null){
                boolean exist = tableField.exist();
                if(!exist){
                    existField.add(f.getName());
                    continue;
                }
            }

            switch (f.getName()) {
                // 更新人
                case MyBatisConstants.FIELD_UPDATE_BY:
                    // 如果更新人 为空则进行默认赋值
                    Object updateValue = ReflectUtil.getFieldValue(arg, f.getName());
                    if(StringUtils.isBlank(Convert.toStr(updateValue))){
                        BeanUtil.setProperty(arg, MyBatisConstants.FIELD_UPDATE_BY, UserUtil.getUser().getId());
                    }
                    break;
                // 更新日期
                case MyBatisConstants.FIELD_UPDATE_TIME:
                    BeanUtil.setProperty(arg, MyBatisConstants.FIELD_UPDATE_TIME, DateUtil.date());
                    break;
                default:
                    break;
            }
        }
    }

    // =======================================

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
