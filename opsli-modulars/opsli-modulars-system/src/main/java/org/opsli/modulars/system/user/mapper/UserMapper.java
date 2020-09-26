package org.opsli.modulars.system.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.user.entity.SysUser;

import java.util.List;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.mapper
 * @Author: Parker
 * @CreateTime: 2020-09-17 13:01
 * @Description: 用户 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {

    /**
     * 唯一验证
     * @param entity
     * @return
     */
    Integer uniqueVerificationByUsername(SysUser entity);


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
    List<String> queryAllPerms(String userId);

    /**
     * 根据用户ID 获得菜单集合
     * @param userId
     * @return
     */
    List<SysMenu> findMenuListByUserId(String userId);
}
