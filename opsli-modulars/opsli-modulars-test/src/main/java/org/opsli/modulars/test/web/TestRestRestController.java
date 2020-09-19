package org.opsli.modulars.test.web;

import cn.hutool.core.thread.ThreadUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.wrapper.test.TestModel;
import org.opsli.api.web.test.TestApi;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.base.concroller.BaseRestController;
import org.opsli.core.cache.pushsub.enums.CacheType;
import org.opsli.core.cache.pushsub.msgs.DictMsgFactory;
import org.opsli.modulars.test.entity.TestEntity;
import org.opsli.modulars.test.service.ITestService;
import org.opsli.plugins.mail.MailPlugin;
import org.opsli.plugins.mail.model.MailModel;
import org.opsli.plugins.redis.RedisLockPlugins;
import org.opsli.plugins.redis.RedisPlugin;
import org.opsli.plugins.redis.lock.RedisLock;
import org.opsli.plugins.redis.pushsub.entity.BaseSubMessage;
import org.springframework.beans.factory.annotation.Autowired;

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
@ApiRestController("/test")
public class TestRestRestController extends BaseRestController<TestModel, TestEntity, ITestService>
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
    public ResultVo sendMail(){
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
    public ResultVo sendMsg(){

        BaseSubMessage msg = DictMsgFactory.createMsg("test", "aaa", 123213, CacheType.UPDATE);

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
    public ResultVo redisTest(){
        boolean ret = redisPlugin.put("opsli:test", "12315");
        if(ret){
            Object o = redisPlugin.get("opsli:test");
            ResultVo resultVo = new ResultVo();
            resultVo.put("data",o);
            return resultVo;
        }
        return ResultVo.error("发送订阅消息失败！！！！！！");
    }


    /**
     * 发送 Redis 分布式锁
     * @return
     */
    @ApiOperation(value = "发起 Redis 分布式锁", notes = "发起 Redis 分布式锁")
    @Override
    public ResultVo testLock(){

        // 锁凭证 redisLock 贯穿全程
        RedisLock redisLock = new RedisLock();
        redisLock.setLockName("aaabbb")
                 .setAcquireTimeOut(2000L)
                 .setLockTimeOut(10000L);

        redisLock = redisLockPlugins.tryLock(redisLock);

        if(redisLock == null){
            ResultVo error = ResultVo.error("获得锁失败！！！！！！");
            error.put("redisLock",redisLock);
            return error;
        }

        // 睡眠 模拟线程执行过程
        ThreadUtil.sleep(60, TimeUnit.SECONDS);

        redisLockPlugins.unLock(redisLock);
        ResultVo success = ResultVo.success("获得锁成功！！！！！！");
        success.put("redisLock",redisLock);
        return success;
    }

    /**
     * 新增数据
     * @return
     */
    @ApiOperation(value = "新增数据", notes = "新增数据")
    @Override
    public ResultVo insert(TestModel model){
        // 转化对象 处理 ApiModel 与 本地对象

        model.setName("测试名称"+random.nextInt());
        model.setRemark("测试备注"+random.nextInt());

        // 调用新增方法
        TestModel insert = IService.insert(model);

        ResultVo resultVo = new ResultVo();
        resultVo.setMsg("新增成功");
        resultVo.put("data",insert);
        return resultVo;
    }

    /**
     * 修改数据
     * @return
     */
    @ApiOperation(value = "修改数据", notes = "修改数据")
    @Override
    public ResultVo update(TestModel model){
        // 转化对象 处理 ApiModel 与 本地对象

        if(StringUtils.isEmpty(model.getId())){
            model.setId(String.valueOf(random.nextLong()));
        }

        model.setName("修改名称"+random.nextInt());
        model.setRemark("修改备注"+random.nextInt());

        // 不需要做 锁状态处理，需要判断是否成功 能往下走的只能是成功
        TestModel update = IService.update(model);


        ResultVo resultVo = new ResultVo();
        resultVo.setMsg("修改成功");
        resultVo.put("data",update);
        return resultVo;
    }


    /**
     * 查看对象
     * @return
     */
    @ApiOperation(value = "查看对象", notes = "查看对象")
    @Override
    public ResultVo get(TestModel model){
        // 转化对象 处理 ApiModel 与 本地对象
        ResultVo resultVo = new ResultVo();
        resultVo.put("data",model);
        return resultVo;
    }


    /**
     * 删除对象
     * @return
     */
    @ApiOperation(value = "删除对象", notes = "删除对象")
    @Override
    public ResultVo del(String id){

        TestEntity testEntity1 = new TestEntity();
        testEntity1.setId(id);
        String[] ids  = {id};
        List<TestEntity> idList = new ArrayList<>();
        idList.add(testEntity1);


        //count = IService.delete(id);

        //count = IService.delete(testEntity1);

        //count = IService.deleteAll(idList);


        IService.deleteAll(ids);

        ResultVo resultVo = new ResultVo();
        resultVo.put("data",id);
        resultVo.setMsg("删除对象成功");
        return resultVo;
    }


    /**
     * 删除对象
     * @return
     */
    @ApiOperation(value = "删除全部对象", notes = "删除全部对象")
    @Override
    public ResultVo delAll(){

        //IService.

        int count = 0;

        //count = IService.delete(id);

        //count = IService.delete(testEntity1);

        //count = IService.deleteAll(ids);

        //count = IService.deleteAll(idList);




        ResultVo resultVo = new ResultVo();
        resultVo.setMsg("删除对象成功");
        return resultVo;
    }


}
