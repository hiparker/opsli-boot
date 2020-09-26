package org.opsli.modulars.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.opsli.modulars.system.role.entity.SysRole;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.mapper
 * @Author: Parker
 * @CreateTime: 2020-09-17 13:01
 * @Description: 角色 Mapper
 */
@Mapper
public interface RoleMapper extends BaseMapper<SysRole> {

    /**
     * 唯一验证
     * @param entity
     * @return
     */
    Integer uniqueVerificationByCode(SysRole entity);

}
