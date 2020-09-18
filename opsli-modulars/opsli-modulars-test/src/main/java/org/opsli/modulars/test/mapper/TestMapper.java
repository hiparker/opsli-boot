package org.opsli.modulars.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.opsli.modulars.test.entity.TestEntity;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.mapper
 * @Author: Parker
 * @CreateTime: 2020-09-17 13:01
 * @Description: TODO
 */
@Mapper
public interface TestMapper extends BaseMapper<TestEntity> {

    TestEntity getByName(TestEntity testEntity);

}
