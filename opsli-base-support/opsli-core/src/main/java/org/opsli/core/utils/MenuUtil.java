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

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.menu.MenuApi;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.common.constants.RedisConstants;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.cache.SecurityCache;
import org.opsli.core.msg.CoreMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * 菜单工具类
 *
 * @author Parker
 * @date 2020-09-22 11:17
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class MenuUtil {

    /** 菜单 Api */
    private static MenuApi menuApi;

    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;

    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据 权限 获得菜单
     * @param permissions 权限
     * @return model
     */
    public static MenuModel getMenuByPermissions(String permissions){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);


        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_MENU_CODE + permissions);

        Object cache = SecurityCache.get(redisTemplate, cacheKey, (k) -> {
            // 查询数据库
            ResultWrapper<MenuModel> resultVo = menuApi.getByPermissions(permissions);
            if(!ResultWrapper.isSuccess(resultVo)){
                return null;
            }
            return resultVo.getData();
        }, true);

        return Convert.convert(MenuModel.class, cache);
    }


    // ============== 刷新缓存 ==============

    /**
     * 刷新菜单 - 删就完了
     * @param menu 菜单model
     * @return boolean
     */
    public static boolean refreshMenu(MenuModel menu){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(menu == null || StringUtils.isEmpty(menu.getPermissions())){
            return true;
        }

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_MENU_CODE + menu.getPermissions());

        // 删除缓存
        return SecurityCache.remove(redisTemplate, cacheKey);
    }


    // =====================================

    /**
     * 初始化
     */
    @Autowired
    public  void init(MenuApi menuApi,
                      RedisTemplate<String, Object> redisTemplate) {
        MenuUtil.menuApi = menuApi;
        MenuUtil.redisTemplate = redisTemplate;
        IS_INIT = true;
    }

}
