package org.opsli.modulars.system.role.service.impl;

import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.base.service.impl.CrudServiceImpl;
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
    public RoleModel insert(RoleModel model) {
        if(model == null) return null;

        SysRole entity = super.transformM2T(model);
        // 唯一验证
        Integer count = mapper.uniqueVerificationByCode(entity);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_UNIQUE);
        }

        return super.insert(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoleModel update(RoleModel model) {
        if(model == null) return null;

        SysRole entity = super.transformM2T(model);
        // 唯一验证
        Integer count = mapper.uniqueVerificationByCode(entity);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_UNIQUE);
        }

        return super.update(model);
    }

}


