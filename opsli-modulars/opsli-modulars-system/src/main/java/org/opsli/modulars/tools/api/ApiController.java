package org.opsli.modulars.tools.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.ApiVersion;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * API 版本控制测试
 *
 * @author Pace
 * @date 2021年10月27日12:50:00
 */

@Tag(name = "API-测试版本控制")
@ApiRestController("/{ver}/tools/api")
public class ApiController {


    @Operation(summary = "测试正常接口")
    @GetMapping("/test")
    public String test() {
        return "test 1";
    }

    @Operation(summary = ">= V1 && <= V4")
    @GetMapping("/fun")
    public String fun1() {
        return "fun 1";
    }


    @Operation(summary = ">= V5 && <= V8")
    @ApiVersion(5)
    @GetMapping("/fun")
    public String fun2() {
        return "fun 2";
    }


    @Operation(summary = ">= V9")
    @ApiVersion(9)
    @GetMapping("/fun")
    public String fun3() {
        return "fun 5";
    }

}
