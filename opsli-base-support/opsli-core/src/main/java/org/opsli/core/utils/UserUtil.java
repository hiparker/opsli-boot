package org.opsli.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.user.UserApi;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.api.TokenThreadLocal;
import org.opsli.common.exception.TokenException;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.msgs.UserMsgFactory;
import org.opsli.core.msg.TokenMsg;
import org.opsli.plugins.redis.RedisLockPlugins;
import org.opsli.plugins.redis.RedisPlugin;
import org.opsli.plugins.redis.lock.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.utils
 * @Author: Parker
 * @CreateTime: 2020-09-19 20:03
 * @Description: 用户工具类
 */
@Slf4j
@Component
public class UserUtil {

    /** 超级管理员名称 */
    public static final String SUPER_ADMIN = "system";
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


    /**
     * 获得当前系统登陆用户
     * @return
     */
    public static UserModel getUser(){
        String token = TokenThreadLocal.get();

        if(StringUtils.isEmpty(token)){
            return null;
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
            roles = (List<String>) CacheUtil.get(PREFIX_ID_ROLES + userId);
            if (roles != null && !roles.isEmpty()){
                return roles;
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

        if(roles == null){
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
            permissions = (List<String>) CacheUtil.get(PREFIX_ID_PERMISSIONS + userId);
            if (permissions != null && !permissions.isEmpty()){
                return permissions;
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

        if(permissions == null){
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
            menus = (List<MenuModel>) CacheUtil.get(PREFIX_ID_MENUS + userId);
            if (menus != null && !menus.isEmpty()){
                return menus;
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

        if(menus == null){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(PREFIX_ID_MENUS + userId);
            return null;
        }

        return menus;
    }

    // ============== 刷新缓存 ==============

    /**
     * 刷新用户
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

        // 只要有一个不为空 则执行刷新
        if (userModelById != null || userModelByUsername != null){
            // 先删除
            CacheUtil.del(PREFIX_ID + user.getId());
            CacheUtil.del(PREFIX_USERNAME + user.getUsername());
            // 再赋值
            CacheUtil.put(PREFIX_ID + user.getId(), user);
            CacheUtil.put(PREFIX_USERNAME + user.getUsername(), user);
            // 清除空拦截
            CacheUtil.putNilFlag(PREFIX_ID + user.getId());
            CacheUtil.putNilFlag(PREFIX_USERNAME + user.getUsername());

            // 发送通知消息
            redisPlugin.sendMessage(
                    UserMsgFactory.createUserMsg(user)
            );
        }
    }


    /**
     * 刷新用户角色
     * @param userId
     * @param roleCodes
     * @return
     */
    public static void refreshUserRoles(String userId, List<String> roleCodes){
        if(roleCodes == null || roleCodes.isEmpty()){
            return;
        }

        try {
            List<String> list = (List<String>) CacheUtil.get(PREFIX_ID_ROLES + userId);
            if(list != null && !list.isEmpty()){
                // 先删除
                CacheUtil.del(PREFIX_ID_ROLES + userId);
                // 存入缓存
                CacheUtil.put(PREFIX_ID_ROLES + userId, list);
                // 清除空拦截
                CacheUtil.putNilFlag(PREFIX_ID_ROLES + userId);

                // 发送通知消息
                redisPlugin.sendMessage(
                        UserMsgFactory.createUserRolesMsg(userId, roleCodes)
                );
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 刷新用户权限
     * @param userId
     * @param permissions
     * @return
     */
    public static void refreshUserAllPerms(String userId, List<String> permissions){
        if(permissions == null || permissions.isEmpty()){
            return;
        }

        try {
            List<String> list = (List<String>) CacheUtil.get(PREFIX_ID_PERMISSIONS + userId);
            if(list != null && !list.isEmpty()){
                // 先删除
                CacheUtil.del(PREFIX_ID_PERMISSIONS + userId);
                // 存入缓存
                CacheUtil.put(PREFIX_ID_PERMISSIONS + userId, permissions);
                // 清除空拦截
                CacheUtil.putNilFlag(PREFIX_ID_PERMISSIONS + userId);

                // 发送通知消息
                redisPlugin.sendMessage(
                        UserMsgFactory.createUserPermsMsg(userId, permissions)
                );
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 刷新用户菜单
     * @param userId
     * @param menus
     * @return
     */
    public static void refreshUserMenus(String userId, List<MenuModel> menus){
        if(menus == null || menus.isEmpty()){
            return;
        }

        try {
            List<MenuModel> list = (List<MenuModel>) CacheUtil.get(PREFIX_ID_MENUS + userId);
            if(list != null && !list.isEmpty()){
                // 先删除
                CacheUtil.del(PREFIX_ID_MENUS + userId);
                // 存入缓存
                CacheUtil.put(PREFIX_ID_MENUS + userId, menus);
                // 清除空拦截
                CacheUtil.putNilFlag(PREFIX_ID_MENUS + userId);

                // 发送通知消息
                redisPlugin.sendMessage(
                        UserMsgFactory.createUserMenusMsg(userId, menus)
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
        if(user == null){
            // 用户为空
            throw new TokenException(TokenMsg.EXCEPTION_USER_NULL);
        }

        // 如果是超级管理员 则不进行租户处理
        if(SUPER_ADMIN.equals(user.getUsername())){
            return null;
        }
        return user.getTenantId();
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
