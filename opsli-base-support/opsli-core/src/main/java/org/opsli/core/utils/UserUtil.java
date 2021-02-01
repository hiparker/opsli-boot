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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.user.UserApi;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.api.TokenThreadLocal;
import org.opsli.common.exception.TokenException;
import org.opsli.core.autoconfigure.GlobalProperties;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.msgs.UserMsgFactory;
import org.opsli.core.msg.TokenMsg;
import org.opsli.plugins.redis.RedisLockPlugins;
import org.opsli.plugins.redis.RedisPlugin;
import org.opsli.plugins.redis.lock.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.utils
 * @Author: Parker
 * @CreateTime: 2020-09-19 20:03
 * @Description: 用户工具类
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@AutoConfigureAfter({GlobalProperties.class, RedisPlugin.class , RedisLockPlugins.class, UserApi.class})
@Lazy(false)
public class UserUtil {

    /** 前缀 */
    public static final String PREFIX_ID = "userId:";
    public static final String PREFIX_ID_ROLES = "userId:roles:";
    public static final String PREFIX_ID_PERMISSIONS = "userId:permissions:";
    public static final String PREFIX_ID_MENUS = "userId:menus:";
    public static final String PREFIX_USERNAME = "username:";


    /** Redis插件 */
    private static RedisPlugin redisPlugin;

    /** Redis分布式锁 */
    private static RedisLockPlugins redisLockPlugins;

    /** 用户Service */
    private static UserApi userApi;

    /** 超级管理员 */
    public static String SUPER_ADMIN;

    /**
     * 获得当前系统登陆用户
     * @return UserModel
     */
    public static UserModel getUser(){
        String token = TokenThreadLocal.get();

        if(StringUtils.isEmpty(token)){
            // Token失效，请重新登录
            throw new TokenException(TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY);
        }

        String userId = UserTokenUtil.getUserIdByToken(token);
        UserModel user = getUser(userId);
        if(user == null){
            // Token失效，请重新登录
            throw new TokenException(TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY);
        }
        return user;
    }

    /**
     * 根据ID 获得用户
     * @param userId 用户ID
     * @return UserModel
     */
    public static UserModel getUser(String userId){
        // 先从缓存里拿
        UserModel userModel = CacheUtil.get(PREFIX_ID + userId, UserModel.class);
        if (userModel != null){
            return userModel;
        }


        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_ID + userId);
        if(hasNilFlag){
            return null;
        }

        // 锁凭证 redisLock 贯穿全程
        RedisLock redisLock = new RedisLock();
        redisLock.setLockName(PREFIX_ID + userId)
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
            userModel = CacheUtil.get(PREFIX_ID + userId, UserModel.class);
            if (userModel != null){
                return userModel;
            }

            // 查询数据库
            UserModel userModelTemp = new UserModel();
            userModelTemp.setId(userId);
            // 设置为系统内部调用 否则 会拿到 空值
            userModelTemp.setIzApi(true);
            ResultVo<UserModel> resultVo = userApi.get(userModelTemp);
            if(resultVo.isSuccess()){
                userModel = resultVo.getData();
                // 存入缓存
                CacheUtil.put(PREFIX_ID + userId, userModel);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            // ============ 释放锁
            redisLockPlugins.unLock(redisLock);
        }

        if(userModel == null){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(PREFIX_ID + userId);
            return null;
        }

        return userModel;
    }


    /**
     * 根据 userName 获得用户
     * @param userName 用户名
     * @return UserModel
     */
    public static UserModel getUserByUserName(String userName){
        // 先从缓存里拿
        UserModel userModel = CacheUtil.get(PREFIX_USERNAME + userName, UserModel.class);
        if (userModel != null){
            return userModel;
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_USERNAME + userName);
        if(hasNilFlag){
            return null;
        }

        // 锁凭证 redisLock 贯穿全程
        RedisLock redisLock = new RedisLock();
        redisLock.setLockName(PREFIX_USERNAME + userName)
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
            userModel = CacheUtil.get(PREFIX_USERNAME + userName, UserModel.class);
            if (userModel != null){
                return userModel;
            }

            // 查询数据库
            ResultVo<UserModel> resultVo = userApi.getUserByUsername(userName);
            if(resultVo.isSuccess()){
                userModel = resultVo.getData();
                // 存入缓存
                CacheUtil.put(PREFIX_USERNAME + userName, userModel);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            // ============ 释放锁
            redisLockPlugins.unLock(redisLock);
        }

        if(userModel == null){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(PREFIX_USERNAME + userName);
            return null;
        }

        return userModel;
    }

    /**
     * 根据 userId 获得用户角色列表
     * @param userId 用户ID
     * @return List
     */
    public static List<String> getUserRolesByUserId(String userId){
        List<String> roles = null;

        // 先从缓存里拿
        try {
            Object obj = CacheUtil.get(PREFIX_ID_ROLES + userId);
            if(obj instanceof List){
                List<String> list = Convert.toList(String.class, obj);
                if (!list.isEmpty()) {
                    return list;
                }
            }else {
                JSONArray jsonArray = Convert.convert(JSONArray.class, obj);
                if (jsonArray != null && !jsonArray.isEmpty()) {
                    return jsonArray.toJavaList(String.class);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_ID_ROLES + userId);
        if(hasNilFlag){
            return null;
        }

        // 锁凭证 redisLock 贯穿全程
        RedisLock redisLock = new RedisLock();
        redisLock.setLockName(PREFIX_ID_ROLES + userId)
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
            try {
                Object obj = CacheUtil.get(PREFIX_ID_ROLES + userId);
                if(obj instanceof List){
                    List<String> list = Convert.toList(String.class, obj);
                    if (!list.isEmpty()) {
                        return list;
                    }
                }else {
                    JSONArray jsonArray = Convert.convert(JSONArray.class, obj);
                    if (jsonArray != null && !jsonArray.isEmpty()) {
                        return jsonArray.toJavaList(String.class);
                    }
                }
            }catch (Exception ignored){}

            // 查询数据库
            ResultVo<List<String>> resultVo = userApi.getRolesByUserId(userId);
            if(resultVo.isSuccess()){
                roles = resultVo.getData();
                // 存入缓存
                CacheUtil.put(PREFIX_ID_ROLES + userId, roles);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            // ============ 释放锁
            redisLockPlugins.unLock(redisLock);
        }

        if(roles == null || roles.size() == 0){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(PREFIX_ID_ROLES + userId);
            return null;
        }

        return roles;
    }


    /**
     * 根据 userId 获得用户权限列表
     * @param userId 用户ID
     * @return List
     */
    public static List<String> getUserAllPermsByUserId(String userId){
        List<String> permissions = null;

        // 先从缓存里拿
        try {
            Object obj = CacheUtil.get(PREFIX_ID_PERMISSIONS + userId);
            if(obj instanceof List){
                List<String> list = Convert.toList(String.class, obj);
                if (!list.isEmpty()) {
                    return list;
                }
            }else {
                JSONArray jsonArray = Convert.convert(JSONArray.class, obj);
                if (jsonArray != null && !jsonArray.isEmpty()) {
                    return jsonArray.toJavaList(String.class);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }


        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_ID_PERMISSIONS + userId);
        if(hasNilFlag){
            return null;
        }

        // 锁凭证 redisLock 贯穿全程
        RedisLock redisLock = new RedisLock();
        redisLock.setLockName(PREFIX_ID_PERMISSIONS + userId)
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
            try {
                Object obj = CacheUtil.get(PREFIX_ID_PERMISSIONS + userId);
                if(obj instanceof List){
                    List<String> list = Convert.toList(String.class, obj);
                    if (!list.isEmpty()) {
                        return list;
                    }
                }else {
                    JSONArray jsonArray = Convert.convert(JSONArray.class, obj);
                    if (jsonArray != null && !jsonArray.isEmpty()) {
                        return jsonArray.toJavaList(String.class);
                    }
                }
            }catch (Exception ignored){}

            // 查询数据库
            ResultVo<List<String>> resultVo = userApi.getAllPerms(userId);
            if(resultVo.isSuccess()){
                permissions = resultVo.getData();
                // 存入缓存
                CacheUtil.put(PREFIX_ID_PERMISSIONS + userId, permissions);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            // ============ 释放锁
            redisLockPlugins.unLock(redisLock);
        }

        if(permissions == null || permissions.size() == 0){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(PREFIX_ID_PERMISSIONS + userId);
            return null;
        }

        return permissions;
    }

    /**
     * 根据 userId 获得用户菜单
     * @param userId 用户ID
     * @return List
     */
    public static List<MenuModel> getMenuListByUserId(String userId){
        List<MenuModel> menus = null;

        // 先从缓存里拿
        try {
            Object obj = CacheUtil.get(PREFIX_ID_MENUS + userId);
            if(obj instanceof List){
                List<?> list = Convert.toList(obj);
                if (!list.isEmpty()) {
                    List<MenuModel> menuModels = Lists.newArrayListWithCapacity(list.size());
                    for (Object menuObj : list) {
                        if(menuObj instanceof MenuModel){
                            menuModels.add((MenuModel) menuObj);
                        }else if(menuObj instanceof JSONObject){
                            JSONObject jsonObject = (JSONObject) menuObj;
                            MenuModel t = JSONObject.toJavaObject(jsonObject, MenuModel.class);
                            menuModels.add(t);
                        }
                    }
                    return menuModels;
                }
            }else {
                JSONArray jsonArray = Convert.convert(JSONArray.class, obj);
                if (jsonArray != null && !jsonArray.isEmpty()) {
                    return jsonArray.toJavaList(MenuModel.class);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }


        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_ID_MENUS + userId);
        if(hasNilFlag){
            return null;
        }

        // 锁凭证 redisLock 贯穿全程
        RedisLock redisLock = new RedisLock();
        redisLock.setLockName(PREFIX_ID_MENUS + userId)
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
            try {
                Object obj = CacheUtil.get(PREFIX_ID_MENUS + userId);
                if(obj instanceof List){
                    List<?> list = Convert.toList(obj);
                    if (!list.isEmpty()) {
                        List<MenuModel> menuModels = Lists.newArrayListWithCapacity(list.size());
                        for (Object menuObj : list) {
                            if(menuObj instanceof MenuModel){
                                menuModels.add((MenuModel) menuObj);
                            }else if(menuObj instanceof JSONObject){
                                JSONObject jsonObject = (JSONObject) menuObj;
                                MenuModel t = JSONObject.toJavaObject(jsonObject, MenuModel.class);
                                menuModels.add(t);
                            }
                        }
                        return menuModels;
                    }
                }else {
                    JSONArray jsonArray = Convert.convert(JSONArray.class, obj);
                    if (jsonArray != null && !jsonArray.isEmpty()) {
                        return jsonArray.toJavaList(MenuModel.class);
                    }
                }
            }catch (Exception ignored){}

            // 查询数据库
            ResultVo<List<MenuModel>> resultVo = userApi.getMenuListByUserId(userId);
            if(resultVo.isSuccess()){
                menus = resultVo.getData();
                // 存入缓存
                CacheUtil.put(PREFIX_ID_MENUS + userId, menus);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            // ============ 释放锁
            redisLockPlugins.unLock(redisLock);
        }

        if(menus == null || menus.size() == 0){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(PREFIX_ID_MENUS + userId);
            return null;
        }

        return menus;
    }

    // ============== 刷新缓存 ==============

    /**
     * 刷新用户 - 删就完了
     * @param user 用户
     * @return boolean
     */
    public static boolean refreshUser(UserModel user){
        if(user == null || StringUtils.isEmpty(user.getId())){
            return true;
        }

        UserModel userModelById = CacheUtil.get(PREFIX_ID + user.getId(), UserModel.class);
        UserModel userModelByUsername = CacheUtil.get(PREFIX_USERNAME + user.getUsername(),
                                                            UserModel.class);

        boolean hasNilFlagById = CacheUtil.hasNilFlag(PREFIX_ID + user.getId());
        boolean hasNilFlagByName = CacheUtil.hasNilFlag(PREFIX_USERNAME + user.getUsername());

        // 计数器
        int count = 0;

        if (hasNilFlagById){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(PREFIX_ID + user.getId());
            if(tmp){
                count--;
            }
        }

        if (hasNilFlagByName){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(PREFIX_USERNAME + user.getUsername());
            if(tmp){
                count--;
            }
        }

        // 只要有一个不为空 则执行刷新
        if (userModelById != null){
            count++;
            // 先删除
            boolean tmp = CacheUtil.del(PREFIX_ID + user.getId());
            if(tmp){
                count--;
            }
        }

        if (userModelByUsername != null){
            count++;
            // 先删除
            boolean tmp = CacheUtil.del(PREFIX_USERNAME + user.getUsername());
            if(tmp){
                count--;
            }
        }

        // 发送通知消息
        redisPlugin.sendMessage(
                UserMsgFactory.createUserMsg(user)
        );

        return count == 0;
    }


    /**
     * 刷新用户角色 - 删就完了
     * @param userId 用户ID
     * @return boolean
     */
    public static boolean refreshUserRoles(String userId){
        Object obj = CacheUtil.get(PREFIX_ID_ROLES + userId);
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_ID_ROLES + userId);

        // 计数器
        int count = 0;

        // 只要不为空 则执行刷新
        if (hasNilFlag){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(PREFIX_ID_ROLES + userId);
            if(tmp){
                count--;
            }
        }

        if(obj != null){
            count++;
            // 先删除
            boolean tmp = CacheUtil.del(PREFIX_ID_ROLES + userId);
            if(tmp){
                count--;
            }
        }

        // 发送通知消息
        redisPlugin.sendMessage(
                UserMsgFactory.createUserRolesMsg(userId, null)
        );

        return count == 0;
    }

    /**
     * 刷新用户权限 - 删就完了
     * @param userId 用户ID
     * @return boolean
     */
    public static boolean refreshUserAllPerms(String userId){
        Object obj = CacheUtil.get(PREFIX_ID_PERMISSIONS + userId);
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_ID_PERMISSIONS + userId);

        // 计数器
        int count = 0;

        // 只要不为空 则执行刷新
        if (hasNilFlag){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(PREFIX_ID_PERMISSIONS + userId);
            if(tmp){
                count--;
            }
        }

        if(obj != null){
            count++;
            // 先删除
            boolean tmp = CacheUtil.del(PREFIX_ID_PERMISSIONS + userId);
            if(tmp){
                count--;
            }
        }

        // 发送通知消息
        redisPlugin.sendMessage(
                UserMsgFactory.createUserPermsMsg(userId, null)
        );

        return count == 0;
    }

    /**
     * 刷新用户菜单 - 删就完了
     * @param userId 用户ID
     * @return boolean
     */
    public static boolean refreshUserMenus(String userId){
        Object obj = CacheUtil.get(PREFIX_ID_MENUS + userId);
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_ID_MENUS + userId);

        // 计数器
        int count = 0;

        // 只要不为空 则执行刷新
        if (hasNilFlag){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(PREFIX_ID_MENUS + userId);
            if(tmp){
                count--;
            }
        }

        if(obj != null){
            count++;
            // 先删除
            boolean tmp = CacheUtil.del(PREFIX_ID_MENUS + userId);
            if(tmp){
                count--;
            }
        }

        // 发送通知消息
        redisPlugin.sendMessage(
                UserMsgFactory.createUserMenusMsg(userId, null)
        );

        return count == 0;
    }

    /**
     * 获得 租户ID
     * @return String
     */
    public static String getTenantId(){
        // 判断权限 如果是 admin 超级管理员 则租户ID清空 且findList 不做处理 否则默认都会做处理
        // 如果表中 没有 tenant_id 字段 则不进行多租户处理

        UserModel user = getUser();

        // 如果是超级管理员 则不进行租户处理
        if(StringUtils.equals(SUPER_ADMIN, user.getUsername())){
            return null;
        }
        return user.getTenantId();
    }

    /**
     * 处理密码
     * @param password 密码
     * @param secretkey 盐值
     * @return String
     */
    public static String handlePassword(String password, String secretkey){
        return new Md5Hash(password, secretkey).toHex();
    }

    // =====================================

    @Autowired
    public void setRedisPlugin(RedisPlugin redisPlugin) {
        UserUtil.redisPlugin = redisPlugin;
    }

    @Autowired
    public void setRedisLockPlugins(RedisLockPlugins redisLockPlugins) {
        UserUtil.redisLockPlugins = redisLockPlugins;
    }

    @Autowired
    public void setUserApi(UserApi userApi) {
        UserUtil.userApi = userApi;
    }

    /**
     * 初始化
     * @param globalProperties 配置类
     */
    @Autowired
    public void init(GlobalProperties globalProperties){
        if(globalProperties != null && globalProperties.getAuth() != null
                && globalProperties.getAuth().getToken() != null
            ){
            // 获得 超级管理员
            UserUtil.SUPER_ADMIN = globalProperties.getAuth().getSuperAdmin();
        }
    }

}
