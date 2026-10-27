package org.opsli.core.filters.interceptor.crypto;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import opsli.plugins.crypto.spring.annotation.CryptoMapperField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.chrono.ChronoLocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mybatis 拦截器 Object 处理器
 *
 * @author Pace
 * @date 2025-06-01
 */
@Slf4j
public class ObjectProcessor {

    private static final int MAX_RECURSION_DEPTH = 50;
    private static final int MAX_COLLECTION_SIZE = -1;

    // 类字段缓存 - 避免重复反射
    private static final ConcurrentHashMap<Class<?>, List<Field>> FIELD_CACHE = new ConcurrentHashMap<>();

    // 注解缓存 - 避免重复获取注解
    private static final ConcurrentHashMap<Field, Boolean> ANNOTATION_CACHE = new ConcurrentHashMap<>();

    // Bean类型缓存 - 避免重复判断
    private static final ConcurrentHashMap<Class<?>, Boolean> BEAN_TYPE_CACHE = new ConcurrentHashMap<>();

    // 过滤类型缓存 - 避免重复类型判断
    private static final Set<Class<?>> FILTER_TYPES = Set.of(
            String.class, CharSequence.class, Number.class, Collection.class,
            Date.class, ChronoLocalDate.class
    );

    // 基础类型快速判断集合
    private static final Set<Class<?>> PRIMITIVE_WRAPPER_TYPES = Set.of(
            Boolean.class, Byte.class, Character.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class
    );

    // 使用更高效的访问对象跟踪
    private final IdentityHashMap<Object, Object> visitedObjects = new IdentityHashMap<>();
    private int currentDepth = 0;

    /**
     * 处理对象入口方法
     */
    public void handleObject(Object obj, Class<?> oClass, HashMap<Field, Object> fieldObjectHashMap) {
        try {
            visitedObjects.clear();
            currentDepth = 0;
            processObject(obj, oClass, fieldObjectHashMap);
        } finally {
            visitedObjects.clear();
        }
    }

    /**
     * 处理对象的核心方法 - 内联优化
     */
    private void processObject(Object obj, Class<?> oClass, HashMap<Field, Object> fieldObjectHashMap) {
        // 快速过滤检查 - 内联优化
        if (obj == null || isFilterType(oClass) || !isBeanType(oClass)) {
            return;
        }

        // 循环引用检查 - 使用更快的 IdentityHashMap
        if (visitedObjects.containsKey(obj)) {
            return; // 移除日志以提升性能
        }

        // 递归深度检查
        if (currentDepth >= MAX_RECURSION_DEPTH) {
            return; // 移除日志以提升性能
        }

        visitedObjects.put(obj, null);
        currentDepth++;

        try {
            processObjectFieldsOptimized(obj, oClass, fieldObjectHashMap);
        } finally {
            currentDepth--;
            visitedObjects.remove(obj);
        }
    }

    /**
     * 优化的字段处理方法
     */
    private void processObjectFieldsOptimized(Object obj, Class<?> oClass, HashMap<Field, Object> fieldObjectHashMap) {
        List<Field> fields = getFieldsCached(oClass);

        // 使用传统 for 循环避免迭代器开销
        for (int i = 0, size = fields.size(); i < size; i++) {
            Field field = fields.get(i);

            try {
                // 内联字段处理逻辑
                Object value = BeanUtil.getProperty(obj, field.getName());

                if (value == null) {
                    continue;
                }

                // 快速类型判断和处理
                processValueOptimized(value, field, obj, fieldObjectHashMap);

            } catch (Exception e) {
                // 简化异常处理，避免字符串拼接
                if (log.isDebugEnabled()) {
                    log.debug("处理字段异常: {}.{}", oClass.getSimpleName(), field.getName());
                }
            }
        }
    }

    /**
     * 优化的值处理方法
     */
    private void processValueOptimized(Object value, Field field, Object parentObj,
                                       HashMap<Field, Object> fieldObjectHashMap) {
        Class<?> valueClass = value.getClass();

        // 快速类型判断 - 避免枚举遍历
        if (valueClass == String.class) {
            // String 处理 - 内联优化
            if (hasAnnotationCached(field)) {
                fieldObjectHashMap.put(field, parentObj);
            }
        } else if (isNumberType(valueClass)) {
            // Number 类型直接跳过
            return;
        } else if (value instanceof Collection) {
            // Collection 处理
            processCollectionOptimized((Collection<?>) value, fieldObjectHashMap);
        } else if (value instanceof Map) {
            // Map 处理
            processMapOptimized((Map<?, ?>) value, fieldObjectHashMap);
        } else {
            // 自定义对象处理
            processObject(value, valueClass, fieldObjectHashMap);
        }
    }

    /**
     * 优化的集合处理
     */
    private void processCollectionOptimized(Collection<?> collection, HashMap<Field, Object> fieldObjectHashMap) {
        if (collection.isEmpty()) {
            return;
        }

        int processCount = 0;
        Class<?> elementType = null;
        boolean typeChecked = false;

        // 使用增强 for 循环，但避免类型重复检查
        for (Object item : collection) {
            if (MAX_COLLECTION_SIZE > 0 && processCount >= MAX_COLLECTION_SIZE) {
                break;
            }

            if (item == null) {
                continue;
            }

            // 优化：只检查第一个元素的类型
            if (!typeChecked) {
                elementType = item.getClass();
                typeChecked = true;
                if (isFilterType(elementType)) {
                    break; // 整个集合都跳过
                }
            } else if (elementType != item.getClass()) {
                // 类型不一致时才重新检查
                if (isFilterType(item.getClass())) {
                    continue;
                }
            }

            processObject(item, item.getClass(), fieldObjectHashMap);
            processCount++;
        }
    }

    /**
     * 优化的 Map 处理
     */
    private void processMapOptimized(Map<?, ?> map, HashMap<Field, Object> fieldObjectHashMap) {
        if (map.isEmpty()) {
            return;
        }

        int processCount = 0;

        // 直接遍历 values，避免 Entry 对象创建
        for (Object mapValue : map.values()) {
            if (MAX_COLLECTION_SIZE > 0 && processCount >= MAX_COLLECTION_SIZE) {
                break;
            }

            if (mapValue != null && !isFilterType(mapValue.getClass())) {
                processObject(mapValue, mapValue.getClass(), fieldObjectHashMap);
            }
            processCount++;
        }
    }

    /**
     * 缓存字段获取 - 避免重复反射
     */
    private List<Field> getFieldsCached(Class<?> oClass) {
        return FIELD_CACHE.computeIfAbsent(oClass, this::mergeFieldOptimized);
    }

    /**
     * 优化的字段合并方法
     */
    private List<Field> mergeFieldOptimized(Class<?> oClass) {
        List<Field> allFields = new ArrayList<>();

        // 使用栈避免递归，提升性能
        Deque<Class<?>> classStack = new ArrayDeque<>();
        Class<?> currentClass = oClass;

        // 收集所有父类
        while (currentClass != null && currentClass != Object.class) {
            classStack.push(currentClass);
            currentClass = currentClass.getSuperclass();
        }

        // 从父类到子类处理字段
        while (!classStack.isEmpty()) {
            Class<?> clazz = classStack.pop();
            try {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (isValidField(field)) {
                        allFields.add(field);
                    }
                }
            } catch (SecurityException e) {
                // 忽略安全异常
            }
        }

        return allFields;
    }

    /**
     * 快速字段验证
     */
    private boolean isValidField(Field field) {
        int modifiers = field.getModifiers();
        return !(Modifier.isStatic(modifiers) ||
                Modifier.isFinal(modifiers) ||
                Modifier.isVolatile(modifiers) ||
                Modifier.isSynchronized(modifiers) ||
                Modifier.isTransient(modifiers));
    }

    /**
     * 缓存注解检查
     */
    private boolean hasAnnotationCached(Field field) {
        return ANNOTATION_CACHE.computeIfAbsent(field,
                f -> f.getAnnotation(CryptoMapperField.class) != null);
    }

    /**
     * 缓存 Bean 类型检查
     */
    private boolean isBeanType(Class<?> clazz) {
        return BEAN_TYPE_CACHE.computeIfAbsent(clazz, BeanUtil::isBean);
    }

    /**
     * 快速过滤类型检查 - 避免 instanceof 链
     */
    private boolean isFilterType(Class<?> clazz) {
        // 快速检查基础类型
        if (clazz.isPrimitive() || PRIMITIVE_WRAPPER_TYPES.contains(clazz)) {
            return true;
        }

        // 检查预定义的过滤类型
        for (Class<?> filterType : FILTER_TYPES) {
            if (filterType.isAssignableFrom(clazz)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 快速数字类型检查
     */
    private boolean isNumberType(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz) ||
                clazz.isPrimitive() && (
                        clazz == int.class || clazz == long.class ||
                                clazz == double.class || clazz == float.class ||
                                clazz == short.class || clazz == byte.class
                );
    }

    /**
     * 清理缓存方法 - 防止内存泄漏
     */
    public static void clearCache() {
        FIELD_CACHE.clear();
        ANNOTATION_CACHE.clear();
        BEAN_TYPE_CACHE.clear();
    }

    /**
     * 获取缓存统计信息
     */
    public static Map<String, Integer> getCacheStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("fieldCache", FIELD_CACHE.size());
        stats.put("annotationCache", ANNOTATION_CACHE.size());
        stats.put("beanTypeCache", BEAN_TYPE_CACHE.size());
        return stats;
    }
}
