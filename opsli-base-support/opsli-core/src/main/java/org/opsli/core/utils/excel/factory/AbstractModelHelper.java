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
package org.opsli.core.utils.excel.factory;


import cn.hutool.json.JSONObject;
import org.opsli.api.base.warpper.ApiWrapper;

/**
 *
 * 抽象 实体助手
 *
 * @author Parker
 * @date 2020-09-16
 */
public abstract class AbstractModelHelper {

    /**
     * 抽象 创建导入对象
     *
     * @param dictJson 字典Json
     * @param wrapper 导入 wrapper
     */
    abstract public void transformByImport(JSONObject dictJson, ApiWrapper wrapper);

    /**
     * 抽象 创建导出对象
     *
     * @param dictJson 字典Json
     * @param wrapper 导出 wrapper
     */
    abstract public void transformByExport(JSONObject dictJson, ApiWrapper wrapper);

}
