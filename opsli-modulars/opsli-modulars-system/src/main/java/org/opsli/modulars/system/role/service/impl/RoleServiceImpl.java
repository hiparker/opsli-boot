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

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.persistence.querybuilder.chain.TenantHandler;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.role.entity.SysRole;
import org.opsli.modulars.system.role.mapper.RoleMapper;
import org.opsli.modulars.system.role.service.IRoleMenuRefService;
import org.opsli.modulars.system.role.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


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
    @Autowired
    private IRoleMenuRefService iRoleMenuRefService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleModel insert(RoleModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        Integer count = this.uniqueVerificationByCode(model);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_UNIQUE);
        }

        return super.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleModel update(RoleModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        Integer count = this.uniqueVerificationByCode(model);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_UNIQUE);
        }

        return super.update(model);
    }

    @Override
    public boolean delete(String id) {
        // 删除角色下 权限
        iRoleMenuRefService.delPermsByRoleIds(Convert.toList(String.class, id));
        return super.delete(id);
    }

    @Override
    public boolean deleteAll(String[] ids) {
        // 删除角色下 权限
        iRoleMenuRefService.delPermsByRoleIds(Convert.toList(String.class, ids));
        return super.deleteAll(ids);
    }

    @Override
    public boolean delete(RoleModel model) {
        if(model == null){
            return false;
        }
        // 删除角色下 权限
        iRoleMenuRefService.delPermsByRoleIds(Convert.toList(String.class, model.getId()));
        return super.delete(model);
    }

    @Override
    public boolean deleteAll(Collection<RoleModel> models) {
        List<String> roleIds = Lists.newArrayList();
        for (RoleModel model : models) {
            roleIds.add(model.getId());
        }
        // 删除角色下 权限
        iRoleMenuRefService.delPermsByRoleIds(roleIds);
        return super.deleteAll(models);
    }

    /**
     * 唯一验证
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public Integer uniqueVerificationByCode(RoleModel model){
        if(model == null){
            return null;
        }
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();

        // code 唯一
        wrapper.eq(MyBatisConstants.FIELD_DELETE_LOGIC,  DictType.NO_YES_NO.getCode())
                .eq("role_code", model.getRoleCode());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        // 租户检测
        wrapper = new TenantHandler().handler(super.entityClazz, wrapper);

        return super.count(wrapper);
    }


}


