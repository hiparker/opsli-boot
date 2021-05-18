package org.opsli.api.web.test;

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
 * 测试类
 *
 * 对外 API 直接 暴露 @GetMapping 或者 @PostMapping
 * 对内也推荐 单机版 不需要设置 Mapping 但是调用方法得从Controller写起
 *
 * 这样写法虽然比较绕，但是当单体项目想要改造微服务架构时 时非常容易的
 *
 * @author Parker
 * @date 2020-09-13 17:40
 */
public interface TestApi {


    /**
     * 发送邮件
     * @return ResultVo
     */
    @GetMapping("/sendMail")
    ResultVo<?> sendMail();


    /**
     * 发送 Redis 订阅消息
     * @return ResultVo
     */
    @GetMapping("/sendMsg")
    ResultVo<?> sendMsg();


    /**
     * 发送 Redis 订阅消息
     * @return ResultVo
     */
    @GetMapping("/redisTest")
    ResultVo<?> redisTest();


    /**
     * 发起 Redis 分布式锁
     * @return ResultVo
     */
    @GetMapping("/testLock")
    ResultVo<?> testLock();

    /**
     * 新增数据
     * @param entity entity
     * @return ResultVo
     */
    @GetMapping("/insert")
    ResultVo<TestModel> insert(TestModel entity);

    /**
     * 修改数据
     * @param entity entity
     * @return ResultVo
     */
    @GetMapping("/update")
    ResultVo<TestModel> update(TestModel entity);


    /**
     * 查看对象
     * @param entity entity
     * @return ResultVo
     */
    @GetMapping("/get")
    ResultVo<TestModel> get(TestModel entity);


    /**
     * 删除对象
     * @param id id
     * @return ResultVo
     */
    @GetMapping("/del")
    ResultVo<?> del(String id);


    /**
     * 删除全部对象
     * @return ResultVo
     */
    @GetMapping("/delAll")
    ResultVo<?> delAll();


    /**
     * 查找一个集合
     * @param request request
     * @return ResultVo
     */
    @GetMapping("/findList")
    ResultVo<List<TestModel>> findList(HttpServletRequest request);


    /**
     * 查找一个全部集合
     * @return ResultVo
     */
    @GetMapping("/findAllList")
    ResultVo<List<TestModel>> findAllList();


    /**
     * 查找一个分页
     *
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultVo
     */
    @GetMapping("/findPage")
    ResultVo<?> findPage(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    );


    /**
     * Excel 导出
     *
     * @param request request
     * @param response response
     */
    @GetMapping("/exportExcel")
    void exportExcel(HttpServletRequest request, HttpServletResponse response);

    /**
     * Excel 导入
     *
     * @param request request
     * @return ResultVo
     */
    @PostMapping("/importExcel")
    ResultVo<?> importExcel(MultipartHttpServletRequest request);

    /**
     * Excel 下载导入模版
     *
     * @param response response
     */
    @GetMapping("/importExcel/template")
    void importTemplate(HttpServletResponse response);

}
