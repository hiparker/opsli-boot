/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.modulars.test.service.impl;

import org.opsli.api.wrapper.test.TestModel;
import org.opsli.common.annotation.hotdata.EnableHotData;
import org.opsli.common.annotation.hotdata.HotDataDel;
import org.opsli.common.annotation.hotdata.HotDataPut;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.modulars.test.entity.TestEntity;
import org.opsli.modulars.test.mapper.TestMapper;
import org.opsli.modulars.test.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;


/**
 * 测试接口
 *
 * 需要开启热点数据的 需要首先 接口 开启 @EnableHotData 注解
 *
 * 然后对 需要处理数据的 方法重写 并且加上 热数据处理 注解
 * -- @HotDataPut 热数据更新     @HotDataDel 热数据删除
 *
 * @author Parker
 * @date 2020-09-17 13:01
 */
@Service
@EnableHotData
public class TestServiceImpl extends CrudServiceImpl<TestMapper, TestEntity,TestModel > implements ITestService {

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
    public boolean delete(String id) {
        return super.delete(id);
    }

    @Override
    @HotDataDel
    public boolean delete(TestModel model) {
        return super.delete(model);
    }

    @Override
    @HotDataDel
    public boolean deleteAll(String[] ids) {
        return super.deleteAll(ids);
    }

    @Override
    @HotDataDel
    public boolean deleteAll(Collection<TestModel> models) {
        return super.deleteAll(models);
    }

}


