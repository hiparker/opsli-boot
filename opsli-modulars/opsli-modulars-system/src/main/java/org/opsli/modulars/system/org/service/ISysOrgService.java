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
package org.opsli.modulars.system.org.service;

import org.opsli.core.base.entity.HasChildren;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;


import org.opsli.modulars.system.org.entity.SysOrg;
import org.opsli.api.wrapper.system.org.SysOrgModel;

import java.util.List;
import java.util.Set;

/**
 * 组织机构 Service
 *
 * @author Parker
 * @date 2021-02-07 18:24:38
 */
public interface ISysOrgService extends CrudServiceInterface<SysOrg, SysOrgModel> {

    /**
     * 是否有下级
     * @param parentIds 父节点Id Set
     * @return List
     */
    List<HasChildren> hasChildren(Set<String> parentIds);

}
