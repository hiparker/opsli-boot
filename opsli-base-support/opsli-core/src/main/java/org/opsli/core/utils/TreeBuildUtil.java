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
import com.google.common.collect.Lists;

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

    private static final String DEF_ID = "";

    /** 默认排除字段 */
    private static final List<String> DEF_EXCLUDE_FIELDS;
    static {
        DEF_EXCLUDE_FIELDS = ListUtil.list(false);
        DEF_EXCLUDE_FIELDS.add("izApi");
        DEF_EXCLUDE_FIELDS.add("izManual");
    }

    /**
     * 构建Tree
     * @param dataList 数据集合
     * @return List<Tree<Object>>
     */
    public List<Tree<Object>> build(List<?> dataList){
        return this.build(dataList, DEF_PARENT_ID, null);
    }

    /**
     * 构建Tree
     * @param dataList 数据集合
     * @param parentId 父节点ID
     * @return List<Tree<Object>>
     */
    public List<Tree<Object>> build(List<?> dataList, String parentId){
        return this.build(dataList, parentId, null);
    }

    /**
     * 构建Tree
     * @param dataList 数据集合
     * @param config 配置
     * @return List<Tree<Object>>
     */
    public List<Tree<Object>> build(List<?> dataList, TreeNodeConfig config){
        return this.build(dataList, DEF_PARENT_ID, config);
    }

    /**
     * 构建Tree
     * @param dataList 数据集合
     * @param parentId 父节点ID
     * @param config 配置
     * @return List<Tree<Object>>
     */
    public List<Tree<Object>> build(List<?> dataList, String parentId, TreeNodeConfig config){
        if(CollUtil.isEmpty(dataList)){
            return ListUtil.empty();
        }

        // 处理Map集合
        Object obj = dataList.get(0);

        if(!(obj instanceof Map)){
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
        excludeFields.add(treeConfig.getIdKey());
        excludeFields.add(treeConfig.getParentIdKey());
        excludeFields.add(treeConfig.getWeightKey());

        //转换器
        return TreeUtil.build(dataList, defParentId, treeConfig,
                (treeNode, tree) -> handlerTreeNode(treeNode, tree, treeConfig, excludeFields));
    }

    /**
     * 构建Tree
     * @param dataList 数据集合
     * @return List<Tree<Object>>
     */
    public List<Tree<Object>> buildByLazy(List<?> dataList){
        return this.buildByLazy(dataList, null);
    }

    /**
     * 构建Tree
     * @param dataList 数据集合
     * @param config 配置
     * @return List<Tree<Object>>
     */
    public List<Tree<Object>> buildByLazy(List<?> dataList, TreeNodeConfig config){
        if(CollUtil.isEmpty(dataList)){
            return ListUtil.empty();
        }

        // 处理Map集合
        Object obj = dataList.get(0);
        if(!(obj instanceof Map)){
            // 处理Bean 验证
            boolean isBean = BeanUtil.isBean(obj.getClass());
            if(!isBean){
                return ListUtil.empty();
            }
        }

        // 默认值处理
        final TreeNodeConfig treeConfig = ObjectUtil.defaultIfNull(config, TreeNodeConfig.DEFAULT_CONFIG);
        List<String> excludeFields = ListUtil.list(false);
        excludeFields.addAll(DEF_EXCLUDE_FIELDS);
        excludeFields.add(treeConfig.getIdKey());
        excludeFields.add(treeConfig.getParentIdKey());
        excludeFields.add(treeConfig.getWeightKey());

        //转换器
        List<Tree<Object>> treeNodes = Lists.newArrayListWithCapacity(dataList.size());
        for (Object model : dataList) {
            Tree<Object> emptyNode = TreeUtil.createEmptyNode(DEF_ID);
            // 处理对象数据
            TreeBuildUtil.INSTANCE.handlerTreeNode(model, emptyNode, treeConfig, excludeFields);
            treeNodes.add(emptyNode);
        }

        return treeNodes;
    }


    /**
     * 处理 树节点
     * @param config 配置
     * @param excludeFields 忽略字段集合
     * @param treeNode 树节点对象
     * @param tree 树节点
     */
    private void handlerTreeNode(Object treeNode, Tree<Object> tree,
                                 TreeNodeConfig config, List<String> excludeFields) {
        // 非空校验
        if(ObjectUtil.isEmpty(treeNode) || null == tree){
            return;
        }

        // 初始化
        if(null == config){
            config = TreeNodeConfig.DEFAULT_CONFIG;
        }
        if(CollUtil.isEmpty(excludeFields)){
            excludeFields = ListUtil.list(false);
            excludeFields.addAll(DEF_EXCLUDE_FIELDS);
            excludeFields.add(config.getIdKey());
            excludeFields.add(config.getParentIdKey());
            excludeFields.add(config.getWeightKey());
        }

        // Bean 对象转 Map
        Map<String, Object> beanMap;
        if(treeNode instanceof Map){
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
    }


    /**
     * 欺骗编译器 强制转换
     * @param obj 对象
     * @param <T> 泛型
     * @return T
     */
    private <T> Comparable<T> cast(T obj){
        @SuppressWarnings("unchecked")
        Comparable<T> comparable = (Comparable<T>) Convert.toInt(obj);
        return comparable;
    }

}
