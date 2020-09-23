package org.opsli.modulars.test.service;

import org.opsli.api.wrapper.test.TestModel;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.modulars.test.entity.TestEntity;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-17 13:07
 * @Description: 测试接口
 */
public interface ITestService extends CrudServiceInterface<TestEntity,TestModel> {

    /**
     * 根据名称 获得对象
     * @param model
     * @return
     */
    TestModel getByName(TestModel model);

}
