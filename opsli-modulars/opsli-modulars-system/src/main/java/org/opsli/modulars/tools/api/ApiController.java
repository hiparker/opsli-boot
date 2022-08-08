package org.opsli.modulars.tools.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.ApiVersion;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * API 版本控制测试
 *
 * @author Parker
 * @date 2021年10月27日12:50:00
 */
@Api(tags = "API-测试版本控制")
@ApiRestController("/{ver}/tools/api")
public class ApiController {

    @ApiOperation(value = "测试正常接口", notes = "测试正常接口")
    @GetMapping("/test")
    public String test() {
        return "test 1";
    }

    @ApiOperation(value = ">= V1 && <= V4", notes = ">= V1 && <= V4")
    @GetMapping("/fun")
    public String fun1() {
        return "fun 1";
    }


    @ApiOperation(value = ">= V5 && <= V8", notes = ">= V5 && <= V8")
    @ApiVersion(5)
    @GetMapping("/fun")
    public String fun2() {
        return "fun 2";
    }


    @ApiOperation(value = ">= V9", notes = ">= V9")
    @ApiVersion(9)
    @GetMapping("/fun")
    public String fun3() {
        return "fun 5";
    }

}
