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
package org.opsli.plugins.generator.converter;

import org.opsli.plugins.generator.enums.TypeEnum;

import java.util.List;

/**
 * 将数据库类型转换成各语言对应的类型
 *
 * @author Parker
 * @date 2021年5月26日14:22:31
 */
public interface ColumnTypeConverter {

    /**
     * 将数据库类型转成基本类型
     * @param type 数据库类型
     * @return 基本类型
     */
    String convertType(TypeEnum type);

    /**
     * 将数据库类型转成基本类型 (会多返 一个String 兜底类型)
     * @param type 数据库类型
     * @return List 基本类型
     */
    List<String> convertTypeBySafety(TypeEnum type);

}
