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
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.utils
 * @Author: Parker
 * @CreateTime: 2020-09-19 00:08
 * @Description: 转化对象工具类
 *
 * 用于 Wrapper 对象 转化为本地对象，或者本地对象转化为Wrapper对象
 *
 */
@Slf4j
public final class WrapperUtil {

    /** 私有化构造函数 */
    private WrapperUtil(){}

    /**
     * 转化对象
     * @param from
     * @param toClass
     * @param <M>
     * @return
     */
    public static <T,M> M transformInstance(T from, Class<M> toClass){
        M m = null;
        try {
            Object toObj = ReflectUtil.newInstance(toClass);
            Map<String, Object> stringBeanMap = BeanUtil.beanToMap(from);
            if(stringBeanMap != null){
                Object toInstance = BeanUtil.fillBeanWithMapIgnoreCase(stringBeanMap, toObj, true);
                if(toInstance != null){
                    m = (M) toInstance;
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return m;
    }


    /**
     * 转化集合对象
     * @param froms
     * @param toClass
     * @param <M>
     * @return
     */
    public static <T,M> List<M> transformInstance(List<T> froms, Class<M> toClass){
        List<M> toInstanceList = Lists.newArrayListWithCapacity(froms.size());
        try {
            for (Object from : froms) {
                Object toObj = ReflectUtil.newInstance(toClass);
                Map<String, Object> stringBeanMap = BeanUtil.beanToMap(from);
                if(stringBeanMap != null){
                    Object toInstance = BeanUtil.fillBeanWithMapIgnoreCase(stringBeanMap, toObj, true);
                    if(toInstance != null){
                        toInstanceList.add((M) toInstance);
                    }
                }
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return toInstanceList;
    }

    /**
     * 转化集合对象
     * @param froms
     * @param toClass
     * @param <M>
     * @return
     */
    public static <T,M> Set<M> transformInstance(Set<T> froms, Class<M> toClass){
        Set<M> toInstanceList = Sets.newHashSetWithExpectedSize(froms.size());
        try {
            for (Object from : froms) {
                Object toObj = ReflectUtil.newInstance(toClass);
                Map<String, Object> stringBeanMap = BeanUtil.beanToMap(from);
                if(stringBeanMap != null){
                    Object toInstance = BeanUtil.fillBeanWithMapIgnoreCase(stringBeanMap, toObj, true);
                    if(toInstance != null){
                        toInstanceList.add((M) toInstance);
                    }
                }
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return toInstanceList;
    }

    /**
     * 克隆并且转化对象
     * @param from
     * @param toClass
     * @param <M>
     * @return
     */
    public static <T,M> M cloneTransformInstance(T from, Class<M> toClass){
        T fromByClone = ObjectUtil.cloneByStream(from);
        return transformInstance(fromByClone,toClass);
    }


    /**
     * 克隆并且转化集合对象
     * @param froms
     * @param toClass
     * @param <M>
     * @return
     */
    public static <T,M> List<M> cloneTransformInstance(List<T> froms, Class<M> toClass){
        List<T> ts = ObjectUtil.cloneByStream(froms);
        return transformInstance(ts,toClass);
    }

    /**
     * 克隆并且转化集合对象
     * @param froms
     * @param toClass
     * @param <M>
     * @return
     */
    public static <T,M> Set<M> cloneTransformInstance(Set<T> froms, Class<M> toClass){
        Set<T> ts = ObjectUtil.cloneByStream(froms);
        return transformInstance(ts,toClass);
    }

}
