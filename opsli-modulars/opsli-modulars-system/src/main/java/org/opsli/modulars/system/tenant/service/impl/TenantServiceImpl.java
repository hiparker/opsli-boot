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
package org.opsli.modulars.system.tenant.service.impl;

import org.opsli.api.wrapper.system.tenant.TenantModel;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.tenant.entity.SysTenant;
import org.opsli.modulars.system.tenant.mapper.TenantMapper;
import org.opsli.modulars.system.tenant.service.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.system.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:34
 * @Description: 租户 接口实现类
 */
@Service
public class TenantServiceImpl extends CrudServiceImpl<TenantMapper, SysTenant, TenantModel> implements ITenantService {

    @Autowired(required = false)
    private TenantMapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TenantModel insert(TenantModel model) {
        if(model == null){
            return null;
        }

        SysTenant entity = super.transformM2T(model);
        // 唯一验证
        Integer count = mapper.uniqueVerificationByName(entity);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_TENANT_UNIQUE);
        }

        return super.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TenantModel update(TenantModel model) {
        if(model == null){
            return null;
        }

        SysTenant entity = super.transformM2T(model);
        // 唯一验证
        Integer count = mapper.uniqueVerificationByName(entity);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_TENANT_UNIQUE);
        }

        return super.update(model);
    }


}


