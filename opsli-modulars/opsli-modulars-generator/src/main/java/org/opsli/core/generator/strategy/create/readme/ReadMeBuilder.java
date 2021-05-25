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
package org.opsli.core.generator.strategy.create.readme;

import cn.hutool.core.date.DateUtil;
import com.jfinal.kit.Kv;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.common.utils.ZipUtils;
import org.opsli.core.generator.strategy.create.CodeBuilder;
import org.opsli.core.generator.utils.EnjoyUtil;
import org.opsli.modulars.generator.logs.wrapper.GenBuilderModel;

import java.util.HashMap;
import java.util.Map;

/**
 * ReadMe 构建器
 *
 * @author parker
 * @date 2020-09-13 19:36
 */
public enum ReadMeBuilder {

    /** 实例对象 */
    INSTANCE;

    /**
     * 生成 ReadMe
     * @param builderModelTmp Build 模型
     * @param dataStr 数据字符串
     * @return Map
     */
    public Map<String,String> createReadMe(GenBuilderModel builderModelTmp, String dataStr){
        GenBuilderModel builderModel =
                WrapperUtil.transformInstance(builderModelTmp, GenBuilderModel.class, true);

        String codeStr = EnjoyUtil.render("/readme/TemplateReadMe.html",
                this.createKv(builderModel)
        );

        Map<String,String> entityMap = new HashMap<>();
        entityMap.put(ZipUtils.FILE_PATH, CodeBuilder.BASE_PATH + dataStr + "/");
        entityMap.put(ZipUtils.FILE_NAME, "README.md");
        entityMap.put(ZipUtils.FILE_DATA, codeStr);
        return entityMap;
    }

    /**
     * 创建 Kv
     * @param builderModel Build 模型
     * @return Kv
     */
    private Kv createKv(GenBuilderModel builderModel){
        return Kv.by("data", builderModel)
                .set("currTime", DateUtil.now());
    }


}
