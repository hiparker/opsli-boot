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
package org.opsli.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.menu.MenuApi;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.msgs.MenuMsgFactory;
import org.opsli.plugins.redis.RedisLockPlugins;
import org.opsli.plugins.redis.RedisPlugin;
import org.opsli.plugins.redis.lock.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.utils
 * @Author: Parker
 * @CreateTime: 2020-09-19 20:03
 * @Description: 菜单工具类
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class MenuUtil {

    /** 前缀 */
    public static final String PREFIX_CODE = "menu:code:";


    /** Redis插件 */
    private static RedisPlugin redisPlugin;

    /** Redis分布式锁 */
    private static RedisLockPlugins redisLockPlugins;

    /** 菜单 Api */
    private static MenuApi menuApi;


    /**
     * 根据 userName 获得用户
     * @param menuCode
     * @return
     */
    public static MenuModel getMenuByCode(String menuCode){
        // 先从缓存里拿
        MenuModel menuModel = CacheUtil.get(PREFIX_CODE + menuCode, MenuModel.class);
        if (menuModel != null){
            return menuModel;
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_CODE + menuCode);
        if(hasNilFlag){
            return null;
        }

        // 锁凭证 redisLock 贯穿全程
        RedisLock redisLock = new RedisLock();
        redisLock.setLockName(PREFIX_CODE + menuCode)
                .setAcquireTimeOut(3000L)
                .setLockTimeOut(5000L);

        try {
            // 这里增加分布式锁 防止缓存击穿
            // ============ 尝试加锁
            redisLock = redisLockPlugins.tryLock(redisLock);
            if(redisLock == null){
                return null;
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            menuModel = CacheUtil.get(PREFIX_CODE + menuCode, MenuModel.class);
            if (menuModel != null){
                return menuModel;
            }

            // 查询数据库
            ResultVo<MenuModel> resultVo = menuApi.getByCode(menuCode);
            if(resultVo.isSuccess()){
                menuModel = resultVo.getData();
                // 存入缓存
                CacheUtil.put(PREFIX_CODE + menuCode, menuModel);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            // ============ 释放锁
            redisLockPlugins.unLock(redisLock);
        }

        if(menuModel == null){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(PREFIX_CODE + menuCode);
            return null;
        }

        return menuModel;
    }


    // ============== 刷新缓存 ==============

    /**
     * 刷新用户 - 删就完了
     * @param menu
     * @return
     */
    public static boolean refreshMenu(MenuModel menu){
        if(menu == null || StringUtils.isEmpty(menu.getMenuCode())){
            return true;
        }

        // 计数器
        int count = 0;

        MenuModel menuModel = CacheUtil.get(PREFIX_CODE + menu.getMenuCode(), MenuModel.class);
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_CODE + menu.getMenuCode());

        // 只要不为空 则执行刷新
        if (hasNilFlag){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(PREFIX_CODE + menu.getMenuCode());
            if(tmp){
                count--;
            }
        }

        if(menuModel != null){
            count++;
            // 先删除
            boolean tmp = CacheUtil.del(PREFIX_CODE + menu.getMenuCode());
            if(tmp){
                count--;
            }

            // 发送通知消息
            redisPlugin.sendMessage(
                    MenuMsgFactory.createMenuMsg(menu)
            );
        }

        return count == 0;
    }




    // =====================================

    @Autowired
    public  void setRedisPlugin(RedisPlugin redisPlugin) {
        MenuUtil.redisPlugin = redisPlugin;
    }

    @Autowired
    public  void setRedisLockPlugins(RedisLockPlugins redisLockPlugins) {
        MenuUtil.redisLockPlugins = redisLockPlugins;
    }

    @Autowired
    public  void setMenuApi(MenuApi menuApi) {
        MenuUtil.menuApi = menuApi;
    }
}
