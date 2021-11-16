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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.user.UserApi;
import org.opsli.api.web.system.user.UserOrgRefApi;
import org.opsli.api.web.system.user.UserRoleRefApi;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.api.wrapper.system.user.UserOrgRefWebModel;
import org.opsli.common.exception.TokenException;
import org.opsli.core.api.TokenThreadLocal;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.msg.TokenMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * 用户工具类
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class UserUtil {

    /** 前缀 */
    public static final String PREFIX_ID = "userId:";
    public static final String PREFIX_ID_ROLES = "userId:roles:";
    public static final String PREFIX_ID_DEF_ROLE = "userId:def_role:";
    public static final String PREFIX_ID_ORGS = "userId:orgs";
    public static final String PREFIX_ID_DEF_ORG = "userId:def_org:";
    public static final String PREFIX_ID_PERMISSIONS = "userId:permissions:";
    public static final String PREFIX_ID_MENUS = "userId:menus:";
    public static final String PREFIX_USERNAME = "username:";

    /** 修改租户权限 */
    private static final String PERMS_TENANT = "system_user_tenant";

    /** 用户Service */
    private static UserApi userApi;

    /** 用户角色 Api */
    private static UserRoleRefApi userRoleRefApi;

    /** 用户组织 Api */
    private static UserOrgRefApi userOrgRefApi;

    /** 超级管理员 */
    public static String SUPER_ADMIN;

    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;

    /**
     * 获得当前系统登陆用户
     * @return UserModel
     */
    public static UserModel getUser(){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        String token = TokenThreadLocal.get();

        // 如果还是没获取到token 则抛出异常
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
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = PREFIX_ID + userId;

        // 先从缓存里拿
        UserModel userModel = CacheUtil.getTimed(UserModel.class, cacheKey);
        if (userModel != null){
            return userModel;
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKey);
        if(hasNilFlag){
            return null;
        }

        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKey)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return null;
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            userModel = CacheUtil.getTimed(UserModel.class, cacheKey);
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
                CacheUtil.put(cacheKey, userModel);
            }

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKey);
        }

        if(userModel == null){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKey);
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
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = PREFIX_USERNAME + userName;

        // 先从缓存里拿
        UserModel userModel = CacheUtil.getTimed(UserModel.class, cacheKey);
        if (userModel != null){
            return userModel;
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKey);
        if(hasNilFlag){
            return null;
        }

        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKey)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return null;
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            userModel = CacheUtil.getTimed(UserModel.class, cacheKey);
            if (userModel != null) {
                return userModel;
            }

            // 查询数据库
            ResultVo<UserModel> resultVo = userApi.getUserByUsername(userName);
            if (resultVo.isSuccess()) {
                userModel = resultVo.getData();
                // 存入缓存
                CacheUtil.put(cacheKey, userModel);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKey);
        }

        if(userModel == null){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKey);
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
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = PREFIX_ID_ROLES + userId;

        List<String> roles;

        // 先从缓存里拿
        Object obj = CacheUtil.getTimed(cacheKey);
        roles = Convert.toList(String.class, obj);
        if(CollUtil.isNotEmpty(roles)){
            return roles;
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKey);
        if(hasNilFlag){
            return ListUtil.empty();
        }

        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKey)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return ListUtil.empty();
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            obj = CacheUtil.getTimed(cacheKey);
            roles = Convert.toList(String.class, obj);
            if(CollUtil.isNotEmpty(roles)){
                return roles;
            }

            // 查询数据库
            ResultVo<List<String>> resultVo = userRoleRefApi.getRolesByUserId(userId);
            if(resultVo.isSuccess()){
                roles = resultVo.getData();
                // 存入缓存
                CacheUtil.put(cacheKey, roles);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKey);
        }

        if(CollUtil.isEmpty(roles)){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKey);
            return ListUtil.empty();
        }

        return roles;
    }


    /**
     * 根据 userId 获得用户权限列表
     * @param userId 用户ID
     * @return List
     */
    public static List<String> getUserAllPermsByUserId(String userId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = PREFIX_ID_PERMISSIONS + userId;

        List<String> permissions;

        // 先从缓存里拿
        Object obj = CacheUtil.getTimed(cacheKey);
        permissions = Convert.toList(String.class, obj);
        if(CollUtil.isNotEmpty(permissions)){
            return permissions;
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKey);
        if(hasNilFlag){
            return ListUtil.empty();
        }

        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKey)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return ListUtil.empty();
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            obj = CacheUtil.getTimed(cacheKey);
            permissions = Convert.toList(String.class, obj);
            if(CollUtil.isNotEmpty(permissions)){
                return permissions;
            }

            // 查询数据库
            ResultVo<List<String>> resultVo = userRoleRefApi.getAllPerms(userId);
            if(resultVo.isSuccess()){
                permissions = resultVo.getData();
                // 存入缓存
                CacheUtil.put(cacheKey, permissions);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKey);
        }

        if(CollUtil.isEmpty(permissions)){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKey);
            return ListUtil.empty();
        }

        return permissions;
    }

    /**
     * 根据 userId 获得用户组织机构
     * @param userId 用户ID
     * @return List
     */
    public static List<UserOrgRefModel> getOrgListByUserId(String userId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = PREFIX_ID_ORGS + userId;

        List<UserOrgRefModel> orgList;

        // 先从缓存里拿
        Object obj = CacheUtil.getTimed(cacheKey);
        orgList = Convert.toList(UserOrgRefModel.class, obj);
        if(CollUtil.isNotEmpty(orgList)){
            return orgList;
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKey);
        if(hasNilFlag){
            return ListUtil.empty();
        }


        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKey)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return ListUtil.empty();
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            obj = CacheUtil.getTimed(cacheKey);
            orgList = Convert.toList(UserOrgRefModel.class, obj);
            if(CollUtil.isNotEmpty(orgList)){
                return orgList;
            }

            // 查询数据库
            ResultVo<List<UserOrgRefModel>> resultVo = userOrgRefApi.findListByUserId(userId);
            if(resultVo.isSuccess()){
                orgList = resultVo.getData();
                // 存入缓存
                CacheUtil.put(cacheKey, orgList);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKey);
        }

        if(CollUtil.isEmpty(orgList)){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKey);
            return ListUtil.empty();
        }

        return orgList;
    }

    /**
     * 根据 当前用户的组织机构
     * @return List
     */
    public static List<UserOrgRefModel> getOrgByCurrUser(){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        UserModel user = UserUtil.getUser();
        return getOrgListByUserId(user.getId());
    }

    /**
     * 根据 userId 获得用户菜单
     * @param userId 用户ID
     * @return List
     */
    public static List<MenuModel> getMenuListByUserId(String userId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = PREFIX_ID_MENUS + userId;

        List<MenuModel> menus;

        // 先从缓存里拿
        Object obj = CacheUtil.getTimed(cacheKey);
        menus = Convert.toList(MenuModel.class, obj);
        if(CollUtil.isNotEmpty(menus)){
            return menus;
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKey);
        if(hasNilFlag){
            return ListUtil.empty();
        }


        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKey)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return ListUtil.empty();
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            obj = CacheUtil.getTimed(cacheKey);
            menus = Convert.toList(MenuModel.class, obj);
            if(CollUtil.isNotEmpty(menus)){
                return menus;
            }

            // 查询数据库
            ResultVo<List<MenuModel>> resultVo = userRoleRefApi.getMenuListByUserId(userId);
            if(resultVo.isSuccess()){
                menus = resultVo.getData();
                // 存入缓存
                CacheUtil.put(cacheKey, menus);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKey);
        }

        if(CollUtil.isEmpty(menus)){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKey);
            return ListUtil.empty();
        }

        return menus;
    }

    /**
     * 根据 userId 获得用户默认角色
     * @param userId 用户ID
     * @return List
     */
    public static RoleModel getUserDefRoleByUserId(String userId){

        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = PREFIX_ID_DEF_ROLE + userId;

        // 先从缓存里拿
        RoleModel roleModel = CacheUtil.getTimed(RoleModel.class, cacheKey);
        if (roleModel != null){
            return roleModel;
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKey);
        if(hasNilFlag){
            return null;
        }

        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKey)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return null;
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            roleModel = CacheUtil.getTimed(RoleModel.class, cacheKey);
            if (roleModel != null){
                return roleModel;
            }

            // 查询数据库
            ResultVo<RoleModel> resultVo = userRoleRefApi.getDefRoleByUserId(userId);
            if(resultVo.isSuccess()){
                roleModel = resultVo.getData();
                // 存入缓存
                CacheUtil.put(cacheKey, roleModel);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKey);
        }

        if(roleModel == null){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKey);
            return null;
        }

        return roleModel;
    }


    /**
     * 根据 userId 获得用户默认组织
     * @param userId 用户ID
     * @return List
     */
    public static UserOrgRefModel getUserDefOrgByUserId(String userId){

        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = PREFIX_ID_DEF_ORG + userId;

        // 先从缓存里拿
        UserOrgRefModel orgModel = CacheUtil.getTimed(UserOrgRefModel.class, cacheKey);
        if (orgModel != null){
            return orgModel;
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKey);
        if(hasNilFlag){
            return null;
        }

        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKey)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return null;
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            orgModel = CacheUtil.getTimed(UserOrgRefModel.class, cacheKey);
            if (orgModel != null){
                return orgModel;
            }

            // 查询数据库
            ResultVo<UserOrgRefModel> resultVo = userOrgRefApi.getDefOrgByUserId(userId);
            if(resultVo.isSuccess()){
                orgModel = resultVo.getData();
                // 存入缓存
                CacheUtil.put(cacheKey, orgModel);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKey);
        }

        if(orgModel == null){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKey);
            return null;
        }

        return orgModel;
    }


    // ============== 刷新缓存 ==============

    /**
     * 刷新用户 - 删就完了
     * @param user 用户
     * @return boolean
     */
    public static boolean refreshUser(UserModel user){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(user == null || StringUtils.isEmpty(user.getId())){
            return true;
        }

        UserModel userModelById = CacheUtil.getTimed(UserModel.class, PREFIX_ID + user.getId());
        UserModel userModelByUsername = CacheUtil.getTimed(UserModel.class, PREFIX_USERNAME + user.getUsername());

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

        return count == 0;
    }


    /**
     * 刷新用户角色 - 删就完了
     * @param userId 用户ID
     * @return boolean
     */
    public static boolean refreshUserRoles(String userId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        Object obj = CacheUtil.getTimed(PREFIX_ID_ROLES + userId);
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

        return count == 0;
    }

    /**
     * 刷新用户默认角色 - 删就完了
     * @param userId 用户ID
     * @return boolean
     */
    public static boolean refreshUserDefRole(String userId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(StringUtils.isEmpty(userId)){
            return true;
        }

        // 计数器
        int count = 0;

        RoleModel roleModel = CacheUtil.getTimed(RoleModel.class, PREFIX_ID_DEF_ROLE + userId);
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_ID_DEF_ROLE + userId);

        // 只要不为空 则执行刷新
        if (hasNilFlag){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(PREFIX_ID_DEF_ROLE + userId);
            if(tmp){
                count--;
            }
        }

        if(roleModel != null){
            count++;
            // 先删除
            boolean tmp = CacheUtil.del(PREFIX_ID_DEF_ROLE + userId);
            if(tmp){
                count--;
            }
        }

        return count == 0;
    }



    /**
     * 刷新用户权限 - 删就完了
     * @param userId 用户ID
     * @return boolean
     */
    public static boolean refreshUserAllPerms(String userId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);


        Object obj = CacheUtil.getTimed(PREFIX_ID_PERMISSIONS + userId);
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


        return count == 0;
    }

    /**
     * 刷新用户组织 - 删就完了
     * @param userId 用户ID
     * @return boolean
     */
    public static boolean refreshUserOrgs(String userId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(StringUtils.isEmpty(userId)){
            return true;
        }

        // 计数器
        int count = 0;

        UserOrgRefWebModel orgRefModel = CacheUtil.getTimed(UserOrgRefWebModel.class, PREFIX_ID_ORGS + userId);
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_ID_ORGS + userId);

        // 只要不为空 则执行刷新
        if (hasNilFlag){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(PREFIX_ID_ORGS + userId);
            if(tmp){
                count--;
            }
        }

        if(orgRefModel != null){
            count++;
            // 先删除
            boolean tmp = CacheUtil.del(PREFIX_ID_ORGS + userId);
            if(tmp){
                count--;
            }
        }
        return count == 0;
    }

    /**
     * 刷新用户默认组织 - 删就完了
     * @param userId 用户ID
     * @return boolean
     */
    public static boolean refreshUserDefOrg(String userId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(StringUtils.isEmpty(userId)){
            return true;
        }

        // 计数器
        int count = 0;

        UserOrgRefModel orgModel = CacheUtil.getTimed(UserOrgRefModel.class, PREFIX_ID_DEF_ORG + userId);
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_ID_DEF_ORG + userId);

        // 只要不为空 则执行刷新
        if (hasNilFlag){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(PREFIX_ID_DEF_ORG + userId);
            if(tmp){
                count--;
            }
        }

        if(orgModel != null){
            count++;
            // 先删除
            boolean tmp = CacheUtil.del(PREFIX_ID_DEF_ORG + userId);
            if(tmp){
                count--;
            }
        }

        return count == 0;
    }


    /**
     * 刷新用户菜单 - 删就完了
     * @param userId 用户ID
     * @return boolean
     */
    public static boolean refreshUserMenus(String userId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);


        Object obj = CacheUtil.getTimed(PREFIX_ID_MENUS + userId);
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

        return count == 0;
    }

    /**
     * 获得 租户ID
     * @return String
     */
    public static String getTenantId(){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 判断权限 如果是 admin 超级管理员 则租户ID清空 且findList 不做处理 否则默认都会做处理
        // 如果表中 没有 tenant_id 字段 则不进行多租户处理

        UserModel user = getUser();

        // 如果是超级管理员 则不进行租户处理 默认为0
        if(StringUtils.equals(SUPER_ADMIN, user.getUsername())){
            return "0";
        }
        return user.getTenantId();
    }

    /**
     * 获得 真实租户ID
     * @return String
     */
    public static String getRealTenantId(){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        UserModel user = getUser();
        return user.getTenantId();
    }

    /**
     * 是否有修改租户的权限
     * @param currUser model
     * @return boolean
     */
    public static boolean isHasUpdateTenantPerms(final UserModel currUser){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);


        // 排除超级管理员
        if(UserUtil.SUPER_ADMIN.equals(currUser.getUsername())){
            return true;
        }

        // 获得当前用户权限
        List<String> userAllPermsByUserId = UserUtil.getUserAllPermsByUserId(currUser.getId());
        return !CollUtil.isEmpty(userAllPermsByUserId) &&
                userAllPermsByUserId.contains(PERMS_TENANT);
    }

    /**
     * 处理密码
     * @param password 密码
     * @param secretKey 盐值
     * @return String
     */
    public static String handlePassword(String password, String secretKey){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);


        return new Md5Hash(password, secretKey).toHex();
    }

    // =====================================

    /**
     * 初始化
     */
    @Autowired
    public void init(GlobalProperties globalProperties,
                     UserApi userApi,
                     UserRoleRefApi userRoleRefApi,
                     UserOrgRefApi userOrgRefApi){
        if(globalProperties != null && globalProperties.getAuth() != null
                && globalProperties.getAuth().getToken() != null
            ){
            // 获得 超级管理员
            UserUtil.SUPER_ADMIN = globalProperties.getAuth().getSuperAdmin();
        }

        UserUtil.userApi = userApi;

        UserUtil.userRoleRefApi = userRoleRefApi;

        UserUtil.userOrgRefApi = userOrgRefApi;

        IS_INIT = true;
    }

}
