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
package org.opsli.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 转化对象工具类
 * 用于 Wrapper 对象 转化为本地对象，或者本地对象转化为Wrapper对象
 *
 * @author Parker
 * @date 2020-09-19 00:08
 */
@Slf4j
public final class WrapperUtil {

    /** 私有化构造函数 */
    private WrapperUtil(){}

    /**
     * 转化对象
     * @param source 源数据
     * @param target 目标
     * @param <M> 泛型
     * @return M
     */
    public static <M> M transformInstance(Object source, Class<M> target){
        return transformInstance(source, target, false);
    }


    /**
     * 转化集合对象
     * @param source 源数据
     * @param target 目标
     * @param <M> 泛型
     * @return List<M>
     */
    public static <T,M> List<M> transformInstance(Collection<T> source, Class<M> target){
        return transformInstance(source, target, false);
    }


    /**
     * 克隆并且转化对象
     * @param source 源数据
     * @param target 目标
     * @param isClone 是否克隆
     * @return M
     */
    public static <T,M> M transformInstance(Object source, Class<M> target, boolean isClone){
        if(source == null){
            return null;
        }

        if(isClone){
            source = ObjectUtil.cloneByStream(source);
        }

        M m = null;
        try {
            m = BeanUtil.copyProperties(source, target);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return m;
    }


    /**
     * 克隆并且转化集合对象
     * @param source 源数据
     * @param target 目标
     * @param isClone 是否克隆
     * @param <M> M
     * @return List<M>
     */
    public static <T,M> List<M> transformInstance(Collection<T> source, Class<M> target, boolean isClone){
        if(CollUtil.isEmpty(source)){
            return Lists.newArrayList();
        }

        if(isClone){
            source = ObjectUtil.cloneByStream(source);
        }

        List<M> toInstanceList = Lists.newArrayList();
        try {
            toInstanceList = source.stream().map((s) -> transformInstance(s, target, true)).collect(Collectors.toList());
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return toInstanceList;
    }

}
