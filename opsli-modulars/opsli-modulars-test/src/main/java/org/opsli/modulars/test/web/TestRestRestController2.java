package org.opsli.modulars.test.web;

import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.test.TestApi;
import org.opsli.api.wrapper.test.TestModel;
import org.opsli.common.annotation.ApiRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 测试类
 */
@ApiRestController("/test2")
public class TestRestRestController2{

    @Autowired
    TestApi testApi;

    @GetMapping("/insert2")
    public ResultVo insert(){
        return testApi.insert(new TestModel());
    }


}
