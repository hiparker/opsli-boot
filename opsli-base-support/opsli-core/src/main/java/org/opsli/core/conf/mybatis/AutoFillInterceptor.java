package org.opsli.core.conf.mybatis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
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
 */
@Component
@Slf4j
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class AutoFillInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws IllegalAccessException, InvocationTargetException {
        fillField(invocation);
        return invocation.proceed();
    }

    /**
     * 注入字段
     * @param invocation
     */
    private void fillField(Invocation invocation) {
        Object[] args = invocation.getArgs();
        SqlCommandType sqlCommandType = null;
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            String className = arg.getClass().getName();
            log.info(i + " 参数类型：" + className);
            //第一个参数处理。根据它判断是否给“操作属性”赋值。
            if (arg instanceof MappedStatement) {//如果是第一个参数 MappedStatement
                MappedStatement ms = (MappedStatement) arg;
                sqlCommandType = ms.getSqlCommandType();
                log.info("操作类型：" + sqlCommandType);
                if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {//如果是“增加”或“更新”操作，则继续进行默认操作信息赋值。否则，则退出
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
     * @param arg
     */
    public void insertFill(Object arg) {
        Field[] fields = ReflectUtil.getFields(arg.getClass());
        for (Field f : fields) {
            f.setAccessible(true);
            switch (f.getName()) {
                // 创建人
                case MyBatisConstants.FIELD_CREATE_BY:
                    setProperty(arg, MyBatisConstants.FIELD_CREATE_BY, "测试");
                    break;
                // 创建日期
                case MyBatisConstants.FIELD_CREATE_TIME:
                    setProperty(arg, MyBatisConstants.FIELD_CREATE_TIME, new Date());
                    break;
                // 更新人
                case MyBatisConstants.FIELD_UPDATE_BY:
                    setProperty(arg, MyBatisConstants.FIELD_UPDATE_BY, "测试");
                    break;
                // 更新日期
                case MyBatisConstants.FIELD_UPDATE_TIME:
                    setProperty(arg, MyBatisConstants.FIELD_UPDATE_TIME, new Date());
                    break;
                // 乐观锁
                case MyBatisConstants.FIELD_OPTIMISTIC_LOCK:
                    setProperty(arg, MyBatisConstants.FIELD_OPTIMISTIC_LOCK, 0);
                    break;
                // 逻辑删除
                case MyBatisConstants.FIELD_DELETE_LOGIC:
                    setProperty(arg, MyBatisConstants.FIELD_DELETE_LOGIC,  MyBatisConstants.LOGIC_NOT_DELETE_VALUE);
                    break;
                // 多租户设置
                case MyBatisConstants.FIELD_TENANT:
                    setProperty(arg, MyBatisConstants.FIELD_TENANT,  UserUtil.getTenantId());
                    break;
                default:
                    break;
            }
            f.setAccessible(false);
        }
    }

    /**
     * 修改数据
     * @param arg
     */
    public void updateFill(Object arg) {
        // 2020-09-19
        // 修改这儿 有可能会拿到一个 MapperMethod，需要特殊处理
        Field[] fields;
        if (arg instanceof MapperMethod.ParamMap) {
            MapperMethod.ParamMap<?> paramMap = (MapperMethod.ParamMap<?>) arg;
            if (paramMap.containsKey("et")) {
                arg = paramMap.get("et");
            } else {
                arg = paramMap.get("param1");
            }
            if (arg == null) {
                return;
            }
            fields = ReflectUtil.getFields(arg.getClass());
        } else {
            fields = ReflectUtil.getFields(arg.getClass());
        }
        for (Field f : fields) {
            f.setAccessible(true);
            switch (f.getName()) {
                // 更新人
                case MyBatisConstants.FIELD_UPDATE_BY:
                    setProperty(arg, MyBatisConstants.FIELD_UPDATE_BY, "测试修改");
                    break;
                // 更新日期
                case MyBatisConstants.FIELD_UPDATE_TIME:
                    setProperty(arg, MyBatisConstants.FIELD_UPDATE_TIME, new Date());
                    break;
                default:
                    break;
            }
            f.setAccessible(false);
        }
    }

    // =======================================

    /**
     * 为对象的操作属性赋值
     *
     * @param bean
     */
    private void setProperty(Object bean, String name, Object value) {
        //根据需要，将相关属性赋上默认值
        BeanUtil.setProperty(bean, name, value);
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}