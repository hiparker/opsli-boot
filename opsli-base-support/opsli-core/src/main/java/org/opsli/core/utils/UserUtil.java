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
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
//import org.apache.shiro.crypto.hash.Md5Hash;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.user.UserApi;
import org.opsli.api.web.system.user.UserOrgRefApi;
import org.opsli.api.web.system.user.UserRoleRefApi;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.common.constants.RedisConstants;
import org.opsli.common.exception.TokenException;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.cache.SecurityCache;
import org.opsli.core.holder.UserContextHolder;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.msg.TokenMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
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

    /** 修改租户权限 */
    private static final String PERMS_TENANT = "system_set_tenant_admin";

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

    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * 获得当前系统登陆用户
     * @return UserModel
     */
    public static UserModel getUser(){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        String token = UserContextHolder.getToken().orElseThrow(() -> new TokenException(
                TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY));

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
     * 获得当前系统登陆用户
     * @return UserModel
     */
    public static UserModel getUserBySource(){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        String token = UserContextHolder.getToken().orElseThrow(() -> new TokenException(
                TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY));

        // 如果还是没获取到token 则抛出异常
        if(StringUtils.isEmpty(token)){
            // Token失效，请重新登录
            throw new TokenException(TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY);
        }

        String userId = UserTokenUtil.getUserIdByToken(token);
        UserModel user = getUserBySource(userId);
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
        return getUser(userId, false);
    }

    /**
     * 根据ID 获得用户 (不会去递归查找用户)
     * @param userId 用户ID
     * @return UserModel
     */
    public static UserModel getUserBySource(String userId){
        return getUser(userId, true);
    }

    /**
     * 根据ID 获得用户
     * @param userId 用户ID
     * @param isRecursion 是否递归触发
     * @return UserModel
     */
    private static UserModel getUser(String userId, boolean isRecursion){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(StrUtil.isEmpty(userId)){
            return null;
        }

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID + userId);

        Object cache = SecurityCache.get(redisTemplate, cacheKey, (k) -> {
            // 查询数据库
            UserModel userModelTemp = new UserModel();
            userModelTemp.setId(userId);
            // 设置为系统内部调用 否则 会拿到 空值
            userModelTemp.setIzApi(true);

            // 查询数据库
            ResultWrapper<UserModel> resultVo = userApi.get(userModelTemp);
            if(!ResultWrapper.isSuccess(resultVo)){
                return null;
            }
            return resultVo.getData();
        }, true);

        UserModel userModel = Convert.convert(UserModel.class, cache);

        // 如果不是递归触发 可进入一次
        if(!isRecursion){
            // 如果是 切换租户权限 则进行新一轮判断
            // 注意不要陷入递归死循环 只保障循环一次
            if (StringUtils.isNotBlank(userModel.getSwitchTenantUserId())){
                userModel = getUser(userModel.getSwitchTenantUserId(), true);
            }
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

        if(StrUtil.isEmpty(userName)){
            return null;
        }

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_USERNAME + userName);

        String userId = (String) SecurityCache.get(redisTemplate, cacheKey, (k) -> {
            // 查询数据库
            ResultWrapper<UserModel> resultVo = userApi.getUserByUsername(userName);
            if(!ResultWrapper.isSuccess(resultVo)){
                return null;
            }

            if(null == resultVo.getData()){
                return null;
            }

            return resultVo.getData().getId();
        }, true);

        // 只查一次
        return getUser(userId, true);
    }


    /**
     * 根据 mobile 获得用户
     * @param mobile 手机号
     * @return UserModel
     */
    public static UserModel getUserByMobile(String mobile){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(StrUtil.isEmpty(mobile)){
            return null;
        }

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_MOBILE + mobile);

        String userId = (String) SecurityCache.get(redisTemplate, cacheKey, (k) -> {
            // 查询数据库
            ResultWrapper<UserModel> resultVo = userApi.getUserByMobile(mobile);
            if(!ResultWrapper.isSuccess(resultVo)){
                return null;
            }

            if(null == resultVo.getData()){
                return null;
            }

            return resultVo.getData().getId();
        }, true);

        // 只查一次
        return getUser(userId, true);
    }


    /**
     * 根据 email 获得用户
     * @param email 邮箱
     * @return UserModel
     */
    public static UserModel getUserByEmail(String email){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(StrUtil.isEmpty(email)){
            return null;
        }

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_EMAIL + email);

        String userId = (String) SecurityCache.get(redisTemplate, cacheKey, (k) -> {
            // 查询数据库
            ResultWrapper<UserModel> resultVo = userApi.getUserByEmail(email);
            if(!ResultWrapper.isSuccess(resultVo)){
                return null;
            }

            if(null == resultVo.getData()){
                return null;
            }

            return resultVo.getData().getId();
        }, true);

        // 只查一次
        return getUser(userId, true);
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

        // 处理 切换租户
        UserModel currUser = getUser(userId);
        if (null != currUser &&
                StringUtils.isNotBlank(currUser.getSwitchTenantUserId())){
            userId = currUser.getSwitchTenantUserId();
        }

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID_AND_ROLES + userId);

        final String finalUserId = userId;
        Object cache = SecurityCache.get(redisTemplate, cacheKey, (k) -> {
            // 查询数据库
            ResultWrapper<List<String>> resultVo = userRoleRefApi.getRolesByUserId(finalUserId);
            if(!ResultWrapper.isSuccess(resultVo)){
                return null;
            }
            return resultVo.getData();
        }, true);

        List<String> roles = Convert.toList(String.class, cache);
        if(null == roles){
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

        // 处理 切换租户
        UserModel currUser = getUser(userId);
        if (null != currUser &&
                StringUtils.isNotBlank(currUser.getSwitchTenantUserId())){
            userId = currUser.getSwitchTenantUserId();
        }


        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID_PERMISSIONS + userId);

        final String finalUserId = userId;
        Object cache = SecurityCache.get(redisTemplate, cacheKey, (k) -> {
            // 查询数据库
            ResultWrapper<List<String>> resultVo = userRoleRefApi.getAllPerms(finalUserId);
            if(!ResultWrapper.isSuccess(resultVo)){
                return null;
            }
            return resultVo.getData();
        }, true);

        List<String> permissions = Convert.toList(String.class, cache);
        if(null == permissions){
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

        // 处理 切换租户
        UserModel currUser = getUser(userId);
        if (null != currUser &&
                StringUtils.isNotBlank(currUser.getSwitchTenantUserId())){
            userId = currUser.getSwitchTenantUserId();
        }


        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID_ORGS + userId);

        final String finalUserId = userId;
        Object cache = SecurityCache.get(redisTemplate, cacheKey, (k) -> {
            // 查询数据库
            ResultWrapper<List<UserOrgRefModel>> resultVo = userOrgRefApi.findListByUserId(finalUserId);
            if(!ResultWrapper.isSuccess(resultVo)){
                return null;
            }
            return resultVo.getData();
        }, true);

        List<UserOrgRefModel> orgList = Convert.toList(UserOrgRefModel.class, cache);
        if(null == orgList){
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

        // 处理 切换租户
        UserModel currUser = getUser(userId);
        if (null != currUser &&
                StringUtils.isNotBlank(currUser.getSwitchTenantUserId())){
            userId = currUser.getSwitchTenantUserId();
        }


        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID_MENUS + userId);

        final String finalUserId = userId;
        Object cache = SecurityCache.get(redisTemplate, cacheKey, (k) -> {
            // 查询数据库
            ResultWrapper<List<MenuModel>> resultVo = userRoleRefApi.getMenuListByUserId(finalUserId);
            if(!ResultWrapper.isSuccess(resultVo)){
                return null;
            }
            return resultVo.getData();
        }, true);

        List<MenuModel> menus = Convert.toList(MenuModel.class, cache);
        if(null == menus){
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

        // 处理 切换租户
        UserModel currUser = getUser(userId);
        if (null != currUser &&
                StringUtils.isNotBlank(currUser.getSwitchTenantUserId())){
            userId = currUser.getSwitchTenantUserId();
        }


        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID_DEF_ROLE + userId);

        final String finalUserId = userId;
        Object cache = SecurityCache.get(redisTemplate, cacheKey, (k) -> {
            // 查询数据库
            ResultWrapper<RoleModel> resultVo = userRoleRefApi.getDefRoleByUserId(finalUserId);
            if(!ResultWrapper.isSuccess(resultVo)){
                return null;
            }
            return resultVo.getData();
        }, true);

        return Convert.convert(RoleModel.class, cache);
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

        // 处理 切换租户
        UserModel currUser = getUser(userId);
        if (null != currUser &&
                StringUtils.isNotBlank(currUser.getSwitchTenantUserId())){
            userId = currUser.getSwitchTenantUserId();
        }

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID_DEF_ORG + userId);

        final String finalUserId = userId;
        Object cache = SecurityCache.get(redisTemplate, cacheKey, (k) -> {
            // 查询数据库
            ResultWrapper<UserOrgRefModel> resultVo = userOrgRefApi.getDefOrgByUserId(finalUserId);
            if(!ResultWrapper.isSuccess(resultVo)){
                return null;
            }
            return resultVo.getData();
        }, true);

        return Convert.convert(UserOrgRefModel.class, cache);
    }


    // ============== 刷新缓存 ==============

    /**
     * 获得当前系统登陆用户
     * @return UserModel
     */
    public static boolean updateUser(UserModel user){
        if(null == user){
            return false;
        }

        // 先清空缓存
        boolean flag = refreshUser(user);
        if(!flag){
            return false;
        }

        // 缓存Key
        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID + user.getId());
        // 存入缓存
        SecurityCache.put(redisTemplate, cacheKey, user);

        return true;
    }

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

        String cacheKeyByUserId = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID + user.getId());
        String cacheKeyByUsername = CacheUtil.formatKey(RedisConstants.PREFIX_USER_USERNAME + user.getUsername());
        String cacheKeyByMobile = CacheUtil.formatKey(RedisConstants.PREFIX_USER_MOBILE + user.getMobile());
        String cacheKeyByEmail = CacheUtil.formatKey(RedisConstants.PREFIX_USER_EMAIL + user.getEmail());

        return SecurityCache.remove(redisTemplate,
                cacheKeyByUserId, cacheKeyByUsername, cacheKeyByMobile, cacheKeyByEmail);
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

        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID_AND_ROLES + userId);

        return SecurityCache.remove(redisTemplate, cacheKey);
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

        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID_DEF_ROLE + userId);

        return SecurityCache.remove(redisTemplate, cacheKey);
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

        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID_PERMISSIONS + userId);

        return SecurityCache.remove(redisTemplate, cacheKey);
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

        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID_ORGS + userId);

        return SecurityCache.remove(redisTemplate, cacheKey);
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

        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID_DEF_ORG + userId);

        return SecurityCache.remove(redisTemplate, cacheKey);
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

        String cacheKey = CacheUtil.formatKey(RedisConstants.PREFIX_USER_ID_MENUS + userId);

        return SecurityCache.remove(redisTemplate, cacheKey);
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
            return TenantUtil.SUPER_ADMIN_TENANT_ID;
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

    // =====================================

    /**
     * 初始化
     */
    @Autowired
    public void init(GlobalProperties globalProperties,
                     UserApi userApi,
                     UserRoleRefApi userRoleRefApi,
                     UserOrgRefApi userOrgRefApi,
                     RedisTemplate<String, Object> redisTemplate) {
        if(globalProperties != null && globalProperties.getAuth() != null
                && globalProperties.getAuth().getToken() != null
            ){
            // 获得 超级管理员
            UserUtil.SUPER_ADMIN = globalProperties.getAuth().getSuperAdmin();
        }

        UserUtil.userApi = userApi;
        UserUtil.userRoleRefApi = userRoleRefApi;
        UserUtil.userOrgRefApi = userOrgRefApi;
        UserUtil.redisTemplate = redisTemplate;
        IS_INIT = true;
    }

}
