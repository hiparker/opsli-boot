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
package org.opsli.core.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.List;
import java.util.Map;

/**
 * 树状结构工具类
 *
 * 注：排序只支持 int 类型
 *
 * @author Parker

 * @date 2017-05-20 14:41
 */
public enum TreeBuildUtil {

    /** 实例 */
    INSTANCE;

    /** 默认父节点ID */
    public static final String DEF_PARENT_ID = "0";

    /** 默认排除字段 */
    private static final List<String> DEF_EXCLUDE_FIELDS;
    static {
        DEF_EXCLUDE_FIELDS = ListUtil.list(false);
        DEF_EXCLUDE_FIELDS.add("izApi");
        DEF_EXCLUDE_FIELDS.add("izManual");
    }

    public List<Tree<Object>> build(List<?> dataList){
        return this.build(dataList, DEF_PARENT_ID, null);
    }

    public List<Tree<Object>> build(List<?> dataList, String parentId){
        return this.build(dataList, parentId, null);
    }

    public List<Tree<Object>> build(List<?> dataList, TreeNodeConfig config){
        return this.build(dataList, DEF_PARENT_ID, config);
    }

    public List<Tree<Object>> build(List<?> dataList, String parentId, TreeNodeConfig config){
        if(CollUtil.isEmpty(dataList)){
            return ListUtil.empty();
        }

        boolean isMap = false;

        // 处理Map集合
        Object obj = dataList.get(0);
        if(obj instanceof Map){
            isMap = true;
        }

        if(!isMap){
            // 处理Bean 验证
            boolean isBean = BeanUtil.isBean(obj.getClass());
            if(!isBean){
                return ListUtil.empty();
            }
        }

        // 默认值处理
        final String defParentId = ObjectUtil.defaultIfNull(parentId, DEF_PARENT_ID);
        final TreeNodeConfig treeConfig = ObjectUtil.defaultIfNull(config, TreeNodeConfig.DEFAULT_CONFIG);

        List<String> excludeFields = ListUtil.list(false);
        excludeFields.addAll(DEF_EXCLUDE_FIELDS);
        excludeFields.add(config.getIdKey());
        excludeFields.add(config.getParentIdKey());
        excludeFields.add(config.getWeightKey());

        //转换器
        final boolean finalIsMap = isMap;
        return TreeUtil.build(dataList, defParentId, treeConfig,
                (treeNode, tree) -> {
                    // 非空校验
                    if(ObjectUtil.isEmpty(treeNode)){
                        return;
                    }

                    // Bean 对象转 Map
                    Map<String, Object> beanMap;
                    if(finalIsMap){
                        beanMap = Convert.toMap(String.class, Object.class, treeNode);
                    }else{
                        beanMap = BeanUtil.beanToMap(treeNode);
                    }

                    // 主要属性
                    tree.setId(beanMap.get(config.getIdKey()));
                    tree.setParentId(beanMap.get(config.getParentIdKey()));
                    tree.setWeight(
                            cast(
                                    beanMap.get(config.getWeightKey())));

                    // 扩展属性 ...
                    for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        // 排除字段
                        if(excludeFields.contains(key)){
                            continue;
                        }
                        tree.putExtra(key, value);
                    }
                });
    }


    /**
     * 欺骗编译器 强制转换
     * @param obj
     * @param <T>
     * @return
     */
    private <T> Comparable<T> cast(T obj){
        return (Comparable<T>) Convert.toInt(obj);
    }

}
