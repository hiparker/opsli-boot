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
package org.opsli.modulars.system.tenant.service;

import org.opsli.api.wrapper.system.tenant.TenantModel;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.modulars.system.tenant.entity.SysTenant;


/**
 * 租户 Service
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
public interface ITenantService extends CrudServiceInterface<SysTenant, TenantModel> {


    /**
     * 变更租户状态
     *
     * @param tenantId 租户ID
     * @param enable 启用状态
     * @return boolean
     */
    boolean enableTenant(String tenantId, String enable);


}
