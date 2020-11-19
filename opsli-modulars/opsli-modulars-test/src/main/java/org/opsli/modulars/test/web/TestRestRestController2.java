package org.opsli.modulars.test.web;

import io.swagger.annotations.ApiOperation;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.test.TestApi;
import org.opsli.api.wrapper.test.TestModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.constants.CacheConstants;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.plugins.cache.EhCachePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 测试类
 */
//@ApiRestController("/test2")
public class TestRestRestController2{

    @Autowired
    TestApi testApi;
    @Autowired
    EhCachePlugin ehCachePlugin;

    @ApiOperation(value = "测试2", notes = "测试2")
    @GetMapping("/getDictBy")
    public ResultVo<?> t1(){
        String id = "test";
        List<TestModel> testModelList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestModel testModel = new TestModel();
            testModel.setId(id+i);
            testModel.setName("测试数据"+i);
            testModelList.add(testModel);
            CacheUtil.put(id+i,testModel);
        }
        return ResultVo.success(testModelList);
    }


}
