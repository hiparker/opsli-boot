package org.opsli.modulars.test.service;

import org.opsli.common.annotation.HotDataDel;
import org.opsli.common.annotation.HotDataPut;
import org.opsli.modulars.test.entity.TestEntity;
import org.springframework.stereotype.Service;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:34
 * @Description: 测试接口
 */
@Service
public class TestService {

    /**
     * 测试 存入热点数据
     * @param testEntity
     * @return
     */
    @HotDataPut
    public TestEntity save(TestEntity testEntity){


        return testEntity;
    }


    /**
     * 测试 存入热点数据
     * @param id
     * @return
     */
    @HotDataDel
    public TestEntity del(String id){
        TestEntity t = new TestEntity();
        t.setId(id);

        return t;
    }

}
