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
import org.opsli.modulars.generator.template.entity.GenTemplateDetail;
import org.opsli.modulars.generator.template.wrapper.GenTemplateDetailModel;

import java.util.List;


/**
 * 代码模板详情 Service
 *
 * @author Parker
 * @date 2021-05-28 17:12:38
 */
public interface IGenTemplateDetailService extends CrudServiceInterface<GenTemplateDetail, GenTemplateDetailModel> {

    /**
     * 根据父类ID 删除
     * @param parentId 父类ID
     * @return boolean
     */
    boolean delByParent(String parentId);

    /**
     * 根据父类ID 查询List 集合
     * @param parentId 父类ID
     * @return List
     */
    List<GenTemplateDetailModel> findListByParent(String parentId);

}
