package org.opsli.modulars.test.service.impl;

import org.opsli.api.wrapper.test.TestModel;
import org.opsli.common.annotation.EnableHotData;
import org.opsli.common.annotation.HotDataDel;
import org.opsli.common.annotation.HotDataPut;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.modulars.test.entity.TestEntity;
import org.opsli.modulars.test.mapper.TestMapper;
import org.opsli.modulars.test.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:34
 * @Description: 测试接口
 *
 * 需要开启热点数据的 需要首先 接口 开启 @EnableHotData 注解
 *
 * 然后对 需要处理数据的 方法重写 并且加上 热数据处理 注解
 * -- @HotDataPut 热数据更新     @HotDataDel 热数据删除
 *
 *
 */
@Service
// 开启热数据标示 不加不生效
//@EnableHotData
public class TestServiceImpl extends CrudServiceImpl<TestMapper, TestModel, TestEntity> implements ITestService {

    @Autowired(required = false)
    private TestMapper mapper;

    @Override
    public TestModel getByName(TestModel model) {
        TestEntity entity = super.transformM2T(model);
        return super.transformT2M(
                mapper.getByName(entity));
    }

    // ============== 重写 父类方法 实现热数据操作 ==============

    @Override
    @HotDataPut
    public TestModel insert(TestModel model) {
        return super.insert(model);
    }

    @Override
    @HotDataPut
    public TestModel update(TestModel model) {
        return super.update(model);
    }

    @Override
    @HotDataDel
    public int delete(String id) {
        return super.delete(id);
    }

    @Override
    @HotDataDel
    public int delete(TestModel model) {
        return super.delete(model);
    }

    @Override
    @HotDataDel
    public int deleteAll(String[] ids) {
        return super.deleteAll(ids);
    }

    @Override
    @HotDataDel
    public int deleteAll(Collection<TestModel> models) {
        return super.deleteAll(models);
    }

}


