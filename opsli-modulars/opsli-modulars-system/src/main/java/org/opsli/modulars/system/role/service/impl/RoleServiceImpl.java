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
package org.opsli.modulars.system.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.chain.TenantHandler;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.role.entity.SysRole;
import org.opsli.modulars.system.role.mapper.RoleMapper;
import org.opsli.modulars.system.role.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:34
 * @Description: 角色 接口实现类
 */
@Service
public class RoleServiceImpl extends CrudServiceImpl<RoleMapper, SysRole, RoleModel> implements IRoleService {

    @Autowired(required = false)
    private RoleMapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleModel insert(RoleModel model) {
        if(model == null) return null;

        SysRole entity = super.transformM2T(model);
        // 唯一验证
        QueryBuilder<SysRole> queryBuilder = new GenQueryBuilder<>();
        // 多租户处理
        TenantHandler tenantHandler = new TenantHandler();
        QueryWrapper<SysRole> qWrapper = tenantHandler.handler(entityClazz, queryBuilder.build());
        Integer count = mapper.uniqueVerificationByCode(entity,qWrapper);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_UNIQUE);
        }

        return super.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleModel update(RoleModel model) {
        if(model == null) return null;

        SysRole entity = super.transformM2T(model);
        // 唯一验证
        QueryBuilder<SysRole> queryBuilder = new GenQueryBuilder<>();
        // 多租户处理
        TenantHandler tenantHandler = new TenantHandler();
        QueryWrapper<SysRole> qWrapper = tenantHandler.handler(entityClazz, queryBuilder.build());
        Integer count = mapper.uniqueVerificationByCode(entity,qWrapper);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_UNIQUE);
        }

        return super.update(model);
    }


}


