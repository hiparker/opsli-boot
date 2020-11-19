package org.opsli.modulars.test.web;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.test.TestApi;
import org.opsli.api.wrapper.system.dict.DictWrapper;
import org.opsli.api.wrapper.test.TestModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.concroller.BaseRestController;
import org.opsli.core.cache.pushsub.enums.CacheType;
import org.opsli.core.cache.pushsub.msgs.DictMsgFactory;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.core.utils.DictUtil;
import org.opsli.modulars.test.entity.TestEntity;
import org.opsli.modulars.test.service.ITestService;
import org.opsli.plugins.mail.MailPlugin;
import org.opsli.plugins.mail.model.MailModel;
import org.opsli.plugins.redis.RedisLockPlugins;
import org.opsli.plugins.redis.RedisPlugin;
import org.opsli.plugins.redis.lock.RedisLock;
import org.opsli.plugins.redis.pushsub.entity.BaseSubMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 测试类
 */
@Slf4j
//@ApiRestController("/test")
public class TestRestRestController extends BaseRestController<TestEntity, TestModel, ITestService>
        implements TestApi {


    private Random random = new Random();

    @Autowired
    private MailPlugin mailPlugin;

    @Autowired
    private RedisPlugin redisPlugin;

    @Autowired
    private RedisLockPlugins redisLockPlugins;

    @ApiOperation(value = "发送邮件", notes = "发送邮件")
    @Override
    public ResultVo<?> sendMail(){
        MailModel mailModel = new MailModel();
        mailModel.setTo("meet.carina@foxmail.com");
        mailModel.setSubject("测试邮件功能");
        mailModel.setContent("<h1>这是哪里呢？</h1><br><font color='red'>lalalalalalallalalalalal!!!!</font>");
        mailPlugin.send(mailModel);
        return ResultVo.success("发送邮件成功！！！！！！");
    }


    /**
     * 发送 Redis 订阅消息
     * @return
     */
    @ApiOperation(value = "发送 Redis 订阅消息", notes = "发送 Redis 订阅消息")
    @Override
    public ResultVo<?> sendMsg(){
        DictWrapper model = new DictWrapper();

        BaseSubMessage msg = DictMsgFactory.createMsg(model, CacheType.UPDATE);

        boolean ret = redisPlugin.sendMessage(msg);
        if(ret){
            return ResultVo.success("发送订阅消息成功！！！！！！");
        }
        return ResultVo.error("发送订阅消息失败！！！！！！");
    }


    /**
     * 发送 Redis 测试
     * @return
     */
    @ApiOperation(value = "发送 Redis 测试", notes = "发送 Redis 测试")
    @Override
    public ResultVo<?> redisTest(){
        boolean ret = redisPlugin.put("opsli:test", "12315");
        if(ret){
            Object o = redisPlugin.get("opsli:test");
            return ResultVo.success(o);
        }
        return ResultVo.error("发送订阅消息失败！！！！！！");
    }


    /**
     * 发送 Redis 分布式锁
     * @return
     */
    @ApiOperation(value = "发起 Redis 分布式锁", notes = "发起 Redis 分布式锁")
    @Override
    public ResultVo<RedisLock> testLock(){

        // 锁凭证 redisLock 贯穿全程
        RedisLock redisLock = new RedisLock();
        redisLock.setLockName("aaabbb")
                 .setAcquireTimeOut(2000L)
                 .setLockTimeOut(10000L);

        redisLock = redisLockPlugins.tryLock(redisLock);

        if(redisLock == null){
            ResultVo<RedisLock> error = ResultVo.error(500,"获得锁失败！！！！！！",redisLock);
            return error;
        }

        // 睡眠 模拟线程执行过程
        ThreadUtil.sleep(60, TimeUnit.SECONDS);

        redisLockPlugins.unLock(redisLock);
        ResultVo<RedisLock> success = ResultVo.success("获得锁成功！！！！！！",redisLock);
        success.setData(redisLock);
        return success;
    }

    /**
     * 新增数据
     * @return
     */
    @ApiOperation(value = "新增数据", notes = "新增数据")
    @Override
    public ResultVo<TestModel> insert(TestModel model){
        // 转化对象 处理 ApiModel 与 本地对象

        model.setName("测试名称"+random.nextInt());
        model.setRemark("测试备注"+random.nextInt());

        // 调用新增方法
        TestModel insert = IService.insert(model);

        return ResultVo.success("新增成功",insert);
    }

    /**
     * 修改数据
     * @return
     */
    @ApiOperation(value = "修改数据", notes = "修改数据")
    @Override
    public ResultVo<TestModel> update(TestModel model){
        // 转化对象 处理 ApiModel 与 本地对象

        if(StringUtils.isEmpty(model.getId())){
            model.setId(String.valueOf(random.nextLong()));
        }

        model.setName("修改名称"+random.nextInt());
        model.setRemark("修改备注"+random.nextInt());

        // 不需要做 锁状态处理，需要判断是否成功 能往下走的只能是成功
        TestModel update = IService.update(model);

        return ResultVo.success("修改成功",update);
    }


    /**
     * 查看对象
     * @return
     */
    @ApiOperation(value = "查看对象", notes = "查看对象")
    @Override
    public ResultVo<TestModel> get(TestModel model){
        return ResultVo.success(model);
    }


    /**
     * 删除对象
     * @return
     */
    @ApiOperation(value = "删除对象", notes = "删除对象")
    @Override
    public ResultVo<?> del(String id){

        TestEntity testEntity1 = new TestEntity();
        testEntity1.setId(id);
        String[] ids  = {id};
        List<TestEntity> idList = new ArrayList<>();
        idList.add(testEntity1);


        //count = IService.delete(id);

        //count = IService.delete(testEntity1);

        //count = IService.deleteAll(idList);

        IService.deleteAll(ids);

        return ResultVo.error("删除对象成功");
    }


    /**
     * 删除对象
     * @return
     */
    @ApiOperation(value = "删除全部对象", notes = "删除全部对象")
    @Override
    public ResultVo<?> delAll(){

        //IService.

        int count = 0;

        //count = IService.delete(id);

        //count = IService.delete(testEntity1);

        //count = IService.deleteAll(ids);

        //count = IService.deleteAll(idList);

        return ResultVo.error("删除对象成功");
    }

    @ApiOperation(value = "查找一个集合", notes = "查找一个集合")
    @Override
    public ResultVo<List<TestModel>> findList(HttpServletRequest request) {

        QueryBuilder<TestEntity> queryBuilder = new WebQueryBuilder<>(TestEntity.class, request.getParameterMap());
        List<TestEntity> entitys = IService.findList(queryBuilder.build());
        List<TestModel> models = WrapperUtil.transformInstance(entitys, TestModel.class);

        return ResultVo.success(models);
    }

    @ApiOperation(value = "查找全部数据", notes = "查找全部数据")
    @Override
    public ResultVo<List<TestModel>> findAllList() {
        List<TestEntity> list = IService.findAllList();
        List<TestModel> testModels = WrapperUtil.transformInstance(list, TestModel.class);
        return ResultVo.success(testModels);
    }

    @ApiOperation(value = "查询分页", notes = "查询分页")
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<TestEntity> queryBuilder = new WebQueryBuilder<>(TestEntity.class, request.getParameterMap());
        Page<TestEntity,TestModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultVo.success(page.getBootstrapData());
    }

    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @Override
    public ResultVo<?> exportExcel(HttpServletRequest request, HttpServletResponse response) {
        QueryBuilder<TestEntity> queryBuilder = new WebQueryBuilder<>(TestEntity.class, request.getParameterMap());
        return super.excelExport("测试", queryBuilder.build(), response);
    }

    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @Override
    public ResultVo<?> excelImport(MultipartHttpServletRequest request) {
        return super.excelImport(request);
    }

    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @Override
    public ResultVo<?> importTemplate(HttpServletResponse response) {
        return super.importTemplate("测试", response);
    }


    /**
     * 新增数据
     * @return
     */
    @ApiOperation(value = "批量插入1000个随机新增数据", notes = "批量插入1000个随机新增数据")
    @GetMapping("/insertAll")
    public ResultVo<Boolean> insertAll(){
        List<DictWrapper> testType = DictUtil.getDictList("testType");

        List<TestModel> datas = new ArrayList<>();
        // 转化对象 处理 ApiModel 与 本地对象
        for (int i = 0; i < 1000; i++) {
            int randomNum = RandomUtil.randomInt(0, testType.size());
            TestModel model = new TestModel();
            model.setName("测试名称"+random.nextInt());
            model.setType(testType.get(randomNum).getDictValue());
            model.setRemark("测试备注"+random.nextInt());
            datas.add(model);
        }

        // 调用新增方法
        boolean b = IService.insertBatch(datas);
        return ResultVo.success("新增成功",b);
    }


    @ApiOperation(value = "获得字典 - By Name", notes = "获得字典 - By Name")
    @GetMapping("/getDictByName")
    public ResultVo<?> getDictByName(@RequestParam(name = "typeCode") String typeCode,
                                     @RequestParam(name = "name") String name){
        String value = DictUtil.getDictValueByName(typeCode, name, "我空了");
        return ResultVo.success().setData(value);
    }

    @ApiOperation(value = "获得字典 - By Value", notes = "获得字典 - By Value")
    @GetMapping("/getDictByValue")
    public ResultVo<?> getDictByValue(@RequestParam(name = "typeCode") String typeCode,
                                      @RequestParam(name = "value")  String value){
        String name = DictUtil.getDictNameByValue(typeCode, value, "我空了");
        return ResultVo.success().setData(name);
    }

}
