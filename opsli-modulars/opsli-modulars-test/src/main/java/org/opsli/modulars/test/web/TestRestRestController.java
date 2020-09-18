package org.opsli.modulars.test.web;

import cn.hutool.core.thread.ThreadUtil;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.api.ResultVo;
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
import org.springframework.web.bind.annotation.GetMapping;

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
public class TestRestRestController extends BaseRestController<TestEntity, ITestService> {


    private Random random = new Random();

    @Autowired
    private MailPlugin mailPlugin;

    @Autowired
    private RedisPlugin redisPlugin;

    @Autowired
    private RedisLockPlugins redisLockPlugins;


    @GetMapping("/sendMail")
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
    @GetMapping("/sendMsg")
    public ResultVo sendMsg(){

        BaseSubMessage msg = DictMsgFactory.createMsg("test", "aaa", 123213, CacheType.UPDATE);

        boolean ret = redisPlugin.sendMessage(msg);
        if(ret){
            return ResultVo.success("发送订阅消息成功！！！！！！");
        }
        return ResultVo.error("发送订阅消息失败！！！！！！");
    }


    /**
     * 发送 Redis 订阅消息
     * @return
     */
    @GetMapping("/redisTest")
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
    @GetMapping("/testLock")
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
    @GetMapping("/insert")
    public ResultVo insert(TestEntity entity){


        entity.setId(null);
        entity.setName("测试名称"+random.nextInt());
        entity.setRemark("测试备注"+random.nextInt());

        TestEntity saveObj = IService.insert(entity);
        if(saveObj == null){
            ResultVo success = ResultVo.error("新增对象失败！");
            success.put("object",entity);
            return success;
        }

        ResultVo success = ResultVo.success("新增对象成功！");
        success.put("object",saveObj);

        return success;
    }

    /**
     * 修改数据
     * @return
     */
    @GetMapping("/update")
    public ResultVo update(TestEntity entity){

        if(StringUtils.isEmpty(entity.getId())){
            entity.setId(String.valueOf(random.nextLong()));
        }

        entity.setName("修改名称"+random.nextInt());
        entity.setRemark("修改备注"+random.nextInt());

        TestEntity saveObj = IService.update(entity);
        if(saveObj == null){
            ResultVo success = ResultVo.error("修改对象失败！");
            success.put("object",entity);
            return success;
        }

        ResultVo success = ResultVo.success("修改对象成功！");
        success.put("object",saveObj);

        return success;
    }


    /**
     * 查看对象
     * @return
     */
    @GetMapping("/get")
    public ResultVo get(TestEntity entity){
        ResultVo success = ResultVo.success("操作成功！");
        TestEntity byName = IService.getByName(entity);
        success.put("object",entity);
        success.put("byName",byName);
        return success;
    }


    /**
     * 删除对象
     * @return
     */
    @GetMapping("/del")
    public ResultVo del(String id){

        TestEntity testEntity1 = new TestEntity();
        testEntity1.setId(id);
        String[] ids  = {id};
        List<TestEntity> idList = new ArrayList<>();
        idList.add(testEntity1);

        int count = 0;

        count = IService.delete(id);

        //count = IService.delete(testEntity1);

        //count = IService.deleteAll(ids);

        //count = IService.deleteAll(idList);


        if(count == 0){
            ResultVo success = ResultVo.error("删除对象失败！");
            success.put("object",id);
            return success;
        }

        ResultVo success = ResultVo.success("删除对象成功！");
        success.put("object",id);

        return success;
    }



}
