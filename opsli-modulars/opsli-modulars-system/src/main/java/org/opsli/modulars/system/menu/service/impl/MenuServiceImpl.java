package org.opsli.modulars.system.menu.service.impl;

import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.menu.mapper.MenuMapper;
import org.opsli.modulars.system.menu.service.IMenuService;
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
public class MenuServiceImpl extends CrudServiceImpl<MenuMapper, SysMenu, MenuModel> implements IMenuService {

    @Autowired(required = false)
    private MenuMapper mapper;

    @Override
    public MenuModel insert(MenuModel model) {
        if(model == null) return null;

        SysMenu entity = super.transformM2T(model);
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
    public MenuModel update(MenuModel model) {
        if(model == null) return null;

        SysMenu entity = super.transformM2T(model);
        // 唯一验证
        Integer count = mapper.uniqueVerificationByCode(entity);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_UNIQUE);
        }

        return super.update(model);
    }

}


