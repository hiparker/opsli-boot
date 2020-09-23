package org.opsli.modulars.system.dict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.opsli.modulars.system.dict.entity.SysDict;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.mapper
 * @Author: Parker
 * @CreateTime: 2020-09-17 13:01
 * @Description: 数据字典 Mapper
 */
@Mapper
public interface DictMapper extends BaseMapper<SysDict> {

    /**
     * 唯一验证
     * @param entity
     * @return
     */
    Integer uniqueVerificationByCode(SysDict entity);

}
