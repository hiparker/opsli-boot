package org.opsli.modulars.system.user.service;

import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.modulars.system.user.entity.SysUser;

import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-17 13:07
 * @Description: 用户 接口
 */
public interface IUserService extends CrudServiceInterface<SysUser, UserModel> {

    /**
     * 根据 用户名 获得当前用户
     * @param username
     * @return
     */
    UserModel queryByUserName(String username);

    /**
     * 根据用户ID 获得当前角色编码集合
     * @param userId
     * @return
     */
    List<String> getRoleCodeList(String userId);

    /**
     * 根据用户ID 获得权限
     * @param userId
     * @return
     */
    List<String> getAllPerms(String userId);

    /**
     * 根据用户ID 获得菜单集合
     * @param userId
     * @return
     */
    List<MenuModel> getMenuListByUserId(String userId);

}
