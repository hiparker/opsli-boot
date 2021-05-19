/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.plugins.redis.lock;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.opsli.plugins.redis.RedisLockPlugins;
import org.opsli.plugins.redis.RedisPlugin;
import org.opsli.plugins.redis.scripts.enums.RedisScriptsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Redis 锁实现
 *
 * ===================== Redis 锁相关 =====================
 *
 * 不建议在 Redis集群下使用
 *
 * RedisLock redisLock = new RedisLock()   作为唯一锁凭证 贯穿业务逻辑生命周期
 * redisPlugin.tryLock(redisLock) 加锁
 * redisPlugin.unLock(redisLock) 释放锁
 *
 * 分布式锁需要考虑的问题
 * 1、这把锁没有失效时间，一旦解锁操作失败，就会导致锁记录一直在tair中，其他线程无法再获得到锁。
 * 2、这把锁只能是非阻塞的，无论成功还是失败都直接返回。
 * 3、这把锁是非重入的，一个线程获得锁之后，在释放锁之前，无法再次获得该锁，因为使用到的key在tair中已经存在。无法再执行put操作。
 *
 * @author Parker
 * @date 2020-09-16 11:47
 */
@Slf4j
@Service
public class RedisLockImpl implements RedisLockPlugins {

    /** 锁前缀 */
    private static final String LOCK_NAME_PREFIX = "lock:";

    @Autowired
    private RedisPlugin redisPlugin;

    /**
     * Redis 加分布式锁
     * @param redisLock 锁
     * @return identifier 很重要，解锁全靠他 唯一凭证
     */
    @Override
    public RedisLock tryLock(RedisLock redisLock) {
        // 锁凭证
        String identifier = IdUtil.simpleUUID();
        redisLock = this.tryLock(redisLock,identifier);
        if(redisLock != null){
            log.info(this.getInfo("分布式锁 - 开启",redisLock));
            // 启动看门狗
            this.lockDog(redisLock);
        }
        return redisLock;
    }

    /**
     * Redis 释放分布式锁
     * @param redisLock 锁
     * @return boolean
     */
    @Override
    public boolean unLock(RedisLock redisLock) {
        if(redisLock == null){
            return false;
        }
        try {
            List<String> keys = Collections.singletonList(LOCK_NAME_PREFIX + redisLock.getLockName());
            Long ret = (Long) redisPlugin.callScript(RedisScriptsEnum.REDIS_UN_LOCK, keys,
                    redisLock.getIdentifier());
            // 减去线程锁
            redisLock.threadUnLock();
            log.info(this.getInfo("分布式锁 - 解除",redisLock));
            if(ret == null){
                return false;
            }
            return 1 == ret;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return false;
    }


    /**
     * Redis 加分布式锁
     * @param redisLock 锁
     * @return identifier 很重要，解锁全靠他 唯一凭证
     */
    private RedisLock tryLock(RedisLock redisLock,String identifier) {
        try {
            List<String> keys = Collections.singletonList(redisLock.getLockName());
            long acquireTimeEnd = System.currentTimeMillis() + redisLock.getAcquireTimeOut();
            boolean acquired = false;
            // 尝试获得锁
            while (!acquired && (System.currentTimeMillis() < acquireTimeEnd)) {
                Long ret = (Long) redisPlugin.callScript(RedisScriptsEnum.REDIS_LOCK, keys,
                        identifier,redisLock.getLockTimeOut());
                if(ret == null){
                    break;
                }
                if (1 == ret){
                    acquired = true;
                } else {
                    ThreadUtil.sleep(10);
                }
            }
            redisLock.setIdentifier(identifier);
            return acquired ? redisLock : null;
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return null;
        }
    }

    /**
     * Redis 分布式锁 - 看门狗 自动续命使用
     * @param redisLock 锁
     * @return boolean
     */
    private void lockDog(RedisLock redisLock) {
        if(redisLock == null){
            return;
        }
        Thread t = new Thread(()->{
            try {
                // 倒计时前续命
                long countDownTime = 3000L;
                // 锁释放时间
                long lockTimeOutEnd = System.currentTimeMillis() + redisLock.getLockTimeOut();
                // 看门狗检测 当前线程是否还存活
                while (true) {
                    int lock = redisLock.threadGetLock();
                    if(lock <= 0){
                        // 再一次确定 解锁 防止线程差 最后加锁
                        this.unLock(redisLock);
                        break;
                    }

                    ThreadUtil.sleep(10);

                    // 如果 距离倒计时 前 2000 毫秒还没有动作 则执行续命
                    if((System.currentTimeMillis()+countDownTime) >= lockTimeOutEnd){
                        Object o = this.tryLock(redisLock,redisLock.getIdentifier());
                        if(o != null){
                            lockTimeOutEnd = System.currentTimeMillis() + redisLock.getLockTimeOut();
                            log.info(this.getInfo("分布式锁 - 续命",redisLock));
                        }
                    }
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        });
        t.setName(this.getInfo("分布式锁看门狗",redisLock));
        t.start();
    }

    /**
     * 获得信息
     * @param name 名称
     * @param redisLock 锁
     * @return String
     */
    private String getInfo(String name,RedisLock redisLock){
        return name + " 锁名称: "+redisLock.getLockName()+" 锁凭证: "+redisLock.getIdentifier();
    }


    public static void main(String[] args) {
        int count = 0;
        while (count <= 10) {
            System.out.println(count);
            count++;
        }
    }
}
