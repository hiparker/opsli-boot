package org.opsli.api.web.test;

import io.swagger.annotations.ApiOperation;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.wrapper.test.TestModel;
import org.opsli.common.annotation.ApiRestController;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 测试类
 */
@ApiRestController("/test")
public interface ITestApi {


    @ApiOperation(value = "发送邮件", notes = "发送邮件")
    @GetMapping("/sendMail")
    ResultVo sendMail();


    /**
     * 发送 Redis 订阅消息
     * @return
     */
    @ApiOperation(value = "发送 Redis 订阅消息", notes = "发送 Redis 订阅消息")
    @GetMapping("/sendMsg")
    ResultVo sendMsg();


    /**
     * 发送 Redis 订阅消息
     * @return
     */
    @ApiOperation(value = "发送 Redis 测试", notes = "发送 Redis 测试")
    @GetMapping("/redisTest")
    ResultVo redisTest();


    /**
     * 发起 Redis 分布式锁
     * @return
     */
    @ApiOperation(value = "发起 Redis 分布式锁", notes = "发起 Redis 分布式锁")
    @GetMapping("/testLock")
    ResultVo testLock();

    /**
     * 新增数据
     * @return
     */
    @ApiOperation(value = "新增数据", notes = "新增数据")
    @GetMapping("/insert")
    ResultVo insert(TestModel entity);

    /**
     * 修改数据
     * @return
     */
    @ApiOperation(value = "修改数据", notes = "修改数据")
    @GetMapping("/update")
    ResultVo update(TestModel entity);


    /**
     * 查看对象
     * @return
     */
    @ApiOperation(value = "查看对象", notes = "查看对象")
    @GetMapping("/get")
    ResultVo get(TestModel entity);


    /**
     * 删除对象
     * @return
     */
    @ApiOperation(value = "删除对象", notes = "删除对象")
    @GetMapping("/del")
    ResultVo del(String id);


    /**
     * 删除全部对象
     * @return
     */
    @ApiOperation(value = "删除全部对象", notes = "删除全部对象")
    @GetMapping("/delAll")
    ResultVo delAll();


}
