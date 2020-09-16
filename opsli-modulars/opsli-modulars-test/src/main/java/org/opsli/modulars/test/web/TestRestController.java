package org.opsli.modulars.test.web;

import cn.hutool.core.thread.ThreadUtil;
import org.opsli.common.api.ResultVo;
import org.opsli.common.base.concroller.BaseController;
import org.opsli.core.cache.pushsub.msgs.DictMsgFactory;
import org.opsli.plugins.mail.MailPlugin;
import org.opsli.plugins.mail.model.MailModel;
import org.opsli.plugins.redis.RedisLockPlugins;
import org.opsli.plugins.redis.RedisPlugin;
import org.opsli.plugins.redis.lock.RedisLock;
import org.opsli.plugins.redis.pushsub.entity.BaseSubMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 测试类
 */
@RestController
@RequestMapping("/{opsli.prefix}/{opsli.version}/test")
public class TestRestController extends BaseController {

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

        BaseSubMessage msg = DictMsgFactory.createMsg("test", "aaa", 123213);

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



}
