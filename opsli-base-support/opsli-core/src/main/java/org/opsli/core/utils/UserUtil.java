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
import org.opsli.common.utils.Props;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.msgs.MenuMsgFactory;
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
@AutoConfigureAfter({RedisPlugin.class , RedisLockPlugins.class, UserApi.class})
@Lazy(false)
public class UserUtil {

    /** 超级管理员 */
    public static final String SUPER_ADMIN;

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


    static{
        Props props = new Props("application.yaml");
        SUPER_ADMIN = props.getStr("opsli.superadmin","system");
    }

    /**
     * 获得当前系统登陆用户
     * @return
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
     * @param userId
     * @return
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
            redisLock = null;
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
     * @param userName
     * @return
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
            redisLock = null;
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
     * @param userId
     * @return
     */
    public static List<String> getUserRolesByUserId(String userId){
        List<String> roles = null;

        // 先从缓存里拿
        try {
            Object obj = CacheUtil.get(PREFIX_ID_ROLES + userId);
            if(obj instanceof List){
                List<String> list = (List<String>) obj;
                if (!list.isEmpty()) {
                    return list;
                }
            }else {
                JSONArray jsonArray = (JSONArray) obj;
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
                    List<String> list = (List<String>) obj;
                    if (!list.isEmpty()) {
                        return list;
                    }
                }else {
                    JSONArray jsonArray = (JSONArray) obj;
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
            redisLock = null;
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
     * @param userId
     * @return
     */
    public static List<String> getUserAllPermsByUserId(String userId){
        List<String> permissions = null;

        // 先从缓存里拿
        try {
            Object obj = CacheUtil.get(PREFIX_ID_PERMISSIONS + userId);
            if(obj instanceof List){
                List<String> list = (List<String>) obj;
                if (!list.isEmpty()) {
                    return list;
                }
            }else {
                JSONArray jsonArray = (JSONArray) obj;
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
                    List<String> list = (List<String>) obj;
                    if (!list.isEmpty()) {
                        return list;
                    }
                }else {
                    JSONArray jsonArray = (JSONArray) obj;
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
            redisLock = null;
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
     * @param userId
     * @return
     */
    public static List<MenuModel> getMenuListByUserId(String userId){
        List<MenuModel> menus = null;

        // 先从缓存里拿
        try {
            Object obj = CacheUtil.get(PREFIX_ID_MENUS + userId);
            if(obj instanceof List){
                List<Object> list = (List<Object>) obj;
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
                JSONArray jsonArray = (JSONArray) obj;
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
                    List<Object> list = (List<Object>) obj;
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
                    JSONArray jsonArray = (JSONArray) obj;
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
            redisLock = null;
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
     * @param user
     * @return
     */
    public static void refreshUser(UserModel user){
        if(user == null || StringUtils.isEmpty(user.getId())){
            return;
        }

        UserModel userModelById = CacheUtil.get(PREFIX_ID + user.getId(), UserModel.class);
        UserModel userModelByUsername = CacheUtil.get(PREFIX_USERNAME + user.getUsername(),
                                                            UserModel.class);

        boolean hasNilFlagById = CacheUtil.hasNilFlag(PREFIX_ID + user.getId());
        boolean hasNilFlagByName = CacheUtil.hasNilFlag(PREFIX_USERNAME + user.getUsername());

        // 只要有一个不为空 则执行刷新
        if (hasNilFlagById || hasNilFlagByName){
            // 清除空拦截
            CacheUtil.delNilFlag(PREFIX_ID + user.getId());
            CacheUtil.delNilFlag(PREFIX_USERNAME + user.getUsername());
        }

        // 只要有一个不为空 则执行刷新
        if (userModelById != null || userModelByUsername != null){
            // 先删除
            CacheUtil.del(PREFIX_ID + user.getId());
            CacheUtil.del(PREFIX_USERNAME + user.getUsername());
            // 清除空拦截
            CacheUtil.delNilFlag(PREFIX_ID + user.getId());
            CacheUtil.delNilFlag(PREFIX_USERNAME + user.getUsername());

            // 发送通知消息
            redisPlugin.sendMessage(
                    UserMsgFactory.createUserMsg(user)
            );
        }
    }


    /**
     * 刷新用户角色 - 删就完了
     * @param userId
     * @return
     */
    public static void refreshUserRoles(String userId){
        try {
            Object obj = CacheUtil.get(PREFIX_ID_ROLES + userId);
            boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_ID_ROLES + userId);

            // 只要不为空 则执行刷新
            if (hasNilFlag){
                // 清除空拦截
                CacheUtil.delNilFlag(PREFIX_ID_ROLES + userId);
            }

            if(obj != null){
                // 先删除
                CacheUtil.del(PREFIX_ID_ROLES + userId);

                // 发送通知消息
                redisPlugin.sendMessage(
                        UserMsgFactory.createUserRolesMsg(userId, null)
                );
            }

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 刷新用户权限 - 删就完了
     * @param userId
     * @return
     */
    public static void refreshUserAllPerms(String userId){
        try {
            Object obj = CacheUtil.get(PREFIX_ID_PERMISSIONS + userId);
            boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_ID_PERMISSIONS + userId);

            // 只要不为空 则执行刷新
            if (hasNilFlag){
                // 清除空拦截
                CacheUtil.delNilFlag(PREFIX_ID_PERMISSIONS + userId);
            }

            if(obj != null){
                // 先删除
                CacheUtil.del(PREFIX_ID_PERMISSIONS + userId);

                // 发送通知消息
                redisPlugin.sendMessage(
                        UserMsgFactory.createUserPermsMsg(userId, null)
                );
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 刷新用户菜单 - 删就完了
     * @param userId
     * @return
     */
    public static void refreshUserMenus(String userId){
        try {
            Object obj = CacheUtil.get(PREFIX_ID_MENUS + userId);
            boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_ID_MENUS + userId);

            // 只要不为空 则执行刷新
            if (hasNilFlag){
                // 清除空拦截
                CacheUtil.delNilFlag(PREFIX_ID_MENUS + userId);
            }

            if(obj != null){
                // 先删除
                CacheUtil.del(PREFIX_ID_MENUS + userId);

                // 发送通知消息
                redisPlugin.sendMessage(
                        UserMsgFactory.createUserMenusMsg(userId, null)
                );
            }

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获得 租户ID
     * @return
     */
    public static String getTenantId(){
        // 判断权限 如果是 admin 超级管理员 则租户ID清空 且findList 不做处理 否则默认都会做处理
        // 如果表中 没有 tenant_id 字段 则不进行多租户处理

        UserModel user = getUser();

        // 如果是超级管理员 则不进行租户处理
        if(SUPER_ADMIN.equals(user.getUsername())){
            return null;
        }
        return user.getTenantId();
    }

    /**
     * 处理密码
     * @param password 密码
     * @param secretkey 盐值
     * @return
     */
    public static String handlePassword(String password, String secretkey){
        return new Md5Hash(password, secretkey).toHex();
    }

    // =====================================

    @Autowired
    public  void setRedisPlugin(RedisPlugin redisPlugin) {
        UserUtil.redisPlugin = redisPlugin;
    }

    @Autowired
    public  void setRedisLockPlugins(RedisLockPlugins redisLockPlugins) {
        UserUtil.redisLockPlugins = redisLockPlugins;
    }

    @Autowired
    public  void setUserApi(UserApi userApi) {
        UserUtil.userApi = userApi;
    }
}
