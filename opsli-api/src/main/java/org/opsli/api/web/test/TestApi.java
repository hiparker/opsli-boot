package org.opsli.api.web.test;

import io.swagger.annotations.ApiOperation;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.wrapper.test.TestModel;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 测试类
 *
 * 对外 API 直接 暴露 @GetMapping 或者 @PostMapping
 * 对内也推荐 单机版 不需要设置 Mapping 但是调用方法得从Controller写起
 *
 * 这样写法虽然比较绕，但是当单体项目想要改造微服务架构时 时非常容易的
 *
 *
 */
public interface TestApi {


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
