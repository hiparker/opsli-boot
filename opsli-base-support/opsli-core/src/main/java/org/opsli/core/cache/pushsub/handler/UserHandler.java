package org.opsli.core.cache.pushsub.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.constants.CacheConstants;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.enums.MsgArgsType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.core.cache.pushsub.enums.UserModelType;
import org.opsli.core.utils.UserUtil;
import org.opsli.plugins.cache.EhCachePlugin;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.handler
 * @Author: Parker
 * @CreateTime: 2020-09-15 16:24
 * @Description: 字典消息处理
 */
@Slf4j
public class UserHandler implements RedisPushSubHandler{

    @Autowired
    EhCachePlugin ehCachePlugin;

    @Override
    public PushSubType getType() {
        return PushSubType.USER;
    }

    @Override
    public void handler(JSONObject msgJson) {
        UserModelType userModelType = UserModelType.valueOf((String)
                msgJson.get(MsgArgsType.USER_MODEL_TYPE.toString()));

        // 用户刷新
        if(UserModelType.USER_MODEL == userModelType){
            this.userHandler(msgJson);
        }
        // 用户角色刷新
        else if(UserModelType.USER_ROLES_MODEL == userModelType){
            this.userRolesHandler(msgJson);
        }
        // 用户权限刷新
        else if(UserModelType.USER_PERMS_MODEL == userModelType){
            this.userPermsHandler(msgJson);
        }
        // 用户菜单刷新
        else if(UserModelType.USER_MENU_MODEL == userModelType){
            this.userMenusHandler(msgJson);
        }

    }

    /**
     * 用户数据处理
     * @param msgJson
     */
    private void userHandler(JSONObject msgJson){
        JSONObject data = msgJson.getJSONObject(MsgArgsType.USER_MODEL_DATA.toString());
        // 数据为空则不执行
        if(data == null) return;

        // 获得数据
        UserModel userModel = data.toJavaObject(UserModel.class);
        // 获得用户ID 和 用户名
        String userId = (String) msgJson.get(MsgArgsType.USER_ID.toString());
        String username = (String) msgJson.get(MsgArgsType.USER_USERNAME.toString());
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(username) ){
            return;
        }

        // 先删除
        ehCachePlugin.delete(CacheConstants.HOT_DATA, UserUtil.PREFIX_ID + userId);
        ehCachePlugin.delete(CacheConstants.HOT_DATA, UserUtil.PREFIX_USERNAME + username);
        // 再赋值
        ehCachePlugin.put(CacheConstants.HOT_DATA, UserUtil.PREFIX_ID + userId, userModel);
        ehCachePlugin.put(CacheConstants.HOT_DATA,UserUtil.PREFIX_USERNAME + username, userModel);
        // 清除空拦截
        CacheUtil.putNilFlag(UserUtil.PREFIX_ID + userId);
        CacheUtil.putNilFlag(UserUtil.PREFIX_USERNAME + username);
    }

    /**
     * 用户角色数据处理
     * @param msgJson
     */
    private void userRolesHandler(JSONObject msgJson){
        JSONArray dataArray = msgJson.getJSONArray(MsgArgsType.USER_MODEL_DATA.toString());
        // 数据为空则不执行
        if(dataArray == null || dataArray.isEmpty()) return;

        // 获得用户ID
        String userId = (String) msgJson.get(MsgArgsType.USER_ID.toString());
        if(StringUtils.isEmpty(userId)){
            return;
        }

        // 用户角色列表
        List<String> roleCodes = dataArray.toJavaList(String.class);
        if(roleCodes == null || roleCodes.isEmpty()){
            return;
        }

        // 先删除
        ehCachePlugin.delete(CacheConstants.HOT_DATA, UserUtil.PREFIX_ID_ROLES + userId);
        // 存入缓存
        ehCachePlugin.put(CacheConstants.HOT_DATA, UserUtil.PREFIX_ID_ROLES + userId, roleCodes);
        // 清除空拦截
        CacheUtil.putNilFlag(UserUtil.PREFIX_ID_ROLES + userId);

    }

    /**
     * 用户权限数据处理
     * @param msgJson
     */
    private void userPermsHandler(JSONObject msgJson){
        JSONArray dataArray = msgJson.getJSONArray(MsgArgsType.USER_MODEL_DATA.toString());
        // 数据为空则不执行
        if(dataArray == null || dataArray.isEmpty()) return;

        // 获得用户ID
        String userId = (String) msgJson.get(MsgArgsType.USER_ID.toString());
        if(StringUtils.isEmpty(userId)){
            return;
        }
        // 用户权限列表
        List<String> perms = dataArray.toJavaList(String.class);
        if(perms == null || perms.isEmpty()){
            return;
        }

        // 先删除
        ehCachePlugin.delete(CacheConstants.HOT_DATA, UserUtil.PREFIX_ID_PERMISSIONS + userId);
        // 存入缓存
        ehCachePlugin.put(CacheConstants.HOT_DATA, UserUtil.PREFIX_ID_PERMISSIONS + userId, perms);
        // 清除空拦截
        CacheUtil.putNilFlag(UserUtil.PREFIX_ID_PERMISSIONS + userId);
    }

    /**
     * 用户菜单数据处理
     * @param msgJson
     */
    private void userMenusHandler(JSONObject msgJson){
        JSONArray dataArray = msgJson.getJSONArray(MsgArgsType.USER_MODEL_DATA.toString());
        // 数据为空则不执行
        if(dataArray == null || dataArray.isEmpty()) return;

        // 获得用户ID
        String userId = (String) msgJson.get(MsgArgsType.USER_ID.toString());
        if(StringUtils.isEmpty(userId)){
            return;
        }
        // 用户菜单列表
        List<MenuModel> menus = dataArray.toJavaList(MenuModel.class);
        if(menus == null || menus.isEmpty()){
            return;
        }

        // 先删除
        ehCachePlugin.delete(CacheConstants.HOT_DATA, UserUtil.PREFIX_ID_MENUS + userId);
        // 存入缓存
        ehCachePlugin.put(CacheConstants.HOT_DATA, UserUtil.PREFIX_ID_MENUS + userId, menus);
        // 清除空拦截
        CacheUtil.putNilFlag(UserUtil.PREFIX_ID_MENUS + userId);
    }


}
