package org.opsli.api.web.test;

import io.swagger.annotations.ApiOperation;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.wrapper.test.TestModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


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


    @GetMapping("/sendMail")
    ResultVo<?> sendMail();


    /**
     * 发送 Redis 订阅消息
     * @return
     */
    @GetMapping("/sendMsg")
    ResultVo<?> sendMsg();


    /**
     * 发送 Redis 订阅消息
     * @return
     */
    @GetMapping("/redisTest")
    ResultVo<?> redisTest();


    /**
     * 发起 Redis 分布式锁
     * @return
     */
    @GetMapping("/testLock")
    ResultVo<?> testLock();

    /**
     * 新增数据
     * @return
     */
    @GetMapping("/insert")
    ResultVo<TestModel> insert(TestModel entity);

    /**
     * 修改数据
     * @return
     */
    @GetMapping("/update")
    ResultVo<TestModel> update(TestModel entity);


    /**
     * 查看对象
     * @return
     */
    @GetMapping("/get")
    ResultVo<TestModel> get(TestModel entity);


    /**
     * 删除对象
     * @return
     */
    @GetMapping("/del")
    ResultVo<?> del(String id);


    /**
     * 删除全部对象
     * @return
     */
    @GetMapping("/delAll")
    ResultVo<?> delAll();


    /**
     * 查找一个集合
     * @return
     */
    @GetMapping("/findList")
    ResultVo<List<TestModel>> findList(HttpServletRequest request);


    /**
     * 查找一个全部集合
     * @return
     */
    @GetMapping("/findAllList")
    ResultVo<List<TestModel>> findAllList();


    /**
     * 查找一个分页
     * @return
     */
    @GetMapping("/findPage")
    ResultVo<?> findPage(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    );


    /**
     * Excel 导出
     * @return
     */
    @GetMapping("/exportExcel")
    void exportExcel(HttpServletRequest request, HttpServletResponse response);

    /**
     * Excel 导入
     * @return
     */
    @PostMapping("/importExcel")
    ResultVo<?> importExcel(MultipartHttpServletRequest request);

    /**
     * Excel 下载导入模版
     * @return
     */
    @GetMapping("/importExcel/template")
    void importTemplate(HttpServletResponse response);

}
