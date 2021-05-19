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
package org.opsli.plugins.excel.listener;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 监听
 *
 * @author Parker
 * @date 2020-09-16 11:47
 */
@Slf4j
public abstract class BatchExcelListener<T> extends AnalysisEventListener<T> {

    private static final String SERIAL_VERSION_UID = "serialVersionUID";

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 2000;

    private final List<Object> dataList = new ArrayList<>();

    /**
     * 钩子函数 需要调用方重写 保存方法
     * @param dataList 数据集合
     */
    abstract public void saveData(List<T> dataList);

    /**
     * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
     */
    @Override
    public void invoke(Object object, AnalysisContext context) {
        if(!checkObjAllFieldsIsNull(object)) {
            dataList.add(object);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if (dataList.size() >= BATCH_COUNT) {
                saveData((List<T>) dataList);
                // 存储完成清理 list
                dataList.clear();
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData((List<T>) dataList);
    }


    /**
     * 判断对象中属性值是否全为空
     */
    private static boolean checkObjAllFieldsIsNull(Object object) {
        if (null == object) {
            return true;
        }
        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                //只校验带ExcelProperty注解的属性
                ExcelProperty property = f.getAnnotation(ExcelProperty.class);
                if(property == null || SERIAL_VERSION_UID.equals(f.getName())){
                    continue;
                }
                if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
                    return false;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //do something
        }
        return true;
    }

}
