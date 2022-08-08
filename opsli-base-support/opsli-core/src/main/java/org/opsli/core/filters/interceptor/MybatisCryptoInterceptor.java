package org.opsli.core.filters.interceptor;


import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opsli.plugins.crypto.spring.annotation.CryptoMapperField;
import opsli.plugins.crypto.spring.crypto.ICrypto;
import opsli.plugins.crypto.spring.enums.CryptoType;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.opsli.core.autoconfigure.properties.EncryptProperties;
import org.springframework.stereotype.Component;
import java.lang.reflect.*;
import java.time.chrono.ChronoLocalDate;
import java.util.*;

/**
 * Mybatis 拦截器
 * <p>
 * Signature:定义拦截点，只有符合拦截点的条件才会进入到拦截器
 * Signature.type:定义拦截的类 Executor、ParameterHandler、StatementHandler、ResultSetHandler 当中的一个
 * Signature.method:在定义拦截类的基础之上，在定义拦截的方法
 * Signature.args:在定义拦截方法的基础之上在定义拦截的方法对应的参数，JAVA里面方法可能重载，故注意参数的类型和顺序
 *
 * @author Parker
 * @date 2022-08-07
 */
@Slf4j
@AllArgsConstructor
@Component
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        }
)
public class MybatisCryptoInterceptor implements Interceptor {

    private final EncryptProperties encryptProperties;

    /**
     * @param invocation 拦截到对象
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Method method = invocation.getMethod();

        switch (method.getName()) {
            case "update":
                return updateHandle(invocation);
            case "query":
                return selectHandle(invocation);
            default:
                return invocation.proceed();
        }

    }

    /**
     * 查询操作处理
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    private Object selectHandle(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler<Object> resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();

        CacheKey cacheKey;
        BoundSql boundSql;

        //处理参数作为条件查询需要加密
        handleParameterOrResult(parameter, CryptoType.ENCRYPT);

        //由于逻辑关系，只会进入一次
        if (args.length == 4) {
            //4 个参数时
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            //6 个参数时
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }

        List<Object> resultList = executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);

        for (Object o : resultList) {
            handleParameterOrResult(o, CryptoType.DECRYPT);
        }

        return resultList;
    }

    /**
     * 新增修改操作处理
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    private Object updateHandle(Invocation invocation) throws Throwable {
        //处理参数
        handleParameterOrResult(invocation.getArgs()[1], CryptoType.ENCRYPT);
        return invocation.proceed();
    }

    /**
     * 处理参数或结果
     *
     * @param object
     * @param cryptoType
     * @throws IllegalAccessException
     */
    private void handleParameterOrResult(Object object, CryptoType cryptoType) throws IllegalAccessException {
        HashMap<Field, Object> fieldObjectHashMap = new HashMap<>();
        //多个参数
        if (object instanceof Map) {
            Map paramMap = (Map) object;
            Set keySet = paramMap.keySet();
            for (Object key : keySet) {
                Object o = paramMap.get(key);
                if (o != null) {
                    handleObject(o, o.getClass(), fieldObjectHashMap);
                }

            }
        } else {
            if (object != null) {
                handleObject(object, object.getClass(), fieldObjectHashMap);
            }
        }

        //统一修改加密解密值
        fieldObjectHashMap.keySet().forEach(key -> {
            try {
                handleString(key, fieldObjectHashMap.get(key), cryptoType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 是否是基本类型
     *
     * @param type
     * @return
     */
    private boolean isBase(Type type) {
        return boolean.class.equals(type) ||
                char.class.equals(type) ||
                long.class.equals(type) ||
                int.class.equals(type) ||
                byte.class.equals(type) ||
                short.class.equals(type) ||
                double.class.equals(type) ||
                float.class.equals(type);
    }

    /**
     * 是否是
     *
     * @param object
     * @return
     */
    private boolean isFilter(Object object) {

        return object == null || object instanceof CharSequence || object instanceof Number || object instanceof Collection || object instanceof Date || object instanceof ChronoLocalDate;
    }

    /**
     * 聚合父类属性
     *
     * @param oClass
     * @param fields
     * @return
     */
    private List<Field> mergeField(Class<?> oClass, List<Field> fields) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        Class<?> superclass = oClass.getSuperclass();
        if (superclass != null && !superclass.equals(Object.class) && superclass.getDeclaredFields().length > 0) {
            mergeField(superclass, fields);
        }
        for (Field declaredField : oClass.getDeclaredFields()) {

            int modifiers = declaredField.getModifiers();

            if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers) || Modifier.isVolatile(modifiers) || Modifier.isSynchronized(modifiers)) {
                continue;
            }
            fields.add(declaredField);
        }

        return fields;

    }

    /**
     * 处理Object
     *
     * @param obj
     * @param oClass
     * @throws IllegalAccessException
     */
    private void handleObject(Object obj, Class<?> oClass, HashMap<Field, Object> fieldObjectHashMap) throws IllegalAccessException {
        //过滤
        if (isFilter(obj)) {
            return;
        }

        List<Field> fields = mergeField(oClass, null);

        for (Field declaredField : fields) {

            //静态属性直接跳过
            if (Modifier.isStatic(declaredField.getModifiers())) {
                continue;
            }

            boolean accessible = declaredField.isAccessible();
            declaredField.setAccessible(true);
            Object value = declaredField.get(obj);
            declaredField.setAccessible(accessible);

            if (value == null) {
                // TODO
                continue;
            } else if (value instanceof Number) {
                // TODO
                continue;
            } else if (value instanceof String) {
                CryptoMapperField annotation = declaredField.getAnnotation(CryptoMapperField.class);
                if (annotation != null) {
                    fieldObjectHashMap.put(declaredField, obj);
                }

            } else if (value instanceof Collection) {
                Collection coll = (Collection) value;
                for (Object o : coll) {
                    if (isFilter(o)) {
                        //默认集合内类型一致
                        break;
                    }
                    handleObject(o, o.getClass(), fieldObjectHashMap);
                }
            } else {
                handleObject(value, value.getClass(), fieldObjectHashMap);
            }
        }

    }

    /**
     * 处理字符
     *
     * @param field
     * @param object
     * @param cryptoType
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private void handleString(Field field, Object object, CryptoType cryptoType) throws Exception {

        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        Object value = field.get(object);

        CryptoMapperField annotation = field.getAnnotation(CryptoMapperField.class);
        if (annotation != null) {

            String key;
            //全局配置的key
            String propertiesKey = encryptProperties.getKey();
            log.debug("全局key是：" + propertiesKey);
            //属性上的key
            String annotationKey = annotation.key();
            log.debug("注解key是：" + annotationKey);

            if (StrUtil.isNotBlank(annotationKey)) {
                key = annotationKey;
            } else {
                key = propertiesKey;
            }

            Class<? extends ICrypto> iCryptoImpl = annotation.iCrypto();
            ICrypto iCrypto = iCryptoImpl.newInstance();

            //解密后的值
            String valueResult;
            if (cryptoType.equals(CryptoType.DECRYPT)) {
                valueResult = iCrypto.decrypt(String.valueOf(value), key);
            } else {
                valueResult = iCrypto.encrypt(String.valueOf(value), key);
            }

            log.debug("原值：" + value);
            log.debug("现在：" + valueResult);
            field.set(object, String.valueOf(valueResult));
            field.setAccessible(accessible);
        }
    }

    /**
     * 是否要进行拦截，然后做出决定是否生成一个代理。
     *
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
