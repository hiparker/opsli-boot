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

package org.opsli.modulars.generator.template.service;


import org.opsli.core.base.service.interfaces.CrudServiceInterface;



import org.opsli.modulars.generator.template.entity.GenTemplate;
import org.opsli.modulars.generator.template.wrapper.GenTemplateAndDetailModel;
import org.opsli.modulars.generator.template.wrapper.GenTemplateCopyModel;
import org.opsli.modulars.generator.template.wrapper.GenTemplateModel;


/**
 * 代码模板 Service
 *
 * @author Parker
 * @date 2021-05-27 14:33:49
 */
public interface IGenTemplateService extends CrudServiceInterface<GenTemplate, GenTemplateModel> {

    /**
     * 新增 且 操作明细数据
     * @param model model
     * @return GenTemplateModel
     */
    GenTemplateModel insertAndDetail(GenTemplateAndDetailModel model);

    /**
     * 修改 且 操作明细数据
     * @param model model
     * @return GenTemplateModel
     */
    GenTemplateModel updateAndDetail(GenTemplateAndDetailModel model);

    /**
     * 复制
     * @param model 模型
     * @return model
     */
    GenTemplateModel copy(GenTemplateCopyModel model);
}
