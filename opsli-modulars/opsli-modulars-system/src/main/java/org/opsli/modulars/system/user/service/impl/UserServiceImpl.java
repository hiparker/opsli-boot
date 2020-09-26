package org.opsli.modulars.system.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.HumpUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.mapper.UserMapper;
import org.opsli.modulars.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:34
 * @Description: 角色 接口实现类
 */
@Service
public class UserServiceImpl extends CrudServiceImpl<UserMapper, SysUser, UserModel> implements IUserService {

    @Autowired(required = false)
    private UserMapper mapper;

    @Override
    public UserModel insert(UserModel model) {
        if(model == null) return null;

        SysUser entity = super.transformM2T(model);
        // 唯一验证
        Integer count = mapper.uniqueVerificationByUsername(entity);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_USER_UNIQUE);
        }

        return super.insert(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserModel update(UserModel model) {
        if(model == null) return null;

        SysUser entity = super.transformM2T(model);
        // 唯一验证
        Integer count = mapper.uniqueVerificationByUsername(entity);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_USER_UNIQUE);
        }

        UserModel update = super.update(model);
        if(update != null){
            // 刷新用户缓存
            UserUtil.refreshUser(update);
        }

        return update;
    }

    @Override
    public UserModel queryByUserName(String username) {
        String key = HumpUtil.humpToUnderline("username");
        QueryBuilder<SysUser> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysUser> queryWrapper = queryBuilder.build();
        queryWrapper.eq(key, username);
        SysUser user = this.getOne(queryWrapper);
        return super.transformT2M(user);
    }

    @Override
    public List<String> getRoleCodeList(String userId) {
        return mapper.getRoleCodeList(userId);
    }

    @Override
    public List<String> getAllPerms(String userId) {
        return mapper.queryAllPerms(userId);
    }

    @Override
    public List<MenuModel> getMenuListByUserId(String userId) {
        List<SysMenu> menuList = mapper.findMenuListByUserId(userId);
        return WrapperUtil.transformInstance(menuList, MenuModel.class);
    }
}


