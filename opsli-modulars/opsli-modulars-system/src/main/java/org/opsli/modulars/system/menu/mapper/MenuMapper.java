package org.opsli.modulars.system.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.opsli.modulars.system.menu.entity.SysMenu;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.mapper
 * @Author: Parker
 * @CreateTime: 2020-09-17 13:01
 * @Description: 角色 Mapper
 */
@Mapper
public interface MenuMapper extends BaseMapper<SysMenu> {

    /**
     * 唯一验证
     * @param entity
     * @return
     */
    Integer uniqueVerificationByCode(SysMenu entity);

}
