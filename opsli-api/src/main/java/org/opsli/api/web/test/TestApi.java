package org.opsli.api.web.test;

import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.wrapper.test.TestModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * @return ResultWrapper
     */
    @GetMapping("/sendMail")
    ResultWrapper<?> sendMail();


    /**
     * 发送 Redis 订阅消息
     * @return ResultWrapper
     */
    @GetMapping("/sendMsg")
    ResultWrapper<?> sendMsg();


    /**
     * 发送 Redis 订阅消息
     * @return ResultWrapper
     */
    @GetMapping("/redisTest")
    ResultWrapper<?> redisTest();


    /**
     * 发起 Redis 分布式锁
     * @return ResultWrapper
     */
    @GetMapping("/testLock")
    ResultWrapper<?> testLock();

    /**
     * 新增数据
     * @param entity entity
     * @return ResultWrapper
     */
    @GetMapping("/insert")
    ResultWrapper<TestModel> insert(TestModel entity);

    /**
     * 修改数据
     * @param entity entity
     * @return ResultWrapper
     */
    @GetMapping("/update")
    ResultWrapper<TestModel> update(TestModel entity);


    /**
     * 查看对象
     * @param entity entity
     * @return ResultWrapper
     */
    @GetMapping("/get")
    ResultWrapper<TestModel> get(TestModel entity);


    /**
     * 删除对象
     * @param id id
     * @return ResultWrapper
     */
    @GetMapping("/del")
    ResultWrapper<?> del(String id);


    /**
     * 删除全部对象
     * @return ResultWrapper
     */
    @GetMapping("/delAll")
    ResultWrapper<?> delAll();


    /**
     * 查找一个集合
     * @param request request
     * @return ResultWrapper
     */
    @GetMapping("/findList")
    ResultWrapper<List<TestModel>> findList(HttpServletRequest request);


    /**
     * 查找一个全部集合
     * @return ResultWrapper
     */
    @GetMapping("/findAllList")
    ResultWrapper<List<TestModel>> findAllList();


    /**
     * 查找一个分页
     *
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultWrapper
     */
    @GetMapping("/findPage")
    ResultWrapper<?> findPage(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    );


    /**
     * Excel 导出认证
     *
     * @param type 类型
     * @param request request
     */
    @GetMapping("/excel/auth/{type}")
    ResultWrapper<String> exportExcelAuth(
            @PathVariable("type") String type,
            HttpServletRequest request);

    /**
     * Excel 导出
     *
     * @param certificate 凭证
     * @param response response
     */
    @GetMapping("/excel/export/{certificate}")
    void exportExcel(
            @PathVariable("certificate") String certificate,
            HttpServletResponse response);

    /**
     * Excel 导入
     *
     * @param request request
     * @return ResultWrapper
     */
    @PostMapping("/importExcel")
    ResultWrapper<?> importExcel(MultipartHttpServletRequest request);



}
