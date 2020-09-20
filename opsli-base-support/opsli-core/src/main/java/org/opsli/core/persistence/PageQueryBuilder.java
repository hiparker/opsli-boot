package org.opsli.core.persistence;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.utils.HumpUtil;
import org.opsli.core.base.entity.BaseEntity;

import java.util.Map;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.persistence
 * @Author: Parker
 * @CreateTime: 2020-09-19 21:15
 * @Description: 查询构建器
 *
 * 针对分页查询 无非也就是
 * 全值匹配 eq
 * 模糊匹配 like
 * 日期 begin end 匹配
 *
 */
@Slf4j
public class PageQueryBuilder<E extends ApiWrapper,T extends BaseEntity>{


    // == 匹配条件 ==
    /** 全值匹配 */
    private static final String EQ = "EQ";
    /** 模糊匹配 */
    private static final String LIKE = "LIKE";
    /** 日期匹配 */
    private static final String BEGIN = "BEGIN";
    private static final String END = "END";
    /** 排序方式 */
    private static final String ORDER = "ORDER";
    private static final String ORDER_ASC = "ASC";
    private static final String ORDER_DESC = "DESC";


    /** 当前页 */
    private Integer pageNo;
    /** 每页数量 */
    private Integer pageSize;
    /** 参数 */
    private Map<String, String[]> parameterMap;
    /** Entity Clazz */
    private Class<T> entityClazz;

    /**
     * 构造函数
     * @param entityClazz Entity 的 clazz
     * @param pageNo 当前页
     * @param pageSize 每页显示条数
     * @param parameterMap request 参数
     */
    public PageQueryBuilder(Class<T> entityClazz, Integer pageNo, Integer pageSize, Map<String, String[]> parameterMap){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.parameterMap = parameterMap;
        this.entityClazz = entityClazz;
    }

    /**
     * 构造函数 只是生产 查询器
     * @param entityClazz Entity 的 clazz
     * @param parameterMap request 参数
     */
    public PageQueryBuilder(Class<T> entityClazz, Map<String, String[]> parameterMap){
        this.parameterMap = parameterMap;
        this.entityClazz = entityClazz;
    }


    /**
     * 构建builderPage
     * @return
     */
    public Page<E,T>  builderPage(){
        Page<E,T> page = new Page<>(this.pageNo,this.pageSize);
        QueryWrapper<T> queryWrapper = this.createQueryWrapper();
        page.setQueryWrapper(queryWrapper);
        return page;
    }

    /**
     * 创建 查询条件构造器
     * @return
     */
    public QueryWrapper<T> builderQueryWrapper(){
        return this.createQueryWrapper();
    }

    /**
     * 创建 查询条件构造器
     * @return
     */
    private QueryWrapper<T> createQueryWrapper(){
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if(this.parameterMap == null){
            return queryWrapper;
        }
        for (Map.Entry<String, String[]> stringEntry : this.parameterMap.entrySet()) {
            String keys = stringEntry.getKey();
            String[] values = stringEntry.getValue();
            // 非空检测
            if(StringUtils.isEmpty(keys) || values == null || StringUtils.isEmpty(values[0])){
                continue;
            }

            // 键 和 操作
            String[] key_handle = keys.split("_");
            if(key_handle.length < 2){
                continue;
            }
            // 判断 字段是否合法
            boolean hasField = this.validationField(key_handle);
            if(hasField){
                // 验证操作是否合法
                boolean hasHandle = this.validationHandle(key_handle);
                if(hasHandle){
                    // 操作
                    String handle = key_handle[1];
                    // 键
                    String key = key_handle[0];
                    // 处理值
                    String value = values[0];
                    // 赋值
                    this.handlerValue(queryWrapper, handle, key ,value);
                }
            }
        }
        return queryWrapper;
    }

    /**
     * 处理值
     * @param queryWrapper 查询构造器
     * @param handle 操作
     * @param key 键
     * @param value 值
     * @return
     */
    private void handlerValue(QueryWrapper<T> queryWrapper, String handle, String key, String value){
        if(queryWrapper == null || StringUtils.isEmpty(handle)
               || StringUtils.isEmpty(key) || StringUtils.isEmpty(value)
            ){
            return;
        }
        // 转换驼峰 为 数据库下划线字段
        key = HumpUtil.humpToUnderline(key);
        if (EQ.equals(handle)) {
            // 全值匹配
            queryWrapper.eq(key,value);
        } else if (LIKE.equals(handle)) {
            // 模糊匹配
            queryWrapper.like(key,value);
        } else if (BEGIN.equals(handle)) {
            // 大于等于
            queryWrapper.ge(key,value);
        } else if (END.equals(handle)) {
            // 小于等于
            queryWrapper.le(key,value);
        } else if (ORDER.equals(handle)) {
            // 排序
            if(ORDER_ASC.equals(value)){
                queryWrapper.orderByAsc(key);
            } else if(ORDER_DESC.equals(value)){
                queryWrapper.orderByDesc(key);
            } else{
                queryWrapper.orderByAsc(key);
            }
        }
    }

    /**
     * 检测 字段是否合法
     * @param key_handle
     * @return
     */
    private boolean validationField(String[] key_handle){
        if(entityClazz == null || key_handle == null || StringUtils.isEmpty(key_handle[0])){
            return false;
        }
        // 判断当前传入参数 是否是Entity的字段
        return ReflectUtil.hasField(entityClazz, key_handle[0]);
    }


    /**
     * 检测 操作是否合法
     * @param key_handle
     * @return
     */
    private boolean validationHandle(String[] key_handle){
        if(key_handle == null || StringUtils.isEmpty(key_handle[1])){
            return false;
        }
        String handle = key_handle[1];
        if (EQ.equals(handle)) {
            return true;
        } else if (LIKE.equals(handle)) {
            return true;
        } else if (BEGIN.equals(handle)) {
            return true;
        } else if (END.equals(handle)) {
            return true;
        } else if (ORDER.equals(handle)) {
            return true;
        }
        return false;
    }

}
