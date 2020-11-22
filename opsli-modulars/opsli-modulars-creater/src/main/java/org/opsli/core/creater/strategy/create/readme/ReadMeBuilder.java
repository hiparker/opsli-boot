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
package org.opsli.core.creater.strategy.create.readme;

import cn.hutool.core.date.DateUtil;
import com.jfinal.kit.Kv;
import org.opsli.common.utils.HumpUtil;
import org.opsli.common.utils.Props;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.common.utils.ZipUtils;
import org.opsli.core.creater.strategy.create.CodeBuilder;
import org.opsli.core.creater.utils.EnjoyUtil;
import org.opsli.modulars.creater.column.wrapper.CreaterTableColumnModel;
import org.opsli.modulars.creater.createrlogs.wrapper.CreaterBuilderModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.creater.strategy.create.backend
 * @Author: Parker
 * @CreateTime: 2020-11-20 17:30
 * @Description: ReadMe 构建器
 */
public enum ReadMeBuilder {

    INSTANCE;

    /**
     * 生成 ReadMe
     * @param builderModelTmp
     * @return
     */
    public Map<String,String> createReadMe(CreaterBuilderModel builderModelTmp, String dataStr){
        CreaterBuilderModel builderModel =
                WrapperUtil.cloneTransformInstance(builderModelTmp, CreaterBuilderModel.class);

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
     * @param builderModel
     * @return
     */
    private Kv createKv(CreaterBuilderModel builderModel){
        return Kv.by("data", builderModel)
                .set("currTime", DateUtil.now());
    }


}
