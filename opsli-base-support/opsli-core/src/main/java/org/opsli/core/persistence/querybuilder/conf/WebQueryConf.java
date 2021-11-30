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
package org.opsli.core.persistence.querybuilder.conf;

import org.apache.poi.ss.formula.functions.T;
import org.opsli.common.utils.FieldUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Web 请求配置类
 *
 * @author Parker
 * @date 2021年3月3日17:12:08
 */
public class WebQueryConf {

    private final Map<String, String> queryMap;

    public WebQueryConf(){
        queryMap = new HashMap<>();
    }


    /**
     * 存放
     * @param fieldFn 字段
     * @param value 值
     */
    public <T> void pub(FieldUtil.SFunction<T, ?> fieldFn, String value){
        String fileName = FieldUtil.getFileName(fieldFn);
        queryMap.putIfAbsent(fileName, value);
    }

    /**
     * 存放
     * @param fileName 字段
     * @param value 值
     */
    public void pub(String fileName, String value){
        queryMap.putIfAbsent(fileName, value);
    }

    /**
     * 获取
     * @param field 字段
     */
    public String get(String field){
        return queryMap.get(field);
    }

    /**
     * 判断是否包含 field
     * @param field 字段
     * @return boolean
     */
    public boolean hashKey(String field){
        return queryMap.containsKey(field);
    }

}