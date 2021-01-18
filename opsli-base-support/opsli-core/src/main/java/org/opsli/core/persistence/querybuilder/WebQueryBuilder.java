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
package org.opsli.core.persistence.querybuilder;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.utils.HumpUtil;
import org.opsli.core.base.entity.BaseEntity;

import java.util.Map;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.persistence.querybuilder
 * @Author: Parker
 * @CreateTime: 2020-09-21 23:57
 * @Description: Web 条件构造器
 */
public class WebQueryBuilder<T extends BaseEntity> implements QueryBuilder<T>{

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

    /** 参数 */
    private final Map<String, String[]> parameterMap;
    /** Entity Clazz */
    private final Class<? extends BaseEntity> entityClazz;
    /** 默认排序字段 */
    private final String defaultOrderField;

    /**
     * 构造函数 只是生产 查询器
     * @param entityClazz Entity 的 clazz
     * @param parameterMap request 参数
     */
    public WebQueryBuilder(Class<T> entityClazz, Map<String, String[]> parameterMap){
        this.parameterMap = parameterMap;
        this.entityClazz = entityClazz;
        this.defaultOrderField = MyBatisConstants.FIELD_UPDATE_TIME;
    }

    /**
     * 构造函数 只是生产 查询器
     * @param entityClazz Entity 的 clazz
     * @param parameterMap request 参数
     * @param defaultOrderField 默认排序字段
     */
    public WebQueryBuilder(Class<T> entityClazz, Map<String, String[]> parameterMap,
                           String defaultOrderField){
        this.parameterMap = parameterMap;
        this.entityClazz = entityClazz;
        this.defaultOrderField = defaultOrderField;
    }

    @Override
    public QueryWrapper<T> build() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        return this.createQueryWrapper(queryWrapper);
    }

    /**
     * 创建 查询条件构造器
     * @return
     */
    private  <T extends BaseEntity> QueryWrapper<T> createQueryWrapper(QueryWrapper<T> queryWrapper){
        if(this.parameterMap == null){
            return queryWrapper;
        }
        // order 排序次数 如果为0 则默认按照修改时间来排序
        int orderCount = 0;
        for (Map.Entry<String, String[]> stringEntry : this.parameterMap.entrySet()) {
            String keys = stringEntry.getKey();
            String[] values = stringEntry.getValue();
            // 非空检测
            if(StringUtils.isEmpty(keys) || values == null || StringUtils.isEmpty(values[0])){
                continue;
            }

            // 键 和 操作
            String[] keyHandle = keys.split("_");
            if(keyHandle.length < 2){
                continue;
            }
            // 判断 字段是否合法
            boolean hasField = this.validationField(keyHandle);
            if(hasField){
                // 验证操作是否合法
                boolean hasHandle = this.validationHandle(keyHandle);
                if(hasHandle){
                    // 操作
                    String handle = keyHandle[1];
                    // 键
                    String key = keyHandle[0];
                    // 处理值
                    String value = values[0];
                    // 赋值
                    this.handlerValue(queryWrapper, handle, key ,value);
                    // 如果有排序 就+1
                    if(ORDER.equals(handle)){
                        orderCount++;
                    }
                }
            }
        }
        // 如果没有排序 默认按照 修改时间倒叙排序
        if(orderCount == 0){
            if(StringUtils.isNotEmpty(this.defaultOrderField)){
                queryWrapper.orderByDesc(HumpUtil.humpToUnderline(this.defaultOrderField));
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
    private <T extends BaseEntity> void handlerValue(QueryWrapper<T> queryWrapper, String handle, String key, String value){
        if(queryWrapper == null || StringUtils.isEmpty(handle)
                || StringUtils.isEmpty(key) || StringUtils.isEmpty(value)
        ){
            return;
        }
        // 转换驼峰 为 数据库下划线字段
        key = HumpUtil.humpToUnderline(key);
        switch (handle) {
            case EQ:
                // 全值匹配
                queryWrapper.eq(key, value);
                break;
            case LIKE:
                // 模糊匹配
                queryWrapper.like(key, value);
                break;
            case BEGIN:
                // 大于等于
                queryWrapper.ge(key, value);
                break;
            case END:
                // 小于等于
                queryWrapper.le(key, value);
                break;
            case ORDER:
                // 排序
                if (ORDER_ASC.equals(value)) {
                    queryWrapper.orderByAsc(key);
                } else if (ORDER_DESC.equals(value)) {
                    queryWrapper.orderByDesc(key);
                } else {
                    queryWrapper.orderByAsc(key);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 检测 字段是否合法
     * @param keyHandle
     * @return
     */
    private boolean validationField(String[] keyHandle){
        if(entityClazz == null || keyHandle == null || StringUtils.isEmpty(keyHandle[0])){
            return false;
        }
        // 判断当前传入参数 是否是Entity的字段
        return ReflectUtil.hasField(entityClazz, keyHandle[0]);
    }


    /**
     * 检测 操作是否合法
     * @param keyHandle
     * @return
     */
    private boolean validationHandle(String[] keyHandle){
        if(keyHandle == null || StringUtils.isEmpty(keyHandle[1])){
            return false;
        }
        String handle = keyHandle[1];
        if (EQ.equals(handle)) {
            return true;
        } else if (LIKE.equals(handle)) {
            return true;
        } else if (BEGIN.equals(handle)) {
            return true;
        } else if (END.equals(handle)) {
            return true;
        }
        return ORDER.equals(handle);
    }
}
